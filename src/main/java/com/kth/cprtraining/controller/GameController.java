package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.ExtremePointDTO;

import com.kth.cprtraining.dto.PointDTO;
import com.kth.cprtraining.dto.ZeroPoint;
import com.kth.cprtraining.model.Batch;
import jakarta.persistence.criteria.CriteriaBuilder;
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
    int pressureId =0;

    List<Integer> theGameList =new ArrayList<>();

    List<Integer> pointsList=new ArrayList<>();
    ExtremePointDTO mostRecentExtremePoints=new ExtremePointDTO();
    PointDTO mostRecentPoint=new PointDTO();
    int zeroPoint;
    int avgPressure;
    boolean gameStart=false;
    boolean gameEnd=false;

    @GetMapping("/extreme")//by id
    public ExtremePointDTO sendExtreme(){

        return mostRecentExtremePoints;
    }

    @GetMapping("/points")//by id
    public PointDTO sendPoints(){

        return mostRecentPoint;
    }

    List<Integer> pointsBatch=new ArrayList<>();
    int pointsId=0;
    @PostMapping(path="/send")
    public ResponseEntity<Integer> send(@RequestBody String test){
        int pressure = Integer.parseInt(test);
       // boolean started = Boolean.parseBoolean(gameHasStarted);
        System.out.println(pressure);
        pressure=handleBatch2(pressure);
        System.out.println(pressure);
        if (pointsBatch.size()<5){
            pointsBatch.add(pressure);

        }else {
            pointsBatch.clear();
            pointsBatch.add(pressure);
        }
        if (pointsBatch.size()==5){
            mostRecentPoint.setId(pointsId);
            pointsId++;
            mostRecentPoint.setFrequency(getFrequency());
            mostRecentPoint.setPointsMax(calculatePoints(getFrequency()));//här ska vi beräkna max points
            mostRecentPoint.setPointsMin(calculatePoints(getFrequency()));
        }

        System.out.println("Frequency :"+ getFrequency());
        theGameList.add(pressure);
        mostRecentExtremePoints.setId(pressureId);
        mostRecentExtremePoints.setPressure(pressure);
        //mostRecentExtremePoints.setFrequency(getFrequency());
        pressureId++;
        return new ResponseEntity<>(pressure,HttpStatus.CREATED);
    }

    @PostMapping(path="/zeropoint")
    public ResponseEntity<Boolean> getZeroPoint(@RequestBody ZeroPoint zero){
        zeroPoint=zero.getZeroPointInt();
        avgPressure=zero.getAvgInt();
        System.out.println(zero);
        System.out.println(zeroPoint+" the mode is:"+zero.getMode());
        return new ResponseEntity<>(true,HttpStatus.CREATED);
    }

    @PostMapping("/gamestart")
    public ResponseEntity<Boolean> gameStart(@RequestBody String gameHasStarted){
        boolean started = Boolean.parseBoolean(gameHasStarted);

        System.out.println("hit kommer vi");
        System.out.println("Started: "+started);

        if (started){
            theGameList.clear();

            pressureId = 0;

        }
        gameEnd = false;
        gameStart=started;


        System.out.println("gameStart is:"+ gameStart);
        return new ResponseEntity<>(true,HttpStatus.CREATED);
    }

    @GetMapping("/gamestart")//by id
    public boolean sendGameStart(){

        return gameStart;
    }

    @PostMapping("/gameEnd")
    public ResponseEntity<Boolean> recivedGameEnd(@RequestBody String gameHasEnded){

        boolean ended = Boolean.parseBoolean(gameHasEnded);
        //remember to save thegamelist om vi ska ha den i databasen
        if (ended){
            //spara gamelist
        }

        System.out.println("ended");
        gameStart = false;
        gameEnd = ended;

        return new ResponseEntity<>(true,HttpStatus.CREATED);
    }
    @GetMapping("/gameEnd")//by id
    public boolean sendGameEnd(){

        return gameEnd;
    }



    public Integer handleBatch2(Integer pressure){
        pressure = pressure-zeroPoint;
        if(pressure<20

        ) pressure = 0;
        return pressure;
    }

    public int getMax2(List<Integer> batch){
        int max=0;
        for (int j = 0; j < batch.size(); j++) {
            max=Math.max(batch.get(j),max);
        }
        return max;
    }
    public int getMin2(List<Integer> batch){
        int min=batch.get(0);
        for (int j = 1; j < batch.size(); j++) {
            min=Math.min(batch.get(j),min);
        }
        return min;
    }

        public double getFrequency(){
            //3sec ger 3*20=60
            int current;
            int old=0;
            int next;
            int peakCount=0;
            int oldOld = 0;
            int nextNext;

            if (theGameList.size()<32)return 0;
            old = theGameList.get(theGameList.size() - 31);
            oldOld = theGameList.get(theGameList.size() - 32);
            for (int j = theGameList.size()-32 + 2; j < theGameList.size()-2 ; j++) {
                current=theGameList.get(j);
                next = theGameList.get(j+1);
                nextNext = theGameList.get(j+2);
                if (current>50){//200 får bli minsta gränsen
                    if (old<current && current>next && old>oldOld && next>nextNext){
                        peakCount++;
                    }
                }
                oldOld = old;
                old = current;
            }
            return peakCount/(3.0/60.0);
        }


    private int calculatePoints(double currentFrequency) {

        int minRequiredBpm=100;
        int maxRequiredBpm=120;

        if (minRequiredBpm<=currentFrequency&&maxRequiredBpm>=currentFrequency){
            return calculatePointsByTime(2);
        }else if(minRequiredBpm-20<=currentFrequency&&maxRequiredBpm+20>=currentFrequency) {
            return calculatePointsByTime(1);
        }else{
            return calculatePointsByTime(0);
        }
    }
    private int calculatePointsByTime(int multiplyer){
        int points=0;
        int diff=200;
        int changePerTime=50;
        if (theGameList.size()<1200/4){
            points+=calculatePointsByPressureMax(multiplyer,1,avgPressure-diff,avgPressure+diff);
            points+=calculatePointsByPressureMin(multiplyer,1,200);
        } else if (theGameList.size()<(1200/4)*2) {
            points+=calculatePointsByPressureMax(multiplyer,2,avgPressure-diff+changePerTime,avgPressure+diff-changePerTime);
            points+=calculatePointsByPressureMin(multiplyer,1,150);
        } else if (theGameList.size()<(1200/4)*3) {
            points+=calculatePointsByPressureMax(multiplyer,3,avgPressure-diff+changePerTime*2,avgPressure+diff-changePerTime*2);
            points+=calculatePointsByPressureMin(multiplyer,1,100);
        }else {
            points+=calculatePointsByPressureMax(multiplyer,4,avgPressure-diff+changePerTime*3,avgPressure+diff-changePerTime*3);
            points+=calculatePointsByPressureMin(multiplyer,1,50);
        }
        return points;
    }
    private int calculatePointsByPressureMax(int multiplyer,int multiplyer2,int minAllowed,int maxAllowed){
       // int pressure=mostRecentExtremePoints.getMaxPressure();
        int pressure=getMax2(pointsBatch);
        if (pressure<maxAllowed&&pressure>minAllowed){
            return 5*multiplyer*multiplyer2;
        }else {
            return 0;
        }
    }
    private int calculatePointsByPressureMin(int multiplyer,int multiplyer2,int maxAllowed){
        //int pressure=mostRecentExtremePoints.getMinPressure();
        int pressure=getMin2(pointsBatch);
        //int maxAllowed=100;
        if (pressure<maxAllowed){
            return 5*multiplyer*multiplyer2;

        }else {

            return 0;
        }
    }
}