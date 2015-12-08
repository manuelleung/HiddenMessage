package com.hiddenmessageteam.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Manuel on 12/2/2015.
 */

public class MessageDeletion implements NetworkCheck.OnTaskCompleted {

    /**
     * Message deletion interface
     * */
    public interface onMessageDeletionCompleted {
        void onRequestCompleted(JSONObject jsonObject);
    }

    private onMessageDeletionCompleted listener;

    private static final String KEY_SUCCESS = "success";

    private Context context;
    private View view;

    private HashMap<String, String> user_id_array;
    private HashMap<String, String> message_id_array;

    /**
     * Constructor
     * Initializes interface listener, view, context
     * Checks internet connection
     * */
    public  MessageDeletion(Context context, View view, onMessageDeletionCompleted listener) {
        this.listener = listener;
        this.context = context;
        this.view = view;
        NetworkCheck checkConnection = new NetworkCheck(context, this);
        checkConnection.netAsync();
    }

    /**
     * Sets the user and message ID to be deleted
     * */
    public void setUserMessageId(HashMap<String, String> user_id_array, HashMap<String, String> message_id_array) {
        this.user_id_array=user_id_array;
        this.message_id_array=message_id_array;

    }

    /**
     * Connection interface response
     * if connected try to delete message
     * */
    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessMessageDeletion().execute();
        }
        else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Async class that processes message deletion
     * */
    private class ProcessMessageDeletion extends AsyncTask<String, String, JSONObject> {

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
         * try to delete an array of messages from the database
         * Calls userFunctions.deleteMessage
         * passes user_id and message_id
         * returns the JSONObject to onPostExecute
         * */
        @Override
        protected JSONObject doInBackground(String... params) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = null;

            Iterator it = user_id_array.entrySet().iterator();
            Iterator it2 = message_id_array.entrySet().iterator();
            while(it.hasNext()){
                HashMap.Entry pair = (HashMap.Entry)it.next();
                while(it2.hasNext()) {
                    HashMap.Entry pair2 = (HashMap.Entry) it2.next();
                    json = userFunctions.deleteMessage(pair.getValue().toString(), pair2.getValue().toString());
                    it2.remove();
                }
                it.remove();
            }


            /*
            for (HashMap.Entry<String, String> user_entry : user_id_array.entrySet()) {
                for(HashMap.Entry<String, String> message_entry : message_id_array.entrySet()) {
                    json = userFunctions.deleteMessage(user_id_array.get(user_entry.getValue()), message_entry.getValue());
                }
            }
            */
            //JSONObject json = userFunctions.deleteMessage();
            return json;
        }

        /**
         * Executes after doInBackground has finished
         * parameter is JSONObject from doInBackground
         * will check if json was successful or error
         * if success then pass boolean true to listener
         * else pass false to listener
         * */
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS))==1) {
                        Toast.makeText(context, "Deleted messages", Toast.LENGTH_SHORT).show();
                        listener.onRequestCompleted(json);
                    }
                    else {
                        Toast.makeText(context, "Error occured in deleting messages", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

