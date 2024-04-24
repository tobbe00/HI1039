package com.kth.cprtraining.dto;


import com.kth.cprtraining.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoundDTO {
    private Long roundId;

    private int points;

    //private Long userId;
    private long userId;
}
