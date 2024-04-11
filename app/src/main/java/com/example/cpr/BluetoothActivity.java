package com.example.cpr;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Set;


public class BluetoothActivity extends AppCompatActivity {


    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        Button checkConnectionButton = findViewById(R.id.checkConnectionButton);
        checkConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the simulated Bluetooth is "connected"
                /*Log.d("t","test");
                 */

                if(mBluetoothAdapter.isEnabled()){
                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                    for (BluetoothDevice device : pairedDevices){
                        Toast.makeText(getApplicationContext(),device.getName() +" CONNECTED",Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent(BluetoothActivity.this, MainActivity.class);
                    startActivity(intent);

                }else{
                    Intent enableBtIntent = new Intent(mBluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(enableBtIntent);

                    TextView textViewError = findViewById(R.id.textViewError);
                    textViewError.setText("Can't connect or find device");
                    textViewError.setVisibility(View.VISIBLE); // Make sure the TextView is visible
                }

               /* if (BluetoothConnectionSimulator.isBluetoothConnected) {
                    // If connected, move to MainActivity
                    Intent intent = new Intent(BluetoothActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    TextView textViewError = findViewById(R.id.textViewError);
                    textViewError.setText("Can't connect or find device");
                    textViewError.setVisibility(View.VISIBLE); // Make sure the TextView is visible

                }*/
            }
        });
    }
}