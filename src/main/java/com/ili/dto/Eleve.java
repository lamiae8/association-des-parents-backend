package com.ili.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class Eleve {
    private Long id;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String niveau;
}
