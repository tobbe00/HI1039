package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.service.UserService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
public class UserController {
    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping(path = "/user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        userDTO = userService.saveUser(userDTO);
        return  new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }
}
