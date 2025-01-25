package com.ili.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class Distribution {
    private Long id;
    private String distributeur;
    private String lieu;
    private String dateDistribution;
}
