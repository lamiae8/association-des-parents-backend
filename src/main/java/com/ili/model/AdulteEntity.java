package com.ili.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ili.dto.Role;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "adulte")
public class AdulteEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 40,nullable = false)
    private String nom;

    @Column(length = 40,nullable = false)
    private String prenom;

    @Column(length = 80,nullable = false,unique = true)
    private String mail;

    @Column(length = 11,nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 100,nullable = false)
    private String motDePasse;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private boolean active;

    @ManyToMany
    @JoinTable(
            name = "adulte_eleve",
            joinColumns = @JoinColumn(name = "adulte_id"),
            inverseJoinColumns = @JoinColumn(name = "eleve_id"))
    List<EleveEntity> eleveEntities;

}
