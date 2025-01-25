package com.ili.mapper;

import com.ili.dto.Produit;
import com.ili.model.ProduitEntity;


public class ProduitEntityMapper {
    public static ProduitEntity mapToEntity(Produit produit){
        return ProduitEntity.builder()
                .id(produit.getId())
                .nom(produit.getNom())
                .fournisseur(produit.getFournisseur())
                .prix(produit.getPrix())
                .option(produit.isOption())
                .build();
    }

    public static Produit mapToDto(ProduitEntity produitEntity){
        return Produit.builder()
                .id(produitEntity.getId())
                .nom(produitEntity.getNom())
                .fournisseur(produitEntity.getFournisseur())
                .prix(produitEntity.getPrix())
                .option(produitEntity.isOption())
                .build();
    }
}
