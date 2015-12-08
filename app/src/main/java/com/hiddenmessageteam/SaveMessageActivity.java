package com.hiddenmessageteam;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.HandleMessagePost;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

import java.util.HashMap;

public class SaveMessageActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted {

    private static final String KEY_SUCCESS = "success";
    private final static String KEY_ERROR = "error";

    private EditText inputDisplayName;
    private EditText inputTitle;
    private EditText inputContent;

    private String user_id;
    private String title;
    private String content;

    private String latitude="";
    private String longitude="";

    private HandleMessagePost messagePost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_message);

        inputTitle = (EditText) findViewById(R.id.edit_title);
        inputContent = (EditText) findViewById(R.id.edit_content);

        DatabaseHandler db = new DatabaseHandler(this);
        HashMap user = new HashMap();
        user = db.getUserDetails();
        user_id = user.get("user_id").toString();
        //user_id = db.getUserId();

        latitude = getIntent().getExtras().getString("latitude");
        longitude = getIntent().getExtras().getString("longitude");

        messagePost = new HandleMessagePost();

        final Button postButton = (Button) findViewById(R.id.button_post);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* do your stuff here issac */
                title = inputTitle.getText().toString();
                content = inputContent.getText().toString();

                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), SaveMessageActivity.this);
                if ((!user_id.equals("")) && (!title.equals("")) && (!content.equals(""))) {
                    //Toast.makeText(getApplicationContext(), "user_id" + user_id, Toast.LENGTH_SHORT).show();
                    postButton.setEnabled(false);
                    checkConnection.netAsync();
                } else {
                    Toast.makeText(getApplicationContext(), "One of the fields is empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    /**
     * Connection check method from interface
     * if connected we will execute save messages
     * */
    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessSaveMessage().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Async class that processes message save
     * */
    private class ProcessSaveMessage extends AsyncTask<String, String, JSONObject> {

        /**
         * Executes before do doInBackground
         * used for initialization
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Works in the background
         * try to request message posting
         * Calls userFunctions.postMyMessages
         * passes userid, title, content, lat, long
         * returns the JSONObject to onPostExecute
         * */
        @Override
        protected JSONObject doInBackground(String... params) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.postMessage(user_id, title, content, latitude, longitude);
            return json;
        }

        /**
         * Executes after doInBackground has finished
         * parameter is JSONObject from doInBackground
         * will check if json was successful or error
         * if success then we put the data from the message input
         * into the bundle to be passed to the maps activity to create a
         * mark on the map for this newly posted message
         * go to map activity
         * else error
         * */
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS) !=null ) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS))==1) {
                        Toast.makeText(getApplicationContext(), "Message Posted", Toast.LENGTH_SHORT).show();
                        String message_id = json.getJSONObject("message").getString("message_id");
                        Bundle bundle = new Bundle();
                        bundle.putString("message_id", message_id);
                        bundle.putString("user_id", user_id);
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
