package com.ili.service;

import com.ili.model.CommandeEntity;
import com.ili.repository.CommandeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
@Transactional
public class CommandeService {

    private final CommandeRepository commandeRepository;
    public Optional<CommandeEntity> getById(long id) {
        return commandeRepository.findByIdOptional(id);
    }

    public void delete(int id) {
        commandeRepository.deleteById((long)id);

    }

    public CommandeEntity update(CommandeEntity commande) {
        try{
            CommandeEntity commandeSaved = commandeRepository.findById(commande.getId());
            commandeSaved.setPayer(commande.isPayer());
            commandeSaved.setDistribuer(commande.isDistribuer());
            commandeSaved.setCommandeProduitEntities(commande.getCommandeProduitEntities());
            if(commande.getEleve() != null){
                commandeSaved.setEleve(commande.getEleve());
            }
            commandeRepository.persist(commandeSaved);
            return commandeSaved;
        } catch (Exception e) {
            throw new IllegalArgumentException("Commande not found");
        }
    }

    public CommandeEntity create(CommandeEntity commandeEntity) {
        try{
            commandeEntity.setId(null);
            //commandeEntity.setPayer(false);
            commandeEntity.setDistribuer(false);
            commandeRepository.persist(commandeEntity);
            return commandeEntity;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating commande");
        }
    }

    public List<CommandeEntity> getByIdAdulte(long id) {
        return commandeRepository.findByAdulteId(id);
    }

    public List<CommandeEntity> getByIdEvenement(long id) {
        return commandeRepository.findByEvenementId(id);
    }
}
