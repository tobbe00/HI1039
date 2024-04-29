package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.ExtremePointDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/game")
public class GameController {
    int i=0;
    @GetMapping("/extreme")//by id
    public ExtremePointDTO sendExtreme(){
        // return userService.getUserById(id); detta e med geduserbyid efter kommer nr2 med optional
        ExtremePointDTO mostRecentExtreme=new ExtremePointDTO(457,0);

        mostRecentExtreme.setId(i);
        i++;

        return mostRecentExtreme;
    }
}
