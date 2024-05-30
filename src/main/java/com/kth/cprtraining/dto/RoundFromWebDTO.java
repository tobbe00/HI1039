package com.kth.cprtraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for Round information received from the web.
 * Includes details such as round ID, points, and email of the user.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoundFromWebDTO {
    private Long roundId;

    private int points;

    private String email;
}
