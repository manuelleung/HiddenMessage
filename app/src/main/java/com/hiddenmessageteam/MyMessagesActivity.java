package com.hiddenmessageteam;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.MessageDeletion;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Manuel on 12/2/2015.
 */
public class MyMessagesActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted, CompoundButton.OnCheckedChangeListener, MessageDeletion.onMessageDeletionCompleted {
    private static final String KEY_SUCCESS = "success";
    private final static String KEY_ERROR = "error";

    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams lparams;

    private String user_id;

    private JSONObject messagesObj;

    private UserFunctions userFunctions;

    private HashMap<String, String> user_id_array;
    private HashMap<String, String> message_id_array;

    private int viewid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);

        user_id_array = new HashMap<String, String>();
        message_id_array = new HashMap<String, String>();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        HashMap user = new HashMap();
        user = db.getUserDetails();
        user_id = user.get("user_id").toString();
        //user_id = db.getUserId();

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

        Button delBtn = (Button)findViewById(R.id.button_delete);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDeletion messageDeletion = new MessageDeletion(getApplicationContext(), findViewById(R.id.list_my_messages), MyMessagesActivity.this);
                messageDeletion.setUserMessageId(user_id_array, message_id_array);
            }
        });
    }

    @Override
    public void onRequestCompleted(JSONObject jsonObject) {
        // change to remove only deleted ones later //////
        //View myView = findViewById(viewid);
        //ViewGroup parent = (ViewGroup) myView.getParent();
        //parent.removeView(myView);

        linearLayout.removeAllViews();
        ///////////////////////////////////////////////
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            try {
                //title = messagesObj.getJSONObject(buttonView.getId()+"").getString("title");
                user_id_array.put(buttonView.getId()+"", messagesObj.getJSONObject(buttonView.getId()+"").getString("user_id"));
                message_id_array.put(buttonView.getId()+"", messagesObj.getJSONObject(buttonView.getId() + "").getString("message_id"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //viewid = buttonView.getId();
            //Toast.makeText(getApplicationContext(), "Checked " + buttonView.getId(), Toast.LENGTH_SHORT).show();

        }
        else {
            user_id_array.remove(buttonView.getId()+"");
            message_id_array.remove(buttonView.getId()+"");
            //Toast.makeText(getApplicationContext(), "NOT Checked " + buttonView.getId(), Toast.LENGTH_SHORT).show();
        }
    }



    private class ProcessRetrieveMyMessages extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            userFunctions = new UserFunctions();
            JSONObject json = userFunctions.retrieveMyMessages(user_id);
            return json;
        }
        @Override
        protected void onPostExecute(final JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS))==1) {
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
                                TextView rowTextView = new TextView(MyMessagesActivity.this);
                                rowTextView.setLayoutParams(lparams);
                                rowTextView.setText(value);
                                //rowTextView.setId(i);
                                linearLayout.addView(rowTextView);

                                CheckBox checkBox = new CheckBox(MyMessagesActivity.this);
                                checkBox.setLayoutParams(lparams);
                                checkBox.setOnCheckedChangeListener(MyMessagesActivity.this);
                                checkBox.setId(i);

                                linearLayout.addView(checkBox);


                            }
                        }
                        messagesObj = json;
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


