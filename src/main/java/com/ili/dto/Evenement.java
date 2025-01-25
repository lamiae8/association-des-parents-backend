package com.ili.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Evenement {

    private Long id;
    private String nom;
    private String dateDebut;
    private String dateFin;
    private String datePaiement;
    private List<Distribution> distributionList;
    private List<Produit> produitList;
    private Boolean valider;
    private Boolean avecEleve;

}
