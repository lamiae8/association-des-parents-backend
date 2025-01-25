package com.ili.service;

import com.ili.dto.Role;
import com.ili.model.AdulteEntity;
import com.ili.model.CommandeEntity;
import com.ili.model.EleveEntity;
import com.ili.repository.AdulteRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class AdulteServiceTest {

    @Inject
    AdulteService adulteService;
    private static final LocalDate testDate = LocalDate.of(2000, 1, 1);
    @InjectMock
    AdulteRepository adulteRepository;
    @Test
    @Order(1)
    public void testCreateAdulte() throws Exception {
        AdulteEntity mockAdulte = getMockAdulte(1L);
        System.out.println("Before create: " + mockAdulte);

        adulteService.create(mockAdulte);


        verify(adulteRepository, Mockito.times(1)).persist(any(AdulteEntity.class));
        System.out.println("After create: " + mockAdulte);
    }

    @Test
    @Order(2)
    public void testFetchAdulte() {
        AdulteEntity mockAdulte = getMockAdulte(1L);
        Mockito.when(adulteRepository.findByIdOptional(mockAdulte.getId())).thenReturn(Optional.of(mockAdulte));
        System.out.println("Mock setup for adulteId " + mockAdulte.getId() + ": " + mockAdulte);


        Optional<AdulteEntity> foundAdulte = adulteService.getById(mockAdulte.getId());


        assertThat(foundAdulte).isPresent();
        assertThat(foundAdulte.get().getNom()).isEqualTo("Nacer");
        assertThat(foundAdulte.get().getPrenom()).isEqualTo("Test");
        System.out.println("Found adulte: " + foundAdulte.get());
    }

    private static AdulteEntity getMockAdulte(Long id) {
        AdulteEntity mockAdulte = new AdulteEntity();
        mockAdulte.setId(id);
        mockAdulte.setNom("Nacer");
        mockAdulte.setPrenom("Test");
        mockAdulte.setMail("nacer.test@example.com");
        mockAdulte.setRole(Role.PARENT);
        mockAdulte.setMotDePasse("password");
        mockAdulte.setTelephone("0606060606");

        List<EleveEntity> eleves = new ArrayList<>();
        EleveEntity mockEleve = new EleveEntity();
        mockEleve.setNom("TestEleve");
        mockEleve.setPrenom("TestEleve");
        mockEleve.setDateNaissance(testDate);
        mockEleve.setNiveau("ce1");
        mockEleve.setAdulteEntities(List.of(new AdulteEntity()));
        eleves.add(mockEleve);
        mockAdulte.setEleveEntities(eleves);




        List<CommandeEntity> commandes = new ArrayList<>();
        CommandeEntity mockCommande = new CommandeEntity();
        mockCommande.setPayer(true);

        commandes.add(mockCommande);
        return mockAdulte;
    }

}
