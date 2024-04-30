package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO saveUser(UserDTO user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    boolean checkPassword(UserDTO userDTO);
    String findSalt(String email);
    /*Optional<UserDTO> findUserById(Long userId);
    List<UserDTO> findAllUsers();
    void deleteUserById(Long userId);
    */
}
