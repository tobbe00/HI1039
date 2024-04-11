package com.example.cpr;

import android.os.Bundle;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private TextView countdownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        countdownText = findViewById(R.id.countDownTimer); // Make sure you have a TextView in your layout for the countdown

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make the button disappear
                startButton.setVisibility(View.GONE);

                // Start a countdown of 15 seconds, for example
                new CountDownTimer(15000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        // Update a TextView to show the countdown
                        countdownText.setText(String.valueOf(millisUntilFinished / 1000));
                    }

                    public void onFinish() {
                        countdownText.setText("Done");
                    }
                }.start();
            }
        });
    }
}