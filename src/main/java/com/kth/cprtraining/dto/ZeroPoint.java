package com.kth.cprtraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ZeroPoint {
    int zero;
    String mode;
    int avg;

    public int getZeroPointInt() {
        return zero;
    }
}
