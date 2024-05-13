package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.LeaderboardDTO;
import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.dto.RoundFromWebDTO;
import com.kth.cprtraining.mapper.Mapper;
import com.kth.cprtraining.model.Round;
import com.kth.cprtraining.model.User;
import com.kth.cprtraining.repository.RoundRepository;
import com.kth.cprtraining.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class RoundServiceImpl implements RoundService{
    private RoundRepository roundRepository;
    private Mapper<Round, RoundDTO> mapper;
    private UserRepository userRepository;

    public RoundServiceImpl(RoundRepository roundRepository, Mapper<Round, RoundDTO> mapper,UserRepository userRepository) {
        this.roundRepository = roundRepository;
        this.mapper = mapper;
        this.userRepository=userRepository;
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
    public boolean saveRoundFromWeb(RoundFromWebDTO roundFromWebDTO) {
        User user=userRepository.findUserByEmail(roundFromWebDTO.getEmail());
        Round roundToSave=new Round();
        roundToSave.setUser(user);
        roundToSave.setUsername(user.getUsername());
        roundToSave.setPoints(roundFromWebDTO.getPoints());


        Round round2 = roundRepository.save(roundToSave);
        if (round2 != null)
            return true;

        return false;
    }

    @Override
    public List<LeaderboardDTO> getLeaderboardTop100() {
        List<Round> top100Rounds = roundRepository.findTop100ByOrderByPointsDesc();
        List<LeaderboardDTO> leaderboardDTOs = new ArrayList<>();
        int i=1;
        for (Round round : top100Rounds) {
            LeaderboardDTO dto = new LeaderboardDTO();
            dto.setUsername(round.getUsername());
            dto.setPoints(round.getPoints());
            dto.setRank(i++);
            leaderboardDTOs.add(dto);
        }

        return leaderboardDTOs;
    }

    /*@Override
    public List<RoundDTO> findAllRounds() {
        return (List<RoundDTO>) roundRepository.findAll();
    }*/

}
