package com.kth.cprtraining.repository;

import com.kth.cprtraining.model.Round;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends CrudRepository<Round, Long> {
    List<Round> findTop100ByOrderByPointsDesc();
    List<Round> findByUsernameOrderByPointsDesc(String username);
}
