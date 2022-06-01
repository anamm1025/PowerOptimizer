package com.example.janeeta.poweroptimizer;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.janeeta.poweroptimizer.Constants.FIRST_COLUMN;
import static com.example.janeeta.poweroptimizer.Constants.SECOND_COLUMN;

public class roomScreen extends AppCompatActivity {
    private ArrayList<HashMap<String, String>> list;
    static TCPClient mTcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_screen);

        new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

        while (mTcpClient == null) {

        }


        ListView listView = (ListView) findViewById(R.id.roomList);

        list = new ArrayList<HashMap<String, String>>();


        //get data from internal db
        powerOptimizerOfflineDB dbHelper = new powerOptimizerOfflineDB(this);
        Cursor cursor = dbHelper.getAllRooms();
        while (cursor.moveToNext()) {
            HashMap<String, String> temp = new HashMap<String, String>();

            int index = cursor.getColumnIndex(dbHelper.COLUMN_OF_ROOM_TABLE_ROOMID);
            String id = cursor.getString(index);
            int index1 = cursor.getColumnIndex(dbHelper.COLUMN_OF_ROOM_TABLE_ROOMNAME);
            String name = cursor.getString(index1);

            temp.put(FIRST_COLUMN, name);
            temp.put(SECOND_COLUMN, id);
            list.add(temp);
        }


        ListViewAdapters adapter = new ListViewAdapters(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                String dname = ((TextView) view.findViewById(R.id.roomName)).getText().toString();
                int did = Integer.parseInt(((TextView) view.findViewById(R.id.roomId)).getText().toString());

                MyApplication g = (MyApplication) getApplication();
                g.setRoomName(dname);
                g.setRoomId(did);

                //here goto the next screen
                startActivity(new Intent(roomScreen.this, selectedRoomScreen.class));

            }

        });


    }

    public void logoutClicked(View view) {

        try {

            mTcpClient.stopClient();

        }
        catch(Exception e){


        }
        Intent i = new Intent(this, LoginScreen.class);
        startActivity(i);

    }


    public void settingsClicked(View view) {
        Intent i = new Intent(this, Settings.class);
        startActivity(i);


    }

    public void turnOnAutomationClicked(View view) {//here  write also the code to turn ON the Automation
        //here we have to update the Appliance table in db and change the Automation column of every device to ON
        // custom dialog

        //update online DB
        ArrayList<String> list = new ArrayList<String>();

        list.add("ON");
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

        //update offline DB


        powerOptimizerOfflineDB dbHelper = new powerOptimizerOfflineDB(this);
        dbHelper.turnOnAutomationForAllAppliacnes();
        Toast.makeText(this,"Request Sent.", Toast.LENGTH_SHORT).show();

        try {


            if(mTcpClient==null){
                new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                while (mTcpClient==null){

                }
            }


            //sends the message to the server
            if (mTcpClient != null) {

                mTcpClient.sendMessage("b");

            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error::" + e);
        }




    }

    public void turnOffAutomationClicked(View view) {//here  write also the code to turn OFF the Automation
        //here we have to update the Appliance table in db and change the Automation column for every device to OFF
        // custom dialog

        //update online DB
        ArrayList<String> list = new ArrayList<String>();
        list.add("OFF");
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

        //update offline DB

        powerOptimizerOfflineDB dbHelper = new powerOptimizerOfflineDB(this);
        dbHelper.turnOffAutomationForAllAppliacnes();
        Toast.makeText(this,"Request Sent.", Toast.LENGTH_SHORT).show();

        try {


            if(mTcpClient==null){
                new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                while (mTcpClient==null){

                }
            }


            //sends the message to the server
            if (mTcpClient != null) {

                mTcpClient.sendMessage("c");

            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error::" + e);
        }





    }

    public void checkSummaryForAllDevicesClicked(View view) {

        ((MyApplication) this.getApplication()).setCheckSummary("building");//it will indicate to show the summary of all devices in the building

        Intent i = new Intent(this, CheckSummary.class);
        startActivity(i);
    }

    private class AsyncTaskRunner extends AsyncTask<ArrayList, String, String> {

        protected String doInBackground(ArrayList... params) {

            String type = params[0].get(0).toString();


            String link = "";
            if (type.equals("ON")) {
                link = "http://poweroptimizer.comlu.com/turnOnAutomationForAllAppliacnes.php";
            } else if (type.equals("OFF")) {
                link = "http://poweroptimizer.comlu.com/turnOffAutomationForAllAppliacnes.php";
            }
            try {

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
            } catch (Exception e) {

                //Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
            }

            return "Updated";
        }
    }


    public class connectTask extends AsyncTask<String, String, TCPClient> {

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
}
