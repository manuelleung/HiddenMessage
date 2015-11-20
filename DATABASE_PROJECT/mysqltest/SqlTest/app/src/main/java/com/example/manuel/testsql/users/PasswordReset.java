package com.example.manuel.testsql.users;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.manuel.testsql.R;
import com.example.manuel.testsql.database.NetworkCheck;
import com.example.manuel.testsql.database.UserFunctions;
import org.json.JSONObject;

/**
 * Created by Manuel on 10/30/2015.
 */
public class PasswordReset extends AppCompatActivity implements NetworkCheck.OnTaskCompleted {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    private EditText email;
    private TextView alert;
    private Button reset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        email = (EditText) findViewById(R.id.user_email);
        alert = (TextView) findViewById(R.id.text_alert);
        Button reset = (Button) findViewById(R.id.button_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), PasswordReset.this);
                if( !(email.getText().toString()).equals("") ) {
                    checkConnection.netAsync(v);
                }
            }
        });
    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            Toast.makeText(getApplicationContext(), "Connection Success!!!", Toast.LENGTH_SHORT).show();
            new ProcessReset().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "NOT !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProcessReset extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        String forgotten_password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            forgotten_password = email.getText().toString();
            pDialog = new ProgressDialog(PasswordReset.this);
            pDialog.setTitle("Contacting servers");
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.forgotPassword(forgotten_password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    alert.setText("");
                    String result = json.getString(KEY_SUCCESS);
                    String err = json.getString(KEY_ERROR);

                    if(Integer.parseInt(result)==1) {
                        pDialog.dismiss();
                        alert.setText("A recovery email has been sent");
                    }
                    else if(Integer.parseInt(err)==2) {
                        pDialog.dismiss();
                        alert.setText("Email does not exist");
                    }
                    else {
                        pDialog.dismiss();
                        alert.setText("Error occured");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
