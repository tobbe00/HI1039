package com.example.cpr.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * RetrofitService manages the creation and configuration of a Retrofit instance.
 * It initializes Retrofit with a specific base URL and configures it to handle both scalar and JSON responses.
 */
public class RetrofitService {
    private Retrofit retrofit;

    /**
     * Constructs the RetrofitService and initializes the Retrofit instance.
     *
     * @param url The base URL for the web service.
     */
    public RetrofitService(String url){
        initializeRetrofit(url);
    }

    /**
     * Initializes the Retrofit instance with a base URL and converters.
     *
     * @param url The base URL to be used by Retrofit.
     */
    private void initializeRetrofit(String url){
        //url needed
        retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(new Gson())).build();

    }

    /**
     * Provides the initialized Retrofit instance for making HTTP requests.
     *
     * @return The configured Retrofit instance.
     */
    public Retrofit getRetrofit(){
        return retrofit;
    }
}
