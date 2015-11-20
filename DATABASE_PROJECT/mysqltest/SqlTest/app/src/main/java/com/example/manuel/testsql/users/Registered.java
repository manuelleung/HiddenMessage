package com.example.manuel.testsql.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.manuel.testsql.R;
import com.example.manuel.testsql.database.DatabaseHandler;
import java.util.HashMap;

/**
 * Created by Manuel on 10/30/2015.
 */
public class Registered extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        HashMap user = new HashMap();
        user = db.getUserDetails();

        final TextView fname = (TextView)findViewById(R.id.first_name);
        final TextView lname = (TextView)findViewById(R.id.last_name);
        final TextView uname = (TextView)findViewById(R.id.username);
        final TextView email = (TextView)findViewById(R.id.user_email);
        final TextView created_at = (TextView) findViewById(R.id.created_date);

        fname.setText(user.get("fname").toString());
        lname.setText(user.get("lname").toString());
        uname.setText(user.get("uname").toString());
        email.setText(user.get("email").toString());
        created_at.setText(user.get("created_at").toString());

        Button login = (Button) findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
