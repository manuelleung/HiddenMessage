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

    public interface OnTaskCompleted {
        void onConnCompleted(boolean conn);
    }

    private OnTaskCompleted listener;

    public NetworkCheck(Context base, OnTaskCompleted listener) {
        super(base);
        this.listener = listener;
    }

    public void netAsync(View view){
        new NetCheck().execute();
    }

    private class NetCheck extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

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