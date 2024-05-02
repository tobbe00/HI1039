package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.ExtremePointDTO;

import com.kth.cprtraining.model.Batch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {
    int i=0;
    List<Integer> theGameList =new ArrayList<>();
    ExtremePointDTO mostRecentExtremePoints=new ExtremePointDTO();

    @GetMapping("/extreme")//by id
    public ExtremePointDTO sendExtreme(){
        // return userService.getUserById(id); detta e med geduserbyid efter kommer nr2 med optional
        //ExtremePointDTO mostRecentExtreme=new ExtremePointDTO(457,0);
        //mostRecentExtreme.setId(i);
        i++;

        return mostRecentExtremePoints;
    }
    int batchCount=0;
    @PostMapping(path="/send")
    public ResponseEntity<List<Integer>> send(@RequestBody List<Integer> batch){
        System.out.println(batch);

        if (theGameList.size()==1200){
            //saveGame(); lagg metod för att spara
        }
        Batch b=new Batch();
        b.setBatchID(batchCount);

        for (int j = 0; j < 5; j++) {
            b.setTheBatchATIndex(batch.get(j),j);//gör om till batch från listan vi tar in
        }

        b= handleBatch(b);//gör om till att 0 inte e 650

        //theGameList.addAll(batch);
        for (int a:b.getTheBatch()) {
            theGameList.add(a);
        }
        mostRecentExtremePoints.setId(batchCount);
        mostRecentExtremePoints.setMaxPressure(getMax(b));
        mostRecentExtremePoints.setMinPressure(getMin(b));
        mostRecentExtremePoints.setMaxBeforeMin(isMaxBeforeMin(b));
        batchCount++;
        return new ResponseEntity<>(batch,HttpStatus.CREATED);
    }

    //hjälp metoder
    public Batch handleBatch(Batch batch){
        int displace=650;
        for (int j = 0; j < 5; j++) {
            batch.setTheBatchATIndex(displace-batch.getBatchIntAtID(i),i);
        }
        return batch;
    }

    public int getMax(Batch batch){
        int max=0;
        for (int j = 0; j < 5; j++) {
            max=Math.max(batch.getBatchIntAtID(j),max);
        }
        return max;
    }
    public int getMin(Batch batch){
        int min=batch.getBatchIntAtID(0);
        for (int j = 1; j < 5; j++) {
            min=Math.min(batch.getBatchIntAtID(j),min);
        }
        return min;
    }

    public boolean isMaxBeforeMin(Batch batch){
        int min=getMin(batch);
        int max=getMax(batch);
        int minIndex=0;
        int maxIndex=0;
        for (int j = 0; j < 5; j++) {
            if (batch.getBatchIntAtID(j)==min){
                minIndex=j;
            }
            if (batch.getBatchIntAtID(j)==max){
                maxIndex=j;
            }

        }
        return maxIndex>minIndex;
    }

}
