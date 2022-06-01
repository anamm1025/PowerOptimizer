package com.example.janeeta.poweroptimizer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class selectedRoomScreen extends Activity {

    private List<Device> listt = new ArrayList<Device>();
    Context context=this;
    int selectedRoomId;
    TCPClient mTcpClient;
    String serverMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_room_screen);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        selectedRoomId=((MyApplication) this.getApplication()).getRoomId();


        //here get the specific room info from Room Table
        powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(this);

        Room room=db.getSpecificRoom(selectedRoomId);

        TextView nameText = (TextView) findViewById(R.id.roomName);
        nameText.setText("Room: "+room.getName());

        TextView devText = (TextView) findViewById(R.id.devicesAttached);
        devText.setText("# of Devices Connected: "+ room.getNoOfdevices());

        TextView personText = (TextView) findViewById(R.id.personsNo);
        personText.setText("# of Persons: "+ room.getNoOfPersons());

        //here getting all the devices attached to a specific room

        ListView listView=(ListView)findViewById(R.id.deviceListView);

        listt=db.getAllDevicesOfARoom(selectedRoomId);

        ArrayAdapter<Device> adapter = new DeviceAdapter(this,R.layout.device_list_layout, listt);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                String dname = ((TextView) view.findViewById(R.id.deviceName)).getText().toString();
                int did = Integer.parseInt(((TextView) view.findViewById(R.id.devID)).getText().toString());


                MyApplication g = (MyApplication) getApplication();
                g.setDeviceId(did);
                g.setDeviceName(dname);

                //here goto the next screen
                powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(context);
                boolean check=db.isDeviceAttached(did);//this will return true if device is attached
                if(check==true)
                    startActivity(new Intent(selectedRoomScreen.this, deviceInfoScreen.class));
                else//goto the other screen
                    startActivity(new Intent(selectedRoomScreen.this, removedDeviceScreen.class));

            }

        });


    }

    public void logoutClicked(View view){

        Intent i = new Intent(this,LoginScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);

    }

    public void  turnOnAutomationClicked(View view){//here  write also the code to turn ON the Automation
        //here we have to update the Appliance table in db and change the Automation column of every device to ON
        // custom dialog

        //update online DB
        ArrayList<String> list = new ArrayList<String>();
        list.add("ON");
        list.add(Integer.toString(selectedRoomId));
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);


        powerOptimizerOfflineDB dbHelper = new powerOptimizerOfflineDB(this);
        dbHelper.turnOnAutomationOfRoom(selectedRoomId);
        Toast.makeText(this,"Request Sent.", Toast.LENGTH_SHORT).show();

////send arduino command///

        mTcpClient = roomScreen.mTcpClient;

        try {


            if(mTcpClient==null){
                new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                while (mTcpClient==null){

                }
            }


            //sends the message to the server
            if (mTcpClient != null) {

                mTcpClient.sendMessage("9");

            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error::" + e);
        }









    }

    public void  turnOffAutomationClicked(View view){//here  write also the code to turn OFF the Automation
        //here we have to update the Appliance table in db and change the Automation column for every device to OFF
        // custom dialog

        //update online DB
        ArrayList<String> list = new ArrayList<String>();
        list.add("OFF");
        list.add(Integer.toString(selectedRoomId));
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);



        powerOptimizerOfflineDB dbHelper = new powerOptimizerOfflineDB(this);
        dbHelper.turnOffAutomationOfRoom(selectedRoomId);
        Toast.makeText(this,"Request Sent.", Toast.LENGTH_SHORT).show();

        mTcpClient = roomScreen.mTcpClient;


        try {

            if(mTcpClient==null){
                new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

            }

            while (mTcpClient==null){

            }
            //sends the message to the server
            if (mTcpClient != null) {

                mTcpClient.sendMessage("a");

            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error::" + e);
        }



    }

    public void checkSummaryForAllDevicesClicked(View view)
    {

        ((MyApplication) this.getApplication()).setCheckSummary("room");//it will indicate to show the summary of all devices in the room

        Intent i = new Intent(this,CheckSummary.class);
        startActivity(i);
    }

    public void backClicked(View view)
    {
        Intent i=new Intent(this,roomScreen.class);;
        startActivity(i);

    }


    private class AsyncTaskRunner extends AsyncTask<ArrayList, String, String> {

        protected String doInBackground(ArrayList... params) {

            String type = params[0].get(0).toString();
            String id = params[0].get(1).toString();


            String link="";
            if(type.equals("ON")) {
                link = "http://poweroptimizer.comlu.com/turnOnAutomationOfRoom.php?id=" + id ;
            }
            if(type.equals("OFF")) {
                link = "http://poweroptimizer.comlu.com/turnOffAutomationOfRoom.php?id=" + id ;
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

    public void humanCountClicked(View view)
    {
        mTcpClient = roomScreen.mTcpClient;


        try {

            if(mTcpClient==null){
                new connectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

            }

            while (mTcpClient==null){

            }
            //sends the message to the server
            if (mTcpClient != null) {

                mTcpClient.sendMessage("0");

            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error::" + e);
        }
    }

}
