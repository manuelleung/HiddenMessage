package com.hiddenmessageteam;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.database.FriendRequest;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

/**
 * Created by Manuel on 12/9/2015.
 */
public class SearchFriendActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted{

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_ERROR = "error";

    private EditText searchFriendInput;
    private Button searchFriendButton;

    private String username;
    private String user_id;
    private String email;
    private String firstname;
    private String lastname;

    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams lparams;

    private Button rowButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);


        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().show();
        }

        linearLayout = (LinearLayout) findViewById(R.id.list_search_friend);
        lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        searchFriendInput = (EditText) findViewById(R.id.search_friend);
        searchFriendButton = (Button) findViewById(R.id.search_friend_button);

        searchFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = searchFriendInput.getText().toString();
                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), SearchFriendActivity.this);
                checkConnection.netAsync();
            }
        });


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

    /**
     * Connection check method from interface
     * if connected we will execute
     * */
    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessSearchFriend().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * (WILL CHANGE THIS TO MESSAGE REQUEST CLASS LATER)
     * Async class that processes retrieving of only my messages
     * */
    private class ProcessSearchFriend extends AsyncTask<String, String, JSONObject> {

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
         * try to request my messages
         * Calls userFunctions.retrieveMyMessages
         * passes userid
         * returns the JSONObject to onPostExecute
         * */
        @Override
        protected JSONObject doInBackground(String... params) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.searchFriend(username);
            return json;
        }

        /**
         * Executes after doInBackground has finished
         * parameter is JSONObject from doInBackground
         * will check if json was successful or error
         * if success then we display each of the messages onto the interface
         * by creating views and setting them on layout
         * else error
         * */
        @Override
        protected void onPostExecute(final JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS))==1) {
                        Toast.makeText(getApplicationContext(), "Friend search success", Toast.LENGTH_SHORT).show();
                        JSONObject user = json.getJSONObject("user");
                        user_id = user.getString("user_id");
                        firstname = user.getString("firstname");
                        lastname = user.getString("lastname");
                        email = user.getString("email");

                        String value = json.getString("user");
                        TextView rowTextView = new TextView(SearchFriendActivity.this);
                        rowTextView.setLayoutParams(lparams);
                        rowTextView.setText(value);
                        //rowTextView.setId(i);

                        rowButton = new Button(SearchFriendActivity.this);
                        rowButton.setText("Add");
                        rowButton.setLayoutParams(lparams);
                        setAddListener();

                        linearLayout.addView(rowTextView);
                        linearLayout.addView(rowButton);

                    }
                    else {
                        Toast.makeText(getApplicationContext(), username+" was not found", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAddListener() {
        rowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pressed", Toast.LENGTH_SHORT).show();
                FriendRequest friendRequest = new FriendRequest(getApplicationContext());
                friendRequest.addFriend(user_id, true);
            }
        });
    }

}
