package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.dto.UserDTO;
import com.kth.cprtraining.model.Round;
import com.kth.cprtraining.service.RoundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/rounds")
public class RoundController {
    private RoundService roundService;

    public RoundController(RoundService roundService){
         this.roundService = roundService;
    }
    @PostMapping
    public ResponseEntity<Boolean> createRound(@RequestBody Round round){
        boolean status = false;

        if(roundService.saveRound(round)){
            status = true;
        }
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }
}
