package com.kth.cprtraining.mapper;

import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * UserMapperImpl provides implementation for mapping between User entities and UserDTOs.
 * It uses ModelMapper to facilitate the conversion.
 */
@Component
public class UserMapperImpl implements Mapper<User, UserDTO>{
    private ModelMapper mapper;

    /**
     * Constructs a UserMapperImpl with the specified ModelMapper.
     * @param mapper The ModelMapper to use for mapping.
     */
    public UserMapperImpl(ModelMapper mapper){
        this.mapper = mapper;
    }

    /**
     * Maps a User entity to a UserDTO.
     * @param user The User entity to map.
     * @return The mapped UserDTO.
     */
    @Override
    public UserDTO mapToDTO(User user) {
        return mapper.map(user, UserDTO.class);
    }


    /**
     * Maps a UserDTO to a User entity.
     * @param userDTO The UserDTO to map.
     * @return The mapped User entity.
     */
    @Override
    public User mapToEntity(UserDTO userDTO) {
        return mapper.map(userDTO,User.class);
    }
}
