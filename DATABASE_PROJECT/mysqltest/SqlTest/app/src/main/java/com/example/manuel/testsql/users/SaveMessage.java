package com.example.manuel.testsql.users;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.manuel.testsql.R;
import com.example.manuel.testsql.database.NetworkCheck;
import com.example.manuel.testsql.database.UserFunctions;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Issac on 11/10/15.
 */
public class SaveMessage extends AppCompatActivity implements NetworkCheck.OnTaskCompleted{
    private String title;
    private String content;
    private String name;

    private static final String KEY_SUCCESS = "success";

    private EditText input_name;
    private EditText input_title;
    private EditText input_content;

    private Button button_post;


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_savemessage);

        input_name = (EditText) findViewById(R.id.name);
        input_title = (EditText) findViewById(R.id.title);
        input_content = (EditText) findViewById(R.id.content);

        button_post = (Button) findViewById(R.id.button_post);



        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = input_name.getText().toString();
                title = input_title.getText().toString();
                content = input_content.getText().toString();

                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), SaveMessage.this);
                if(!(name.equals("")) || !(title.equals("")) || !(content.equals("")) ) {
                    checkConnection.netAsync(v);
                }
                else {
                    Toast.makeText(getApplicationContext(), "One of the fields is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            Toast.makeText(getApplicationContext(), "Connection Success!!!", Toast.LENGTH_SHORT).show();
            new ProcessMessage().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "NOT !!!", Toast.LENGTH_SHORT).show();
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

