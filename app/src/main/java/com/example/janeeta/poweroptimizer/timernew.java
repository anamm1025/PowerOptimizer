package com.example.janeeta.poweroptimizer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.*;
public class timernew extends AppCompatActivity {


    long chosenTimeMills;
    Calendar c;
    TimePicker timePicker;
    Calendar cal;
    int deviceId;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timernew);
        Bundle bundle=this.getIntent().getExtras();
        deviceId=bundle.getInt("deviceID");

        if(bundle.getString("type").equals("change"))
        {
            Button btn = (Button) findViewById(R.id.btOneTime);
            btn.setText("UPDATE TIMER");

            Button deleteBtn = (Button) findViewById(R.id.deleteTimer);
            deleteBtn.setText("TURN OFF");

            String time = bundle.getString("time");
            String date = bundle.getString("date");
            String target = bundle.getString("target");

            int index1 = time.indexOf(":");
            int hour = Integer.parseInt(time.substring(0, index1));
            int index2 = time.indexOf(":", index1 + 1);
            int minute = Integer.parseInt(time.substring(index1+1,index2));
            int sec = Integer.parseInt(time.substring(index2+1,time.length()));


            timePicker = (TimePicker) findViewById(R.id.timePicker);
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);

            Button targetAction = (Button) findViewById(R.id.target);
            targetAction.setText(target);

            datePicker = (DatePicker)findViewById(R.id.datePicker);

            index1 = date.indexOf("-");
            int year = Integer.parseInt(date.substring(0,index1));
            index2 = date.indexOf("-", index1+1);
            int month = Integer.parseInt(date.substring(index1+1,index2));
            int day = Integer.parseInt(date.substring(index2+1,date.length()));


            datePicker.updateDate(year, month, day);
        }

        else
        {
            Button btn = (Button) findViewById(R.id.btOneTime);
            btn.setText("SET TIMER");

            Button deleteBtn = (Button) findViewById(R.id.deleteTimer);
            deleteBtn.setText("CANCEL");


            Calendar cal=Calendar.getInstance();

            int year=cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DAY_OF_MONTH);
            int hour=cal.get(Calendar.HOUR_OF_DAY);
            int min=cal.get(Calendar.MINUTE);

            datePicker = (DatePicker)findViewById(R.id.datePicker);
            datePicker.updateDate(year, month, day);

            timePicker = (TimePicker) findViewById(R.id.timePicker);
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(min);

            Button targetAction = (Button) findViewById(R.id.target);
            targetAction.setText("OFF");

        }


    }

    public void onTimeChanged(int hourOfDay, int minute) {
        //Display the new time to app interface
        if (hourOfDay > 11) {
            hourOfDay = hourOfDay - 12;

        }
    }
    public void onetimeTimer(View view){

        powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        String time =  Integer.toString(timePicker.getCurrentHour())+":"+Integer.toString(timePicker.getCurrentMinute())+":00";
        String date =  Integer.toString(datePicker.getYear())+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth();
        Button targetBtn = (Button) findViewById(R.id.target);
        String target = targetBtn.getText().toString();

        Button btn = (Button) findViewById(R.id.btOneTime);
        Intent intent = getIntent();
        ArrayList<String> list = new ArrayList<String>();

        if(btn.getText().toString().equals("SET TIMER"))
        {

            db.addTimer(deviceId, target, time, date);
            intent.putExtra("MESSAGE", "Timer has been SET");
            list.add("add");

        }

        else{

            list.add("update");
            db.updateTimer(deviceId, target, time, date);
            intent.putExtra("MESSAGE", "Timer has been UPDATED");
        }

        list.add(Integer.toString(deviceId));
        list.add(time);
        list.add(date);
        list.add(target);
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

        startService(view);

        this.setResult(RESULT_OK, intent);
        finish();
    }

    public void onTargetClick(View view)
    {
        Button btn = (Button) findViewById(R.id.target);
        if(btn.getText().toString().equals("ON"))
        {
            btn.setText("OFF");
        }
        else
        {
            btn.setText("ON");
        }


    }
    public void deleteTimerClicked(View view)
    {
        Button btn = (Button) view;
        if(btn.getText().toString().equals("TURN OFF")) {
            powerOptimizerOfflineDB db = new powerOptimizerOfflineDB(this);
            db.deleteTimer(deviceId);
            Intent intent = getIntent();
            intent.putExtra("MESSAGE", "Timer has been turned OFF");
            this.setResult(RESULT_OK, intent);

            ArrayList<String> list = new ArrayList<String>();
            list.add("delete");
            list.add(Integer.toString(deviceId));
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);


            finish();
        }

        else
        {
            finish();
        }


    }


    private class AsyncTaskRunner extends AsyncTask<ArrayList, String, String> {

        protected String doInBackground(ArrayList... params) {

            String type = params[0].get(0).toString();
            String id = params[0].get(1).toString();
            String link="";
            if(type.equals("update"))
            {
                String time = params[0].get(2).toString();
                String date = params[0].get(3).toString();
                String target = params[0].get(4).toString();
                link = "http://poweroptimizer.comlu.com/updateTimer.php?id=" + id + "&time=" + time+ "&date=" + date+ "&target=" + target;

            }
            else if(type.equals("add"))
            {
                String time = params[0].get(2).toString();
                String date = params[0].get(3).toString();
                String target = params[0].get(4).toString();
                link = "http://poweroptimizer.comlu.com/addTimer.php?id=" + id + "&time=" + time+ "&date=" + date+ "&target=" + target;

            }

            else if(type.equals("delete"))
            {
                link = "http://poweroptimizer.comlu.com/deleteTimer.php?id=" + id;
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


    public void backClicked(View view)
    {
        //Intent i=new Intent(this,deviceInfoScreen.class);;
        //startActivity(i);
        finish();

    }

    public void logoutClicked(View view){

        Intent i = new Intent(this,LoginScreen.class);
        startActivity(i);

    }

    public void startService(View V)
    {
        // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
        // we fetch  the current time in milliseconds and added 1 day time
        // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day

        timePicker = (TimePicker) findViewById(R.id.timePicker);

        datePicker=(DatePicker) findViewById(R.id.datePicker);

        c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        c.set(Calendar.MINUTE,timePicker.getCurrentMinute());
        c.set(Calendar.SECOND, 0);

        Date currDate = new Date(System.currentTimeMillis());
        Date selectedDate=new Date(System.currentTimeMillis());
        selectedDate.setMonth(datePicker.getMonth());
        selectedDate.setDate(datePicker.getDayOfMonth());
        //selectedDate.setYear(datePicker.getYear());
        selectedDate.setHours(timePicker.getCurrentHour());
        selectedDate.setMinutes(timePicker.getCurrentMinute());
        selectedDate.setSeconds(0);



        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date(System.currentTimeMillis());
        cal.setTime(currentDate);

        //Long diff = c.getTimeInMillis() - System.currentTimeMillis()  ;

        //Long time = cal.getTimeInMillis()+diff;

        Long diff = selectedDate.getTime()-currDate.getTime();
        Log.e("curr date=",currDate.toString());
        Log.e("sel date=",selectedDate.toString());

        Long time=cal.getTimeInMillis()+diff;

        // create an Intent and set the class which will execute when Alarm triggers, here we have
        // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
        // alarm triggers and
        //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class

        Intent intentAlarm = new Intent(this, AlarmReciever.class);
        Button targetBtn = (Button) findViewById(R.id.target);
        String target = targetBtn.getText().toString();

        if (target.contains("OFF"))
            intentAlarm.putExtra("action","off" );//to turn off the device
        if(target.contains("ON"))
            intentAlarm.putExtra("action","on" );//to turn off the device

        intentAlarm.putExtra("deviceId", deviceId);

        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        //Toast.makeText(this, "Service Started...!!", Toast.LENGTH_LONG).show();

    }
}
