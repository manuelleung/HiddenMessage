package com.hiddenmessageteam;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Manuel on 12/2/2015.
 */
public class MyMessagesActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted {
    private static final String KEY_SUCCESS = "success";
    private final static String KEY_ERROR = "error";

    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams lparams;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        user_id = db.getUserId();

        linearLayout = (LinearLayout) findViewById(R.id.list_my_messages);
        lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button retrieveAllButton = (Button) findViewById(R.id.button_my_message);

        retrieveAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), MyMessagesActivity.this);
                checkConnection.netAsync(v);
            }
        });

        NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), MyMessagesActivity.this);
        checkConnection.netAsync(findViewById(R.id.list_my_messages));

    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessRetrieveMyMessages().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProcessRetrieveMyMessages extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.retrieveMyMessages(user_id);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS))==1) {
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
                                TextView rowTextView = new TextView(MyMessagesActivity.this);
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


