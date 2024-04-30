package com.kth.cprtraining.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Batch {
    
    private int[] theBatch;
    private int batchID;
}
