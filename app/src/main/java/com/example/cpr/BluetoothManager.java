package com.example.cpr;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BluetoothManager {
    private BluetoothGatt bluetoothGatt;
    private Context context;

    public BluetoothManager(Context context) {
        this.context = context;
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("BluetoothGatt", "Connected to GATT server.");
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("BluetoothGatt", "Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                for (BluetoothGattService service : gatt.getServices()) {
                    Log.i("BluetoothGatt", "Service discovered: " + service.getUuid());
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        Log.i("BluetoothGatt", "Characteristic discovered: " + characteristic.getUuid());
                        // Optionally filter and subscribe to certain characteristics dynamically
                        /*if (isDesiredCharacteristic(characteristic.getUuid())) {
                            subscribeToCharacteristic(gatt, service.getUuid(), characteristic.getUuid());
                        }*/
                    }
                }
            } else {
                Log.w("BluetoothGatt", "onServicesDiscovered received: " + status);
            }
        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            Log.i("BluetoothGatt", "Data received: " + new String(data));
            String dataString = new String(data);
            writeToFile(dataString, context);
        }
    };

    private void subscribeToCharacteristic(BluetoothGatt gatt, UUID serviceUUID, UUID characteristicUUID) {
        BluetoothGattService service = gatt.getService(serviceUUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            if (characteristic != null) {
                gatt.setCharacteristicNotification(characteristic, true);
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                        UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                if (descriptor != null) {
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt.writeDescriptor(descriptor);
                }
            }
        }
    }


    public BluetoothGatt connectToDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice("C8:5A:35:2F:3F:64");
        bluetoothGatt = device.connectGatt(context, false, gattCallback);
        return bluetoothGatt;
    }

    public void disconnect() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
    }
    private void writeToFile(String data, Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("data_log.txt", Context.MODE_APPEND);
            fos.write(data.getBytes());
            fos.write("\n".getBytes());
        } catch (IOException e) {
            Log.e("BluetoothGatt", "File write failed", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e("BluetoothGatt", "Error closing file", e);
                }
            }
        }
    }
}