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
public class Commande {
    private Long id;
    private boolean payer;
    private boolean distribuer;
    private int evenement;
    private Adulte adulte;
    private Eleve eleve;
    private List<CommandeProduit> commandeProduits;
}
