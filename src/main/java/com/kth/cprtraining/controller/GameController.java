package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.BatchDTO;
import com.kth.cprtraining.dto.ExtremePointDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @PostMapping(path="/send")
    public ResponseEntity<List<Integer>> send(@RequestBody List<Integer> batch){
        System.out.println(batch);




        return new ResponseEntity<>(batch,HttpStatus.CREATED);
    }
}
