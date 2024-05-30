package com.kth.cprtraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ExtremePointDTO is a Data Transfer Object for transporting extreme point data.
 * This DTO includes identification and pressure details for extreme points.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ExtremePointDTO {
    private int id;
    private int pressure;
}
