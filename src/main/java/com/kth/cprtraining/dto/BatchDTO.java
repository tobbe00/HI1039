package com.kth.cprtraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BatchDTO {
   private int[] theBatch=new int[2];
   private int batchID;
}
