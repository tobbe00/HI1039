package com.example.cpr.utils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {
    private Context context;
    private List<BluetoothDevice> devices;

    public DeviceListAdapter(Context context, List<BluetoothDevice> devices) {
        super(context, android.R.layout.simple_list_item_1, devices);
        this.context = context;
        this.devices = devices;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, android.R.layout.simple_list_item_1, null);
        }

        BluetoothDevice device = devices.get(position);
        TextView text1 = convertView.findViewById(android.R.id.text1);
        text1.setText(device.getName() != null ? device.getName() : device.getAddress());

        return convertView;
    }
}