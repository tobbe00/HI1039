package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.ExtremePointDTO;
import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.mapper.Mapper;
import com.kth.cprtraining.model.User;
import com.kth.cprtraining.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/login")
    public Map<String, Boolean> logInUser(@RequestBody UserDTO userDTO){
        String errorMsg = "";

        boolean success=false;
        if(userService.existsByEmail(userDTO.getEmail())){
           if (userService.checkPassword(userDTO)){
                success=true;
           }
        }else {
            errorMsg += "Email already exists!";
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);
        return response;
    }
    @GetMapping("/salt")
    public Map<String, String> sendSalt(String email){
        // return userService.getUserById(id); detta e med geduserbyid efter kommer nr2 med optional

        Map<String, String> response = new HashMap<>();
        response.put("salt", userService.findSalt(email));
        return response;
    }




}
