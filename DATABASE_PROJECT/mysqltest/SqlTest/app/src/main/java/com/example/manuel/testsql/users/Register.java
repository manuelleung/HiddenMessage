package com.example.manuel.testsql.users;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manuel.testsql.R;
import com.example.manuel.testsql.database.DatabaseHandler;
import com.example.manuel.testsql.database.UserFunctions;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Manuel on 10/29/2015.
 */
public class Register extends AppCompatActivity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_USERNAME = "uname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "error";

    private EditText input_firstname;
    private EditText input_lastname;
    private EditText input_email;
    //private EditText input_username;
    private EditText input_password;
    //private EditText input_confirmpassword;

    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    //private String confirmpassword;

    private Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        input_firstname = (EditText) findViewById(R.id.first_name);
        input_lastname = (EditText) findViewById(R.id.last_name);
        input_email = (EditText) findViewById(R.id.user_email);
        //input_username = (EditText) findViewById(R.id.username);
        input_password = (EditText) findViewById(R.id.user_password);
        //input_confirmpassword = (EditText) findViewById(R.id.confirm_password);



        button_register = (Button) findViewById(R.id.button_register);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = input_firstname.getText().toString();
                lastname = input_lastname.getText().toString();
                email = input_email.getText().toString();
                //username = input_username.getText().toString();
                password = input_password.getText().toString();
                //confirmpassword = input_confirmpassword.getText().toString();

                if( (!firstname.equals("")) && (!lastname.equals(""))
                        && (!email.equals("")) && (!password.equals(""))
                         ) {
                    //get username from email
                    String[] tokens = email.split("@");
                    username = tokens[0];

                    NetAsync(v);

                }
                else {
                    Toast.makeText(getApplicationContext(), "One or more fields are empty",
                            Toast.LENGTH_SHORT).show();
                }
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
            pDialog = new ProgressDialog(Register.this);
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

                    if(conn.getResponseCode() == 200) {
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
                new ProcessRegister().execute();
            }
            else {
                pDialog.dismiss();
                Toast.makeText(Register.this, "Error in Network Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

        //private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*
            pDialog = new ProgressDialog(Register.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            */
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.registerUser(firstname, lastname, email, username, password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    String result = json.getString(KEY_SUCCESS);
                    String err = json.getString(KEY_ERROR);

                    // FIX LATER////////////////////////////////////////////////////////////////
                    if(Integer.parseInt(result)==0) {
                        Toast.makeText(Register.this, "SUCCESSFULLY REGISTER", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                    //////////////////////////////////////////////////////////////////////

                    if(Integer.parseInt(result)==1) {
                        //pDialog.setTitle("Getting Data");
                        //pDialog.setMessage("Loading Info");

                        Toast.makeText(Register.this, "Successfuly Registered", Toast.LENGTH_SHORT).show();

                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        /*remove prev sqlite data , login user*/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_FIRSTNAME), json_user.getString(KEY_LASTNAME),
                                json_user.getString(KEY_EMAIL), json_user.getString(KEY_USERNAME),
                                json_user.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));

                        Intent registered = new Intent(getApplicationContext(), Registered.class);
                        registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //pDialog.dismiss();
                        startActivity(registered);

                        finish();
                    }
                    else if(Integer.parseInt(err)==2) {
                        //pDialog.dismiss();
                        Toast.makeText(Register.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }
                    else if(Integer.parseInt(err)==3) {
                        //pDialog.dismiss();
                        Toast.makeText(Register.this, "Invalid Email id", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    //pDialog.dismiss();
                    Toast.makeText(Register.this, "Error occured in registration", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
