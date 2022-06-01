package com.example.janeeta.poweroptimizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CheckSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_summary);

        //here get if the check summary is for the room or building
        String checkRoomOrBuilding=((MyApplication) this.getApplication()).getCheckSummary();

        powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);
        List<Device> deviceList = new ArrayList<Device>();

        TextView nameText = (TextView) findViewById(R.id.checkText);

        if(checkRoomOrBuilding.contains("building"))
        {
            deviceList=db.getAllDevices();//getting all devices of a building
            nameText.setText("( "+"BUILDING"+" )");
        }
        else if(checkRoomOrBuilding.contains("room"))
        {
            int rid=((MyApplication) this.getApplication()).getRoomId();
            deviceList=db.getAllDevicesOfARoom(rid);//getting all devices of a room
            String rname=((MyApplication) this.getApplication()).getRoomName();
            nameText.setText("( "+"ROOM: "+rname + " )");

        }

        //here show the data in the listView

        ListView listView=(ListView)findViewById(R.id.summaryList);

        ArrayAdapter<Device> adapter = new checkSummaryAdapter(this,R.layout.check_summary_list_layout, deviceList);
        listView.setAdapter(adapter);
    }

    public void logoutClicked(View view){

        Intent i = new Intent(this,LoginScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    public void backClicked(View view)
    {
        Intent i=new Intent(this,roomScreen.class);;
        if(((MyApplication) this.getApplication()).getCheckSummary().contains("room"))
        {
            i = new Intent(this,selectedRoomScreen.class);
        }
        else if(((MyApplication) this.getApplication()).getCheckSummary().contains("building"))
        {
            i = new Intent(this,roomScreen.class);
        }


        startActivity(i);

    }
}
