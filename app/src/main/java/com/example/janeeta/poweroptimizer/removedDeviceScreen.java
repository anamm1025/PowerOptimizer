package com.example.janeeta.poweroptimizer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class removedDeviceScreen extends AppCompatActivity {

    int selectedDeviceId;
    final Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.removed_device_screen);
        selectedDeviceId=((MyApplication) this.getApplication()).getDeviceId();

    }

     public void attachedButtonClicked(View view)
        {

            //update online DB
            ArrayList<String> list = new ArrayList<String>();
            list.add("attach");
            list.add(Integer.toString(selectedDeviceId));
            list.add("YES");
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

            //update offline DB
            powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);

                db.changeAttachmentStatus(selectedDeviceId, "YES");

                showDialog("Device Status");

        }


    public void showDialog(String title) {

               final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom2);
        dialog.setTitle(title);

        // set the custom dialog components - text, image and button
        final TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("\n" + "Enter Device's Current Status, (ON/OFF)");

        Button cancelButton = (Button)dialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }});

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setText("OKAY");
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText status = (EditText)dialog.findViewById(R.id.status);
                String statuss = status.getText().toString();

               // status.lowerCase();
                if(statuss.equals("ON")||statuss.equals("OFF")) {

                    //update online DB
                    ArrayList<String> list = new ArrayList<String>();
                    list.add("control");
                    list.add(Integer.toString(selectedDeviceId));
                    list.add(statuss);
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

                    //update offline DB

                    powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(context);
                    db.changeDeviceStatus(selectedDeviceId, statuss);
                    Intent intent = new Intent(context, deviceInfoScreen.class);
                    startActivity(intent);
                    dialog.dismiss();
                }

                else{
                    text.setText("\n" + "Invalid Status\n Please Enter 'ON' or 'OFF'");
                }
            }
        });

        dialog.show();

    }


    public void backClicked(View view)
    {
        Intent i=new Intent(this,selectedRoomScreen.class);;
        startActivity(i);

    }

    public void logoutClicked(View view){

        Intent i = new Intent(this,LoginScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    private class AsyncTaskRunner extends AsyncTask<ArrayList, String, String> {

        protected String doInBackground(ArrayList... params) {

            String type = params[0].get(0).toString();
            String id = params[0].get(1).toString();
            String command = params[0].get(2).toString();
            String link="";



            if(type.equals("attach"))
            {
                link = "http://poweroptimizer.comlu.com/updateAttachmentOfDevice.php?id=" + id + "&status=" + command;

            }

            else if(type.equals("control"))
            {
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
