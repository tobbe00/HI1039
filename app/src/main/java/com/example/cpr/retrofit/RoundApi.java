package com.example.cpr.retrofit;

import com.example.cpr.Model.Round;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RoundApi {
    @POST("/rounds/save")
    Call<Round> save(@Body Round round);
}
