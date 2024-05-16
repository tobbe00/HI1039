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


public class BluetoothActivity extends BaseActivity {


    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    boolean isConnected = false;
    private BluetoothGatt mBluetoothGatt = null;

    private TextView dataText;
    private Handler mHandler;


    private boolean mScanning;

    private ArrayList<BluetoothDevice> mDeviceList;
    private DeviceListAdapter deviceListAdapter;
    private ListView deviceListView;

    public static final int REQUEST_ENABLE_BT = 1000;
    public static final int REQUEST_ACCESS_LOCATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mHandler = new Handler();

        mDeviceList = new ArrayList<>();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(broadcastReceiver, filter);

       // Button checkConnectionButton = findViewById(R.id.checkConnectionButton);

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




        /*checkConnectionButton.setOnClickListener(new View.OnClickListener() {
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
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        initBLE();
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

    private void scanForDevices(final boolean enable) {
        Log.d("test1","clicked");
        final BluetoothLeScanner scanner =
                mBluetoothAdapter.getBluetoothLeScanner();
        if (enable) {
            if (!mScanning) {
                // stop scanning after a pre-defined scan period, SCAN_PERIOD
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

    /*
     * Implementation of scan callback methods
     */
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //Log.i(LOG_TAG, "onScanResult");
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

    // callback for Activity.requestPermissions
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