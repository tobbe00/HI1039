package com.kth.cprtraining.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * LeaderboardDTO is a Data Transfer Object for transporting leaderboard-related data.
 * This DTO includes user details and scoring information relevant to leaderboard rankings.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LeaderboardDTO {
    private String username;
    private int points;
    private int rank;
    private long roundId;


}
