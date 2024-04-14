package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.mapper.Mapper;
import com.kth.cprtraining.model.Round;
import com.kth.cprtraining.repository.RoundRepository;
import org.springframework.stereotype.Service;

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
    public RoundDTO saveRound(RoundDTO roundDto) {
        Round round = mapper.mapToEntity(roundDto);
        roundDto = mapper.mapToDTO(roundRepository.save(round));
        return roundDto;
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

    /*@Override
    public List<RoundDTO> findAllRounds() {
        return (List<RoundDTO>) roundRepository.findAll();
    }*/

    @Override
    public void deleteRoundById(Long roundId) {
        roundRepository.deleteById(roundId);
    }
}
