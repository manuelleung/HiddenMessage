package com.example.manuel.testsql.users;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.manuel.testsql.R;
import com.example.manuel.testsql.database.DatabaseHandler;
import com.example.manuel.testsql.database.UserFunctions;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Manuel on 10/31/2015.
 */
public class ChangePassword extends AppCompatActivity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    private EditText newpassword;
    private TextView alert;
    private Button button_change;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        newpassword = (EditText)findViewById(R.id.new_password);
        alert = (TextView) findViewById(R.id.text_alert);
        button_change = (Button) findViewById(R.id.button_changepassword);

        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetAsync(v);
            }
        });
    }

    public void NetAsync(View view) {
        new NetCheck().execute();
    }

    private class NetCheck extends AsyncTask<String, String, Boolean> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setTitle("Checking Network");
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if(netInfo!=null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.connect();
                    if(conn.getResponseCode()==200) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result==true) {
                pDialog.dismiss();
                new ProcessChange().execute();
            } else {
                pDialog.dismiss();
                alert.setText("Error in network connection");
            }
        }

    }

    private class ProcessChange extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        String newpass, email;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            HashMap user = new HashMap();
            user = db.getUserDetails();
            newpass = newpassword.getText().toString();
            email = user.get("email").toString();
            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setTitle("Contacting servers");
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.changePassword(newpass, email);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    alert.setText("");
                    String result = json.getString(KEY_SUCCESS);
                    String err = json.getString(KEY_ERROR);

                    if(Integer.parseInt(result)==1) {
                        pDialog.dismiss();
                        alert.setText("Password changed!");
                    }
                    else if (Integer.parseInt(err)==2) {
                        pDialog.dismiss();
                        alert.setText("Invalid old password");
                    }
                    else {
                        pDialog.dismiss();
                        alert.setText("Error occured");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
