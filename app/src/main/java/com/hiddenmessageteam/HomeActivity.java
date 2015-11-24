package com.hiddenmessageteam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiddenmessageteam.database.DatabaseHandler;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private TextView createAccountText;
    private Button loginButton;
    private Button guestButton;

    private TextView forgotPasswordText;

    private Intent createAccountIntent;
    private Intent loginIntent;
    private Intent mapsIntent;

    private Intent forgotPasswordIntent;

    // RASHED
    private Intent onProgressInterface;

    /***************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        /*CHECK IF AN USER ALREADY LOGGED IN*/
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        if(db.getRowCount()>0) {
            Intent loggedin = new Intent(getApplicationContext(), MapsActivity.class);
            loggedin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loggedin);
        }

        // Initialize Intents
        createAccountIntent = new Intent(this, RegisterActivity.class);
        loginIntent = new Intent(this, LoginActivity.class);
        mapsIntent = new Intent(this, MapsActivity.class);

        forgotPasswordIntent = new Intent(this, ForgotPasswordActivity.class);

        //RASHED
        onProgressInterface = new Intent(this, MainActivity.class);

        RelativeLayout backgroundLayout = (RelativeLayout) findViewById(R.id.home_activity_layout);
        new BackgroundAnimation(backgroundLayout);

        // Initialize the views:
        createAccountText = (TextView) findViewById(R.id.createAccountText);
        loginButton = (Button) findViewById(R.id.loginButton);
        guestButton = (Button) findViewById(R.id.guestButton);

        forgotPasswordText = (TextView) findViewById(R.id.forgot_password_text);

        // RASHED
        Button onProgressInt = (Button) findViewById(R.id.onProgressActivity);
        onProgressInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(onProgressInterface);
            }
        });

        buildForgotPasswordFunctionality();

        buildCreateAccountFunctionality();
        buildLoginButtonFunctionality();
        buildGuestButtonFunctionality();
    }

    public void buildForgotPasswordFunctionality() {
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(forgotPasswordIntent);
            }
        });
    }

    public void buildCreateAccountFunctionality() {
        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(createAccountIntent);
            }
        });
    }

    public void buildLoginButtonFunctionality() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
            }
        });
    }

    public void buildGuestButtonFunctionality() {
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mapsIntent);
            }
        });
    }
}
