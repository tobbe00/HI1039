package com.kth.cprtraining.repository;

import com.kth.cprtraining.model.Round;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RoundRepository provides CRUD operations for Round entities.
 * It includes methods for retrieving rounds based on specific criteria, such as top scores and user-specific rounds.
 */
@Repository
public interface RoundRepository extends CrudRepository<Round, Long> {
    /**
     * Retrieves the top 100 rounds ordered by points in descending order.
     * @return A list of the top 100 rounds based on points.
     */
    List<Round> findTop100ByOrderByPointsDesc();

    /**
     * Retrieves all rounds associated with a specific username, ordered by points in descending order.
     * @param username The username to search for.
     * @return A list of rounds associated with the given username, ordered by points.
     */
    List<Round> findByUsernameOrderByPointsDesc(String username);
}
