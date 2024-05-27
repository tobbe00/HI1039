package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.LeaderboardDTO;
import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.dto.RoundFromWebDTO;
import com.kth.cprtraining.mapper.Mapper;
import com.kth.cprtraining.model.FrequencyData;
import com.kth.cprtraining.model.PressureData;
import com.kth.cprtraining.model.Round;
import com.kth.cprtraining.model.User;
import com.kth.cprtraining.repository.FrequencyDataRepository;
import com.kth.cprtraining.repository.PressureDataRepository;
import com.kth.cprtraining.repository.RoundRepository;
import com.kth.cprtraining.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoundServiceImpl implements RoundService {

    private final RoundRepository roundRepository;
    private final PressureDataRepository pressureDataRepository;
    private final FrequencyDataRepository frequencyDataRepository;
    private final Mapper<Round, RoundDTO> mapper;
    private final UserRepository userRepository;

    @Autowired
    public RoundServiceImpl(RoundRepository roundRepository, Mapper<Round, RoundDTO> mapper, UserRepository userRepository, PressureDataRepository pressureDataRepository, FrequencyDataRepository frequencyDataRepository) {
        this.roundRepository = roundRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.pressureDataRepository = pressureDataRepository;
        this.frequencyDataRepository = frequencyDataRepository;
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
    public List<Integer> getPressuresForRound(Long roundId) {
        Optional<Round> roundOpt = roundRepository.findById(roundId);
        if (roundOpt.isPresent()) {
            List<PressureData> pressureDataList = pressureDataRepository.findByRound(roundOpt.get());
            if (!pressureDataList.isEmpty()) {
                // Assuming there is only one PressureData per roundId
                PressureData pressureData = pressureDataList.get(0);
                return pressureData.getPressuresAsList();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<Double> getFrequenciesForRound(Long roundId) {
        Optional<Round> roundOpt = roundRepository.findById(roundId);
        if (roundOpt.isPresent()) {
            List<FrequencyData> frequencyDataList = frequencyDataRepository.findByRound(roundOpt.get());
            if (!frequencyDataList.isEmpty()) {
                FrequencyData frequencyData = frequencyDataList.get(0); // Assuming one entry per round
                return frequencyData.getFrequenciesAsList();
            }
        }
        return new ArrayList<>();
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
            dto.setRoundId(round.getRoundId()); 
            leaderboardDTOs.add(dto);
        }
        return leaderboardDTOs;
    }

    @Override
    public List<RoundDTO> getRoundsByUsername(String username) {
        List<Round> rounds = roundRepository.findByUsernameOrderByPointsDesc(username);
        List<RoundDTO> roundDTOs = new ArrayList<>();
        for (Round round : rounds) {
            RoundDTO dto = new RoundDTO();
            dto.setUsername(round.getUsername());
            dto.setPoints(round.getPoints());

            dto.setRoundId(round.getRoundId());
            roundDTOs.add(dto);
        }
        return roundDTOs;
    }

    @Override
    public boolean saveRoundWithPressuresAndFrequencies(RoundDTO roundDTO, List<Integer> pressures, List<Double> frequencies) {
        Round round = mapper.mapToEntity(roundDTO);
        round.setUsername(roundDTO.getUsername());
        round = roundRepository.save(round);

        if (round != null) {
            PressureData pd = new PressureData();
            pd.setRound(round);
            pd.setPressuresFromList(pressures);
            pressureDataRepository.save(pd);

            FrequencyData fd = new FrequencyData();
            fd.setRound(round);
            fd.setFrequenciesFromList(frequencies);
            frequencyDataRepository.save(fd);

            return true;
        }
        return false;
    }


    
}



    


