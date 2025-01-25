package com.ili.repository;

import com.ili.error.PersistRepositoryException;
import com.ili.model.CommandeEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CommandeRepository implements PanacheRepository<CommandeEntity> {

    @Override
    public void persist(CommandeEntity commande) {
        try {
            PanacheRepository.super.persist(commande);
        } catch (Exception e) {
            throw new PersistRepositoryException(e.getMessage());
        }
    }

    public List<CommandeEntity> findByAdulteId(Long id){
        return find("adulte.id", id).list();
    }

    public List<CommandeEntity> findByEvenementId(long id) {
        return find("evenement.id", id).list();
    }
}
