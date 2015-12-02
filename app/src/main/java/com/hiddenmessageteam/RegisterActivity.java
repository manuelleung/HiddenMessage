package com.hiddenmessageteam;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted {

    private final static String KEY_SUCCESS = "success";
    private final static String KEY_UID = "uid";
    private final static String KEY_FIRSTNAME = "fname";
    private final static String KEY_LASTNAME = "lname";
    private final static String KEY_USERNAME = "uname";
    private final static String KEY_EMAIL = "email";
    private final static String KEY_CREATED_AT = "created_at";
    private final static String KEY_ERROR = "error";

    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;

    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_register);

        inputFirstName = (EditText) findViewById(R.id.edit_first_name);
        inputLastName = (EditText) findViewById(R.id.edit_last_name);
        inputEmail = (EditText) findViewById(R.id.edit_email);
        inputPassword = (EditText) findViewById(R.id.edit_password);
        inputConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);

        RelativeLayout backgroundLayout = (RelativeLayout) findViewById(R.id.register_layout);
        new BackgroundAnimation(backgroundLayout);

        Button registerButton = (Button) findViewById(R.id.button_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = inputFirstName.getText().toString();
                lastname = inputLastName.getText().toString();
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                confirmPassword = inputConfirmPassword.getText().toString();

                // Check internet connection
                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), RegisterActivity.this);

                if ((!firstname.equals("")) && (!lastname.equals(""))
                        && (!email.equals("")) && (!password.equals(""))
                        && (!confirmPassword.equals(""))) {
                    //check password match
                    if (password.equals(confirmPassword)) {
                        String[] tokens = email.split("@");
                        username = tokens[0];
                        checkConnection.netAsync(v);
                    } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT).show();
                }


            }
        });

        Button cancelButton = (Button) findViewById(R.id.button_cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessRegister().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.userRegister(firstname, lastname,
                    email, username, password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    String error = json.getString(KEY_ERROR);
                    String result = json.getString(KEY_SUCCESS);

                    // GOOD
                    if(Integer.parseInt(result)==1) {
                        Toast.makeText(getApplicationContext(), "Successfuly Registered", Toast.LENGTH_SHORT).show();

                        //Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(loginIntent);
                        //finish();
                    }
                    else if(Integer.parseInt(error)==2) {
                        //pDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }
                    else if(Integer.parseInt(error)==3) {
                        //pDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Invalid Email id", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Error occured in registration", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
