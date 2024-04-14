package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.model.Round;

import java.util.List;
import java.util.Optional;

public interface RoundService {
    RoundDTO saveRound(RoundDTO round);
    RoundDTO findRoundById(Long roundId);
    /*List<RoundDTO> findAllRounds();*/
    void deleteRoundById(Long roundId);

}
