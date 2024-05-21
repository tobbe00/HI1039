package com.kth.cprtraining.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
