package com.example.cpr.retrofit;

import com.example.cpr.Model.Batch;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SendApi {

    @POST("/game/send")
    Call<Batch> send(@Body Batch batch);
}