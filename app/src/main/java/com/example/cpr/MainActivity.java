package com.example.cpr;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cpr.Model.Batch;
import com.example.cpr.retrofit.RetrofitService;
import com.example.cpr.retrofit.SendApi;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private TextView countdownText;
    private SendApi sendApi;


    private BluetoothGatt mBluetoothGatt = null;
    private List<Integer> batch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        countdownText = findViewById(R.id.countDownTimer); // Make sure you have a TextView in your layout for the countdown
        batch = new ArrayList<>();

        RetrofitService retrofitService = new RetrofitService();

        sendApi = retrofitService.getRetrofit().create(SendApi.class);


        BluetoothDevice device = getIntent().getExtras().getParcelable("btdevice");

        mBluetoothGatt = device.connectGatt(MainActivity.this, false, mBtGattCallback);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make the button disappear
                startButton.setVisibility(View.GONE);
                mBluetoothGatt.discoverServices();

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

    private final BluetoothGattCallback mBtGattCallback = new BluetoothGattCallback() {
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Debug: list discovered services
                List<BluetoothGattService> services = gatt.getServices();

                for (BluetoothGattService service : services) {
                    Log.i("test1", "Service: " + service.getUuid().toString());
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic chara : characteristics) {
                        Log.i("test1", "Charateristics: " + chara.getUuid().toString());
                    }
                }

                BluetoothGattService uartService = gatt.getService(UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"));

                BluetoothGattCharacteristic characteristic = uartService.getCharacteristic(UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e"));

                boolean success = gatt.setCharacteristicNotification(characteristic, true);

                if (success) {
                    Log.i("test1", "setCharactNotification success");

                    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));

                    //descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);

                    gatt.writeDescriptor(descriptor); // callback: onDescriptorWrite
                } else {
                    Log.i("test1", "setCharacteristicNotification failed");
                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {
            Log.i("test1", "onCharacteristicWrite " + characteristic.getUuid().toString());
        }

        @Override
        public void onDescriptorWrite(final BluetoothGatt gatt, BluetoothGattDescriptor
                descriptor, int status) {

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("test1", "Descriptor written succesfully");
            } else {
                Log.d("test1", "Failed to write descriptor");
            }
        }

        //temporary for debug purposes
        int count = 0;
        int batchId =0;
        //int batch[] = new int[5];

        /**
         * Callback called on characteristic changes, e.g. when a sensor data value is changed.
         * This is where we receive notifications on new sensor data.
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            //Log.d("test1", "oncharacteristichcanged: " + characteristic.getUuid());

            byte[] data = characteristic.getValue();

            String s = new String(data, StandardCharsets.UTF_8);

            //ascii to int
            int value = (s.charAt(0)-48)*100+(s.charAt(1)-48)*10+(s.charAt(2)-48);

            Log.d("test1",""+value);

            batch.add(value);
            count++;

            if(batch.size()==5){

                Log.d("test1",batch.toString());
                sendApi.send(batch).enqueue(new Callback<List<Integer>>() {
                    @Override
                    public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                        Log.d("test1", "Save successful");
                    }

                    @Override
                    public void onFailure(Call<List<Integer>> call, Throwable t) {
                        Log.d("test1", "Save failed");
                    }
                });

                batch.clear();
            }





        /*mHandler.post(new Runnable() {
            public void run() {
                dataText.setVisibility(View.VISIBLE);
                dataText.setText(s);
            }
        });*/






            /*if (UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e").equals(characteristic.getUuid())) {

                Log.d("test1", "test");
            }*/

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {

            Log.d("test1", "onCharacteristicChanged " + characteristic.getUuid());
            Log.d("test1", "received");
            // Log.i("test", "onCharacteristicRead " + characteristic.getUuid().toString());
            if (UUID.fromString("00002902-0000-1000-8000-00805f9b34fb").equals(characteristic.getUuid())) {
                byte[] data = characteristic.getValue();

            }
        }

    };

    public static int byteArrayToIntBigEndian(byte[] byteArray) {
        if (byteArray.length < 4) {
            throw new IllegalArgumentException("Byte array too short!");
        }
        return ByteBuffer.wrap(byteArray)
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
    }

    public static int byteArrayToIntLittleEndian(byte[] byteArray) {
        if (byteArray.length < 4) {
            throw new IllegalArgumentException("Byte array too short!");
        }
        return ByteBuffer.wrap(byteArray)
                .order(ByteOrder.LITTLE_ENDIAN)
                .getInt();
    }

}