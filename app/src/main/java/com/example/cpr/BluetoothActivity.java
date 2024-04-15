package com.example.cpr;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;
import java.util.Set;


public class BluetoothActivity extends AppCompatActivity {


    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    boolean isConnected = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(broadcastReceiver, filter);

        Button checkConnectionButton = findViewById(R.id.checkConnectionButton);
        checkConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mBluetoothAdapter.isEnabled()){

                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                    for (BluetoothDevice device : pairedDevices) {
                        checkIfConnected(device);
                        if(isConnected){
                            //Log.d("Test",device.getBluetoothClass().toString());
                            Toast.makeText(getApplicationContext(),device.getName() +" CONNECTED",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BluetoothActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"Please connect to a device",Toast.LENGTH_LONG).show();
                        }
                    }
                    }else{
                    Intent enableBtIntent = new Intent(mBluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(enableBtIntent);

                    TextView textViewError = findViewById(R.id.textViewError);
                    textViewError.setText("Can't connect or find device");
                    textViewError.setVisibility(View.VISIBLE); // Make sure the TextView is visible
                }
            }
        });
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        BluetoothDevice device;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                isConnected=true;
                Toast.makeText(getApplicationContext(), "Device is now Connected",    Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                isConnected=false;
                Toast.makeText(getApplicationContext(), "Device is disconnected",       Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void checkIfConnected(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("isConnected", (Class[]) null);
            boolean connected = (boolean) m.invoke(device, (Object[]) null);

            if(connected==true){
                isConnected=true;
            }else{
                isConnected=false;
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }



}