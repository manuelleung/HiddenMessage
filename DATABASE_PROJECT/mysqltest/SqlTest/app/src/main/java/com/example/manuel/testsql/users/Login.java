package com.example.manuel.testsql.users;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.manuel.testsql.R;
import com.example.manuel.testsql.database.DatabaseHandler;
import com.example.manuel.testsql.database.NetworkCheck;
import com.example.manuel.testsql.database.UserFunctions;
import org.json.JSONObject;

/**
 * Created by Manuel on 10/29/2015.
 */
public class Login extends AppCompatActivity implements NetworkCheck.OnTaskCompleted{

    private Button button_login;
    private Button button_reset;

    private EditText input_email;
    private EditText input_password;

    private String user_email;
    private String user_password;

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_UID = "uid";
    private static final String KEY_USERNAME = "uname";
    private static final String KEY_FIRSTNAME = "fname";
    private static final String KEY_LASTNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CREATED_AT = "created_at";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*CHECK IF AN USER ALREADY LOGGED IN*/
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        if(db.getRowCount()>0) {
            Intent loggedin = new Intent(getApplicationContext(), LoggedIn.class);
            loggedin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loggedin);
        }

        input_email = (EditText) findViewById(R.id.user_email);
        input_password = (EditText) findViewById(R.id.user_password);

        button_login = (Button) findViewById(R.id.button_login);
        button_reset = (Button) findViewById(R.id.button_reset);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email = input_email.getText().toString();
                user_password = input_password.getText().toString();

                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), Login.this);
                if( (!user_email.equals("")) && (!user_password.equals("")) )
                {
                    checkConnection.netAsync(v);
                }
                else if( (!user_email.equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if( (!user_password.equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password fields are empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PasswordReset.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            Toast.makeText(getApplicationContext(), "Connection Success!!!", Toast.LENGTH_SHORT).show();
            new ProcessLogin().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "NOT !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        String email, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email = input_email.getText().toString();
            password = input_password.getText().toString();

            pDialog = new ProgressDialog(Login.this);
            pDialog.setTitle("Login Attempt");
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.loginUser(email, password);
            return json;
        }

        @Override
        protected  void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String result = json.getString(KEY_SUCCESS);

                    if (Integer.parseInt(result) == 1) {
                        pDialog.setMessage("Loading user space");
                        pDialog.setTitle("Getting data...");

                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        // clear all previous data in SQLite db
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),
                                json_user.getString(KEY_EMAIL), json_user.getString(KEY_USERNAME),
                                json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));


                        Intent loggedin = new Intent(getApplicationContext(), LoggedIn.class);
                        loggedin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(loggedin);

                        finish();
                    } else {
                        pDialog.dismiss();
                        Toast.makeText(Login.this, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
