package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.mapper.Mapper;
import com.kth.cprtraining.model.User;
import com.kth.cprtraining.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl implements the UserService interface, providing concrete methods
 * for managing user data such as creating, checking existence, and authenticating users.
 */
@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private Mapper<User, UserDTO> mapper;

    /**
     * Constructs UserServiceImpl with required dependencies.
     * @param userRepository The repository handling user data storage.
     * @param mapper Converts between User entity and UserDTO objects.
     */
    public UserServiceImpl(UserRepository userRepository, Mapper<User, UserDTO> mapper){
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    /**
     * Saves a user if the username and email are not already taken.
     * @param userDTO The user data transfer object containing new user information.
     * @return The saved user data as a DTO, or null if the email or username already exists.
     */
    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        if(userRepository.existsByEmail(userDTO.getEmail()) || userRepository.existsByUsername(userDTO.getUsername()))
            return null;

        User user = mapper.mapToEntity(userDTO);
        userDTO = mapper.mapToDTO(userRepository.save(user));
        return userDTO;
    }

    /**
     * Checks if a username exists in the database.
     * @param username The username to check.
     * @return true if the username exists, otherwise false.
     */
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if an email exists in the database.
     * @param email The email to check.
     * @return true if the email exists, otherwise false.
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Retrieves the username associated with a specific email.
     * @param email The email of the user.
     * @return The username of the user, or null if the user does not exist.
     */
    @Override
    public String findUserByEmail(String email) {
        String username = userRepository.findUserByEmail(email).getUsername();
        return username;
    }

    /**
     * Checks if the provided user DTO's password matches the stored password for the user.
     * @param userDTO The user data transfer object containing the login credentials.
     * @return true if the password matches, otherwise false.
     */
    @Override
    public boolean checkPassword(UserDTO userDTO) {
        User user=userRepository.findUserByEmail(userDTO.getEmail());
        return user.getPassword().equals(userDTO.getPassword());
    }

    /**
     * Retrieves the salt associated with a specific email used for hashing the password.
     * @param email The email of the user.
     * @return The salt used in password hashing, or null if the user does not exist.
     */
    @Override
    public String findSalt(String email) {
        User user=userRepository.findUserByEmail(email);
        return user.getSalt();
    }
}
