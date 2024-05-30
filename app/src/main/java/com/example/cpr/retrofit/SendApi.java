package com.example.cpr.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * SendApi defines the endpoints for sending game-related data to the server using Retrofit.
 */
public interface SendApi {

    /**
     * Sends a batch of pressure data to the server.
     * @param batch The batch of pressure data to be sent.
     * @return A Call object to send the request asynchronously.
     */
    @POST("/game/send")
    Call<Integer> send(@Body int batch);

    /**
     * Sends a signal to the server indicating that the game has started.
     * @param gameStarted A boolean indicating that the game has started.
     * @return A Call object to send the request asynchronously.
     */
    @POST("/game/gamestart")
    Call<Boolean> sendGameStart(@Body Boolean gameStarted);

    /**
     * Sends a signal to the server indicating that the game has ended.
     * @param gameEnded A boolean indicating that the game has ended.
     * @return A Call object to send the request asynchronously.
     */
    @POST("/game/gameEnd")
    Call<Boolean> sendGameEnd(@Body Boolean gameEnded);

}