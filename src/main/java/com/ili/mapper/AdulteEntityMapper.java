package com.ili.mapper;
import com.ili.dto.Adulte;
import com.ili.model.AdulteEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.Optional;

@ApplicationScoped
public class AdulteEntityMapper {

    public static AdulteEntity mapToEntity(Adulte adulte) {

        return AdulteEntity.builder()
                .id(adulte.getId())
                .nom(adulte.getNom())
                .prenom(adulte.getPrenom())
                .mail(adulte.getMail())
                .role(adulte.getRole())
                .motDePasse(adulte.getMotDePasse())
                .telephone(adulte.getTelephone())
                .active(adulte.isActive())
                .eleveEntities(
                        Optional.ofNullable(adulte.getEleves())
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .map(EleveEntityMapper::mapToEntity)
                                .toList()
                )
                .build();
    }

    public static Adulte mapToDto(AdulteEntity adulteEntity) {
        return Adulte.builder()
                .id(adulteEntity.getId())
                .nom(adulteEntity.getNom())
                .prenom(adulteEntity.getPrenom())
                .mail(adulteEntity.getMail())
                .role(adulteEntity.getRole())
                .motDePasse(adulteEntity.getMotDePasse())
                .telephone(adulteEntity.getTelephone())
                .eleves(
                        Optional.ofNullable(adulteEntity.getEleveEntities())
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .map(EleveEntityMapper::mapToDto)
                                .toList())
                .active(adulteEntity.isActive())
                .build();
    }
}
