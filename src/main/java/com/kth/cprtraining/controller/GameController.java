package com.kth.cprtraining.controller;

import com.kth.cprtraining.dto.ExtremePointDTO;

import com.kth.cprtraining.dto.ZeroPoint;
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
    int batchId =0;

    List<Integer> theGameList =new ArrayList<>();

    List<Integer> pointsList=new ArrayList<>();
    ExtremePointDTO mostRecentExtremePoints=new ExtremePointDTO();
    int zeroPoint;
    int avgPressure;
    boolean gameStart=false;
    boolean gameEnd=false;


    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    @GetMapping("/extreme")//by id
    public ExtremePointDTO sendExtreme(){
        // return userService.getUserById(id); detta e med geduserbyid efter kommer nr2 med optional
        //ExtremePointDTO mostRecentExtreme=new ExtremePointDTO(457,0);
        //mostRecentExtreme.setId(i);



        pointsList.add(mostRecentExtremePoints.getPointsMax());
        pointsList.add(mostRecentExtremePoints.getPointsMin());


        return mostRecentExtremePoints;
    }




    //int batchCount=0;
    @PostMapping(path="/send")
    public ResponseEntity<List<Integer>> send(@RequestBody List<Integer> batch){
        System.out.println(batch);

        if (theGameList.size()==1200){
            //saveGame(); lagg metod för att spara
        }

        Batch b=new Batch();
        b.setBatchID(batchId);

        for (int j = 0; j < 2; j++) {
            b.setTheBatchATIndex(batch.get(j),j);//gör om till batch från listan vi tar in
        }

        b= handleBatch(b);//gör om till att 0 inte e 650

        //theGameList.addAll(batch);
        for (int a:b.getTheBatch()) {
            theGameList.add(a);
        }
        mostRecentExtremePoints.setId(batchId);
        mostRecentExtremePoints.setMaxPressure(getMax(b));
        mostRecentExtremePoints.setMinPressure(getMin(b));
        mostRecentExtremePoints.setMaxBeforeMin(isMaxBeforeMin(b));
        mostRecentExtremePoints.setFrequency(getFrequency());
        mostRecentExtremePoints.setPointsMax(calculatePoints(mostRecentExtremePoints.getFrequency()));
        mostRecentExtremePoints.setPointsMin(calculatePoints(mostRecentExtremePoints.getFrequency()));
        batchId++;
        return new ResponseEntity<>(batch,HttpStatus.CREATED);
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
            System.out.println("Resetting batchId. Previous value: " + batchId);
            batchId = 0;
            System.out.println("New batchId value: " + batchId);
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





    //hjälp metoder
    public void emptyGameList(){
        for (int j = theGameList.size()-1; j >= 0; j--) {
            theGameList.remove(j);
        }
        batchId =0;
    }
    public Batch handleBatch(Batch batch){
        int displace=zeroPoint;
        for (int j = 0; j < 2; j++) {
            batch.setTheBatchATIndex(Math.abs(displace-batch.getBatchIntAtID(j)),j);
        }
        return batch;
    }

    public int getMax(Batch batch){
        int max=0;
        for (int j = 0; j < 2; j++) {
            max=Math.max(batch.getBatchIntAtID(j),max);
        }
        return max;
    }
    public int getMin(Batch batch){
        int min=batch.getBatchIntAtID(0);
        for (int j = 1; j < 2; j++) {
            min=Math.min(batch.getBatchIntAtID(j),min);
        }
        return min;
    }

    public boolean isMaxBeforeMin(Batch batch){
        int min=getMin(batch);
        int max=getMax(batch);
        int minIndex=0;
        int maxIndex=0;
        for (int j = 0; j < 2; j++) {
            if (batch.getBatchIntAtID(j)==min){
                minIndex=j;
            }
            if (batch.getBatchIntAtID(j)==max){
                maxIndex=j;
            }

        }
        return maxIndex>minIndex;
    }
        public double getFrequency(){
            //3sec ger 3*20=60
            int current;
            int old=0;
            int next;
            int peakCount=0;

            if (theGameList.size()<31)return 0;
            for (int j = theGameList.size()-31; j < theGameList.size()-1 ; j++) {
                current=theGameList.get(j);
                if (j==theGameList.size()-31){
                    old=current;
                }
                next=theGameList.get(j+1);
                if (current>=50){//200 får bli minsta gränsen
                    if (old<current&&current>next){
                        peakCount++;
                    }
                }
            }
            return peakCount/(3.0/60.0);
        }


    private int calculatePoints( double currentFrequency ) {

        int minRequiredBpm=100;
        int maxRequiredBpm=120;

        if (minRequiredBpm<=currentFrequency&&maxRequiredBpm>=currentFrequency){
            return calculatePointsByTime(5);
        }else if(minRequiredBpm-20<=currentFrequency&&maxRequiredBpm+20>=currentFrequency){
            return calculatePointsByTime(4);
        }else if(minRequiredBpm-40<=currentFrequency&&maxRequiredBpm+40>=currentFrequency){
            return calculatePointsByTime(3);
        }else if(minRequiredBpm-60<=currentFrequency&&maxRequiredBpm+60>=currentFrequency){
            return calculatePointsByTime(2);
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
        int pressure=mostRecentExtremePoints.getMaxPressure();
        if (pressure<maxAllowed&&pressure>minAllowed){
            return 5*multiplyer*multiplyer2;
        }else {
            return 0;
        }
    }
    private int calculatePointsByPressureMin(int multiplyer,int multiplyer2,int maxAllowed){
        int pressure=mostRecentExtremePoints.getMinPressure();
        //int maxAllowed=100;
        if (pressure<maxAllowed){
            return 5*multiplyer*multiplyer2;

        }else {

            return 0;
        }
    }
}