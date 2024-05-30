package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.UserDTO;

import com.kth.cprtraining.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * UserController manages HTTP requests related to user operations.
 * It provides endpoints for user creation, login, and fetching user-specific data such as salt and username.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    /**
     * Constructs the UserController with the necessary UserService dependency.
     * @param userService The service layer for managing user-related operations.
     */
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Creates a new user with the provided user data.
     * @param userDTO Data transfer object containing user details.
     * @return A ResponseEntity containing the created UserDTO and HTTP status.
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        String errorMsg = "";

        if(userService.existsByUsername(userDTO.getUsername()))
            errorMsg += "Username already exists! ";
        if(userService.existsByEmail(userDTO.getEmail()))
            errorMsg += "Email already exists!";
        if(!errorMsg.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
        userDTO = userService.saveUser(userDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    /**
     * Logs in a user based on email and password checks.
     * @param userDTO Data transfer object containing login credentials.
     * @return A Map representing the success status of the login attempt.
     */
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

    /**
     * Provides the salt associated with a user's email for secure password management.
     * @param email The email of the user whose salt is requested.
     * @return A Map containing the user's salt.
     */
    @GetMapping("/salt")
    public Map<String, String> sendSalt(String email){
        Map<String, String> response = new HashMap<>();
        response.put("salt", userService.findSalt(email));
        return response;
    }

    /**
     * Retrieves the username associated with a given email.
     * @param email The email of the user whose username is requested.
     * @return A Map containing the username.
     */
    @GetMapping("/username")
    public Map<String, String> getUsername(String email){
        Map<String, String> response = new HashMap<>();
        response.put("username", userService.findUserByEmail(email));
        return response;
    }
}
