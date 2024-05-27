package com.kth.cprtraining.repository;

import com.kth.cprtraining.model.FrequencyData;
import com.kth.cprtraining.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FrequencyDataRepository extends JpaRepository<FrequencyData, Long> {
    List<FrequencyData> findByRound(Round round);
}
