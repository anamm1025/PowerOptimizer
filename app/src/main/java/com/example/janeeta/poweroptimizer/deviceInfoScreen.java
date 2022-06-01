package com.example.janeeta.poweroptimizer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class deviceInfoScreen extends AppCompatActivity {

    final Context context = this;
    int selectedDeviceId;
    Device device;
    TCPClient mTcpClient;
    String serverMsg;
    static TextView devStatusText;
    static TextView devTimerText;
    static Button devControlButton;
    static Button devTimerButton;

    static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info_screen);

        selectedDeviceId = ((MyApplication) this.getApplication()).getDeviceId();

    }
    @Override
    public void onResume()
    {
        super.onResume();
        powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);

        //reading specific device from db
        device =db.getSpecificDevice(selectedDeviceId);

        //here show device Info on screen
        TextView devNameText = (TextView) findViewById(R.id.deviceName);
        devStatusText = (TextView) findViewById(R.id.statusText);
        devTimerText = (TextView) findViewById(R.id.timerStatusText);
        TextView devAutoText = (TextView) findViewById(R.id.automationText);
        TextView devAttachedText = (TextView) findViewById(R.id.attachedText);

        devControlButton = (Button) findViewById(R.id.controlButton);
        devTimerButton = (Button) findViewById(R.id.setTimerButton);
        Button devAutoButton = (Button) findViewById(R.id.autoButton);
        Button devAttachButton = (Button) findViewById(R.id.attachedButton);

        devNameText.setText("DEVICE NAME: "+device.getDeviceName());//setting name of device
        devStatusText.setText("STATUS: "+device.getStatus());//setting status
        if(device.getStatus().contains("OFF") || (device.getStatus().contains("off")))
        {
            devControlButton.setText("TURN ON");
        }
        if(device.getStatus().contains("ON") || (device.getStatus().contains("on")))
        {
            devControlButton.setText("TURN OFF");
        }

        if(device.getTimerStatus().contains("OFF") || (device.getTimerStatus().contains("off")))
        {
            devTimerText.setText("TIMER: "+device.getTimerStatus());
            devTimerButton.setText("SET TIMER");
        }
        else
        {
            Cursor cursor = db.getSpecificTimerInfo(selectedDeviceId);
            String time = cursor.getString(cursor.getColumnIndex(db.COLUMN_OF_TIMER_TABLE_TIME));
            String date = cursor.getString(cursor.getColumnIndex(db.COLUMN_OF_TIMER_TABLE_DATE));
            String target = cursor.getString(cursor.getColumnIndex(db.COLUMN_OF_TIMER_TABLE_TARGETACTION));
            devTimerText.setText("TIMER: "+device.getTimerStatus() + "\n"+time+"\n"+date+"\nTARGET: "+target);//setting timer status

        }
        if(device.getTimerStatus().contains("ON") || (device.getTimerStatus().contains("on")))
        {
            devTimerButton.setText("CHANGE TIMER");
        }

        devAutoText.setText("Automation: "+device.getIsAutomation());//setting timer status
        if(device.getIsAutomation().contains("OFF") || (device.getIsAutomation().contains("off")))
        {
            devAutoButton.setText("TURN AUTOMATION ON");
        }
        if(device.getIsAutomation().contains("ON") || (device.getIsAutomation().contains("on")))
        {
            devAutoButton.setText("TURN AUTOMATION OFF");
        }

        devAttachedText.setText("Attached: "+device.getIsAttached());//setting timer status
        if(device.getIsAttached().contains("NO") || (device.getIsAttached().contains("no")))
        {
            devAttachButton.setText("ADD DEVICE");
        }
        if(device.getIsAttached().contains("YES") || (device.getIsAttached().contains("yes")))
        {
            devAttachButton.setText("REMOVE DEVICE");
        }
    }




    public void backClicked(View view)
    {
        //Intent i=new Intent(this,selectedRoomScreen.class);

        finish();

    }

    public void logoutClicked(View view){

        Intent i = new Intent(this,LoginScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        try {

            mTcpClient.stopClient();

        }
        catch(Exception e){
        }
        startActivity(i);

    }
    //////temp/////
    public void controlButtonClicked(View view) {

        // connect to the server

        // new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");

        int textID=R.id.statusText;
        int buttonID=R.id.controlButton;
        String statusText="";
        String buttonText="";

        final Button devControlButton = (Button) findViewById(R.id.controlButton);
        String command = devControlButton.getText().toString();
        powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);
        String msg;
        mTcpClient = roomScreen.mTcpClient;
        if (command.contains("ON")) {

            try {
                //sends the message to the server
                if(mTcpClient==null){
                    new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

                    while (mTcpClient==null){

                    }
                }

                if (mTcpClient != null) {

                    if(selectedDeviceId==1)

                        mTcpClient.sendMessage("1");
                    if(selectedDeviceId==2)

                        mTcpClient.sendMessage("3");
                }
                //update online DB
                ArrayList<String> list = new ArrayList<String>();
                list.add("control");
                list.add(Integer.toString(selectedDeviceId));
                list.add("ON");
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);


                //update offline DB
                db.changeDeviceStatus(selectedDeviceId, "ON");
                msg = "Device has been turned ON";
                statusText = "STATUS: ON";
                buttonText = "TURN OFF";
                Toast.makeText(this,"Request Sent.", Toast.LENGTH_SHORT).show();
                TextView Text = (TextView) findViewById(textID);
                Button Button = (Button) findViewById(buttonID);
                Button.setText(buttonText);
                Text.setText(statusText);

                //showDialog("Status", msg, textID, buttonID, statusText, buttonText);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("Error::" + e);
            }
        }


        if (command.contains("OFF")) {

            try {
                mTcpClient = roomScreen.mTcpClient;
                //ArduinoActivity b=new ArduinoActivity();
                //sends the message to the server
                if(mTcpClient==null){
                    new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

                    while (mTcpClient==null){

                    }
                }


                if (mTcpClient != null) {
                    if(selectedDeviceId==1)

                        mTcpClient.sendMessage("2");
                    if(selectedDeviceId==2)

                        mTcpClient.sendMessage("4");

                    //update online DB
                    ArrayList<String> list = new ArrayList<String>();
                    list.add("control");
                    list.add(Integer.toString(selectedDeviceId));
                    list.add("OFF");
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

                    //update offline DB
                    db.changeDeviceStatus(selectedDeviceId, "OFF");
                    msg = "Device has been turned OFF";
                    statusText = "STATUS: OFF";
                    buttonText="TURN ON";
                    Toast.makeText(this,"Request Sent.", Toast.LENGTH_SHORT).show();
                    TextView Text = (TextView) findViewById(textID);
                    Button Button = (Button) findViewById(buttonID);
                    Button.setText(buttonText);
                    Text.setText(statusText);

                    //  showDialog("Status", msg, textID, buttonID, statusText, buttonText);
                }

            } catch (Exception e) {
                //TODO Auto-generated catch block
                System.out.println("Error::" + e);
            }




        }


    }



    /////////////

    public void timerButtonClicked(View view)//here goto the timer Screen
    {
        Intent intent=new Intent(this,timernew.class);;


        Button btn = (Button)findViewById(R.id.setTimerButton);
        if(btn.getText().toString().equals("CHANGE TIMER"))
        {
            powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);
            Cursor cursor = db.getSpecificTimerInfo(selectedDeviceId);
            String time = cursor.getString(cursor.getColumnIndex(db.COLUMN_OF_TIMER_TABLE_TIME));
            String date = cursor.getString(cursor.getColumnIndex(db.COLUMN_OF_TIMER_TABLE_DATE));
            String target = cursor.getString(cursor.getColumnIndex(db.COLUMN_OF_TIMER_TABLE_TARGETACTION));

            intent.putExtra("type", "change");
            intent.putExtra("time", time);
            intent.putExtra("date", date);
            intent.putExtra("target", target);
            intent.putExtra("deviceID", selectedDeviceId);

        }

        else{
            intent.putExtra("type", "set");
            intent.putExtra("deviceID", selectedDeviceId);
        }


        startActivityForResult(intent, 2);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2) {
            if (resultCode == RESULT_OK) {
                String message = data.getStringExtra("MESSAGE");
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            }
        }
    }



    public void autoButtonClicked(View view)
    {

        int textID=R.id.automationText;
        int buttonID=R.id.autoButton;
        String statusText="";
        String buttonText="";
        String msg;

        Button devAutoButton = (Button) findViewById(R.id.autoButton);
        String command = devAutoButton.getText().toString();
        powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);

        mTcpClient = roomScreen.mTcpClient;



        if(command.contains("AUTOMATION ON"))
        {

            try {
                //sends the message to the server
                if (mTcpClient == null) {
                    new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

                    while (mTcpClient == null) {

                    }
                }

                if (mTcpClient != null) {

                    if (selectedDeviceId == 1)

                        mTcpClient.sendMessage("5");
                    if (selectedDeviceId == 2)

                        mTcpClient.sendMessage("7");
                }
                //update online DB
                ArrayList<String> list = new ArrayList<String>();
                list.add("auto");
                list.add(Integer.toString(selectedDeviceId));
                list.add("ON");
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

                //update offline DB
                db.changeAutomationStatus(selectedDeviceId, "ON");
                msg = "Automation has been turned ON";
                statusText = "Automation: ON";
                buttonText = "TURN AUTOMATION OFF";
                Toast.makeText(this,"Request Sent.", Toast.LENGTH_SHORT).show();

                TextView Text = (TextView) findViewById(textID);
                Button Button = (Button) findViewById(buttonID);
                Button.setText(buttonText);
                Text.setText(statusText);

                // showDialog("Automation Status", msg, textID, buttonID, statusText, buttonText);
            }
            catch(Exception e)
            {}

        }

        if(command.contains("AUTOMATION OFF"))
        {

            try {
                //sends the message to the server
                if (mTcpClient == null) {
                    new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

                    while (mTcpClient == null) {

                    }
                }

                if (mTcpClient != null) {

                    if (selectedDeviceId == 1)

                        mTcpClient.sendMessage("6");
                    if (selectedDeviceId == 2)

                        mTcpClient.sendMessage("8");
                }
                //update online DB
                ArrayList<String> list = new ArrayList<String>();
                list.add("auto");
                list.add(Integer.toString(selectedDeviceId));
                list.add("OFF");
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

                //update offline DB
                db.changeAutomationStatus(selectedDeviceId, "OFF");
                msg = "Automation has been turned OFF";
                statusText = "Automation: OFF";
                buttonText = "TURN AUTOMATION ON";
                Toast.makeText(this,"Request Sent.", Toast.LENGTH_SHORT).show();

                TextView Text = (TextView) findViewById(textID);
                Button Button = (Button) findViewById(buttonID);
                Button.setText(buttonText);
                Text.setText(statusText);


                //    showDialog("Automation Status", msg, textID, buttonID, statusText, buttonText);

            }
            catch(Exception e)
            {}
        }
    }

    public void attachedButtonClicked(View view)
    {
        int textID=R.id.attachedText;
        int buttonID=R.id.attachedButton;
        String statusText="";
        String buttonText="";
        Button devattatchButton = (Button) findViewById(R.id.attachedButton);
        String command = devattatchButton.getText().toString();
        powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);
        String msg;
        if(command.contains("REMOVE"))
        {
            //update online DB
            ArrayList<String> list = new ArrayList<String>();
            list.add("attach");
            list.add(Integer.toString(selectedDeviceId));
            list.add("NO");
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

            //update offline DB
            db.changeAttachmentStatus(selectedDeviceId, "NO");
            msg = "Device has been Removed";
            statusText="Attached: NO";
            buttonText = "ATTACH DEVICE";

            showDialog("Device Status", msg, textID, buttonID, statusText, buttonText);
        }

        else
        {
            //update online DB
            ArrayList<String> list = new ArrayList<String>();
            list.add("attach");
            list.add(Integer.toString(selectedDeviceId));
            list.add("YES");
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

            //update offline DB
            db.changeAttachmentStatus(selectedDeviceId, "YES");
            msg = "Device has been Attatched";
            statusText="Attached: YES";
            buttonText = "REMOVE DEVICE";
            showDialog("Device Status", msg, textID, buttonID, statusText, buttonText);

        }

    }


    public void showDialog(String title, String msg, int textID, int buttonID,String statusText, String buttonText) {
        final Context context=this;
        final int TEXTID=textID;
        final int BUTTONID=buttonID;
        final String STATUSTEXT=statusText;
        final String BUTTONTEXT=buttonText;

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle(title);

        // set the custom dialog components - text, image and button
        final TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("\n" + msg);


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setText("OKAY");
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView Text = (TextView) findViewById(TEXTID);
                Button Button = (Button) findViewById(BUTTONID);
                dialog.dismiss();
                if(BUTTONTEXT.contains("ATTACH"))
                {
                    Intent intent = new Intent(context, removedDeviceScreen.class);
                    startActivity(intent);
                }
                Button.setText(BUTTONTEXT);
                Text.setText(STATUSTEXT);

            }
        });

        dialog.show();

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
            else if(type.equals("auto"))
            {
                link = "http://poweroptimizer.comlu.com/updateAutomationOfDevice.php?id=" + id + "&status=" + command;

            }

            else if(type.equals("attach"))
            {
                link = "http://poweroptimizer.comlu.com/updateAttachmentOfDevice.php?id=" + id + "&status=" + command;

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
                    serverMsg=message;
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

}