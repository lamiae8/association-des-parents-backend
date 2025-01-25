package com.ili.mapper;

import com.ili.dto.Eleve;
import com.ili.model.EleveEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class EleveEntityMapper {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static EleveEntity mapToEntity(Eleve eleve){

        return EleveEntity.builder()
                .id(eleve.getId())
                .nom(eleve.getNom())
                .prenom(eleve.getPrenom())
                .niveau(eleve.getNiveau())
                .dateNaissance(LocalDate.parse(eleve.getDateNaissance(),df))
                .build();
    }

    public static Eleve mapToDto(EleveEntity eleveEntity){
        String dateNaissanceStr = null;
        if (eleveEntity.getDateNaissance() != null) {
            dateNaissanceStr = df.format(eleveEntity.getDateNaissance());
        }

        return Eleve.builder()
                .id(eleveEntity.getId())
                .nom(eleveEntity.getNom())
                .prenom(eleveEntity.getPrenom())
                .niveau(eleveEntity.getNiveau())
                .dateNaissance(dateNaissanceStr)
                .build();
    }
}
