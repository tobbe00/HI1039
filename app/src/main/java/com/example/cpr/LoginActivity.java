package com.example.cpr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cpr.Model.User;
import com.example.cpr.retrofit.LoginApi;
import com.example.cpr.retrofit.RetrofitService;

import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * LoginActivity handles user login functionality including input validation, server connection checks,
 * and authentication using a Retrofit service to interact with a backend API.
 */
public class LoginActivity extends BaseActivity {

    // UI elements for username, password input, and connection status display
    private EditText editTextUsername, editTextPassword;
    private TextView connectionView;
    private Button buttonLogin;

    // Variables to handle user authentication and network interaction
    String salt="";
    String hashedPassword="";
    private boolean connected=false;

    // Executor and handler for running tasks on separate threads and updating UI
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    // URL for network connection, default setup
    private static String url = "http://130.229.185.97:8080";

    /**
     * Gets the current URL for network connection.
     * @return The current URL.
     */
    public static String getUrl() {
        return url;
    }

    /**
     * Sets the URL for network connection.
     * @param url The new URL to be set.
     */
    public static void setUrl(String url) {
        LoginActivity.url = "http://"+url+":8080";
    }

    /**
     * Initializes the activity and UI components.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        connectionView = findViewById(R.id.connectionText);


        // Set a click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitService retrofitService = new RetrofitService(url);
                LoginApi loginApi = retrofitService.getRetrofit().create(LoginApi.class);
                testConnection(url);

                // Retrieve entered username and password
                String email = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                loginApi.getSalt(email).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        //salt = response.body();
                        if (response.body() != null) {
                            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                            salt = jsonObject.get("salt").getAsString();

                            hashedPassword = Hashing.sha256()
                                    .hashString(password + salt, StandardCharsets.UTF_8)
                                    .toString();

                            loginApi.login(new User(email, hashedPassword)).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                                    String success = jsonObject.get("success").getAsString();
                                    Log.d("test1", success);
                                    if (success.equals("true")) {
                                        Intent intent = new Intent(LoginActivity.this, BluetoothActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("test1", "Login failed");
                                }
                            });
                        } else {
                            connectionView.setVisibility(View.VISIBLE);
                            connectionView.setText("Wrong email or password");
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("test1", "Failed");
                    }
                });
            }
        });
    }

    /**
     * Checks server connection by attempting to connect to the provided URL.
     * @param url The URL to check connection with.
     */
    private void testConnection(String url) {

        executor.execute(new Runnable() {

            @Override
            public void run() {

                try {
                    Log.d("test1",url);
                    URL myUrl = new URL(url);
                    URLConnection connection = myUrl.openConnection();
                    connection.setConnectTimeout(1000);
                    connection.connect();
                    connected = true;
                } catch (Exception e) {
                    //ta hand om exceptions
                    connected = false;
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        if(!connected){
                            connectionView.setVisibility(View.VISIBLE);
                            connectionView.setText("Failed to login, Server might be down");
                        }
                    }
                });
            }
        });
    }

    /**
     * Adjusts the menu by hiding the logout option on the login screen.
     * @param menu The menu to adjust.
     */
    @Override
    protected void adjustMenu(Menu menu) {
        MenuItem logoutItem = menu.findItem(R.id.logout);
        if (logoutItem != null) {
            logoutItem.setVisible(false); // Hide the logout item
        }
    }
}