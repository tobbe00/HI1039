package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.service.UserService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        String errorMsg = "";

        if(userService.existsByUsername(userDTO.getUsername()))
            errorMsg += "Username already exists! ";
        if(userService.existsByEmail(userDTO.getEmail()))
            errorMsg += "Email already exists!";
        if(!errorMsg.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
            //return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMsg);

        userDTO = userService.saveUser(userDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }


}
