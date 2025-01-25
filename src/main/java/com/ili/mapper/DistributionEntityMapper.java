package com.ili.mapper;

import com.ili.dto.Distribution;
import com.ili.model.DistributionEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DistributionEntityMapper {

    private DistributionEntityMapper() {
    }
    public static DistributionEntity mapToEntity(Distribution distribution){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


        return DistributionEntity.builder()
                .id(distribution.getId())
                .lieu(distribution.getLieu())
                .dateDistribution(LocalDateTime.parse(distribution.getDateDistribution(), dateTimeFormatter))
                .distributeur(distribution.getDistributeur())
                .build();
    }

    public static Distribution mapToDto(DistributionEntity distributionEntity){
        return Distribution.builder()
                .id(distributionEntity.getId())
                .lieu(distributionEntity.getLieu())
                .dateDistribution(distributionEntity.getDateDistribution().toString())
                .distributeur(distributionEntity.getDistributeur())
                .build();
    }
}
