package com.hiddenmessageteam.database;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Issac on 12/6/15.
 */
public class FriendRequest implements NetworkCheck.OnTaskCompleted {

    private static final String KEY_SUCCESS = "success";

    private Context context;
    private View view;

    private String user_id;
    private String target_id;

    public FriendRequest(Context context, View view){
        this.context = context;
        this.view = view;
    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn){
            new ProcessFriendRequest().execute();
        }
        else{
            Toast.makeText(context, "No internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProcessFriendRequest extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        //need to get target_id
        @Override
        protected JSONObject doInBackground(String... params) {
            DatabaseHandler databaseHandler = new DatabaseHandler(context);
            UserFunctions userFunctions = new UserFunctions();
            HashMap<String, String> localUserInfo;
            localUserInfo = databaseHandler.getUserDetails();
            user_id = localUserInfo.get("user_id");
            JSONObject json = userFunctions.addFriend(user_id, target_id);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json){
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS))==1) {
                        Toast.makeText(context, "Friend Added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Error occures in adding friend", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
