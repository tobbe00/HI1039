package com.example.cpr.retrofit;

import com.example.cpr.Model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {

    @GET("/users/salt?")
    Call<String> getSalt(@Query("email") String email);

    @POST("/users/login")
    Call<String> login(@Body User user);




}
