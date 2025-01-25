package com.ili.mapper;

import com.ili.dto.CommandeProduit;
import com.ili.model.CommandeProduitEntity;

public class CommandeProduitMapper {

    public static CommandeProduitEntity mapToEntity(CommandeProduit dto) {
        return CommandeProduitEntity.builder()
                .produit(ProduitEntityMapper.mapToEntity(dto.getProduit()))
                .quantite(dto.getQuantite())
                .build();
    }

    public static CommandeProduit mapToDto(CommandeProduitEntity entity) {

        return CommandeProduit.builder()
                .produit(ProduitEntityMapper.mapToDto(entity.getProduit()))
                .quantite(entity.getQuantite())
                .build();
    }
}

