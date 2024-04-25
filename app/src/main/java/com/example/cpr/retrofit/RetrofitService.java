package com.example.cpr.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService(){
        initializeRetrofit();
    }

    private void initializeRetrofit(){
        //url needed
        retrofit = new Retrofit.Builder().baseUrl("http://130.229.137.214:8080").addConverterFactory(GsonConverterFactory.create(new Gson())).build();
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }
}
