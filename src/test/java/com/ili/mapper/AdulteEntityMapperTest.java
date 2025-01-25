package com.ili.mapper;

import com.ili.dto.Adulte;
import com.ili.dto.Eleve;
import com.ili.dto.Role;
import com.ili.model.AdulteEntity;
import com.ili.model.EleveEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class AdulteEntityMapperTest {


    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Test
    public void testMapToEntity() {
        Adulte adulteDto = new Adulte();
        adulteDto.setNom("Test Nom");
        adulteDto.setPrenom("Test Prenom");
        adulteDto.setMail("test@example.com");
        adulteDto.setRole(Role.PARENT);
        adulteDto.setMotDePasse("secret");
        adulteDto.setTelephone("1234567890");
        adulteDto.setEleves(eleves());

        AdulteEntity entity = AdulteEntityMapper.mapToEntity(adulteDto);

        assertThat(entity.getNom()).isEqualTo(adulteDto.getNom());
        assertThat(entity.getPrenom()).isEqualTo(adulteDto.getPrenom());
        assertThat(entity.getMail()).isEqualTo(adulteDto.getMail());
        assertThat(entity.getRole()).isEqualTo(adulteDto.getRole());
        assertThat(entity.getTelephone()).isEqualTo(adulteDto.getTelephone());
    }

    @Test
    public void testMapToDto() {
        AdulteEntity adulteEntity = getAdulte();

        Adulte dto = AdulteEntityMapper.mapToDto(adulteEntity);

        assertThat(dto.getNom()).isEqualTo(adulteEntity.getNom());
        assertThat(dto.getPrenom()).isEqualTo(adulteEntity.getPrenom());
        assertThat(dto.getMail()).isEqualTo(adulteEntity.getMail());
        assertThat(dto.getRole()).isEqualTo(adulteEntity.getRole());
        assertThat(dto.getMotDePasse()).isEqualTo(adulteEntity.getMotDePasse());
        assertThat(dto.getTelephone()).isEqualTo(adulteEntity.getTelephone());
    }

    private static AdulteEntity getAdulte() {
        AdulteEntity adulteEntity = new AdulteEntity();
        adulteEntity.setNom("Test Nom");
        adulteEntity.setPrenom("Test Prenom");
        adulteEntity.setMail("test@example.com");
        adulteEntity.setRole(Role.PARENT);
        adulteEntity.setTelephone("1234567890");
        adulteEntity.setEleveEntities(elevesEntities());
        return adulteEntity;
    }

    private List<Eleve> eleves(){
        Eleve eleve1 = new Eleve(1L,"test1","test1","01/01/2015","ce1");
        Eleve eleve2 = new Eleve(2L,"test2","test2","02/01/2015","ce1");
        return List.of(eleve1,eleve2);
    }

    private static List<EleveEntity> elevesEntities(){
        EleveEntity eleve1 = new EleveEntity(1L,"test1","test1","ce1",LocalDate.parse("01-01-2015",formatter),List.of());
        EleveEntity eleve2 = new EleveEntity(2L,"test2","test2","ce1",LocalDate.parse("02-01-2015",formatter),List.of());
        return List.of(eleve1,eleve2);
    }
}
