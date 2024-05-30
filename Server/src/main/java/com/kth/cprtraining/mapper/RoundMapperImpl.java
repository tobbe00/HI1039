package com.kth.cprtraining.mapper;

import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.model.Round;
import org.mapstruct.Mappings;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.Mapping;

/**
 * RoundMapperImpl provides implementation for mapping between Round entities and RoundDTOs.
 * It uses ModelMapper to facilitate the conversion.
 */
@Component
public class RoundMapperImpl implements Mapper<Round, RoundDTO>{
    private ModelMapper mapper;

    /**
     * Constructs a RoundMapperImpl with the specified ModelMapper.
     * @param mapper The ModelMapper to use for mapping.
     */
    public RoundMapperImpl(ModelMapper mapper){
        this.mapper = mapper;
    }

    /**
     * Maps a Round entity to a RoundDTO.
     * @param round The Round entity to map.
     * @return The mapped RoundDTO.
     */
    @Override
    public RoundDTO mapToDTO(Round round) {
        return mapper.map(round, RoundDTO.class);
    }

    /**
     * Maps a RoundDTO to a Round entity.
     * @param roundDTO The RoundDTO to map.
     * @return The mapped Round entity.
     */
    @Override
    public Round mapToEntity(RoundDTO roundDTO) {
        return mapper.map(roundDTO, Round.class);
    }
}
