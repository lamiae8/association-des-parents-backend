package com.ili.repository;

import com.ili.error.PersistRepositoryException;
import com.ili.model.CommandeProduitEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped

public class CommandeProduitRepository implements PanacheRepository<CommandeProduitEntity> {

    @Override
    public void persist(CommandeProduitEntity commandeProduit) {
        try {
            PanacheRepository.super.persist(commandeProduit);
        } catch (Exception e) {
            throw new PersistRepositoryException(e.getMessage());
        }
    }
}
