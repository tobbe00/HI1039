package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.LeaderboardDTO;
import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.mapper.Mapper;
import com.kth.cprtraining.model.Round;
import com.kth.cprtraining.repository.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class RoundServiceImpl implements RoundService{
    private RoundRepository roundRepository;
    private Mapper<Round, RoundDTO> mapper;


    public RoundServiceImpl(RoundRepository roundRepository, Mapper<Round, RoundDTO> mapper) {
        this.roundRepository = roundRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean saveRound(Round round) {
        //Round round = mapper.mapToEntity(roundDto);
        //roundDto = mapper.mapToDTO(roundRepository.save(round));
        Round round2 = roundRepository.save(round);
        if (round2 != null)
            return true;

        return false;
    }

    @Override
    public RoundDTO findRoundById(Long roundId) {
        Optional<Round> opt = roundRepository.findById(roundId);
        RoundDTO roundDTO = null;
        if(opt.isPresent()){
            roundDTO = mapper.mapToDTO(opt.get());
        }
        return roundDTO;
    }

    @Override
    public List<LeaderboardDTO> fillLeaderboard() {
        List<Round> rounds = roundRepository.findTop100ByOrderByPointsDesc();
        List<LeaderboardDTO> leaderboardDTOs = new ArrayList<>();
        int i=0;
        for (Round round : rounds) {
            i++;
            LeaderboardDTO dto = new LeaderboardDTO();
            dto.setUsername(round.getUsername());
            dto.setPoints(round.getPoints());
            dto.setRank(i);
            leaderboardDTOs.add(dto);
        }

        return leaderboardDTOs;
    }

    /*@Override
    public List<RoundDTO> findAllRounds() {
        return (List<RoundDTO>) roundRepository.findAll();
    }*/

}
