package com.example.cpr;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cpr.utils.DeviceListAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * BluetoothActivity manages Bluetooth operations including scanning, connecting to devices,
 * and handling Bluetooth Low Energy (BLE) communication.
 */
public class BluetoothActivity extends BaseActivity {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothGatt mBluetoothGatt = null;

    private TextView dataText;
    private Handler mHandler;
    private boolean mScanning;
    private ArrayList<BluetoothDevice> mDeviceList;
    private DeviceListAdapter deviceListAdapter;
    private ListView deviceListView;

    public static final int REQUEST_ENABLE_BT = 1000;
    public static final int REQUEST_ACCESS_LOCATION = 1001;

    /**
     * Initializes Bluetooth setup and UI components.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        mHandler = new Handler();
        mDeviceList = new ArrayList<>();
        Button scanButton = findViewById(R.id.scanButton);
        deviceListView = findViewById(R.id.device_list);
        dataText = findViewById(R.id.dataText);
        deviceListAdapter = new DeviceListAdapter(this, mDeviceList);
        deviceListView.setAdapter(deviceListAdapter);

        scanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mBluetoothAdapter.isEnabled()){
                    mDeviceList.clear();
                    scanForDevices(true);
                }else{
                    Toast.makeText(getApplicationContext(), "Please enable bluetooth to continue", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device  =(BluetoothDevice) parent.getItemAtPosition(position);
                Log.d("test1","Value is "+device.getName());

                mBluetoothGatt = device.connectGatt(BluetoothActivity.this, false, mBtGattCallback);

                if(mBluetoothGatt!=null && mBluetoothGatt.connect()){
                    Intent intent = new Intent (BluetoothActivity.this, MainActivity.class);
                    intent.putExtra("btdevice", device);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Called when the activity becomes visible.
     * Initializes Bluetooth Low Energy (BLE) capabilities.
     */
    @Override
    protected void onStart() {
        super.onStart();
        initBLE();
    }

    // GATT callback to manage Bluetooth connection changes
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
                        Log.d("test1", "Connected");
                    }
                });

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Close connection and display info in ui
                mBluetoothGatt = null;
                mHandler.post(new Runnable() {
                    public void run() {
                        // mDataView.setText(R.string.disconnected);
                        TextView textViewError = findViewById(R.id.connectionText);
                        textViewError.setText("Disconnected");
                        textViewError.setVisibility(View.VISIBLE);

                        Log.d("test1", "Disconnected");
                    }
                });
            }
        }
    };

    /**
     * Initiates BLE scanning.
     * @param enable Enables or disables scanning.
     */
    private void scanForDevices(final boolean enable) {
        Log.d("test1","clicked");
        final BluetoothLeScanner scanner =
                mBluetoothAdapter.getBluetoothLeScanner();
        if (enable) {
            if (!mScanning) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mScanning) {
                            mScanning = false;
                            scanner.stopScan(mScanCallback);
                            Toast.makeText(getApplicationContext(), "BLE scan stopped", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, 5000); //scan for 5000ms

                mScanning = true;
                scanner.startScan(mScanCallback);

                Toast.makeText(getApplicationContext(), "BLE scan started", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (mScanning) {
                mScanning = false;
                scanner.stopScan(mScanCallback);
                Toast.makeText(getApplicationContext(), "BLE scan stopped", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handles scanning results
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            final BluetoothDevice device = result.getDevice();
            final String name = device.getName();

            mHandler.post(new Runnable() {
                public void run() {
                    if (name != null
                            && name.contains("micro:bit")
                            && !mDeviceList.contains(device)) {
                        mDeviceList.add(device);

                        deviceListAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.i("test1", "onBatchScanResult");
        }
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.i("test1", "onScanFailed"+errorCode);
        }
    };

    /**
     * Initializes Bluetooth Low Energy capabilities.
     */
    private void initBLE() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish();
        } else {
            // Access Location is a "dangerous" permission
            int hasAccessLocation = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasAccessLocation != PackageManager.PERMISSION_GRANTED) {
                // ask the user for permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_LOCATION);
                // the callback method onRequestPermissionsResult gets the result of this request
            }
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // turn on BT
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }
    }

    /**
     * Callback for handling the result of permission requests.
     * @param requestCode The request code passed in requestPermissions(android.app.Activity, String[], int).
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_LOCATION) {
            // if request is cancelled, the results array is empty
            if (grantResults.length == 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // stop this activity
                this.finish();
            }
        }
    }
}