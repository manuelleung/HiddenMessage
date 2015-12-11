package com.hiddenmessageteam;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.FriendRequest;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Manuel on 12/9/2015.
 */
public class FriendsActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted{


    private static final String KEY_SUCCESS = "success";


    private Button searchButton;

    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams lparams;

    private Button rowButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().show();
        }

        linearLayout = (LinearLayout) findViewById(R.id.list_my_friends);
        lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        searchButton = (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchFriendActivity.class);
                startActivity(intent);
            }
        });

        NetworkCheck n = new NetworkCheck(getApplicationContext(), this);
        n.netAsync();
    }

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


    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessListFriends().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }
    }


    private class ProcessListFriends extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            HashMap userInfo;
            userInfo = databaseHandler.getUserDetails();
            String user_id = userInfo.get("user_id").toString();
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.listFriends(user_id);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    if (Integer.parseInt(json.getString(KEY_SUCCESS)) == 1) {
                        Toast.makeText(getApplicationContext(), "Friends listed", Toast.LENGTH_SHORT).show();


                        int i;
                        for(i=0; i<json.names().length()-3; i++) {
                            String value = null;
                            try {
                                value = json.getString(i+"");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(value != null) {
                                //viewid[i] = i;
                                TextView rowTextView = new TextView(FriendsActivity.this);
                                rowTextView.setLayoutParams(lparams);
                                rowTextView.setText(value);

                                rowButton = new Button(FriendsActivity.this);
                                rowButton.setText("Accept");
                                rowButton.setLayoutParams(lparams);
                                //setAcceptListener();


                                linearLayout.addView(rowTextView);
                                linearLayout.addView(rowButton);

                            }
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Error occures in listing friends " + json.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
