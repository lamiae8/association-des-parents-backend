package com.ili.repository;

import com.ili.error.PersistRepositoryException;
import com.ili.model.AdulteEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AdulteRepository implements PanacheRepository<AdulteEntity> {

    @Override
    public void persist(AdulteEntity adulte) {
        try {
            PanacheRepository.super.persist(adulte);
        } catch (Exception e) {
            throw new PersistRepositoryException(e.getMessage());
        }
    }

    public AdulteEntity findByMail(String mail) {
        return find("adulte.mail", mail).firstResult();
    }


}
