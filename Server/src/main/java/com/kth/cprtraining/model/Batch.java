package com.kth.cprtraining.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Batch class representing a batch of data with an ID.
 * This class includes an array to store batch data and provides methods to set and get data at specific indices.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Batch {
    
    private int[] theBatch=new int[2];
    private int batchID;

    public void setTheBatchATIndex(int theBatchInt,int index) {
        this.theBatch[index] = theBatchInt;
    }

    public int getBatchIntAtID(int i) {
        return this.theBatch[i];
    }
}

