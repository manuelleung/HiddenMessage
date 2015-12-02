package com.hiddenmessageteam;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.HandleMessagePost;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

public class SaveMessageActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted {

    private static final String KEY_SUCCESS = "success";
    private final static String KEY_ERROR = "error";

    private EditText inputDisplayName;
    private EditText inputTitle;
    private EditText inputContent;

    private String uid;
    private String title;
    private String content;

    private String latitude="";
    private String longitude="";

    private HandleMessagePost messagePost;
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_message);

        inputTitle = (EditText) findViewById(R.id.edit_title);
        inputContent = (EditText) findViewById(R.id.edit_content);


        latitude = getIntent().getExtras().getString("latitude");
        longitude = getIntent().getExtras().getString("longitude");

        messagePost = new HandleMessagePost();

        final Button postButton = (Button) findViewById(R.id.button_post);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* do your stuff here issac */
                uid = databaseHandler.getUid();
                title = inputTitle.getText().toString();
                content = inputContent.getText().toString();

                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), SaveMessageActivity.this);
                if ((!uid.equals("")) && (!title.equals("")) && (!content.equals(""))) {
                    postButton.setEnabled(false);
                    checkConnection.netAsync(v);
                } else {
                    Toast.makeText(getApplicationContext(), "One of the fields is empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessSaveMessage().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProcessSaveMessage extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.postMessage(uid, title, content, latitude, longitude);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS) !=null ) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS))==1) {
                        Toast.makeText(getApplicationContext(), "Message Posted", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("uid", uid);
                        bundle.putString("title", title);
                        bundle.putString("content", content);
                        bundle.putString("latitude", latitude);
                        bundle.putString("longitude", longitude);
                        Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                        mapIntent.putExtras(bundle);
                        setResult(Activity.RESULT_OK, mapIntent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error occured in post message", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
