package com.ili.controller;

import com.ili.dto.Adulte;
import com.ili.dto.Eleve;
import com.ili.dto.Role;
import com.ili.error.NoAdulteException;
import com.ili.error.NoAdulteFoundException;
import com.ili.mapper.AdulteEntityMapper;
import com.ili.model.AdulteEntity;
import com.ili.model.EleveEntity;
import com.ili.repository.EleveRepository;
import com.ili.service.AdulteService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Path("/adulte")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Adulte", description = "Operations about Adulte")
@Transactional
public class AdulteController {

    private final AdulteService adulteService;
    private final EleveRepository eleveRepository;
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");



    @GET
    @Path("/{id}")
    @Operation(summary = "Get an adulte by ID", description = "Returns a single adulte")
    @APIResponse(responseCode = "200", description = "Successful operation")
    @APIResponse(responseCode = "404", description = "Adulte not found")
    public Adulte getById(@PathParam("id") long id) {
        return adulteService.getById(id)
                .map(AdulteEntityMapper::mapToDto)
                .orElseThrow(() -> new NoAdulteException(String.valueOf(id)));
    }

    @GET
    @Path("/mail/{mail}")
    @Operation(summary = "Get an adulte by Mail", description = "Returns a single adulte")
    @APIResponse(responseCode = "200", description = "Successful operation")
    @APIResponse(responseCode = "404", description = "Adulte not found")
    public Adulte getByMail(@PathParam("mail") String mail) {
        return adulteService.getAdulteByMail(mail)
                .map(AdulteEntityMapper::mapToDto)
                .orElseThrow(() -> new NoAdulteException(String.valueOf(mail)));
    }
    @GET
    @Operation(summary = "Get all adultes", description = "Returns a list of all adultes")
    @APIResponse(responseCode = "200", description = "Successful operation")
    @APIResponse(responseCode = "404", description = "No adultes found")
    public List<Adulte> getAll() {
        List<Adulte> adultes = adulteService.getAll().stream()
                .map(AdulteEntityMapper::mapToDto)
                .toList();

        if (adultes.isEmpty()) {
            throw new NoAdulteFoundException();
        }
        return adultes;
    }

    @POST
    @Operation(summary = "Create a new adulte", description = "Returns the created adulte")
    @APIResponse(responseCode = "201", description = "Adulte created successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Adulte create(Adulte adulteDTO) {
        try {
            if(adulteDTO.getRole().equals(Role.PARENT)){
                return AdulteEntityMapper.mapToDto(adulteService.create(AdulteEntityMapper.mapToEntity(adulteDTO)));
            }
            return AdulteEntityMapper.mapToDto(adulteService.createAdm(AdulteEntityMapper.mapToEntity(adulteDTO)));
        } catch (Exception e) {
            throw new BadRequestException("Error creating adulte");
        }
    }



    @PUT
    @Operation(summary = "Update an adulte", description = "Returns the updated adulte")
    @APIResponse(responseCode = "201", description = "Adulte updated successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Adulte update(Adulte adulteDTO) {
        try {
            Optional<AdulteEntity> existingAdulte = adulteService.getAdulteByMail(adulteDTO.getMail());
            if (existingAdulte.isPresent()) {
                AdulteEntity adulteEntity = existingAdulte.get();
                adulteEntity.setNom(adulteDTO.getNom());
                adulteEntity.setPrenom(adulteDTO.getPrenom());
                adulteEntity.setRole(adulteDTO.getRole());
                adulteEntity.setTelephone(adulteDTO.getTelephone());

                List<EleveEntity> eleveEntities = new ArrayList<>();
                for (Eleve eleve : adulteDTO.getEleves()) {
                    EleveEntity eleveEntity;
                    if (eleve.getId() != null) {
                        Optional<EleveEntity> existingEleve = Optional.ofNullable(eleveRepository.findById(eleve.getId()));
                        if (existingEleve.isPresent()) {
                            eleveEntity = existingEleve.get();
                            eleveEntity.setPrenom(eleve.getPrenom());
                            eleveEntity.setNiveau(eleve.getNiveau());
                            eleveEntity.setDateNaissance(LocalDate.parse(eleve.getDateNaissance(),df));
                            eleveEntity.setNom(eleve.getNom());
                        } else {
                            throw new Exception("Élève non trouvé pour l'ID : " + eleve.getId());
                        }
                    } else {
                        eleveEntity = new EleveEntity();
                        eleveEntity.setPrenom(eleve.getPrenom());
                        eleveEntity.setNiveau(eleve.getNiveau());
                        eleveEntity.setDateNaissance(LocalDate.parse(eleve.getDateNaissance(),df));
                        eleveEntity.setNom(eleve.getNom());
                        eleveEntity.setAdulteEntities(List.of(adulteEntity));
                        eleveRepository.persist(eleveEntity);
                    }
                    eleveEntities.add(eleveEntity);
                }
                adulteEntity.setEleveEntities(eleveEntities);
                adulteEntity.setActive(!adulteDTO.getEleves().isEmpty());
                return AdulteEntityMapper.mapToDto(adulteService.update(adulteEntity));
            } else {
                throw new Exception("Adulte not found with email: " + adulteDTO.getMail());
            }
        } catch (Exception e) {

            throw new BadRequestException("Error updating adulte: " + e.getMessage());
        }
    }
    @PUT
    @Path("/role")
    @Operation(summary = "Update an adult's role", description = "Returns the updated adult with the new role")
    @APIResponse(responseCode = "201", description = "Adult role updated successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Adulte updateRole(Adulte adulteDTO) {
        try {
            Optional<AdulteEntity> existingAdulte = adulteService.getAdulteByMail(adulteDTO.getMail());
            if (existingAdulte.isPresent()) {
                AdulteEntity adulteEntity = existingAdulte.get();
                adulteEntity.setRole(adulteDTO.getRole());

                return AdulteEntityMapper.mapToDto(adulteService.update(adulteEntity));
            } else {
                throw new Exception("Adulte not found with email: " + adulteDTO.getMail());
            }
        } catch (Exception e) {
            throw new BadRequestException("Error updating adult's role: " + e.getMessage());
        }
    }


}
