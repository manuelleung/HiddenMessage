package com.hiddenmessageteam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.SignIn.MainActivity;
import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;
import com.hiddenmessageteam.friendlist.FriendsActivity;
import com.hiddenmessageteam.messagecard.MyMessagesActivity;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Manuel on 11/24/2015.
 */
public class SettingActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted,  NavigationView.OnNavigationItemSelectedListener{
    private Button button_logout;
    private Button button_changepassword;

    private EditText inputNewPassword;
    private String newPassword;

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    private NavigationView navView;
    private DrawerLayout drawer;
    DatabaseHandler db;
    HashMap userDetails;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_setting);



        button_changepassword = (Button) findViewById(R.id.button_changepassword);
        button_logout = (Button) findViewById(R.id.button_logout);

        db = new DatabaseHandler(getApplicationContext());
        userDetails = db.getUserDetails();

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFunctions logout = new UserFunctions();
                logout.userLogout(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                if (!(newPassword.equals(""))) {
                    checkConnection.netAsync();
                } else {

                    Toast.makeText(getApplicationContext(), "New password field is empty " + newPassword, Toast.LENGTH_SHORT).show();
                }
            }
        });

        initNavigationBar();
    }

    /**
     * Initializes the nav side bar
     * */
    public void initNavigationBar() {
        String firstName = userDetails.get("fname").toString();
        String lastName = userDetails.get("lname").toString();
        String email = userDetails.get("email").toString();

        navView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView.setNavigationItemSelectedListener(this);
        View header= navView.getHeaderView(0);
        ImageView setPic =(ImageView) header.findViewById(R.id.profilepic);
        TextView navName = (TextView) header.findViewById(R.id.nav_name);
        //TextView navEmail = (TextView) header.findViewById(R.id.nav_email);
        navName.setText(firstName);
        //navEmail.setText(email);


        if(db.getProfilePic()!=null) {
            byte[] b = db.getProfilePic();
            Bitmap decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length);
            setPic.setImageBitmap(decodedByte);
        }

        final Intent goProfile= new Intent(this,EditProfileActivity.class);
        setPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goProfile);

            }
        });
        navName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(goProfile);
                    }
                }, 250);
            }
        });
//        navEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(goProfile);
//                    }
//                }, 250);
//            }
//        });

    }

    /**
     * Connection check method from interface
     * if connected we will execute password change
     * */
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id == R.id.nav_home) {
                    Intent myMapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(myMapIntent);
                    finish();
                } else if (id == R.id.nav_friends) {
                    Intent myMapIntent = new Intent(getApplicationContext(), FriendsActivity.class);
                    startActivity(myMapIntent);
                    finish();
                } else if (id == R.id.nav_my_messages) {
                    Intent myMessagesIntent = new Intent(getApplicationContext(), MyMessagesActivity.class);
                    startActivity(myMessagesIntent);
                    finish();
                } else if (id == R.id.nav_settings) {
                }
            }
        }, 250);


        return true;
    }


    /**
     * Async class that processes password change
     * */
    private class ProcessPasswordChange extends AsyncTask<String, String, JSONObject> {
        String newpass, email;

        /**
         * Executes before do doInBackground
         * used for initialization
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            HashMap user = new HashMap();
            user = db.getUserDetails();
            newpass = newPassword;
            email = user.get("email").toString();
        }

        /**
         * Works in the background
         * try to request password change
         * Calls userFunctions.changePassword
         * passes email address and new password
         * returns the JSONObject to onPostExecute
         * */
        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.changePassword(newpass, email);
            return json;
        }

        /**
         * Executes after doInBackground has finished
         * parameter is JSONObject from doInBackground
         * will check if json was successful or error
         * if success then password was changed
         * else error
         * */
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
