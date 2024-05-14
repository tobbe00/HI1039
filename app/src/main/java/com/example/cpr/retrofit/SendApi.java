package com.example.cpr.retrofit;

import com.example.cpr.Model.Batch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SendApi {

    @POST("/game/send")
    Call<Integer> send(@Body int batch);

    @POST("/game/gamestart")
    Call<Boolean> sendGameStart(@Body Boolean gameStarted);

    @POST("/game/gameEnd")
    Call<Boolean> sendGameEnd(@Body Boolean gameEnded);



}