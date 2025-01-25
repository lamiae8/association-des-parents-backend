package com.ili.controller;

import com.ili.dto.Commande;
import com.ili.error.NoCommandeException;
import com.ili.mapper.CommandeEntityMapper;
import com.ili.model.CommandeEntity;
import com.ili.service.*;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Path("/commande")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Commande", description = "Operations about Commande")
@Transactional
public class CommandeController {

    private final CommandeService commandeService;

    private final EvenementService evenementService;
    private final MailService mailService;

    @POST
    @Operation(summary = "Create a new commande", description = "Returns the created commande")
    @APIResponse(responseCode = "201", description = "Commande created successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Commande createCommande(Commande commandeDTO) {
        try {
            CommandeEntity ce = CommandeEntityMapper.mapToEntity(commandeDTO);
            ce.setEvenement(evenementService.getById(commandeDTO.getEvenement()).get());
            return CommandeEntityMapper.mapToDto(commandeService.create(ce));
        } catch (Exception e) {
            throw new BadRequestException("Error creating commande");
        }
    }

    @PUT
    @Operation(summary = "Update an commande", description = "Returns the updated commande")
    @APIResponse(responseCode = "200", description = "Commande updated successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Commande updateCommande(Commande commandeDTO){
        try {
            if(commandeDTO.getEleve() == null){
                commandeDTO.setEleve(null);
            }
            CommandeEntity ce = CommandeEntityMapper.mapToEntity(commandeDTO);
            ce.setEvenement(evenementService.getById(commandeDTO.getEvenement()).get());
            return CommandeEntityMapper.mapToDto(commandeService.update(ce));
        } catch (Exception e) {
            throw new BadRequestException("Error updating commande");
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a commande", description = "Deletes a commande by ID")
    @APIResponse(responseCode = "204", description = "commande deleted successfully")
    @APIResponse(responseCode = "404", description = "commande not found")
    public void delete(@PathParam("id") int id){
        commandeService.delete(id);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get an commande by ID", description = "Returns a single commande")
    @APIResponse(responseCode = "200", description = "Successful operation")
    @APIResponse(responseCode = "404", description = "Commande not found")
    public Commande getCommandeById(@PathParam("id") long id) throws NoCommandeException {
        return commandeService.getById(id)
                .map(CommandeEntityMapper::mapToDto)
                .orElseThrow(() -> new NoCommandeException("id", String.valueOf(id)));
    }

    @GET
    @Path("/adulte/{id}")
    @Operation(summary = "Get an commande by ID", description = "Returns a single commande")
    @APIResponse(responseCode = "200", description = "Successful operation")
    @APIResponse(responseCode = "404", description = "Commande not found")
    public List<Commande> getCommandeByAdulte(@PathParam("id") long id) throws NoCommandeException {
        List<Commande> commandes = commandeService.getByIdAdulte(id).stream().map(CommandeEntityMapper::mapToDto).toList();
        if (commandes.isEmpty()) {
            throw new NoCommandeException("adulte id", String.valueOf(id));
        } else {
            return commandes;
        }
    }

    @GET
    @Path("/evenement/{id}")
    @Operation(summary = "Get an commande by ID", description = "Returns a single commande")
    @APIResponse(responseCode = "200", description = "Successful operation")
    @APIResponse(responseCode = "404", description = "Commande not found")
    public List<Commande> getCommandeByEvenement(@PathParam("id") long id) throws NoCommandeException {
        List<Commande> commandes = commandeService.getByIdEvenement(id).stream().map(CommandeEntityMapper::mapToDto).toList();
        if (commandes.isEmpty()) {
            throw new NoCommandeException("evenement id", String.valueOf(id));
        } else {
            return commandes;
        }
    }

    @POST
    @Path("/send-reminder/{id}")
    @Operation(summary = "Send a reminder email for unpaid orders", description = "Sends a reminder email to all unpaid orders related to a specific event")
    @APIResponse(responseCode = "200", description = "Emails sent successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Response sendReminder(@PathParam("id") Long id) {
        try {



            List<Commande> commandes = commandeService.getByIdEvenement(id).stream().map(CommandeEntityMapper::mapToDto)
                    .filter(commande -> !commande.isPayer())
                    .toList();


            if (commandes.isEmpty()) {
                throw new NoCommandeException("evenement id", String.valueOf(id));
            } else {



                for (Commande commande : commandes) {
                    LocalDate DATE_LIMIT = evenementService.getById(commande.getEvenement()).get().getDatePaiement();
                    System.out.println(commandes +":::" +DATE_LIMIT);


                    mailService.sendReminderEmail(commande, DATE_LIMIT);
                }
            }

            return Response.status(Response.Status.OK).entity("Reminder emails sent successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error sending reminder emails").build();
        }
    }
}
