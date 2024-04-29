package com.kth.cprtraining.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Round {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundId;
    //private String category;
    @Column(nullable = false)
    private int points;
    @ManyToOne
    private User user;

    @Column
    private String username;

}
