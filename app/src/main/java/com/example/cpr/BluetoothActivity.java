package com.example.cpr;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BluetoothActivity extends BaseActivity {


    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    boolean isConnected = false;
    private BluetoothGatt mBluetoothGatt = null;

    private TextView dataText;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mHandler = new Handler();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(broadcastReceiver, filter);

        Button checkConnectionButton = findViewById(R.id.checkConnectionButton);

        dataText = findViewById(R.id.dataText);


        checkConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                Log.d("Test1", "Clicked on butotn");
                for (BluetoothDevice device : pairedDevices) {
                    Log.d("Test1", "Found bonded devices");


                    mBluetoothGatt = device.connectGatt(BluetoothActivity.this, false, mBtGattCallback);

                    if(mBluetoothGatt!=null && mBluetoothGatt.connect()){
                        Intent intent = new Intent (BluetoothActivity.this, MainActivity.class);
                        intent.putExtra("btdevice", device);
                        startActivity(intent);
                    }

                    if (mBluetoothGatt.connect()) {
                        Log.d("test1", "success");
                    } else {
                        Log.d("test1", "fail");
                    }
                }

                /*if(mBluetoothAdapter.isEnabled()){

                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                    for (BluetoothDevice device : pairedDevices) {
                        Log.d("test",device.getAddress());
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
                }*/
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
                isConnected = true;
                Toast.makeText(getApplicationContext(), "Device is now Connected123", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                isConnected = false;
                Toast.makeText(getApplicationContext(), "Device is disconnected123", Toast.LENGTH_SHORT).show();
            }


        }
    };

    public void checkIfConnected(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("isConnected", (Class[]) null);
            boolean connected = (boolean) m.invoke(device, (Object[]) null);

            isConnected = connected == true;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private final BluetoothGattCallback mBtGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                mBluetoothGatt = gatt;
                mHandler.post(new Runnable() {
                    public void run() {
                        // mDataView.setText(R.string.connected);
                        TextView textViewError = findViewById(R.id.connectionText);
                        textViewError.setText("Connected");
                        textViewError.setVisibility(View.VISIBLE); // Make sure the TextView is visible
                        Log.d("test1", "changed1");
                    }
                });
                // Discover services
               // gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Close connection and display info in ui
                mBluetoothGatt = null;
                mHandler.post(new Runnable() {
                    public void run() {
                        // mDataView.setText(R.string.disconnected);
                        TextView textViewError = findViewById(R.id.connectionText);
                        textViewError.setText("Disconnected");
                        textViewError.setVisibility(View.VISIBLE); // Make sure the TextView is visible

                        Log.d("test1", "changed2");
                    }
                });
            }
        }



    };
}