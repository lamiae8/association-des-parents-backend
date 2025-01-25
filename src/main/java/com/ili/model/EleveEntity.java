package com.ili.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "eleve")
public class EleveEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40,nullable = false)
    private String nom;

    @Column(length = 40,nullable = false)
    private String prenom;

    @Column(length = 20,nullable = false)
    private String niveau;

    @Column(nullable = false)
    private LocalDate dateNaissance;

    @ManyToMany(mappedBy = "eleveEntities")
    List<AdulteEntity> adulteEntities;

    @Override
    public String toString() {
        String adulteNoms = adulteEntities != null ? adulteEntities.stream()
                .map(AdulteEntity::getNom)
                .collect(Collectors.joining(", ", "[", "]")) : "null";

        return "EleveEntity{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", niveau='" + niveau + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", adulteNoms=" + adulteNoms +
                '}';
    }

}
