package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.UserDTO;

/**
 * UserService provides an interface for user management operations,
 * including saving users, checking existence, and verifying user credentials.
 */
public interface UserService {
    /**
     * Saves a user's data into the database and returns the persisted user information.
     * @param user The user data transfer object containing the user's details.
     * @return The persisted user data as a DTO.
     */
    UserDTO saveUser(UserDTO user);

    /**
     * Checks if a user exists by username in the database.
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists by email in the database.
     * @param email The email to check.
     * @return true if the user exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves a user's username based on their email.
     * @param email The email of the user to find.
     * @return The username of the user, or null if no user is found.
     */
    String findUserByEmail(String email);

    /**
     * Verifies if the provided user DTO's credentials match the stored credentials.
     * @param userDTO The user data transfer object containing the login credentials.
     * @return true if the password matches, false otherwise.
     */
    boolean checkPassword(UserDTO userDTO);

    /**
     * Retrieves the salt used for hashing the user's password, based on their email.
     * @param email The email of the user whose salt is requested.
     * @return The salt string used in password hashing.
     */
    String findSalt(String email);

}
