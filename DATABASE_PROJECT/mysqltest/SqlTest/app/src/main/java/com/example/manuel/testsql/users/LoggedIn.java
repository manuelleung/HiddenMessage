package com.example.manuel.testsql.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.manuel.testsql.R;
import com.example.manuel.testsql.database.DatabaseHandler;
import com.example.manuel.testsql.database.UserFunctions;

import java.util.HashMap;

/**
 * Created by Manuel on 10/30/2015.
 */
public class LoggedIn extends AppCompatActivity {

    private Button button_logout;
    private Button button_changepassword;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_logged);

        button_changepassword = (Button) findViewById(R.id.button_changepassword);
        button_logout = (Button) findViewById(R.id.button_logout);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        HashMap user = new HashMap();
        user = db.getUserDetails();

        button_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(intent);
            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFunctions logout = new UserFunctions();
                logout.logoutUser(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        final TextView welcome = (TextView) findViewById(R.id.text_welcome);
        welcome.setText("Welcome " + user.get("fname").toString());
    }

}
