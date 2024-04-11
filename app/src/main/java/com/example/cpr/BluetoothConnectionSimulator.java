package com.example.cpr;

public class BluetoothConnectionSimulator {
    // This flag simulates the Bluetooth connection state
    public static boolean isBluetoothConnected = true;

    // You can add methods to simulate connecting and disconnecting if needed
    public static void connect() {
        isBluetoothConnected = true;
    }

    public static void disconnect() {
        isBluetoothConnected = false;
    }
}

