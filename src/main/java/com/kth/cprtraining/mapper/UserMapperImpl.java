package com.kth.cprtraining.mapper;

import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<User, UserDTO>{
    private ModelMapper mapper;
    public UserMapperImpl(ModelMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public UserDTO mapToDTO(User user) {
        return mapper.map(user, UserDTO.class);
    }

    @Override
    public User mapToEntity(UserDTO userDTO) {
        return mapper.map(userDTO,User.class);
    }
}
