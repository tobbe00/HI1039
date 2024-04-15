package com.example.cpr;

import android.animation.TypeConverter;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BluetoothActivity extends AppCompatActivity {


    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    boolean isConnected = false;
    private BluetoothGatt mBluetoothGatt = null;
    private BluetoothManager mBluetoothManager = null;

    private Handler mHandler;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
         //mBluetoothManager = new BluetoothManager(this);

        mHandler = new Handler();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(broadcastReceiver, filter);

        Button checkConnectionButton = findViewById(R.id.checkConnectionButton);
        checkConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device : pairedDevices) {
                    Log.d("Test",device.getName());

                    mBluetoothGatt = device.connectGatt(BluetoothActivity.this, false, mBtGattCallback);
                    if(mBluetoothGatt!=null){
                        Log.d("test",mBluetoothGatt.getDevice().getName());
                    }
                    if(mBluetoothGatt.connect()){
                        Log.d("test","success");
                    }else{
                        Log.d("test","fail");
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
                isConnected=true;
                Toast.makeText(getApplicationContext(), "Device is now Connected123",    Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                isConnected=false;
                Toast.makeText(getApplicationContext(), "Device is disconnected123",       Toast.LENGTH_SHORT).show();
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

    private final BluetoothGattCallback mBtGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                mBluetoothGatt = gatt;
                mHandler.post(new Runnable() {
                    public void run() {
                       // mDataView.setText(R.string.connected);
                        TextView textViewError = findViewById(R.id.textViewError);
                        textViewError.setText("Connected");
                        textViewError.setVisibility(View.VISIBLE); // Make sure the TextView is visible
                        Log.d("test","changed1");
                    }
                });
                // Discover services
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Close connection and display info in ui
                mBluetoothGatt = null;
                mHandler.post(new Runnable() {
                    public void run() {
                       // mDataView.setText(R.string.disconnected);
                        TextView textViewError = findViewById(R.id.textViewError);
                        textViewError.setText("Disconnected");
                        textViewError.setVisibility(View.VISIBLE); // Make sure the TextView is visible

                        Log.d("test","changed2");
                    }
                });
            }
        }
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Debug: list discovered services
                List<BluetoothGattService> services = gatt.getServices();

                for (BluetoothGattService service : services) {
                    Log.i("test", service.getUuid().toString());
                }

            }
        }
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {
            Log.i("test", "onCharacteristicWrite " + characteristic.getUuid().toString());



        }

        @Override
        public void onDescriptorWrite(final BluetoothGatt gatt, BluetoothGattDescriptor
                descriptor, int status) {
            Log.i("test", "onDescriptorWrite, status " + status);


        }

        /**
         * Callback called on characteristic changes, e.g. when a sensor data value is changed.
         * This is where we receive notifications on new sensor data.
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic) {
            Log.d("test","test");
            // debug
            // Log.i(LOG_TAG, "onCharacteristicChanged " + characteristic.getUuid());

            // if response and id matches



        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {
           // Log.i("test", "onCharacteristicRead " + characteristic.getUuid().toString());
        }
    };

}