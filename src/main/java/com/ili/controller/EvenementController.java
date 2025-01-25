package com.ili.controller;


import com.ili.dto.Commande;
import com.ili.dto.Evenement;
import com.ili.error.NoCommandeException;
import com.ili.error.NoEvenementException;
import com.ili.error.NoEvenementFoundException;
import com.ili.mapper.CommandeEntityMapper;
import com.ili.mapper.EvenementEntityMapper;
import com.ili.service.CommandeService;
import com.ili.service.EvenementService;
import com.ili.service.MailService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@AllArgsConstructor
@Path("/evenement")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Evenement", description = "Operations about Evenement")
public class EvenementController {

    private final EvenementService evenementService;




    @GET
    @Operation(summary = "List all evenements", description = "Returns a list of all evenements")
    @APIResponse(responseCode = "200", description = "Successful operation")
    @APIResponse(responseCode = "404", description = "No evenements found")
    public List<Evenement> listAll(){
        List<Evenement> evenements = evenementService.findAll()
                .stream()
                .map(EvenementEntityMapper::mapToDto)
                .toList();
        if (evenements.isEmpty()) {
            throw new NoEvenementFoundException();
        }
        return evenements;
    }

    @POST
    @Operation(summary = "Create a new evenement", description = "Returns the created evenement")
    @APIResponse(responseCode = "201", description = "Evenement created successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Evenement create(Evenement evenement) {
        try {
            return EvenementEntityMapper.mapToDto(evenementService.create(EvenementEntityMapper.mapToEntity(evenement)));
        } catch (Exception e) {
            throw new BadRequestException("Error creating evenement");
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete an evenement", description = "Deletes an evenement by ID")
    @APIResponse(responseCode = "204", description = "Evenement deleted successfully")
    @APIResponse(responseCode = "404", description = "Evenement not found")
    public void delete(@PathParam("id") int id){
            evenementService.delete(id);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get an evenement by ID", description = "Returns a single evenement")
    @APIResponse(responseCode = "200", description = "Successful operation")
    @APIResponse(responseCode = "404", description = "Evenement not found")
    public Evenement getById(@PathParam("id") int id){
        return evenementService.getById(id)
                .map(EvenementEntityMapper::mapToDto)
                .orElseThrow(() -> new NoEvenementException(String.valueOf(id)));
    }

    @PUT
    @Operation(summary = "Update an evenement", description = "Returns the updated evenement")
    @APIResponse(responseCode = "200", description = "Evenement updated successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Evenement update(Evenement evenement){
        try{
            return EvenementEntityMapper.mapToDto(evenementService.update(EvenementEntityMapper.mapToEntity(evenement)));
        }catch(Exception e) {
            throw new BadRequestException("Error updating evenement");
        }
    }



}