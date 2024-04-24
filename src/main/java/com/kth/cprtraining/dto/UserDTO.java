package com.kth.cprtraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String username;
/*
    public UserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public UserDTO(String email, String password){
        this.email=email;
        this.password=password;
    }

 */

    private String email;
    private String password;

}
