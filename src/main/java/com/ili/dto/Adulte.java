package com.ili.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Adulte {
    private Long id;
    private String nom;
    private String prenom;
    private String mail;
    private Role role;
    private String motDePasse;
    private String telephone;
    private List<Eleve> eleves;
    private boolean active;
}