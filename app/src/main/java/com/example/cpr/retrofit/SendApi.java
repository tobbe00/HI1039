package com.example.cpr.retrofit;

import com.example.cpr.Model.Batch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SendApi {

    @POST("/game/send")
    Call<List<Integer>> send(@Body List<Integer> batch);

    @POST("/game/gamestart")
    Call<Boolean> sendGameStart(@Body Boolean gameStarted);



}