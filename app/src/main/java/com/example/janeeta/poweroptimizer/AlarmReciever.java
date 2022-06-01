package com.example.janeeta.poweroptimizer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by janeeta on 3/5/2016.
 */
public class AlarmReciever extends BroadcastReceiver
{
    private TCPClient mTcpClient;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub


        // here you can start an activity or service depending on your need


        //here send the signal to arduino to turn on/off the device
        Bundle bundle=intent.getExtras();
        int deviceId=bundle.getInt("deviceId");
        String action=bundle.getString("action");


        //delete the timer from the offline and online db
        powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(context);
        db.deleteTimer(deviceId);

        ArrayList<String> list = new ArrayList<String>();
        list.add("delete");
        list.add(Integer.toString(deviceId));
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);


        //update the status of the device in offline db according to the action taken on timer
        db.changeDeviceStatus(deviceId, action.toUpperCase());

        //update status of device in online DB
        ArrayList<String> list2 = new ArrayList<String>();
        list2.add("control");
        list2.add(Integer.toString(deviceId));
        list2.add(action.toUpperCase());
        AsyncTaskRunner runner2 = new AsyncTaskRunner();
        runner2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list2);


        mTcpClient = roomScreen.mTcpClient;


        try {

            if (mTcpClient == null) {
                new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
            }
            while (mTcpClient==null){

            }
            //sends the message to the server
            if (mTcpClient != null) {
                if(action.compareToIgnoreCase("on")==0)

                {
                    //Toast.makeText(context, action, Toast.LENGTH_LONG).show();

                    if(deviceId==1)

                        mTcpClient.sendMessage("1");
                    if(deviceId==2)

                        mTcpClient.sendMessage("3");

                }
                if(action.compareToIgnoreCase("off")==0)
                {
                    if(deviceId==1)

                        mTcpClient.sendMessage("2");
                    if(deviceId==2)

                        mTcpClient.sendMessage("4");
                }
                // Show the toast  like in above screen shot
                Toast.makeText(context, "Time reached.. Device with id:  "+deviceId+" is turned "+action+"..!!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error::" + e);
        }

        //here update the activity

        if(deviceInfoScreen.active==true)
        {
            deviceInfoScreen.devTimerText.setText("TIMER: OFF");
            deviceInfoScreen.devTimerButton.setText("SET TIMER");
            deviceInfoScreen.devStatusText.setText("STATUS: "+action.toUpperCase());
            if(action.equalsIgnoreCase("off"))
            {
                deviceInfoScreen.devControlButton.setText("TURN ON");
            }
            if(action.equalsIgnoreCase("on"))
            {
                deviceInfoScreen.devControlButton.setText("TURN OFF");
            }

        }
    }

    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    Log.i("Debug", "Input message: " + message);
                    //serverMsg=message;
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }
    }
    private class AsyncTaskRunner extends AsyncTask<ArrayList, String, String> {

        protected String doInBackground(ArrayList... params) {

            String type = params[0].get(0).toString();
            String id = params[0].get(1).toString();
            String command =null;
            String link="";

            if(type.equals("delete"))
            {
                link = "http://poweroptimizer.comlu.com/deleteTimer.php?id=" + id;
            }

            else if(type.equals("control"))
            {
                command=params[0].get(2).toString();
                link = "http://poweroptimizer.comlu.com/updateStatusOfDevice.php?id=" + id + "&status=" + command;

            }
            try{

                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream isr = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "UTF-8"));
                String webPage = "", data = "";

                while ((data = reader.readLine()) != null) {
                    webPage += data + "\n";
                }
                isr.close();
                String result = webPage.toString();
                conn.disconnect();
            }
            catch (Exception e) {

                //Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
            }

            return "Updated";
        }
    }



}
