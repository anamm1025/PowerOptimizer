package com.example.janeeta.poweroptimizer;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class passwordScreen extends AppCompatActivity {

    String username;
    String oldSavedPass;
    boolean changed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_screen);
        username = ((MyApplication) this.getApplication()).getUsername();
        oldSavedPass = ((MyApplication) this.getApplication()).getPassword();


    }

    public void logoutClicked(View view){

        Intent i = new Intent(this,LoginScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    public void cancelClicked(View view){

       finish();
    }

    public void confirmClicked(View view){
        TextView oldPass= (TextView)findViewById(R.id.editText3);
        TextView newPass= (TextView)findViewById(R.id.editText4);
        TextView ReNewPass= (TextView)findViewById(R.id.editText5);

        String oldP = oldPass.getText().toString();
        String newP = newPass.getText().toString();
        String ReNewP = ReNewPass.getText().toString();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom);

        if(newP.isEmpty() || ReNewP.isEmpty() || oldP.isEmpty()){
            showDialog("", "Fill all the fields!");

        }


        else if(!(oldP.equals(oldSavedPass))){     //new passwords doesnot match
            showDialog("Old Password Incorrect", "Old Password Incorrect, Please enter it again!!");
        }

        else if(!(newP.equals(ReNewP))){     //new passwords doesnot match
            showDialog("NEW PASSWORDS MISMATCH", "NEW PASSWORDS MISMATCH, Retry Please");
        }

        else
        {
            ArrayList<String> list = new ArrayList<String>();
            list.add(username);
            list.add(newP);

            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);

            showDialog("SUCCESS", "Password Changed Successfully");
            changed=true;
            ((MyApplication) this.getApplication()).setPassword(newP);
        }





    }
    public void showDialog(String title, String msg) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle(title);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("\n" + msg);


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);


        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(changed) {
                    finish();
                }
            }
        });

        dialog.show();

    }


    private class AsyncTaskRunner extends AsyncTask<ArrayList, String, String> {

        protected String doInBackground(ArrayList... params) {

            String username = params[0].get(0).toString();
            String password = params[0].get(1).toString();

            String link="";
            link="http://poweroptimizer.comlu.com/updatePassword.php?username="+username+"&password="+password;
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
