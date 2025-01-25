package com.ili.service;

import com.ili.error.DateException;
import com.ili.error.EvenementCreateError;
import com.ili.model.DistributionEntity;
import com.ili.model.EvenementEntity;
import com.ili.model.ProduitEntity;
import com.ili.repository.DistributionRepository;
import com.ili.repository.EvenementRepository;
import com.ili.repository.ProduitRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
public class EvenementService {
    private final EvenementRepository evenementRepository;
    private final ProduitRepository produitRepository;
    private final DistributionRepository distributionRepository;

    public List<EvenementEntity> findAll(){
        return evenementRepository.listAll();
    }

    public Optional<EvenementEntity> getById(int id){
        return evenementRepository.findByIdOptional((long)id);
    }

    @Transactional
    public EvenementEntity create(EvenementEntity evenementEntity) throws EvenementCreateError {
        try {
            evenementEntity.setValider(false);
            evenementEntity.setId(null);
            validateEventAndDistributionDates(evenementEntity);
            checkAndCreateProduits(evenementEntity.getProduitEntities());
            evenementRepository.persist(evenementEntity);
            return evenementEntity;
        } catch (IllegalArgumentException e) {
            throw new EvenementCreateError(e.getMessage());
        }
    }

    public void checkAndCreateProduits(List<ProduitEntity> produitEntities) {
        for (ProduitEntity produit : produitEntities) {
            if (findProduitFromParameters(produit, produitRepository.listAll())) {
                produit.persist();
            }
        }
    }

    private boolean findProduitFromParameters(ProduitEntity produitEntity, List<ProduitEntity> existingProducts) {
        return existingProducts == null || existingProducts.stream()
                .noneMatch(produit -> Objects.equals(produit.getNom(), produitEntity.getNom()) &&
                        Objects.equals(produit.getFournisseur(), produitEntity.getFournisseur()));
    }

    private void validateEventAndDistributionDates(EvenementEntity evenement){

        List<DistributionEntity> distributions = evenement.getDistributionEntities();
        //Validation des dates
        if (evenement.getDateDebut().isAfter(evenement.getDateFin())) {
            throw new DateException("Date de début doit être avant la date de fin de l'événement.");
        }
        if (evenement.getDateFin().isAfter(evenement.getDatePaiement())) {
            throw new DateException("Date de fin doit être avant la date de paiement de l'événement.");
        }
        for (DistributionEntity distribution : distributions) {
            if (distribution.getDateDistribution().isBefore(evenement.getDatePaiement().atStartOfDay())) {
                throw new DateException("Date de la distribution doit être après la date de paiement de l'événement.");
            }
        }
    }

    @Transactional
    public void delete(int id){
        evenementRepository.delete(evenementRepository.findById((long)id));
    }

    @Transactional
    public EvenementEntity update(EvenementEntity evenement) throws EvenementCreateError {
        try {
            EvenementEntity produitSaved = evenementRepository.findById(evenement.getId());
            produitSaved.setNom(evenement.getNom());
            produitSaved.setValider(evenement.getValider());
            produitSaved.setDateDebut(evenement.getDateDebut());
            produitSaved.setDateFin(evenement.getDateFin());
            produitSaved.setDatePaiement(evenement.getDatePaiement());
            produitSaved.setAvecEleve(evenement.getAvecEleve());
            checkAndUpdateProduits(evenement.getProduitEntities());
            checkAndUpdateDistribution(evenement.getDistributionEntities());
            evenementRepository.persist(produitSaved);
            return produitSaved;
        } catch (IllegalArgumentException e) {
            throw new EvenementCreateError(e.getMessage());
        }
    }


        public void checkAndUpdateProduits(List<ProduitEntity> produitEntities) {
            for (ProduitEntity produit : produitEntities) {
                ProduitEntity produitSaved = produitRepository.findById(produit.getId());
                if (produitSaved == null) {
                    produit.persist();
                } else {
                    updateProduits(produitSaved, produit);
                }
            }
        }

        private void updateProduits(ProduitEntity produitSaved, ProduitEntity produitEntity) {
            produitSaved.setNom(produitEntity.getNom());
            produitSaved.setFournisseur(produitEntity.getFournisseur());
            produitSaved.setPrix(produitEntity.getPrix());
            produitSaved.setOption(produitEntity.isOption());
            produitSaved.persist();
        }

    public void checkAndUpdateDistribution(List<DistributionEntity> distributionEntities) {
        for (DistributionEntity distribution : distributionEntities) {
            DistributionEntity distributionSaved = distributionRepository.findById(distribution.getId());
            if (distributionSaved == null) {
                distribution.persist();
            } else {
                updateDistribution(distributionSaved, distribution);
            }
        }
    }

    private void updateDistribution(DistributionEntity distributionSaved, DistributionEntity distributionEntity) {
        distributionSaved.setDateDistribution(distributionEntity.getDateDistribution());
        distributionSaved.setLieu(distributionEntity.getLieu());
        distributionSaved.setDistributeur(distributionEntity.getDistributeur());
        distributionSaved.persist();
    }
}
