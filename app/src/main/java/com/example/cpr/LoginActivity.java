package com.example.cpr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cpr.Model.Round;
import com.example.cpr.Model.User;
import com.example.cpr.retrofit.LoginApi;
import com.example.cpr.retrofit.RetrofitService;
import com.example.cpr.retrofit.RoundApi;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private TextView connectionView;

    private Button buttonLogin,buttonTest, buttonAddRound;

    String salt="";
    String hashedPassword="";

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonTest = findViewById(R.id.testButton);
        connectionView = findViewById(R.id.connectionText);
        buttonAddRound = findViewById(R.id.addRoundButton);

        RetrofitService retrofitService = new RetrofitService();
        RoundApi roundApi = retrofitService.getRetrofit().create(RoundApi.class);
        LoginApi loginApi = retrofitService.getRetrofit().create(LoginApi.class);

        // Set a click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            // Log.d("test1",hashedPassword);

                            //Log.d("test1", "got response+ "+salt);

                            loginApi.login(new User(email, hashedPassword)).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                                    String success = jsonObject.get("success").getAsString();

                                    if (success.equals("true")) {
                                        Log.d("test1", "Successfully logged in");
                                        Intent intent = new Intent(LoginActivity.this, BluetoothActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                    Log.d("test1", "login failed");

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("test1", "failed");
                    }
                });


                // Implement authentication logic here
                if (email.equals("Admin") && password.equals("123")) {
                    // Successful login
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    //saveLoginState(true);
                    // Create an Intent to start MainActivity
                    Intent intent = new Intent(LoginActivity.this, BluetoothActivity.class);
                    startActivity(intent);

                    // Optionally, if you want to remove LoginActivity from the activity stack so the user cannot navigate back to it using the back button:
                    finish();
                } else {
                    // Failed login
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Ändra url beroende på wifi ip
            String url = "http://130.229.137.214:8080";

        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test","button works");

                //url som inparameter
                testConnection("123");
            }
        });

        buttonAddRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test","button works");

                roundApi.save(new Round(500,1))
                        .enqueue(new Callback<Round>() {
                            @Override
                            public void onResponse(Call<Round> call, Response<Round> response) {

                                Log.d("test","Save successful");
                            }

                            @Override
                            public void onFailure(Call<Round> call, Throwable t) {
                                Log.d("test","Save failed");
                                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE,"error occured");
                            }

                });

            }
        });
    }

    private void testConnection(String url) {

        executor.execute(new Runnable() {
            boolean connected=false;
            @Override
            public void run() {
                //Background work
                try {
                    URL myUrl = new URL("http://192.168.10.124:8080");
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
                        connectionView.setVisibility(View.VISIBLE);
                        if(connected){
                            connectionView.setText("Connected!");
                        }else{
                            connectionView.setText("Not conencted, url might be down");
                        }
                    }
                });
            }
        });
    }

}