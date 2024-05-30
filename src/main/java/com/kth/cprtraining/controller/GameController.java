package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.ExtremePointDTO;

import com.kth.cprtraining.dto.PointDTO;
import com.kth.cprtraining.dto.ZeroPointDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * GameController handles the endpoints related to the CPR training game.
 * It manages the game state, processes incoming data, and calculates points.
 */
@RestController
@RequestMapping("/game")
public class GameController {
    int pressureId =0;

    public static List<Integer> theGameList =new ArrayList<>();
    public static List<Double> frequencies =new ArrayList<>();
    ExtremePointDTO mostRecentExtremePoints=new ExtremePointDTO();
    PointDTO mostRecentPoint=new PointDTO();
    int zeroPoint;
    int avgPressure;
    int scaling;
    boolean gameStart=false;
    boolean gameEnd=false;
    List<Integer> pointsBatch=new ArrayList<>();
    int pointsId=0;

    /**
     * Sends the most recent extreme points.
     * @return The most recent ExtremePointDTO.
     */
    @GetMapping("/extreme")//by id
    public ExtremePointDTO sendExtreme(){
        return mostRecentExtremePoints;
    }

    /**
     * Sends the most recent points.
     * @return The most recent PointDTO.
     */
    @GetMapping("/points")//by id
    public PointDTO sendPoints(){
        return mostRecentPoint;
    }

    /**
     * Receives and processes pressure data.
     * @param test The pressure data as a string.
     * @return The processed pressure value wrapped in a ResponseEntity.
     */
    @PostMapping(path="/send")
    public ResponseEntity<Integer> send(@RequestBody String test){
        int pressure = Integer.parseInt(test);
        System.out.println("Raw value: " + pressure);
        pressure= handleBatch(pressure);
        System.out.println("Adjusted value: " + pressure);
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
        frequencies.add(getFrequency());
        theGameList.add(pressure);
        mostRecentExtremePoints.setId(pressureId);
        mostRecentExtremePoints.setPressure(pressure);
        pressureId++;
        return new ResponseEntity<>(pressure,HttpStatus.CREATED);
    }

    /**
     * Receives and sets zero point and average pressure.
     * @param zero The ZeroPointDTO containing the zero point and average pressure.
     * @return ResponseEntity indicating the success of the operation.
     */
    @PostMapping(path="/zeropoint")
    public ResponseEntity<Boolean> getZeroPoint(@RequestBody ZeroPointDTO zero){
        zeroPoint=zero.getZeroPointInt();
        avgPressure=zero.getAvgInt();
        System.out.println(zero);
        System.out.println(zeroPoint+" the mode is:"+zero.getMode());
        return new ResponseEntity<>(true,HttpStatus.CREATED);
    }

    /**
     * Starts the game by clearing previous data and setting the game state to started.
     * @param gameHasStarted The game start status as a string.
     * @return ResponseEntity indicating the success of the operation.
     */
    @PostMapping("/gamestart")
    public ResponseEntity<Boolean> gameStart(@RequestBody String gameHasStarted){
        boolean started = Boolean.parseBoolean(gameHasStarted);
        System.out.println("Started: " + started);

        if (started){
            theGameList.clear();
            pressureId = 0;
            gameEnd = false;
            gameStart = started;
        }
        System.out.println("gameStart is: "+ gameStart);
        return new ResponseEntity<>(true,HttpStatus.CREATED);
    }

    /**
     * Sends the current game start status.
     * @return The game start status.
     */
    @GetMapping("/gamestart")//by id
    public boolean sendGameStart(){
        return gameStart;
    }

    /**
     * Ends the game by setting the game state to ended.
     * @param gameHasEnded The game end status as a string.
     * @return ResponseEntity indicating the success of the operation.
     */
    @PostMapping("/gameEnd")
    public ResponseEntity<Boolean> recivedGameEnd(@RequestBody String gameHasEnded){

        boolean ended = Boolean.parseBoolean(gameHasEnded);
        if (ended){
            System.out.println("ended");
            gameStart = false;
            gameEnd = ended;
        }
        return new ResponseEntity<>(true,HttpStatus.CREATED);
    }

    /**
     * Sends the current game end status.
     * @return The game end status.
     */
    @GetMapping("/gameEnd")//by id
    public boolean sendGameEnd(){
        return gameEnd;
    }

    /**
     * Processes and scales the pressure value.
     * @param pressure The pressure value to process.
     * @return The processed pressure value.
     */
    public Integer handleBatch(Integer pressure){
        scaling = 200 / avgPressure;
        pressure = Math.abs((pressure - zeroPoint)) * scaling;
        System.out.println(pressure);
        if(pressure<20) pressure = 0;
        return pressure;
    }

    /**
     * Calculates the maximum pressure in a batch.
     * @param batch The batch of pressure values.
     * @return The maximum pressure value.
     */
    public int getMax(List<Integer> batch){
        int max=0;
        for (int j = 0; j < batch.size(); j++) {
            max=Math.max(batch.get(j),max);
        }
        return max;
    }

    /**
     * Calculates the minimum pressure in a batch.
     * @param batch The batch of pressure values.
     * @return The minimum pressure value.
     */
    public int getMin(List<Integer> batch){
        int min=batch.get(0);
        for (int j = 1; j < batch.size(); j++) {
            min=Math.min(batch.get(j),min);
        }
        return min;
    }

    /**
     * Calculates the frequency of pressure peaks.
     * @return The frequency of pressure peaks.
     */
    public double getFrequency() {
        //3sec ger 3*20=60
        int current;
        int old = 0;
        int next;
        int peakCount = 0;
        int oldOld = 0;
        int nextNext;

        if (theGameList.size() < 32) return 0;
        old = theGameList.get(theGameList.size() - 31);
        oldOld = theGameList.get(theGameList.size() - 32);
        for (int j = theGameList.size() - 32 + 2; j < theGameList.size() - 2; j++) {
            current = theGameList.get(j);
            next = theGameList.get(j + 1);
            nextNext = theGameList.get(j + 2);
            if (current > 50) {
                if (old < current && current > next && old > oldOld && next > nextNext) {
                    peakCount++;
                }
            }
            oldOld = old;
            old = current;
        }
        return peakCount / (3.0 / 60.0);
    }

    /**
     * Calculates points based on the current frequency.
     * @param currentFrequency The current frequency of pressure peaks.
     * @return The calculated points.
     */
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

    /**
     * Calculates points based on the elapsed time.
     * @param multiplyer The multiplier based on the time elapsed.
     * @return The calculated points.
     */
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

    /**
     * Calculates points based on the maximum pressure in the batch.
     * @param multiplyer The time-based multiplier.
     * @param multiplyer2 The secondary multiplier.
     * @param minAllowed The minimum allowed pressure.
     * @param maxAllowed The maximum allowed pressure.
     * @return The calculated points.
     */
    private int calculatePointsByPressureMax(int multiplyer,int multiplyer2,int minAllowed,int maxAllowed){
        int pressure= getMax(pointsBatch);
        if (pressure<maxAllowed&&pressure>minAllowed){
            return 5*multiplyer*multiplyer2;
        }else {
            return 0;
        }
    }

    /**
     * Calculates points based on the minimum pressure in the batch.
     * @param multiplyer The time-based multiplier.
     * @param multiplyer2 The secondary multiplier.
     * @param maxAllowed The maximum allowed pressure.
     * @return The calculated points.
     */
    private int calculatePointsByPressureMin(int multiplyer,int multiplyer2,int maxAllowed){
        int pressure= getMin(pointsBatch);

        if (pressure<maxAllowed){
            return 5*multiplyer*multiplyer2;

        }else {
            return 0;
        }
    }
}