package com.kth.cprtraining.repository;

import com.kth.cprtraining.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findUserByEmail(String email);

}
