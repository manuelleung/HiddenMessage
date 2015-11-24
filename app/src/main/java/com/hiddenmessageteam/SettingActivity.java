package com.hiddenmessageteam;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Manuel on 11/24/2015.
 */
public class SettingActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted{
    private Button button_logout;
    private Button button_changepassword;

    private EditText inputNewPassword;
    private String newPassword;

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_setting);

        button_changepassword = (Button) findViewById(R.id.button_changepassword);
        button_logout = (Button) findViewById(R.id.button_logout);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        HashMap user = new HashMap();
        user = db.getUserDetails();

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFunctions logout = new UserFunctions();
                logout.userLogout(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        inputNewPassword = (EditText) findViewById(R.id.new_password);




        button_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword = inputNewPassword.getText().toString();
                NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), SettingActivity.this);
                if( !(newPassword.equals("")) ) {
                    checkConnection.netAsync(v);
                }
                else {

                    Toast.makeText(getApplicationContext(), "New password field is empty " + newPassword, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            //Toast.makeText(getApplicationContext(), "Connection Success!!!", Toast.LENGTH_SHORT).show();
            new ProcessPasswordChange().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "NOT !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProcessPasswordChange extends AsyncTask<String, String, JSONObject> {
        String newpass, email;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            HashMap user = new HashMap();
            user = db.getUserDetails();
            newpass = newPassword;
            email = user.get("email").toString();
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
                    String result = json.getString(KEY_SUCCESS);
                    String err = json.getString(KEY_ERROR);

                    if(Integer.parseInt(result)==1) {
                        Toast.makeText(getApplicationContext(), "Password changed!", Toast.LENGTH_SHORT).show();
                    }
                    else if (Integer.parseInt(err)==2) {
                        Toast.makeText(getApplicationContext(), "Invalid old password!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error occured trying to change password", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



}
