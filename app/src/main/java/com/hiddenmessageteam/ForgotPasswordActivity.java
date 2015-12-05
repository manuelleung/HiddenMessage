package com.hiddenmessageteam;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

/**
 * Created by Manuel on 11/24/2015.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted {
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    private EditText input_email;
    private String email;
    private TextView alert;
    private Button reset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        input_email = (EditText) findViewById(R.id.useremail);

        Button reset = (Button) findViewById(R.id.button_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = input_email.getText().toString();
                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), ForgotPasswordActivity.this);
                if( email.equals("") == false ) {

                    checkConnection.netAsync(v);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Email field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Connection check method from interface
     * if connected we will execute reset
     * */
    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            //Toast.makeText(getApplicationContext(), "NOT !!!", Toast.LENGTH_SHORT).show();
            new ProcessReset().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Async class that processes account reset password
     * */
    private class ProcessReset extends AsyncTask<String, String, JSONObject> {

        /**
         * Executes before do doInBackground
         * used for initialization
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Works in the background
         * try to request password reset
         * Calls userFunctions.forgotPassword
         * passes email address
         * returns the JSONObject to onPostExecute
         * */
        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.forgotPassword(email);
            return json;
        }

        /**
         * Executes after doInBackground has finished
         * parameter is JSONObject from doInBackground
         * will check if json was successful or error
         * if success then password resetted
         * else error
         * */
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    String result = json.getString(KEY_SUCCESS);
                    String err = json.getString(KEY_ERROR);

                    if(Integer.parseInt(result)==1) {
                        Toast.makeText(getApplicationContext(), "Recovery email has been sent", Toast.LENGTH_SHORT).show();
                    }
                    else if(Integer.parseInt(err)==2) {
                        Toast.makeText(getApplicationContext(), "Email does not exist", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error occured in password reset", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
