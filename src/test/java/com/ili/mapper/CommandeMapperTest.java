package com.ili.mapper;
import com.ili.dto.Adulte;
import com.ili.dto.Commande;
import com.ili.model.AdulteEntity;
import com.ili.model.CommandeEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class CommandeMapperTest {
    @Inject
    CommandeEntityMapper commandeEntityMapper;
    
//    @Test
//    public void testMapToEntity(){
//        Commande commande = new Commande(1L,false,new Adulte(), Collections.emptyList());
//        CommandeEntity commandeEntity = CommandeEntityMapper.mapToEntity(commande);
//
//
//        assertThat(commandeEntity.isPayer()).isEqualTo(commande.isPayer());
//
//
//    }
//
//    @Test
//    public void testMapToDto(){
//        CommandeEntity commandeEntity = new CommandeEntity();
//        commandeEntity.setId(1L);
//        commandeEntity.setPayer(false);
//        commandeEntity.setAdulte(new AdulteEntity());
//        commandeEntity.setCommandeProduitEntities(Collections.emptyList());
//
//        Commande commande = CommandeEntityMapper.mapToDto(commandeEntity);
//
//        assertThat(commande.isPayer()).isEqualTo(commandeEntity.isPayer());
//        assertThat(commande.getId()).isEqualTo(commandeEntity.getId());
//    }
}
