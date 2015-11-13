package com.example.manuel.testsql.users;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manuel.testsql.R;
import com.example.manuel.testsql.database.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Issac on 11/10/15.
 */
public class SaveMessage extends AppCompatActivity {
    private String title;
    private String content;
    private String name = "issac";

    private static final String KEY_SUCCESS = "success";

    private EditText input_title;
    private EditText input_content;

    private Button button_post;


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.save_message);

        input_title = (EditText) findViewById(R.id.title);
        input_content = (EditText) findViewById(R.id.content);

        button_post = (Button) findViewById(R.id.button_post);

        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = input_title.getText().toString();
                content = input_content.getText().toString();

                //if(! (title == "") || ! (content == "")){
                    NetAsync(v);

                //}
                //else{
                  //  Toast.makeText(getApplicationContext(), "One or more fields are empty.", Toast.LENGTH_SHORT).show();
                //}
            }
        });
    }

    public void NetAsync(View view){
        new NetCheck().execute();

    }

    private class NetCheck extends AsyncTask<String, String, Boolean>{
        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(SaveMessage.this);
            pDialog.setTitle("Posting Message");
            pDialog.setMessage("Posting...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
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
                pDialog.dismiss();
                new ProcessMessage().execute();

            }
            else{
                pDialog.dismiss();
                Toast.makeText(SaveMessage.this, "Error in Network Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class ProcessMessage extends AsyncTask<String, String, JSONObject>{
        private  ProgressDialog pDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(SaveMessage.this);
            pDialog.setTitle("Posting Message");
            pDialog.setMessage("Posting...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.postMessage(name, title, content);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json){
            try {
                //Toast.makeText(SaveMessage.this, "HEREEEE", Toast.LENGTH_SHORT).show();
                if (json.getString(KEY_SUCCESS) != null) {
                    //String result = json.getString(KEY_SUCCESS);

                    if (Integer.parseInt(json.getString(KEY_SUCCESS)) == 1) {
                        pDialog.dismiss();
                        Toast.makeText(SaveMessage.this, "Message Posted", Toast.LENGTH_SHORT).show();
                    } else {
                        pDialog.dismiss();
                        Toast.makeText(SaveMessage.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch(JSONException ex){
                ex.printStackTrace();
            }
        }
    }

}

