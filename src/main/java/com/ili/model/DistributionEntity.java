package com.ili.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "distribution")
public class DistributionEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 80,nullable = false)
    private String distributeur;

    @Column(length = 80,nullable = false)
    private String lieu;

    @Column(nullable = false)
    private LocalDateTime dateDistribution;
}
