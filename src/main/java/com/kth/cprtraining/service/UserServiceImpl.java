package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.mapper.Mapper;
import com.kth.cprtraining.model.User;
import com.kth.cprtraining.repository.UserRepository;
import org.springframework.stereotype.Service;

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
        if(userRepository.existsByEmail(userDTO.getEmail()) || userRepository.existsByUsername(userDTO.getUsername()))
            return null;

        User user = mapper.mapToEntity(userDTO);
        userDTO = mapper.mapToDTO(userRepository.save(user));
        return userDTO;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean checkPassword(UserDTO userDTO) {

        User user=userRepository.findUserByEmail(userDTO.getEmail());
        return user.getPassword().equals(userDTO.getPassword());

    }

/*
    public boolean saveUser(UserFrontEnd userFrontEnd){

    }

 */

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
