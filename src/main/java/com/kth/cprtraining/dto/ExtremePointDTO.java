package com.kth.cprtraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ExtremePointDTO {
    private int maxPressure;
    private int minPressure;
    private int id;
    private boolean maxBeforeMin;
    private double frequency;
    private int pointsMax;
    private int pointsMin;
}
