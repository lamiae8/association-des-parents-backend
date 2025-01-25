package com.ili.repository;

import com.ili.error.PersistRepositoryException;
import com.ili.model.EvenementEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EvenementRepository implements PanacheRepository<EvenementEntity> {
    @Override
    public void persist(EvenementEntity evenementEntity){
        try {
            PanacheRepository.super.persist(evenementEntity);
        }catch(Exception e){
            throw new PersistRepositoryException(e.getMessage());
        }
    }
}
