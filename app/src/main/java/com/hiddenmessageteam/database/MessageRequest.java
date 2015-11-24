package com.hiddenmessageteam.database;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.BackgroundAnimation;
import com.hiddenmessageteam.R;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Manuel on 11/19/2015.
 */
public class MessageRequest implements NetworkCheck.OnTaskCompleted {

    public interface onMessageRequestCompleted {
        void onRequestCompleted(JSONObject jsonObject);
    }

    private onMessageRequestCompleted listener;

    private static final String KEY_SUCCESS = "success";

    private Context context;
    private View view;
    private JSONObject allMessagesJson;

    public  MessageRequest(Context context, View view, onMessageRequestCompleted listener) {
        this.listener = listener;
        this.context = context;
        this.view = view;
        NetworkCheck checkConnection = new NetworkCheck(context, this);
        checkConnection.netAsync(view);
    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            //Toast.makeText(context, "Connection Success!!!", Toast.LENGTH_SHORT).show();
            new ProcessRetrieveAllMessages().execute();
        }
        else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public JSONObject getAllMessages() {
        return allMessagesJson;
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
                        //Toast.makeText(context, "refreshed!", Toast.LENGTH_SHORT).show();
                        listener.onRequestCompleted(json);
                    }
                    else {
                        //Toast.makeText(context, "Error occured in retrieving all messages", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
