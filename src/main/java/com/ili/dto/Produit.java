package com.ili.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Produit {

    private Long id;
    private String nom;
    private String fournisseur;
    private double prix;
    private boolean option;
}
