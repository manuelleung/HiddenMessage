package com.hiddenmessage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView createAccountText;
    private Button loginButton;
    private Button guestButton;

    private Intent createAccountIntent;
    private Intent loginIntent;
    private Intent mapsIntent;
    /***************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // Initialize Intents
        createAccountIntent = new Intent(this, CreateAccountActivity.class);
        loginIntent = new Intent(this,LoginActivity.class);
        mapsIntent = new Intent(this,MapsActivity.class);

        // Initialize the views:
        createAccountText = (TextView) findViewById(R.id.createAccountText);
        loginButton = (Button) findViewById(R.id.loginButton);
        guestButton =(Button) findViewById(R.id.guestButton);

        buildCreateAccountFunctionality();
        buildLoginButtonFunctionality();
        buildGuestButtonFunctionality();
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
