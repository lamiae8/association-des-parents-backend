package com.ili.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "commande")
public class CommandeEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private boolean payer;

    @Column(nullable = false)
    private boolean distribuer;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="commande_id")
    public List<CommandeProduitEntity> commandeProduitEntities;

    @ManyToOne
    @JoinColumn(name = "adulte_id", nullable = false)
    private AdulteEntity adulte;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private EleveEntity eleve;

    @ManyToOne
    @JoinColumn(name = "evenement_id", nullable = false)
    private EvenementEntity evenement;

}
