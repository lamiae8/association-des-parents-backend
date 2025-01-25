package com.ili.repository;

import com.ili.error.PersistRepositoryException;
import com.ili.model.ProduitEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProduitRepository implements PanacheRepository<ProduitEntity> {
    @Override
    public void persist(ProduitEntity produitEntity){
        try{
            PanacheRepository.super.persist(produitEntity);
        }catch (Exception e){
            throw new PersistRepositoryException(e.getMessage());
        }
    }
}
