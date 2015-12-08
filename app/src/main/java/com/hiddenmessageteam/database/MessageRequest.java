package com.hiddenmessageteam.database;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Manuel on 11/19/2015.
 */
public class MessageRequest implements NetworkCheck.OnTaskCompleted {

    /**
     * Message request interface
     * */
    public interface onMessageRequestCompleted {
        void onRequestCompleted(JSONObject jsonObject);
    }

    private onMessageRequestCompleted listener;

    private static final String KEY_SUCCESS = "success";

    private Context context;
    private View view;
    private JSONObject allMessagesJson;

    /**
     * Constructor
     * Initializes interface listener, view, context
     * Checks internet connection
     * */
    public  MessageRequest(Context context, View view, onMessageRequestCompleted listener) {
        this.listener = listener;
        this.context = context;
        this.view = view;
        NetworkCheck checkConnection = new NetworkCheck(context, this);
        checkConnection.netAsync();
    }

    /**
     * Connection interface response
     * if connected try to retrieve messages
     * */
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

    /**
     * Async class that processes message request for retrieving all messages
     * */
    private class ProcessRetrieveAllMessages extends AsyncTask<String, String, JSONObject> {

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
         * try to request all messages from db
         * Calls userFunctions.retrieveAllMessages
         * returns the JSONObject to onPostExecute
         * */
        @Override
        protected JSONObject doInBackground(String... params) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.retrieveAllMessages();
            return json;
        }

        /**
         * Executes after doInBackground has finished
         * parameter is JSONObject from doInBackground
         * will check if json was successful or error
         * if success then pass object to listener
         * else error
         * */
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS))==1) {
                        //Toast.makeText(context, "refreshed!", Toast.LENGTH_SHORT).show();
                        listener.onRequestCompleted(json);
                    }
                    else {
                        Toast.makeText(context, "Error occured in retrieving all messages", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
