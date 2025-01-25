package com.ili.controller;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.ili.dto.Eleve;
import com.ili.error.NoEleveException;
import com.ili.error.NoEleveFoundException;
import com.ili.mapper.EleveEntityMapper;
import com.ili.service.EleveService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Path("eleve")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Eleve", description = "Operations about Eleve")
public class EleveController {

    private final EleveService eleveService;

    @GET
    @Path("/{id}")
    @Operation(summary = "Get an eleve by ID", description = "Returns a single eleve")
    @APIResponse(responseCode = "200", description = "Successful operation")
    @APIResponse(responseCode = "404", description = "Eleve not found")
    public Eleve getById(@PathParam("id") long id) {
        return eleveService.getById(id)
                .map(EleveEntityMapper::mapToDto)
                .orElseThrow(() -> new NoEleveException(String.valueOf(id)));
    }

    @POST
    @Operation(summary = "Create a new eleve", description = "Returns the created eleve")
    @APIResponse(responseCode = "201", description = "Eleve created successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Eleve create(Eleve eleve) {
        try {
            return EleveEntityMapper.mapToDto(eleveService.create(EleveEntityMapper.mapToEntity(eleve)));
        } catch (Exception e) {
            throw new BadRequestException("Error creating eleve");
        }
    }

    @GET
    @Operation(summary = "Get all eleves", description = "Returns a list of all eleves")
    @APIResponse(responseCode = "200", description = "Successful operation")
    @APIResponse(responseCode = "404", description = "No eleves found")
    public List<Eleve> getAll(){
        List<Eleve> eleves = eleveService.getAll().stream().
                map(EleveEntityMapper::mapToDto)
                .toList();
        if (eleves.isEmpty()) {
            throw new NoEleveFoundException();
        }

        return eleves;

    }
}