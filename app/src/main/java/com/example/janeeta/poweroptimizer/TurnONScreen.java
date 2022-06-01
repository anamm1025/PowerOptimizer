package com.example.janeeta.poweroptimizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class TurnONScreen extends AppCompatActivity {
    final Context context = this;
    private Button button;
    private boolean on = false;
    String serverMsg;
    private TCPClient mTcpClient;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_on);
        new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        while (mTcpClient==null){

        }

    }


    public void logoutClicked(View view){

        Intent i = new Intent(this,LoginScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        try {

            mTcpClient.stopClient();

        }
        catch(Exception e){


        }

    }

    public void cancelClicked(View view){

        try {

            mTcpClient.stopClient();

        }
        catch(Exception e){


        }
        finish();


    }

    public void turnonClicked(View view) {

        // connect to the server

        // new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");

        TextView on_button = (TextView) findViewById(R.id.on1);
        String buttonText = on_button.getText().toString();

        if (buttonText.contains("ON")) {

            try {
                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage("1");
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("Error::" + e);
            }

            /*String message;
            if (mTcpClient != null) {
                message =mTcpClient.serverMessage;
                System.out.println(message);
            }*/
            // custom dialog

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle("TURN ON DEVICE");

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("\nDevice has been turned on..!!");


            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    TextView on_button = (TextView) findViewById(R.id.on1);
                    on_button.setText("      TURN OFF");
                }
            });

            dialog.show();

        }

        if (buttonText.contains("OFF")) {

            try {
                //ArduinoActivity b=new ArduinoActivity();
                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage("2");
                }

            } catch (Exception e) {
                //TODO Auto-generated catch block
                System.out.println("Error::" + e);
            }


            // custom dialog

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle("TURN OFF DEVICE");

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("\nDevice has been turned off..!!");


            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    TextView on_button = (TextView) findViewById(R.id.on1);
                    on_button.setText("      TURN ON");
                }
            });

            dialog.show();

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

