package com.kth.cprtraining.model;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "pressuredata")
public class PressureData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    @Column(name = "pressures", columnDefinition = "TEXT")
    private String pressures;

    // Constructors, getters, setters

    public void setPressuresFromList(List<Integer> pressures) {
        this.pressures = pressures.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public List<Integer> getPressuresAsList() {
        return Arrays.stream(this.pressures.split(","))
                .map(Integer::parseInt)
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

    public String getPressures() {
        return pressures;
    }

    public void setPressures(String pressures) {
        this.pressures = pressures;
    }
}
