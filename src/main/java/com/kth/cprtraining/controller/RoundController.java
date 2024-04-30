package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.LeaderboardDTO;
import com.kth.cprtraining.model.Round;
import com.kth.cprtraining.service.RoundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping//by id
    public List<LeaderboardDTO> getLeaderboardTop100(){
        // return userService.getUserById(id); detta e med geduserbyid efter kommer nr2 med optional
        /*List<LeaderboardDTO> leaderboardList=new ArrayList<>();
        leaderboardList=roundService.fillLeaderboard();
        return leaderboardList;
         */
        return roundService.getLeaderboardTop100();
    }
    
}
