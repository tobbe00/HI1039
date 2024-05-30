package com.kth.cprtraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for Round information.
 * Includes details such as round ID, points, user ID, and username.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundDTO {
    private Long roundId;
    private int points;
    private Long userId;
    private String username;
}
