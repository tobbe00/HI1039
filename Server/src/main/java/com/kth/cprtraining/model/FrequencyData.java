package com.kth.cprtraining.model;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PressureData entity representing pressure data recorded during a CPR training round.
 * This entity includes details such as the associated round and the pressure data points.
 */
@Entity
@Table(name = "frequencydata")
public class FrequencyData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    @Column(name = "frequencies", columnDefinition = "TEXT")
    private String frequencies;

    public void setFrequenciesFromList(List<Double> frequencies) {
        this.frequencies = frequencies.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public List<Double> getFrequenciesAsList() {
        return Arrays.stream(this.frequencies.split(","))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public String getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(String frequencies) {
        this.frequencies = frequencies;
    }
}
