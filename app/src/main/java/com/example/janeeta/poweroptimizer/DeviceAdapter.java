package com.example.janeeta.poweroptimizer;

/**
 * Created by janeeta on 1/16/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class DeviceAdapter extends ArrayAdapter<Device> {


    private List<Device> dlist;

    public DeviceAdapter(Context context, int view, List<Device> passedList) {
        super(context, view, passedList);
        dlist = passedList;

    }

    @Override
    public int getCount() {
        return dlist.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentView = convertView;
        LayoutInflater currentViewInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currentView = currentViewInflater.inflate(R.layout.device_list_layout, null);
        Device currentDevice = dlist.get(position);
        TextView name = (TextView) currentView.findViewById(R.id.deviceName);
        TextView didd = (TextView) currentView.findViewById(R.id.devID);
        TextView statuss = (TextView) currentView.findViewById(R.id.dstatuss);

        name.setText(currentDevice.getDeviceName());
        didd.setText(""+currentDevice.getDeviceId()+"");

        if(currentDevice.getIsAttached().contains("NO") || currentDevice.getIsAttached().contains("no"))
        {
            statuss.setText("DEVICE REMOVED (STATUS: UNKNOWN)");
        }
        else
            statuss.setText(currentDevice.getStatus());

        return currentView;
    }
}