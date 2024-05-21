package com.kth.cprtraining.repository;

import com.kth.cprtraining.model.PressureData;
import com.kth.cprtraining.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PressureDataRepository extends JpaRepository<PressureData, Long> {
    List<PressureData> findByRound(Round round);
}
