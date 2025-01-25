package com.ili.mapper;

import com.ili.dto.Commande;
import com.ili.model.CommandeEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CommandeEntityMapper {

    private CommandeEntityMapper() {
    }

    public static CommandeEntity mapToEntity(Commande commande) {
        CommandeEntity commandeEntity = new CommandeEntity();
        commandeEntity.setId(commande.getId());
        commandeEntity.setPayer(commande.isPayer());
        commandeEntity.setDistribuer(commande.isDistribuer());
        commandeEntity.setAdulte(AdulteEntityMapper.mapToEntity(commande.getAdulte()));
        commandeEntity.setEleve(commande.getEleve() == null ? null : EleveEntityMapper.mapToEntity(commande.getEleve()));
        if(commande.getCommandeProduits() != null && !commande.getCommandeProduits().isEmpty()){
            commandeEntity.setCommandeProduitEntities( commande.getCommandeProduits().stream()
                    .map(CommandeProduitMapper::mapToEntity)
                    .toList());
        }else{
            commandeEntity.setCommandeProduitEntities(List.of());
        }
        return commandeEntity;
    }

    public static Commande mapToDto(CommandeEntity commandeEntity) {

        return Commande.builder()
                .id(commandeEntity.getId())
                .payer(commandeEntity.isPayer())
                .distribuer(commandeEntity.isDistribuer())
                .adulte(AdulteEntityMapper.mapToDto(commandeEntity.getAdulte()))
                .eleve(commandeEntity.getEleve() == null ? null : EleveEntityMapper.mapToDto(commandeEntity.getEleve()))
                .evenement(commandeEntity.getEvenement().getId().intValue())
                .commandeProduits(commandeEntity.getCommandeProduitEntities().stream()
                        .map(CommandeProduitMapper::mapToDto)
                        .toList())
                .build();
    }
}
