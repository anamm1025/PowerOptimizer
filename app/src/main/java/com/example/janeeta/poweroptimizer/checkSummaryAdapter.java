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


public class checkSummaryAdapter extends ArrayAdapter<Device> {


    private List<Device> dlist;

    public checkSummaryAdapter(Context context, int view, List<Device> passedList) {
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
        currentView = currentViewInflater.inflate(R.layout.check_summary_list_layout, null);
        Device currentDevice = dlist.get(position);
        TextView name = (TextView) currentView.findViewById(R.id.deviceName);
        TextView devStatus = (TextView) currentView.findViewById(R.id.deviceStatus);
        TextView devAuto = (TextView) currentView.findViewById(R.id.deviceAutomation);
        TextView devAttached = (TextView) currentView.findViewById(R.id.deviceAttached);
        TextView timerStatuss = (TextView) currentView.findViewById(R.id.timerStatus);


        name.setText(currentDevice.getDeviceName());

        //String jj=currentDevice.getIsAttached();
        devAttached.setText(currentDevice.getIsAttached());
        if(currentDevice.getIsAttached().contains("NO"))//then it means device is removed by user
        {
            devStatus.setText("--");
            timerStatuss.setText("--");
            devAuto.setText("--");

        }
        else
        {
            devStatus.setText(currentDevice.getStatus());
            timerStatuss.setText(currentDevice.getTimerStatus());
            devAuto.setText(currentDevice.getIsAutomation());
        }

        return currentView;
    }
}