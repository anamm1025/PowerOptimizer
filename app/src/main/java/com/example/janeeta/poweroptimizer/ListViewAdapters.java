package com.example.janeeta.poweroptimizer;

/**
 * Created by janeeta on 1/16/2016.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.janeeta.poweroptimizer.Constants.FIRST_COLUMN;
import static com.example.janeeta.poweroptimizer.Constants.SECOND_COLUMN;

public class ListViewAdapters extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    public ListViewAdapters(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub



        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.room_list_layout, null);

            txtFirst=(TextView) convertView.findViewById(R.id.roomName);
            txtSecond=(TextView) convertView.findViewById(R.id.roomId);

        }

        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));

        return convertView;
    }

}
