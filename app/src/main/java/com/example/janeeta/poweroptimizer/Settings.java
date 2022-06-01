package com.example.janeeta.poweroptimizer;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class Settings extends AppCompatActivity {


    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "448698825886";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void changePassClicked(View view)
    {
        Intent i = new Intent(this,passwordScreen.class);
        startActivity(i);

    }

    public void backClicked(View view)
    {
        /*EditText e=(EditText) findViewById(R.id.gcmText);
        String s = e.getText().toString();
        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto: anamm1025@gmail.com, anamm1025@yahoo.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
        intent.putExtra(Intent.EXTRA_TEXT, s);
        startActivity(intent);*/

        Intent i=new Intent(this,roomScreen.class);;
        startActivity(i);

    }

    public void getRegId(View view){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM", msg);

                }
                catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                EditText e=(EditText) findViewById(R.id.gcmText);
                e.setText(msg);
            }
        }.execute(null, null, null);
    }

}
