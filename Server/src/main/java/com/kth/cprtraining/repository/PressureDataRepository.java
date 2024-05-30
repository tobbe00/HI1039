package com.kth.cprtraining.repository;

import com.kth.cprtraining.model.PressureData;
import com.kth.cprtraining.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * PressureDataRepository provides CRUD operations for PressureData entities.
 * It includes methods for retrieving pressure data associated with specific rounds.
 */
public interface PressureDataRepository extends JpaRepository<PressureData, Long> {

    /**
     * Finds a list of PressureData entities associated with a specific round.
     * @param round The round entity to search for associated pressure data.
     * @return A list of PressureData entities linked to the specified round.
     */
    List<PressureData> findByRound(Round round);
}
