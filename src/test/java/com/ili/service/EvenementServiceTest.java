package com.ili.service;

import com.ili.model.DistributionEntity;
import com.ili.model.EvenementEntity;
import com.ili.model.ProduitEntity;
import com.ili.repository.EvenementRepository;
import com.ili.repository.ProduitRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@QuarkusTest
@Transactional
class EvenementServiceTest {

    @Mock
    private EvenementRepository evenementRepository;

    @Mock
    private ProduitRepository produitRepository;
    @InjectMocks
    private EvenementService evenementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEvent_Success(){

        DateTimeFormatter dateFormatterDistrib = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        DistributionEntity distributionEntity = DistributionEntity.builder()
                .lieu("lieu1")
                .dateDistribution(LocalDateTime.parse("20/01/2022 15:12", dateFormatterDistrib))
                .distributeur("Mr titi")
                .build();

         ProduitEntity produitEntity = ProduitEntity.builder()
                 .nom("produit1")
                 .fournisseur("fourn1")
                 .option(false)
                 .prix(14.25)
                 .build();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        EvenementEntity evenementEntity = getEvenementEntitySuccess(dateFormatter, distributionEntity, produitEntity);

        doNothing().when(evenementRepository).persist(any(EvenementEntity.class));
        evenementService.create(evenementEntity);

        verify(evenementRepository, times(1)).persist(evenementEntity);
    }

    private static EvenementEntity getEvenementEntitySuccess(DateTimeFormatter dateFormatter, DistributionEntity distributionEntity, ProduitEntity produitEntity) {
        EvenementEntity evenementEntity = new EvenementEntity();

        evenementEntity.setNom("event1");
        evenementEntity.setDateDebut(LocalDate.parse("01/01/2022", dateFormatter));
        evenementEntity.setDateFin(LocalDate.parse("05/01/2022", dateFormatter));
        evenementEntity.setDatePaiement(LocalDate.parse("15/01/2022", dateFormatter));
        evenementEntity.setValider(false);
        evenementEntity.setDistributionEntities(List.of(distributionEntity));
        evenementEntity.setProduitEntities(List.of(produitEntity));
        evenementEntity.setAvecEleve(false);
        return evenementEntity;
    }

    @Test
    void createEvent_InvalidDates_ThrowsException() {
        DateTimeFormatter dateFormatterDistrib = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        DistributionEntity distributionEntity = DistributionEntity.builder()
                .lieu("lieu1")
                .dateDistribution(LocalDateTime.parse("20/01/2022 15:12", dateFormatterDistrib))
                .distributeur("Mr titi")
                .build();

        ProduitEntity produitEntity = ProduitEntity.builder()
                .nom("produit1")
                .fournisseur("fourn1")
                .option(false)
                .prix(14.25)
                .build();


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        EvenementEntity evenementEntity = new EvenementEntity();
        evenementEntity.setNom("event1");
        evenementEntity.setDateDebut(LocalDate.parse("01/01/2024", dateFormatter));
        evenementEntity.setDateFin(LocalDate.parse("05/01/2022", dateFormatter));
        evenementEntity.setDatePaiement(LocalDate.parse("15/01/2022", dateFormatter));
        evenementEntity.setValider(false);
        evenementEntity.setDistributionEntities(List.of(distributionEntity));
        evenementEntity.setProduitEntities(List.of(produitEntity));
        evenementEntity.setAvecEleve(false);

        Exception exception = assertThrows(Exception.class, () -> evenementService.create(evenementEntity));

        String expectedMessage = "Date de début doit être avant la date de fin de l'événement.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}