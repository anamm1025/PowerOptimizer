package com.example.janeeta.poweroptimizer;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GcmMessageHandler extends IntentService {

     String title;
    String mes;
     private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }
static int notiId=0;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
    	title ="Message received from server.";

        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
 mes="";

       title = extras.getString("title");
        SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
        String current_time_str = time_formatter.format(System.currentTimeMillis());
         if(title.equalsIgnoreCase("Light on"))
        {

            String id = extras.getString("id");
            mes="ROOM 1: Light " + id + " has been turned ON!"+System.getProperty("line.separator")+current_time_str;

            showNotification("Power Optimization Message", mes,1);

            Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

            GcmBroadcastReceiver.completeWakefulIntent(intent);

            return;
        }
        else if(title.equalsIgnoreCase("Light off"))
        {
            String id = extras.getString("id");
            mes="Room 1: Light "+id+" turned OFF!"+System.getProperty("line.separator")+current_time_str;
            showNotification("Power Optimization Message", mes,2);

            Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

            GcmBroadcastReceiver.completeWakefulIntent(intent);

            return;
        }

        else if(title.equalsIgnoreCase("Automation on"))
        {
            String id = extras.getString("id");
            if(id.equalsIgnoreCase("room"))
            {
                mes = "ROOM 1, Automation has been turnedON!"+System.getProperty("line.separator")+current_time_str;
            }
            else if(id.equalsIgnoreCase("building"))
            {
                mes = "Building Automation has been turned ON!"+System.getProperty("line.separator")+current_time_str;
            }
            else
            {
                mes = "Device "+id+" , Automation has bee turned ON!"+System.getProperty("line.separator")+current_time_str;
            }


            showNotification("Power Optimization Message", mes,3);

            Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

            GcmBroadcastReceiver.completeWakefulIntent(intent);

            return;
        }

        else if(title.equalsIgnoreCase("Automation off"))
        {
            String id = extras.getString("id");
            if(id.equalsIgnoreCase("room"))
            {
                mes = "ROOM 1, Automation has been turned OFF!"+System.getProperty("line.separator")+current_time_str;
            }
            else if(id.equalsIgnoreCase("building"))
            {
                mes = "Building Automation has been turned OFF!"+System.getProperty("line.separator")+current_time_str;
            }
            else
            {
                mes = "Device "+id+" , Automation has been turned OFF"+System.getProperty("line.separator")+current_time_str;
            }

            showNotification("Power Optimization Message", mes, 4);

            Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

            GcmBroadcastReceiver.completeWakefulIntent(intent);

            return;
        }

         else if(title.equalsIgnoreCase("human count"))
         {
             String count =""+ extras.getInt("count");
             String action = extras.getString("action");
             String deviceId = extras.getString("id");
             mes="Human Count:"+count+" Device with id "+deviceId+"has been turned "+action+System.getProperty("line.separator")+current_time_str;

             showNotification("Power Optimization Message", mes, 5);

             if(deviceId.equalsIgnoreCase("*")) {
                 //update online DB

                 String link="";
                 link = "http://poweroptimizer.comlu.com/updateStatusOfDevice.php?id=1&status="+action;

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


                 link="";
                 link = "http://poweroptimizer.comlu.com/updateStatusOfDevice.php?id=2&status="+action;

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


                 //update offline DB
                 powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);
                 db.changeDeviceStatus(1, action);
                 db.changeDeviceStatus(2, action);

             }

             else {
                 //update online DB

                 String link="";
                 link = "http://poweroptimizer.comlu.com/updateStatusOfDevice.php?id="+deviceId+"&status="+action;

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


                 //update offline DB
                 powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);
                 db.changeDeviceStatus(Integer.parseInt(deviceId), action);

             }



             //update count
             String link="";
             link = "http://poweroptimizer.comlu.com/updateCount.php?count="+count;

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


             powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);
             db.changeCount(Integer.parseInt(count), Integer.parseInt(deviceId));


             Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

             GcmBroadcastReceiver.completeWakefulIntent(intent);

             return;
         }

         else if(title.equalsIgnoreCase("manual")) {
             String deviceId = extras.getString("id");
             String action = extras.getString("action");

             mes = "Device with "+deviceId+" has been Manually turned "+action+System.getProperty("line.separator")+current_time_str;


             showNotification("Power Optimization Message", mes, 6);

             String link="";
             link = "http://poweroptimizer.comlu.com/updateStatusOfDevice.php?id="+deviceId+"&status="+action;

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


             //update offline DB
             powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);
             db.changeDeviceStatus(Integer.parseInt(deviceId), action);

             Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

             GcmBroadcastReceiver.completeWakefulIntent(intent);

             return;
         }


         }


    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
            }
         });

    }

    public void showNotification(String subject, String msg, int id)
    {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent=null;

        //notificationManager.notify(id, noti);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        Notification notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.bgimage36).setTicker("Power Optimization Notification").setWhen(0)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentTitle(subject)
                        .setContentText(msg).build();

        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);



    }


    private class AsyncTaskRunner extends AsyncTask<ArrayList, String, String> {

        protected String doInBackground(ArrayList... params) {

            String type = params[0].get(0).toString();
            String id = params[0].get(1).toString();
            String command = params[0].get(2).toString();
            String link="";
            if(type.equals("control"))
            {
                link = "http://poweroptimizer.comlu.com/updateStatusOfDevice.php?id=" + id + "&status=" + command;

            }
            else if(type.equals("count"))
            {
                link = "http://poweroptimizer.comlu.com/updateHumanCount.php?roomId=1";

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




