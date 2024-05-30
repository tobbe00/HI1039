package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.LeaderboardDTO;
import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.dto.RoundFromWebDTO;
import com.kth.cprtraining.service.RoundService;
import com.kth.cprtraining.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RoundController manages HTTP requests related to game rounds.
 * It handles the creation, retrieval, and querying of round-specific data like pressures, frequencies, and leaderboard rankings.
 */
@RestController
@RequestMapping("/rounds")
public class RoundController {

    private final RoundService roundService;
    private final UserRepository userRepository;

    /**
     * Constructs the RoundController with necessary service and repository injections.
     * @param roundService The service to handle business logic for rounds.
     * @param userRepository The repository to access user data.
     */
    public RoundController(RoundService roundService, UserRepository userRepository) {
        this.roundService = roundService;
        this.userRepository = userRepository;
    }

    /**
     * Saves a new round based on data received from a web DTO.
     * @param roundFromWebDTO The DTO containing round data from the web interface.
     * @return A ResponseEntity indicating the result of the save operation.
     */
    @PostMapping("/saveRound")
    public ResponseEntity<Boolean> createRound(@RequestBody RoundFromWebDTO roundFromWebDTO) {
        System.out.println("Received request to save round: " + roundFromWebDTO);
        RoundDTO roundDTO = convertFromWebDtoToRoundDto(roundFromWebDTO);
        System.out.println("Converted roundfromwebdto " + roundFromWebDTO);

        List<Integer> theGameList = GameController.theGameList;
        List<Double> frequencies = GameController.frequencies;

        boolean status = roundService.saveRoundWithPressuresAndFrequencies(roundDTO, theGameList, frequencies);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    /**
     * Converts a RoundFromWebDTO to a RoundDTO using user details.
     * @param roundFromWebDTO The round data from the web interface.
     * @return A converted RoundDTO with user and round information.
     */
    private RoundDTO convertFromWebDtoToRoundDto(RoundFromWebDTO roundFromWebDTO) {
        RoundDTO roundDTO = new RoundDTO();
        roundDTO.setPoints(roundFromWebDTO.getPoints());
        roundDTO.setUserId(userRepository.findUserByEmail(roundFromWebDTO.getEmail()).getUserId());
        roundDTO.setUsername(userRepository.findUserByEmail(roundFromWebDTO.getEmail()).getUsername());
        return roundDTO;
    }

    /**
     * Retrieves pressures for a specific round.
     * @param roundsId The identifier for the round.
     * @return A ResponseEntity containing a list of pressures.
     */
    @GetMapping("/pressures/{roundsId}")
    public ResponseEntity<List<Integer>> getPressuresForRound(@PathVariable("roundsId") Long roundsId) {
        List<Integer> pressures = roundService.getPressuresForRound(roundsId);
        return new ResponseEntity<>(pressures, HttpStatus.OK);
    }

    /**
     * Retrieves frequencies for a specific round.
     * @param roundsId The identifier for the round.
     * @return A ResponseEntity containing a list of frequencies.
     */
    @GetMapping("/frequencies/{roundsId}")
    public ResponseEntity<List<Double>> getFrequenciesForRound(@PathVariable("roundsId") Long roundsId) {
        List<Double> frequencies = roundService.getFrequenciesForRound(roundsId);
        return new ResponseEntity<>(frequencies, HttpStatus.OK);
    }

    /**
     * Retrieves the top 100 entries from the leaderboard.
     * @return A list of LeaderboardDTOs.
     */
    @GetMapping("/leaderboard")
    public List<LeaderboardDTO> getLeaderboardTop100() {
        return roundService.getLeaderboardTop100();
    }

    /**
     * Retrieves all rounds for a given username.
     * @param username The username to query rounds for.
     * @return A list of RoundDTOs associated with the username.
     */
    @GetMapping("/getUsersRounds")
    public List<RoundDTO> getRoundsByUsername(@RequestParam String username) {
        List<RoundDTO> rounds = roundService.getRoundsByUsername(username);
        return rounds;
    }

    /**
     * Checks if a round with a given ID exists.
     * @param roundId The identifier of the round to check.
     * @return A Boolean indicating if the round exists.
     */
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