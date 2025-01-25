package com.ili.repository;

import com.ili.error.PersistRepositoryException;
import com.ili.model.EleveEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EleveRepository implements PanacheRepository<EleveEntity> {

    @Override
    public void persist(EleveEntity eleve) {
        try {
            PanacheRepository.super.persist(eleve);
        }catch(Exception e){
            throw new PersistRepositoryException(e.getMessage());
        }
    }
}
