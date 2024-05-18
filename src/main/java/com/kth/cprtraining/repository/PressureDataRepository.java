package com.kth.cprtraining.repository;

import com.kth.cprtraining.model.PressureData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PressureDataRepository extends CrudRepository<PressureData, Long> {
    // You can add custom database queries if needed later
}
