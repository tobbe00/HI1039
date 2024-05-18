package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.LeaderboardDTO;
import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.dto.RoundFromWebDTO;
import com.kth.cprtraining.mapper.Mapper;
import com.kth.cprtraining.model.PressureData;
import com.kth.cprtraining.model.Round;
import com.kth.cprtraining.model.User;
import com.kth.cprtraining.repository.PressureDataRepository;
import com.kth.cprtraining.repository.RoundRepository;
import com.kth.cprtraining.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoundServiceImpl implements RoundService {
    private RoundRepository roundRepository;
    private PressureDataRepository pressureDataRepository;
    private Mapper<Round, RoundDTO> mapper;
    private UserRepository userRepository;

    @Autowired
    public RoundServiceImpl(RoundRepository roundRepository, Mapper<Round, RoundDTO> mapper, UserRepository userRepository, PressureDataRepository pressureDataRepository) {
        this.roundRepository = roundRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.pressureDataRepository = pressureDataRepository;
    }

    @Override
    public boolean saveRound(Round round) {
        Round round2 = roundRepository.save(round);
        return round2 != null;
    }

    @Override
    public RoundDTO findRoundById(Long roundId) {
        Optional<Round> opt = roundRepository.findById(roundId);
        return opt.map(mapper::mapToDTO).orElse(null);
    }

    @Override
    public boolean saveRoundFromWeb(RoundFromWebDTO roundFromWebDTO) {
        User user = userRepository.findUserByEmail(roundFromWebDTO.getEmail());
        Round roundToSave = new Round();
        roundToSave.setUser(user);
        roundToSave.setUsername(user.getUsername());
        roundToSave.setPoints(roundFromWebDTO.getPoints());

        Round round2 = roundRepository.save(roundToSave);
        return round2 != null;
    }

    @Override
    public List<LeaderboardDTO> getLeaderboardTop100() {
        List<Round> top100Rounds = roundRepository.findTop100ByOrderByPointsDesc();
        List<LeaderboardDTO> leaderboardDTOs = new ArrayList<>();
        int i = 1;
        for (Round round : top100Rounds) {
            LeaderboardDTO dto = new LeaderboardDTO();
            dto.setUsername(round.getUsername());
            dto.setPoints(round.getPoints());
            dto.setRank(i++);
            leaderboardDTOs.add(dto);
        }
        return leaderboardDTOs;
    }

    @Override
    public boolean saveRoundWithPressures(RoundDTO roundDTO, List<Integer> pressures) {
        Round round = mapper.mapToEntity(roundDTO); // Convert DTO to entity using injected mapper
        round.setUsername(roundDTO.getUsername()); // Ensure username is set
        System.out.println("Saving round with username: " + round.getUsername());
        round = roundRepository.save(round); // Save the round

        if (round != null) {
            PressureData pd = new PressureData();
            pd.setRound(round);
            pd.setPressuresFromList(pressures); // Set the entire list as one string
            pressureDataRepository.save(pd);
            return true;
        }
        return false;
    }

}


    /*@Override
    public List<RoundDTO> findAllRounds() {
        return (List<RoundDTO>) roundRepository.findAll();
    }*/


