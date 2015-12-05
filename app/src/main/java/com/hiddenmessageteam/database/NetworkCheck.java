package com.hiddenmessageteam.database;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Manuel on 11/18/2015.
 */
public class NetworkCheck extends ContextWrapper {

    /**
     * Network check interface
     * */
    public interface OnTaskCompleted {
        void onConnCompleted(boolean conn);
    }

    private OnTaskCompleted listener;

    /**
     * Constructor
     * Initializes listener
     * gets context from the calling class
     * */
    public NetworkCheck(Context base, OnTaskCompleted listener) {
        super(base);
        this.listener = listener;
    }

    /**
     * Method to check for connection
     * (will remove view since its not used) later
     * */
    public void netAsync(View view){
        new NetCheck().execute();
    }

    /**
     * Async class that processes network check
     * */
    private class NetCheck extends AsyncTask<String, String, Boolean> {

        /**
         * Executes before do doInBackground
         * used for initialization
         * */
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        /**
         * Works in the background
         * try to connect to www.google.com
         * returns the boolean to onPostExecute
         * */
        @Override
        protected Boolean doInBackground(String... params) {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(3000);
                    connection.connect();

                    if (connection.getResponseCode() == 200) {
                        return true;

                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();

                } catch (IOException ex) {
                    ex.printStackTrace();

                }
            }
            return false;
        }

        /**
         * Executes after doInBackground has finished
         * parameter is boolean from doInBackground
         * if true then there is internet connection
         * else no internet connection
         * */
        @Override
        protected void onPostExecute(Boolean result){
            if(result == true){
                listener.onConnCompleted(true);

            }
            else{
                listener.onConnCompleted(false);
            }

        }
    }

}