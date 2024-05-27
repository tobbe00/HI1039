package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.LeaderboardDTO;
import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.dto.RoundFromWebDTO;
import com.kth.cprtraining.service.RoundService;
import com.kth.cprtraining.repository.UserRepository;
import com.kth.cprtraining.model.Round;
import com.kth.cprtraining.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rounds")
public class RoundController {

    private final RoundService roundService;
    private final UserRepository userRepository;

    public RoundController(RoundService roundService, UserRepository userRepository) {
        this.roundService = roundService;
        this.userRepository = userRepository;
    }

    @PostMapping("/saveRound")
    public ResponseEntity<Boolean> createRound2(@RequestBody RoundFromWebDTO roundFromWebDTO) {
        System.out.println("Received request to save round: " + roundFromWebDTO);
        RoundDTO roundDTO = convertFromWebDtoToRoundDto(roundFromWebDTO);
        System.out.println("Converted roundfromwebdto " + roundFromWebDTO);

        // Retrieve theGameList somehow (e.g., from a session or other storage)
        List<Integer> theGameList = GameController.theGameList;
        List<Double> frequencies = GameController.frequencies;

        // Call the service method with the converted DTO
        boolean status = roundService.saveRoundWithPressuresAndFrequencies(roundDTO, theGameList, frequencies);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    private RoundDTO convertFromWebDtoToRoundDto(RoundFromWebDTO roundFromWebDTO) {
        RoundDTO roundDTO = new RoundDTO();
        roundDTO.setPoints(roundFromWebDTO.getPoints());
        roundDTO.setUserId(userRepository.findUserByEmail(roundFromWebDTO.getEmail()).getUserId());
        roundDTO.setUsername(userRepository.findUserByEmail(roundFromWebDTO.getEmail()).getUsername());
        return roundDTO;
    }

    @GetMapping("/pressures/{roundsId}")
    public ResponseEntity<List<Integer>> getPressuresForRound(@PathVariable("roundsId") Long roundsId) {
        List<Integer> pressures = roundService.getPressuresForRound(roundsId);
        return new ResponseEntity<>(pressures, HttpStatus.OK);
    }

    @GetMapping("/frequencies/{roundsId}")
    public ResponseEntity<List<Double>> getFrequenciesForRound(@PathVariable("roundsId") Long roundsId) {
        List<Double> frequencies = roundService.getFrequenciesForRound(roundsId);
        return new ResponseEntity<>(frequencies, HttpStatus.OK);
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardDTO> getLeaderboardTop100() {
        return roundService.getLeaderboardTop100();
    }

     @GetMapping("/getUsersRounds")
    public List<RoundDTO> getRoundsByUsername(@RequestParam String username) {
        List<RoundDTO> rounds = roundService.getRoundsByUsername(username);
        return rounds;
    }

    @GetMapping("/getRoundById")
    public Boolean checkIfRoundExists(@RequestParam String roundId) {
        Long roundID = Long.parseLong(roundId);

        RoundDTO roundDTO =roundService.findRoundById(roundID);
        if(roundDTO!=null){
            System.out.println(true);
            return true;
        }
        
        System.out.println(false);
        return false;
    }
}


    /*@PostMapping
    public ResponseEntity<Boolean> createRound(@RequestBody Round round){
        boolean status = false;

        if(roundService.saveRound(round)){
            status = true;
        }
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }*/

