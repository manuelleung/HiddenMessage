package com.hiddenmessageteam;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_register);
/*
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().show();
        }
*/

        TextView gotologIN=(TextView) findViewById(R.id.signUplogIN);
        gotologIN.setText(Html.fromHtml("<font color= '#525252'>Have an account? </font> <font color= '#525252'> <b>LOG IN</b> </font"));
        inputFirstName = (EditText) findViewById(R.id.edit_first_name);
        inputLastName = (EditText) findViewById(R.id.edit_last_name);
        inputEmail = (EditText) findViewById(R.id.edit_email);
        inputPassword = (EditText) findViewById(R.id.edit_password);
        inputConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);
        TextView rtitle= (TextView) findViewById(R.id.registertitle);
        Typeface changetitle = Typeface.createFromAsset(getAssets(), "fonts/LucidaCalligraphyItalic.ttf");
        rtitle.setTypeface(changetitle);
        RelativeLayout backgroundLayout = (RelativeLayout) findViewById(R.id.register_layout);
       // new BackgroundAnimation(backgroundLayout);

        registerButton = (Button) findViewById(R.id.button_register);
        registerButton.setEnabled(false);
        //registerButton.setAlpha(1);

        checkifpasswordwritten();

        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && inputEmail.getText().length() != 0 && !isEmailValid(inputEmail.getText())) {
                    inputEmail.setError("Invalid Email Address");
                }
            }
        });

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
                        checkConnection.netAsync();
                    } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT).show();
                }


            }
        });

        /*
        Button cancelButton = (Button) findViewById(R.id.button_cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        */
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
*/
    /**
     * Checks if an email is valid from input
     * returns boolean
     * */
    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * FIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIX this later
     * Check if all input has been written on all boxes before allowing user
     * to click on the button to create account
     * */
    private void checkifpasswordwritten() {
        inputConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstname = inputFirstName.getText().toString();
                lastname = inputLastName.getText().toString();
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                confirmPassword = inputConfirmPassword.getText().toString();
                if (s.length() > 0 && (!firstname.equals("")) && (!lastname.equals(""))
                        && (!email.equals("")) && (!password.equals(""))
                        && (!confirmPassword.equals(""))) {
                    registerButton.setEnabled(true);
                    registerButton.setAlpha(1);
                }
                if (s.length() == 0) {
                    registerButton.setAlpha((float) 0.3);
                    registerButton.setEnabled(false);
                }
            }
        });
    }

    /**
     * Connection check method from interface
     * if connected we will execute register
     * */
    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessRegister().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Async class that processes user registration
     * */
    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

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
         * try to request user registration
         * Calls userFunctions.Register
         * passes email, firstname, lastname, username, password
         * returns the JSONObject to onPostExecute
         * */
        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.userRegister(firstname, lastname,
                    email, username, password);
            return json;
        }

        /**
         * Executes after doInBackground has finished
         * parameter is JSONObject from doInBackground
         * will check if json was successful or error
         * if success then user has been added to db
         * ( WILL FIX THIS SO USER IS ADDED TO LOCAL DB AND SKIPS LOGIN LATER )
         * else error if user exists, invalid email, etc
         * */
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    String error = json.getString(KEY_ERROR);
                    String result = json.getString(KEY_SUCCESS);

                    // GOOD
                    if(Integer.parseInt(result)==1) {
                        Toast.makeText(getApplicationContext(), "Successfuly Registered", Toast.LENGTH_SHORT).show();

                        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                        finish();
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
