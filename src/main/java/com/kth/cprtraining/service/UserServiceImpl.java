package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.mapper.Mapper;
import com.kth.cprtraining.model.Round;
import com.kth.cprtraining.model.User;
import com.kth.cprtraining.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private Mapper<User, UserDTO> mapper;
    public UserServiceImpl(UserRepository userRepository, Mapper<User, UserDTO> mapper){
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = mapper.mapToEntity(userDTO);
        userDTO = mapper.mapToDTO(userRepository.save(user));
        return userDTO;
    }

    /*@Override
    public Optional<UserDTO> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

     */

    /*@Override
    public List<UserDTO> findAllUsers() {
        return (List<UserDTO>) userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

     */
}
