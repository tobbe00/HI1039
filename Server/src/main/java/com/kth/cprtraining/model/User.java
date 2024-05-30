package com.kth.cprtraining.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * User entity representing a user in the CPR training application.
 * This entity includes details such as username, email, password, and associated rounds.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, length = 25)
    @Size(min = 5, message = "Username must be between 5 and 25 characters")
    @NotNull
    private String username;
    @Email
    @Column(unique = true, nullable = false)
    @NotNull
    private String email;
    @Size(min = 5, message = "Password must have a min length of 5 characters")
    @Column(nullable = false)
    @NotNull
    private String password;
    @OneToMany( mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Round> rounds;
    private String salt;
}
