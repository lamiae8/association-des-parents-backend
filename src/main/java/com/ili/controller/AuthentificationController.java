package com.ili.controller;

import com.ili.dto.Adulte;
import com.ili.dto.Role;
import com.ili.model.AdulteEntity;
import com.ili.service.AdulteService;
import com.ili.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
@Transactional
public class AuthentificationController {


    private final AdulteService adulteService;
    private final MailService mailService;



    @POST
    @Path("/login")
    @Operation(summary = "Log in", description = "Authenticate a user and return a JWT token if successful")
    @RequestBody(description = "The user's login credentials", required = true,
            content = @Content(schema = @Schema(implementation = Adulte.class)))
    @APIResponse(responseCode = "200", description = "Authentication successful")
    @APIResponse(responseCode = "401", description = "Unauthorized, invalid credentials")
    @APIResponse(responseCode = "400", description = "Bad request, error occurred during authentication")

    public Response login(Adulte loginRequest) {
        try {
            Adulte loginResponse = adulteService.authenticate(loginRequest.getMail(), loginRequest.getMotDePasse());

            if (loginResponse != null) {
                return adulteService.generateJwtTokenRep(loginResponse);
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Une erreur s'est produite lors de la tentative de connexion.").build();
        }
    }

    @POST
    @Path("/login-admin")
    public Response logAdm(Adulte loginRequest){
        try {
            Adulte loginResponse = adulteService.authenticate(loginRequest.getMail(), loginRequest.getMotDePasse());

            if (loginResponse != null && (loginResponse.getRole() == Role.ADMIN || loginResponse.getRole() == Role.SUPER_ADMIN )) {
                return adulteService.generateJwtTokenRep(loginResponse);
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Une erreur s'est produite lors de la tentative de connexion.").build();
        }
    }


    @POST
    @Path("/request-password-reset")
    @Operation(summary = "Request password reset", description = "Sends a password reset email to the user with the given email")
    @APIResponse(responseCode = "200", description = "Password reset email sent successfully")
    @APIResponse(responseCode = "404", description = "User not found with the given email")
    public Response requestPasswordReset(String email) {
        Optional<AdulteEntity> adulteOptional = adulteService.findByEmail(email);
        if (adulteOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        AdulteEntity adulte = adulteOptional.get()  ;
        String token = UUID.randomUUID().toString();
        adulteService.createPasswordResetTokenForUser(adulte, token);


        try {
            mailService.sendPasswordResetMail(adulte, token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


        return Response.ok().build();
    }
    @POST
    @Path("/reset-password")
    @Operation(summary = "Reset password", description = "Resets the user's password using the given token and new password")
    @APIResponse(responseCode = "200", description = "Password reset successfully")
    @APIResponse(responseCode = "404", description = "No user found with the given token")
    @APIResponse(responseCode = "500", description = "Error while hashing the password or updating the user")
    public Response resetPassword(@QueryParam("token") String token, String newPassword) {
        AdulteEntity adulte = adulteService.findAdulteByToken(token);
        if (adulte == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Le token de réinitialisation n'est associé à aucun utilisateur.").build();
        }
        try {
            adulteService.changePassword(adulte, newPassword);
        } catch (Exception e) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur Lors du Chagement du mot de passe. Veuillez réessayer plus tard.")
                    .build();
        }

        return Response.ok().build();
    }
}

