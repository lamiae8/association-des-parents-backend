package com.ili.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "produit")
public class ProduitEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 40,nullable = false)
    private String nom;

    @Column(length = 40,nullable = false)
    private String fournisseur;

    @Column(nullable = false)
    private double prix;

    @Column(nullable = false)
    private boolean option;

    @ManyToMany(mappedBy = "produitEntities")
    List<EvenementEntity> evenementEntities;

}
