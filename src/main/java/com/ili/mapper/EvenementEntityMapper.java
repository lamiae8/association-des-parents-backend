package com.ili.mapper;

import com.ili.dto.Evenement;
import com.ili.model.EvenementEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EvenementEntityMapper {

    private EvenementEntityMapper() {
    }
    public static EvenementEntity mapToEntity(Evenement evenement){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return EvenementEntity.builder()
                .id(evenement.getId())
                .nom(evenement.getNom())
                .dateDebut(LocalDate.parse(evenement.getDateDebut(), dateFormatter))
                .dateFin(LocalDate.parse(evenement.getDateFin(), dateFormatter))
                .datePaiement(LocalDate.parse(evenement.getDatePaiement(), dateFormatter))
                .valider(evenement.getValider())
                .avecEleve(evenement.getAvecEleve())
                .produitEntities(
                        evenement.getProduitList()
                                .stream()
                                .map(ProduitEntityMapper::mapToEntity)
                                .toList()
                )
                .distributionEntities(
                        evenement.getDistributionList()
                                .stream()
                                .map(DistributionEntityMapper::mapToEntity)
                                .toList()
                )
                .build();
    }

    public static Evenement mapToDto(EvenementEntity evenementEntity){
        return Evenement.builder()
                .id(evenementEntity.getId())
                .nom(evenementEntity.getNom())
                .dateDebut(evenementEntity.getDateDebut().toString())
                .dateFin(evenementEntity.getDateFin().toString())
                .datePaiement(evenementEntity.getDatePaiement().toString())
                .valider(evenementEntity.getValider())
                .avecEleve(evenementEntity.getAvecEleve())
                .produitList(
                        evenementEntity.getProduitEntities()
                                .stream()
                                .map(ProduitEntityMapper::mapToDto)
                                .toList()
                )
                .distributionList(
                        evenementEntity.distributionEntities
                                .stream()
                                .map(DistributionEntityMapper::mapToDto)
                                .toList()
                )
                .build();
    }
}
