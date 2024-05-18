package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.LeaderboardDTO;
import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.dto.RoundFromWebDTO;
import com.kth.cprtraining.service.RoundService;
import com.kth.cprtraining.repository.UserRepository;
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
    public ResponseEntity<Boolean> createRound2(@RequestBody RoundFromWebDTO roundFromWebDTO){
        System.out.println("Received request to save round: " + roundFromWebDTO);
        RoundDTO roundDTO = convertFromWebDtoToRoundDto(roundFromWebDTO);
        System.out.println("Converted roundfromwebdto to roundDTO: " + roundDTO);

        // Retrieve theGameList somehow (e.g., from a session or other storage)
        List<Integer> theGameList = GameController.theGameList;

        // Call the service method with the converted DTO
        boolean status = roundService.saveRoundWithPressures(roundDTO, theGameList);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    private RoundDTO convertFromWebDtoToRoundDto(RoundFromWebDTO roundFromWebDTO) {
        User user = userRepository.findUserByEmail(roundFromWebDTO.getEmail());
        return RoundDTO.builder()
                .points(roundFromWebDTO.getPoints())
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();
    }


    @GetMapping
    public List<LeaderboardDTO> getLeaderboardTop100() {
        return roundService.getLeaderboardTop100();
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

