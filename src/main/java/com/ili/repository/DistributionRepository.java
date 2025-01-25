package com.ili.repository;

import com.ili.model.DistributionEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DistributionRepository implements PanacheRepository<DistributionEntity> {
}
