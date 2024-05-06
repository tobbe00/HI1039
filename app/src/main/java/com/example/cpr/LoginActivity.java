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
import com.example.cpr.retrofit.RetrofitService;
import com.example.cpr.retrofit.RoundApi;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Statement;
import java.util.List;
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

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonTest = findViewById(R.id.testButton);
        connectionView = findViewById(R.id.connectionText);
        buttonAddRound = findViewById(R.id.addRoundButton);

        RetrofitService retrofitService = new RetrofitService();
        RoundApi roundApi = retrofitService.getRetrofit().create(RoundApi.class);

        // Set a click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve entered username and password
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Implement authentication logic here
                if (username.equals("Admin") && password.equals("123")) {
                    // Successful login
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
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