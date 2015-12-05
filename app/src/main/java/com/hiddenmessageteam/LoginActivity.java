package com.hiddenmessageteam;

import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_UID = "user_id";
    private static final String KEY_USERNAME = "uname";
    private static final String KEY_FIRSTNAME = "fname";
    private static final String KEY_LASTNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CREATED_AT = "created_at";

    private EditText inputEmail;
    private EditText inputPassword;

    private String userEmail;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.edit_email);
        inputPassword = (EditText) findViewById(R.id.edit_password);

        Button buttonLogin = (Button) findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = inputEmail.getText().toString();
                userPassword = inputPassword.getText().toString();

                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), LoginActivity.this);
                if( (!userEmail.equals("")) && (!userPassword.equals("")) ) {
                    checkConnection.netAsync(v);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Connection check method from interface
     * if connected we will execute login
     * */
    @Override
    public void onConnCompleted(boolean conn) {
        if(conn == true) {
            new ProcessLogin().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Async class that processes user login
     * */
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

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
         * try to request user login
         * Calls userFunctions.userLogin
         * passes email address and password
         * returns the JSONObject to onPostExecute
         * */
        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.userLogin(userEmail, userPassword);
            return json;
        }

        /**
         * Executes after doInBackground has finished
         * parameter is JSONObject from doInBackground
         * will check if json was successful or error
         * if success then we add user row to local database
         * and go to map activity
         * else error
         * */
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS) != null) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS)) == 1) {
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject userJson = json.getJSONObject("user");

                        UserFunctions userFunctions = new UserFunctions();
                        userFunctions.userLogout(getApplicationContext());

                        db.addUser(userJson.getString(KEY_FIRSTNAME),
                                userJson.getString(KEY_LASTNAME),
                                userJson.getString(KEY_EMAIL),
                                userJson.getString(KEY_USERNAME),
                                userJson.getString(KEY_UID),
                                userJson.getString(KEY_CREATED_AT));

                        Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                        mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mapIntent);
                        finish();
                    }
                    else if(Integer.parseInt(json.getString("error")) == 1){
                        Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error occured in login", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
