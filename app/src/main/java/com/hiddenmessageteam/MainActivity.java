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
    private String valid_email;
    private static int emailcount=0;
    private static int passwordcount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // TextView title = (TextView) findViewById(R.id.title);
        Typeface changetitle = Typeface.createFromAsset(getAssets(), "fonts/LucidaCalligraphyItalic.ttf");
//       title.setTypeface(changetitle);

        TextView or = (TextView) findViewById(R.id.orline);
        changetitle = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        or.setTypeface(changetitle);

        TextView forgot = (TextView) findViewById(R.id.forgot);
        forgot.setText(Html.fromHtml("<font color= '#00E000'> Forgot your sign in info? </font> <font color= '#0AFF0A'> <b>GET HELP</b> </font"));

        //------------------------------------------------------------------------------------------------------------------------------
        email = (TextView) findViewById(R.id.email);
        password = (TextView) findViewById(R.id.password);

        signin=(Button) findViewById(R.id.signinbutton);
        signin.setEnabled(false);
        checkifemailwritten();
        checkifpasswordwritten();
        //---------------------------------

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && email.getText().length()!=0&& !isEmailValid(email.getText())) {
                    email.setError("Invalid Email Address");
                }
            }
        });



    }

    private void checkifemailwritten() {

        email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkifpasswordwritten() {

        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
