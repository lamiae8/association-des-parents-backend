package com.ili.service;

import com.ili.dto.Adulte;
import com.ili.dto.Role;
import com.ili.mapper.AdulteEntityMapper;
import com.ili.error.AccesException;
import com.ili.model.AdulteEntity;
import com.ili.model.EleveEntity;
import com.ili.model.PasswordResetToken;
import com.ili.repository.AdulteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.databind.ObjectMapper;



import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import io.smallrye.jwt.build.Jwt;
@ApplicationScoped
@AllArgsConstructor
@Transactional
public class AdulteService {

    private final AdulteRepository adulteRepository;

    public Optional<AdulteEntity> getById(long id) {
        return adulteRepository.findByIdOptional(id);
    }

    public AdulteEntity create(AdulteEntity adulte) throws Exception {
        if(adulte.getRole().equals(Role.PROFESSOR) || adulte.getRole().equals(Role.PARENT)){
            adulte.setMotDePasse(hashPassword(adulte.getMotDePasse()));
            adulte.setId(null);
            adulte.setActive((adulte.getEleveEntities() != null && !adulte.getEleveEntities().isEmpty()) ||
                    !adulte.getRole().equals(Role.PARENT));
            checkAndCreateEleve(adulte.getEleveEntities());
            adulteRepository.persist(adulte);
            return adulte;
        }else{
            throw new AccesException("création de compte Admin");
        }
    }
    public AdulteEntity createAdm(AdulteEntity adulte) throws Exception {
        if(adulte.getRole().equals(Role.ADMIN) ||  adulte.getRole().equals(Role.PROFESSOR)){
            adulte.setMotDePasse(hashPassword(adulte.getMotDePasse()));
            adulte.setId(null);
            adulte.setActive((adulte.getEleveEntities() != null && !adulte.getEleveEntities().isEmpty()) ||
                    !adulte.getRole().equals(Role.PARENT));
            checkAndCreateEleve(adulte.getEleveEntities());
            adulteRepository.persist(adulte);
            return adulte;
        }else{
            throw new AccesException("création de compte Admin");
        }
    }
    public AdulteEntity update(AdulteEntity adulte) throws Exception {
        if(adulte!=null){
            adulteRepository.persist(adulte);
            return adulte;
        }else{
            throw new AccesException("création de compte Admin");
        }
    }

    public Optional<AdulteEntity> getAdulteByMail(String mail) {
         return adulteRepository.find("mail", mail).firstResultOptional();
        }

    public void checkAndCreateEleve(List<EleveEntity> eleveEntities) {
        for (EleveEntity eleve : eleveEntities) {
            if (findEleveFromParameters(eleve, adulteRepository.listAll())) {

                eleve.persist();
            }
        }
    }

    private boolean findEleveFromParameters(EleveEntity eleveEntity, List<AdulteEntity> existingAdultes) {
        return existingAdultes == null || existingAdultes.stream()
                .noneMatch(adulte -> Objects.equals(adulte.getNom(), eleveEntity.getNom()) &&
                        Objects.equals(adulte.getPrenom(), eleveEntity.getPrenom()));
    }
    public Optional<AdulteEntity> findByEmail(String email) {
        return adulteRepository.find("mail", email).firstResultOptional();
    }

    public void delete(AdulteEntity adulte) {adulteRepository.delete(adulte);}

    public List<AdulteEntity> getAll(){
        return adulteRepository.findAll().list();
    }

    public void updateAdulte(AdulteEntity adulteEntity) throws Exception {
        if (adulteEntity.getId() == null) {
            throw new Exception("Entity must have an ID to be updated");
        }
        adulteRepository.persist(adulteEntity);
    }
    public String generateJwtToken(Adulte adulte) {
        long currentTimeInSecs = System.currentTimeMillis() / 1000;
        long expiration = currentTimeInSecs + 3600;

        Set<String> roles = new HashSet<>();
        roles.add(adulte.getRole().toString());

        return Jwt.issuer("https://localhost:8080/")
                .upn(adulte.getMail())
                .groups(roles)
                .claim("id", adulte.getId())
                .claim("nom", adulte.getNom())
                .claim("prenom", adulte.getPrenom())
                .claim("telephone", adulte.getTelephone())
                .claim("active", adulte.isActive())
                .claim("role", adulte.getRole())
                .claim("eleves",  adulte.getEleves())
                .issuedAt(currentTimeInSecs)
                .expiresAt(expiration)
                .sign();
    }
    public Response generateJwtTokenRep(Adulte adulte) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String elevesJson = objectMapper.writeValueAsString(adulte.getEleves());

            long currentTimeInSecs = System.currentTimeMillis() / 1000;
            long expiration = currentTimeInSecs + 3600;

            Set<String> roles = new HashSet<>();
            roles.add(adulte.getRole().toString());

            String token = Jwt.issuer("https://localhost:8080/")
                    .upn(adulte.getMail())
                    .groups(roles)
                    .claim("id", adulte.getId())
                    .claim("nom", adulte.getNom())
                    .claim("prenom", adulte.getPrenom())
                    .claim("telephone", adulte.getTelephone())
                    .claim("active", adulte.isActive())
                    .claim("role", adulte.getRole())
                    .claim("eleves", elevesJson)
                    .issuedAt(currentTimeInSecs)
                    .expiresAt(expiration)
                    .sign();

            JsonObject jsonToken = Json.createObjectBuilder()
                    .add("token", token)
                    .build();

            return Response.ok(jsonToken).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erreur lors de la génération du token : " + e.getMessage()).build();
        }
    }


    public Adulte authenticate(String email, String password) throws SecurityException {
        Optional<AdulteEntity> adulteEntityOptional = findByEmail(email);
        if (adulteEntityOptional.isPresent()) {
            AdulteEntity adulteEntity = adulteEntityOptional.get();

            if (BCrypt.checkpw(password, adulteEntity.getMotDePasse())) {
                Adulte adulte = AdulteEntityMapper.mapToDto(adulteEntity);
                adulte.setMotDePasse(null);
                return adulte;
            }
        }
        throw new SecurityException("Échec de l'authentification.");
    }

    private String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }


    public void createPasswordResetTokenForUser(AdulteEntity adulte, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, adulte);
        myToken.persist();
    }
    public AdulteEntity findAdulteByToken(String token) {
        PasswordResetToken passwordResetToken = PasswordResetToken.find("token", token).firstResult();
        return passwordResetToken != null ? passwordResetToken.getAdulte() : null;
    }

    public AdulteEntity changePassword(AdulteEntity adulte, String newPassword) {
        adulte.setMotDePasse(hashPassword(newPassword));
        adulteRepository.persist(adulte);
        PasswordResetToken passwordResetToken = PasswordResetToken.find("adulte", adulte).firstResult();
    if (passwordResetToken != null) {
        passwordResetToken.delete();
    }
        return adulte;
    }
}
