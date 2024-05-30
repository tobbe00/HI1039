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

import com.example.cpr.retrofit.RetrofitService;
import com.example.cpr.retrofit.SendApi;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MainActivity handles the interaction with a Bluetooth device, including starting and managing game rounds.
 */
public class MainActivity extends BaseActivity {

    private Button startButton;
    private Button restartButton;
    private TextView finishedText;
    private TextView prepareText;
    private TextView countdownText;
    private SendApi sendApi;

    private CountDownTimer preparationCountDownTimer;
    private CountDownTimer mainCountDownTimer;

    // Bluetooth GATT object for Bluetooth operations
    private BluetoothGatt mBluetoothGatt = null;

    // Callback for handling Bluetooth GATT events
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

            String dataString = new String(data, StandardCharsets.UTF_8);

            try {
                // Try parsing the string directly to an integer
                int value = Integer.parseInt(dataString.trim()); // Trim any whitespace

                Log.d("test1", "Received value: " + value);

                // Send the value to the server
                sendApi.send(value).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.d("test1", "Save successful");
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.d("test1", "Save failed");
                    }
                });
            } catch (NumberFormatException e) {
                Log.e("MainActivity", "Failed to parse integer from received data: " + dataString, e);
            }


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


    /**
     * Called when the activity is starting. Initializes the activity.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        prepareText = findViewById(R.id.prepareText);
        startButton = findViewById(R.id.startButton);
        countdownText = findViewById(R.id.countDownTimer);
        finishedText = findViewById(R.id.finishedRound);
        restartButton = findViewById(R.id.restartButton);
        restartButton.setEnabled(false);
        restartButton.setVisibility(View.GONE);

        RetrofitService retrofitService = new RetrofitService(LoginActivity.getUrl());

        sendApi = retrofitService.getRetrofit().create(SendApi.class);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRound();

            }
        });
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameEnd();
                restartRound();
            }
        });
    }

    /**
     * Starts a new game round by establishing a Bluetooth connection and initiating a countdown.
     */
    private void startRound() {
        if (mBluetoothGatt == null) {
            BluetoothDevice device = getIntent().getExtras().getParcelable("btdevice");
            if (device != null) {
                mBluetoothGatt = device.connectGatt(MainActivity.this, false, mBtGattCallback);
            } else {
                Log.e("MainActivity", "Bluetooth Device not available");
                return;
            }
        }

        startButton.setVisibility(View.GONE);
        startButton.setEnabled(false);
        restartButton.setVisibility(View.VISIBLE);
        restartButton.setEnabled(true);
        prepareText.setVisibility(View.VISIBLE);
        countdownText.setVisibility(View.VISIBLE);

        if (preparationCountDownTimer != null) {
            preparationCountDownTimer.cancel();
        }
        preparationCountDownTimer = new CountDownTimer(3500, 1000) {
            public void onTick(long millisUntilFinished) {
                countdownText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                sendApi.sendGameStart(true).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Log.d("test1", "Response from server");
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
                startMainCountdown();
            }
        }.start();
    }

    /**
     * Starts the main countdown timer for the game round.
     */
    private void startMainCountdown() {
        mBluetoothGatt.discoverServices();
        prepareText.setVisibility(View.GONE);
        if (mainCountDownTimer != null) {
            mainCountDownTimer.cancel(); // Cancel existing timer if it exists
        }
        mainCountDownTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                countdownText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                }
                Log.d("test1", " ended");
                gameEnd();
                countdownText.setVisibility(View.GONE);
                finishedText.setVisibility(View.VISIBLE);
                restartButton.setVisibility(View.VISIBLE);
            }
        }.start();

    }

    /**
     * Restarts the game round by resetting timers and UI components.
     */
    private void restartRound() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
        if (preparationCountDownTimer != null) {
            preparationCountDownTimer.cancel();
        }
        if (mainCountDownTimer != null) {
            mainCountDownTimer.cancel();
        }

        prepareText.setVisibility(View.GONE);
        countdownText.setVisibility(View.GONE);
        finishedText.setVisibility(View.GONE);
        restartButton.setVisibility((View.GONE));
        restartButton.setEnabled(false);
        startButton.setVisibility(View.VISIBLE);
        startButton.setEnabled(true);
    }

    /**
     * Handles the end of the game round, resetting UI components and potentially sending end game data to the server.
     */
    private void gameEnd() {
        sendApi.sendGameEnd(true).enqueue((new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
            }
        }));
    }
}