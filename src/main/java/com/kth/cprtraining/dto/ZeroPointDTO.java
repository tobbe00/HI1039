package com.kth.cprtraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for zero point information.
 * Includes details such as the zero point value, mode, and average.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ZeroPointDTO {
    int zero;
    String mode;
    int avg;

    public int getZeroPointInt() {
        return zero;
    }

    public int getAvgInt() {
        return avg;
    }
}
