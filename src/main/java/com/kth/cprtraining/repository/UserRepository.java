package com.kth.cprtraining.repository;

import com.kth.cprtraining.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository provides CRUD operations for User entities.
 * It includes methods to check for existence and retrieve users by specific attributes.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Checks if a user exists by their username.
     * @param username The username to check.
     * @return true if a user with the specified username exists, otherwise false.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists by their email.
     * @param email The email to check.
     * @return true if a user with the specified email exists, otherwise false.
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by their email.
     * @param email The email to search for.
     * @return The User entity if found, otherwise null.
     */
    User findUserByEmail(String email);
}
