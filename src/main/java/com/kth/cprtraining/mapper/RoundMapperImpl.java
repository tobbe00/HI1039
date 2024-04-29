package com.kth.cprtraining.mapper;

import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.model.Round;
import org.mapstruct.Mappings;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.Mapping;

@Component
public class RoundMapperImpl implements Mapper<Round, RoundDTO>{
    private ModelMapper mapper;

    public RoundMapperImpl(ModelMapper mapper){
        this.mapper = mapper;
    }
    @Override
    public RoundDTO mapToDTO(Round round) {
        return mapper.map(round, RoundDTO.class);
    }

    @Override

    public Round mapToEntity(RoundDTO roundDTO) {
        return mapper.map(roundDTO, Round.class);
    }
}
