package com.example.cpr.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService(String url){

        initializeRetrofit(url);
    }

    private void initializeRetrofit(String url){
        //url needed
        retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(new Gson())).build();

    }

    public Retrofit getRetrofit(){
        return retrofit;
    }
}
