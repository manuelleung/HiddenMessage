package com.hiddenmessageteam;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted {

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
    private Button signin;
    private Button signup;
    private Button guest;
    private TextView forgot;

    private static int emailcount=0;
    private static int passwordcount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set the Title to the want typeface.
        TextView title = (TextView) findViewById(R.id.title);
        Typeface changetitle = Typeface.createFromAsset(getAssets(), "fonts/LucidaCalligraphyItalic.ttf");
        title.setTypeface(changetitle);

        // This allows the TextView to have two different color and bold.
        forgot = (TextView) findViewById(R.id.forgot);
        forgot.setText(Html.fromHtml("<font color= '#525252'> Forgot your sign in info? </font> <font color= '#525252'> <b>GET HELP</b> </font"));

        // Initialize Id
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        signin=(Button) findViewById(R.id.signinbutton);
        signup=(Button) findViewById(R.id.signupbutton);
        //guest=(Button) findViewById(R.id.skipbutton);

        signin.setEnabled(false); //Disabling the signin button - user can not sign in until the requirements have been meet.
        checkifemailwritten(); // This method will check if the user enter an email.
        checkifpasswordwritten(); // This method will check if the user enter an password.

        // When the email textfield lose focus, it will check if the email has a valid.
        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && inputEmail.getText().length()!=0&& !isEmailValid(inputEmail.getText())) {
                    inputEmail.setError("Invalid Email Address");
                }
            }
        });

        signupButtonListener();
        signinButtonListener();
        //guestButtonListener();
        forgotButtonListener();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkifemailwritten() {
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailcount=s.length();
                if(s.length()>0&& passwordcount>0&& isEmailValid(s)){
                    signin.setEnabled(true);
                    signin.setAlpha(1);
                }
                if(s.length()==0) {
                    signin.setAlpha((float) 0.3);
                    signin.setEnabled(false);
                }
            }
        });
    }

    private void checkifpasswordwritten() {
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordcount = s.length();
                if (s.length() > 0 && emailcount > 0 && isEmailValid(inputEmail.getText())) {
                    signin.setEnabled(true);
                    signin.setAlpha(1);
                }
                if (s.length() == 0) {
                    signin.setAlpha((float) 0.3);
                    signin.setEnabled(false);
                }
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////
    public void forgotButtonListener() {
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    /////////////////////////////////////////////////////////////////////////////////
//    public void guestButtonListener() {
//        guest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
    ////////////////////////////////////////////////////////////////////////////////
    public void signupButtonListener() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void signinButtonListener() {
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = inputEmail.getText().toString();
                userPassword = inputPassword.getText().toString();

                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), MainActivity.this);
                if ((!userEmail.equals("")) && (!userPassword.equals(""))) {
                    checkConnection.netAsync();
                } else {
                    Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn == true) {
            new ProcessLogin().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.userLogin(userEmail, userPassword);
            return json;
        }

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
