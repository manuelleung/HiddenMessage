package com.hiddenmessageteam;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Issac on 11/19/2015.
 */
public class RetrieveAllMessagesActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted {

    private static final String KEY_SUCCESS = "success";
    private final static String KEY_ERROR = "error";

    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams lparams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_messages);

        linearLayout = (LinearLayout) findViewById(R.id.list_messages);
        lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button retrieveAllButton = (Button) findViewById(R.id.button_all_message);

        retrieveAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), RetrieveAllMessagesActivity.this);
                checkConnection.netAsync(v);
            }
        });

        NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), RetrieveAllMessagesActivity.this);
        checkConnection.netAsync(findViewById(R.id.list_messages));

    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            //Toast.makeText(getApplicationContext(), "Connection Success!!!", Toast.LENGTH_SHORT).show();
            new ProcessRetrieveAllMessages().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProcessRetrieveAllMessages extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.retrieveAllMessages();
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS))==1) {
                        //Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        Iterator<String> keys = json.keys();
                        int count = 0;
                        while(keys.hasNext()) {
                            if(count < 3) {
                                keys.next();
                                count++;
                                continue;
                            }
                            String key = keys.next();
                            String value = null;
                            try {
                                value = json.getString(key);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(value != null) {
                                TextView rowTextView = new TextView(RetrieveAllMessagesActivity.this);
                                rowTextView.setLayoutParams(lparams);
                                rowTextView.setText(value);
                                linearLayout.addView(rowTextView);
                            }
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error occured in retrieve all messages", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}