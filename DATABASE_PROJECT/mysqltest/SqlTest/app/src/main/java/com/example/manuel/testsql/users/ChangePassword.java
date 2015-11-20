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
import com.example.manuel.testsql.database.DatabaseHandler;
import com.example.manuel.testsql.database.NetworkCheck;
import com.example.manuel.testsql.database.UserFunctions;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by Manuel on 10/31/2015.
 */
public class ChangePassword extends AppCompatActivity implements NetworkCheck.OnTaskCompleted {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    private EditText newpassword;
    private TextView alert;
    private Button button_change;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        newpassword = (EditText)findViewById(R.id.new_password);
        alert = (TextView) findViewById(R.id.text_alert);
        button_change = (Button) findViewById(R.id.button_changepassword);

        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), ChangePassword.this);
                if(!(newpassword.getText().toString().equals("")) ) {
                    checkConnection.netAsync(v);
                }
                else {
                    Toast.makeText(getApplicationContext(), "New password field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            Toast.makeText(getApplicationContext(), "Connection Success!!!", Toast.LENGTH_SHORT).show();
            new ProcessChange().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "NOT !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProcessChange extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        String newpass, email;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            HashMap user = new HashMap();
            user = db.getUserDetails();
            newpass = newpassword.getText().toString();
            email = user.get("email").toString();
            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setTitle("Contacting servers");
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.changePassword(newpass, email);
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
                        alert.setText("Password changed!");
                    }
                    else if (Integer.parseInt(err)==2) {
                        pDialog.dismiss();
                        alert.setText("Invalid old password");
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
