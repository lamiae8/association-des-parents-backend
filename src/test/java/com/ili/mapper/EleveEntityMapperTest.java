package com.ili.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ili.dto.Eleve;
import com.ili.model.EleveEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EleveEntityMapperTest {

    private Eleve eleveDto;
    private EleveEntity eleveEntity;
    private final LocalDate testDate = LocalDate.of(2000, 1, 1);

    @BeforeEach
    public void setUp() {

        eleveDto = Eleve.builder()
                .nom("eleveNom")
                .prenom("ElevePrenom")
                .niveau("CP")
                .dateNaissance(testDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();


        eleveEntity = EleveEntity.builder()
                .nom("eleveNom")
                .prenom("ElevePrenom")
                .niveau("CP")
                .dateNaissance(testDate)

                .build();
    }

    @Test
    public void testMapToEntity() {
        EleveEntity mappedEntity = EleveEntityMapper.mapToEntity(eleveDto);

        assertNotNull(mappedEntity);
        assertEquals(eleveDto.getNom(), mappedEntity.getNom());
        assertEquals(eleveDto.getPrenom(), mappedEntity.getPrenom());
        assertEquals(eleveDto.getNiveau(), mappedEntity.getNiveau());
        assertEquals(testDate, mappedEntity.getDateNaissance());

    }

    @Test
    public void testMapToDto() {
        Eleve mappedDto = EleveEntityMapper.mapToDto(eleveEntity);

        assertNotNull(mappedDto);
        assertEquals(eleveEntity.getNom(), mappedDto.getNom());
        assertEquals(eleveEntity.getPrenom(), mappedDto.getPrenom());
        assertEquals(eleveEntity.getNiveau(), mappedDto.getNiveau());
        assertEquals(testDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), mappedDto.getDateNaissance());

    }
}
