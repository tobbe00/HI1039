package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO saveUser(UserDTO user);
    /*Optional<UserDTO> findUserById(Long userId);
    List<UserDTO> findAllUsers();
    void deleteUserById(Long userId);
    */
}
