package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.LeaderboardDTO;
import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.model.Round;

import java.util.List;

public interface RoundService {
    boolean saveRound(Round round);
    RoundDTO findRoundById(Long roundId);

    List<LeaderboardDTO> getLeaderboardTop100();
    /*List<RoundDTO> findAllRounds();
    void deleteRoundById(Long roundId);
    */

}
