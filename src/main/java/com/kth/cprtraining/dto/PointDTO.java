package com.kth.cprtraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for Point information.
 * Includes details such as maximum points, minimum points, ID, and frequency.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PointDTO {
    private int pointsMax;
    private int pointsMin;
    private int id;
    private double frequency;
}
