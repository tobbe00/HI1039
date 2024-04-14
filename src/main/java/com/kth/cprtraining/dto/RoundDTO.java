package com.kth.cprtraining.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundDTO {
    private Long roundId;

    private int points;

    private UserDTO user;
}
