package com.example.janeeta.poweroptimizer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoginScreen extends AppCompatActivity {

    final Context context = this;

    Button dialogButton;
    private Button button;
    String check,msg,title;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

    }

    public void saveCredentials(){
        MyApplication app=((MyApplication) this.getApplication());
        app.setUsername(username);
        app.setPassword(password);
    }

    public void exitClicked(View view)
    {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);


    }
    public void loginButtonClicked(View view) {

        EditText tv1 = (EditText) findViewById(R.id.username);
         username = tv1.getText().toString();

        EditText tv2 = (EditText) findViewById(R.id.password);
         password = tv2.getText().toString();
        ArrayList<String> list = new ArrayList<String>();
        list.add(username);
        list.add(password);


        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

        //check="done";
       /* if(check=="done")
        {
            Intent i = new Intent(context, roomScreen.class);
            startActivity(i);
            check="";
        }

        else if (check=="error"){
            showDialog(title, msg);
            resetFields();
            check="";
        }*/

    }

    public void resetFields() {
        TextView user = (TextView) findViewById(R.id.username);
        TextView pass = (TextView) findViewById(R.id.password);

        user.setText("");
        pass.setText("");
    }

    public void showDialog(String title, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle(title);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("\n" + msg);


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setText("OKAY");
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

    }


    private class AsyncTaskRunner extends AsyncTask<ArrayList, String, String> {


        private String resp;
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        Dialog mOverlayDialog = new Dialog(context, android.R.style.Theme_Panel); //display an invisible overlay dialog to prevent user interaction and pressing back

        @Override
        protected String doInBackground(ArrayList... params) {

            //publishProgress("Loading data from Online DB..."); // Calls onProgressUpdate()
            try {
                check="";
                String username = params[0].get(0).toString();
                String password = params[0].get(1).toString();

                if(verifyCredentials(username,password)==true)
                {
                    //here load data from online db
                    //loadDataFromOnlineDB();

                    check= "done";
                    Intent i = new Intent(context, roomScreen.class);
                    startActivity(i);
                    publishProgress("1");
                    return "jhg";
                }
             /*   else{
                    publishProgress("Login Failed");
                }
*/
            }
            catch (Exception e)
            {
               // publishProgress("Error In Connection");
            }

            check="error";
            return "tyt";
        }


        @Override
        protected void onPostExecute(String tytle) {
            if(check=="done")
            {
                Intent i = new Intent(context, roomScreen.class);
                startActivity(i);
                check="";
            }

            else if (check=="error"){
                showDialog(title, msg);
                resetFields();
                check="";
            }


            linlaHeaderProgress.setVisibility(View.GONE);
            // execution of result of Long time consuming operation
            mOverlayDialog.hide();
        }


        @Override
        protected void onPreExecute() {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            mOverlayDialog.setCancelable(false);
            mOverlayDialog.show();




            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }


        @Override
        protected void onProgressUpdate(String... text) {
            if(text[0].equals("1"))   //condition for showing toast
            {
                showToast("Logged In Successfully");
                saveCredentials(); //save username password

            }

            //finalResult.setText(text[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }



        protected boolean verifyCredentials(String username, String password) {
            /// Connect to online DB and verify credentials
            String result = "";
            InputStream isr = null;
            try {
                String link = "http://poweroptimizer.comlu.com/verifyLogin.php?user=" + username + "&pass=" + password;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                isr = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "UTF-8"));
                String webPage = "", data = "";

                while ((data = reader.readLine()) != null) {
                    webPage += data + "\n";
                }
                isr.close();

                result = webPage.toString();
                conn.disconnect();
            } catch (Exception e) {
                title="Connection Error";
                msg="Couldn't connect to Internet..!!";
                return false;
            }


            try {
                String s = "";
                JSONArray jArray = new JSONArray(result);
                int resultLength = jArray.length();
                if (resultLength > 0) {
                    //errormsg.setText("Login Successful.. Please wait while data is loaded..!!");
                    powerOptimizerOfflineDB db=new powerOptimizerOfflineDB(context);
                    db.emptyAllTables();
                    loadDataFromOnlineDB();
                }
            } catch (Exception e) {
                // TODO: handle exception
                title="Login UnSuccessful";
                msg="Username or Password Incorrect..!!";
                return false;
            }

            return true;
        }

        public void loadDataFromOnlineDB() {
            String linkForRoomTable = "http://poweroptimizer.comlu.com/getAllRooms.php";
            String result = loadOnlineTable(linkForRoomTable);
            insertInRoom(result);


            String linkForApplianceTable = "http://poweroptimizer.comlu.com/getAllAppliances.php";
            result = loadOnlineTable(linkForApplianceTable);
            insertInAppliance(result);



            String linkForTimerTable = "http://poweroptimizer.comlu.com/getAllTimers.php";
            result = loadOnlineTable(linkForTimerTable);
            insertInTimer(result);


            /// show data on login screen, just to check if data is loaded correctly

            /*StringBuffer buffer = new StringBuffer();
            Cursor cursor = dbHelper.getAllTimers();
            while (cursor.moveToNext()) {

                int index = cursor.getColumnIndex(dbHelper.COLUMN_OF_TIMER_TABLE_TIMERID);
                int cid = cursor.getInt(index);
                int index1 = cursor.getColumnIndex(dbHelper.COLUMN_OF_TIMER_TABLE_DATE);
                String cid1 = cursor.getString(index1);
                int index2 = cursor.getColumnIndex(dbHelper.COLUMN_OF_TIMER_TABLE_TIME);
                String cid2 = cursor.getString(index2);
                int index3 = cursor.getColumnIndex(dbHelper.COLUMN_OF_TIMER_TABLE_TARGETACTION);
                String cid3 = cursor.getString(index3);
                int index4 = cursor.getColumnIndex(dbHelper.COLUMN_OF_TIMER_TABLE_APPLIANCEID);
                int cid4 = cursor.getInt(index4);

                buffer.append(cid + " " + cid1 + " " + cid2 + " " + cid3 + " " + cid4 + "\n");
            }*/


        }

        public String loadOnlineTable(String link) {
            powerOptimizerOfflineDB dbHelper = new powerOptimizerOfflineDB(context);
            String result = "";
            InputStream isr = null;
            try {
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                isr = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "UTF-8"));
                String webPage = "", data = "";

                while ((data = reader.readLine()) != null) {
                    webPage += data + "\n";
                }
                isr.close();
                result = webPage.toString();
                conn.disconnect();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }


            return result;

        }


        public void insertInRoom(String result) {

            powerOptimizerOfflineDB dbHelper = new powerOptimizerOfflineDB(context);

            try {
                String s = "";
                JSONArray jArray = new JSONArray(result);
                int resultLength = jArray.length();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    int id = jsonObject.getInt(dbHelper.COLUMN_OF_ROOM_TABLE_ROOMID);
                    String roomName = jsonObject.getString(dbHelper.COLUMN_OF_ROOM_TABLE_ROOMNAME).toString();
                    int noOfDevices = (jsonObject.getInt(dbHelper.COLUMN_OF_ROOM_TABLE_NOOFDEVICES));
                    int noOfPersons = (jsonObject.getInt(dbHelper.COLUMN_OF_ROOM_TABLE_NOOFPERSONS));
                    dbHelper.insertRowInRoomTable(id, roomName, noOfDevices, noOfPersons);

                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error Parsing Data " + e.toString());

            }

        }

        public void insertInAppliance(String result) {

            powerOptimizerOfflineDB dbHelper = new powerOptimizerOfflineDB(context);

            try {
                String s = "";
                JSONArray jArray = new JSONArray(result);
                int resultLength = jArray.length();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    int id = jsonObject.getInt(dbHelper.COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID);
                    String applianceName = jsonObject.getString(dbHelper.COLUMN_OF_APPLIANCE_TABLE_APPLIANCENAME).toString();
                    int roomId = (jsonObject.getInt(dbHelper.COLUMN_OF_APPLIANCE_TABLE_ROOMID));
                    String status = (jsonObject.getString(dbHelper.COLUMN_OF_APPLIANCE_TABLE_STATUS)).toString();
                    String timerStatus = (jsonObject.getString(dbHelper.COLUMN_OF_APPLIANCE_TABLE_TIMERSTATUS)).toString();
                    String auto = (jsonObject.getString(dbHelper.COLUMN_OF_APPLIANCE_TABLE_AUTOMATION)).toString();
                    String attach = (jsonObject.getString(dbHelper.COLUMN_OF_APPLIANCE_TABLE_ATTACHED)).toString();
                    dbHelper.insertRowInApplianceTable(id, applianceName, status, timerStatus, roomId,auto,attach);

                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error Parsing Data " + e.toString());

            }

        }


        public void insertInTimer(String result) {

            powerOptimizerOfflineDB dbHelper = new powerOptimizerOfflineDB(context);

            try {
                String s = "";
                JSONArray jArray = new JSONArray(result);
                int resultLength = jArray.length();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    int id = jsonObject.getInt(dbHelper.COLUMN_OF_TIMER_TABLE_TIMERID);
                    String action = jsonObject.getString(dbHelper.COLUMN_OF_TIMER_TABLE_TARGETACTION).toString();
                    int applianceId = (jsonObject.getInt(dbHelper.COLUMN_OF_TIMER_TABLE_APPLIANCEID));
                    String date = (jsonObject.getString(dbHelper.COLUMN_OF_TIMER_TABLE_DATE)).toString();
                    String time = (jsonObject.getString(dbHelper.COLUMN_OF_TIMER_TABLE_TIME)).toString();
                    dbHelper.insertRowInTimerTable(id, applianceId, date, time, action);

                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error Parsing Data " + e.toString());

            }

        }
    }

}