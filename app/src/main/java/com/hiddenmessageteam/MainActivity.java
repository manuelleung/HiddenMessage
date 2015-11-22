package com.hiddenmessageteam;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView email;
    private TextView password;
    private Button signin;
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
        TextView forgot = (TextView) findViewById(R.id.forgot);
        forgot.setText(Html.fromHtml("<font color= '#8F8F8F'> Forgot your sign in info? </font> <font color= '#D6D6D6'> <b>GET HELP</b> </font"));

        // Initialize Id
        email = (TextView) findViewById(R.id.email);
        password = (TextView) findViewById(R.id.password);
        signin=(Button) findViewById(R.id.signinbutton);

        signin.setEnabled(false); //Disabling the signin button - user can not sign in until the requirements have been meet.
        checkifemailwritten(); // This method will check if the user enter an email.
        checkifpasswordwritten(); // This method will check if the user enter an password.

        // When the email textfield lose focus, it will check if the email has a valid.
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && email.getText().length()!=0&& !isEmailValid(email.getText())) {
                    email.setError("Invalid Email Address");
                }
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkifemailwritten() {

        email.addTextChangedListener(new TextWatcher() {
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

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordcount = s.length();
                if (s.length() > 0 && emailcount > 0 && isEmailValid(email.getText())) {
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

    public void buttonclick(View v){
        Toast.makeText(this,"Under Construction!!",Toast.LENGTH_SHORT).show();
    }
}
