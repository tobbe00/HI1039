package com.kth.cprtraining.repository;

import com.kth.cprtraining.model.FrequencyData;
import com.kth.cprtraining.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * FrequencyDataRepository provides CRUD operations for FrequencyData entities.
 * It includes methods for retrieving frequency data associated with specific rounds.
 */

public interface FrequencyDataRepository extends JpaRepository<FrequencyData, Long> {
    /**
     * Finds a list of FrequencyData entities associated with a specific round.
     * @param round The round entity to search for associated frequency data.
     * @return A list of FrequencyData entities linked to the specified round.
     */
    List<FrequencyData> findByRound(Round round);
}
