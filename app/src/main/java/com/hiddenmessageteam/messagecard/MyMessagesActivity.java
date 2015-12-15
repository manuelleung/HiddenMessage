package com.hiddenmessageteam.messagecard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.EditProfileActivity;
import com.hiddenmessageteam.MapsActivity;
import com.hiddenmessageteam.R;
import com.hiddenmessageteam.SearchFriendActivity;
import com.hiddenmessageteam.SettingActivity;
import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.MessageDeletion;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;
import com.hiddenmessageteam.friendlist.FriendsActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Manuel on 12/2/2015.
 */
public class MyMessagesActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted, MessageDeletion.onMessageDeletionCompleted, NavigationView.OnNavigationItemSelectedListener {
    private static final String KEY_SUCCESS = "success";
    private final static String KEY_ERROR = "error";

    private String user_id;

    private JSONObject messagesObj;

    List<MessageInfo> result;

    private UserFunctions userFunctions;

    private HashMap<String, String> user_id_array;
    private HashMap<String, String> message_id_array;

    private HashMap userDetails;

    NavigationView navView;
    DrawerLayout drawer;
    DatabaseHandler db;
    private int viewid;


    private RecyclerView recList;
    private LinearLayoutManager llm;

    MessageListAdapter ca;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().show();
        }

        ///
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);



        user_id_array = new HashMap<String, String>();
        message_id_array = new HashMap<String, String>();

        db = new DatabaseHandler(getApplicationContext());
        userDetails = db.getUserDetails();
        user_id = userDetails.get("user_id").toString();

        NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), MyMessagesActivity.this);
        checkConnection.netAsync();

        /*
        Button delBtn = (Button)findViewById(R.id.button_delete);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDeletion messageDeletion = new MessageDeletion(getApplicationContext(), v, MyMessagesActivity.this);
                messageDeletion.setUserMessageId(ca.getUserIdArray(), ca.getMessageIdArray());
            }
        });
        */

        initNavigationBar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.delete:
                MessageDeletion messageDeletion = new MessageDeletion(getApplicationContext(), MyMessagesActivity.this);
                messageDeletion.setUserMessageId(ca.getUserIdArray(), ca.getMessageIdArray());
                break;
        }
        return super.onOptionsItemSelected(item);

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
        TextView navEmail = (TextView) header.findViewById(R.id.nav_email);
        navName.setText(firstName);
        navEmail.setText(email);

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
        navEmail.setOnClickListener(new View.OnClickListener() {
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
                } else if (id == R.id.nav_settings) {
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 250);


        return true;
    }
    /**
     * Remove messages method from interface
     * if remove was successful then it will clear the messages from the list
     * and reload only the messages left in db
     * */
    @Override
    public void onRequestCompleted(JSONObject jsonObject) {
        //ca.notifyDataSetChanged();

        // update the list accordingly and remove views from the adapter update adapter
        // so it updates locally

        // instead of doing this... because this forces another conenction to db
        NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), MyMessagesActivity.this);
        checkConnection.netAsync();
    }

    /**
     * Connection check method from interface
     * if connected we will execute retrieve my messages
     * */
    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessRetrieveMyMessages().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * (WILL CHANGE THIS TO MESSAGE REQUEST CLASS LATER)
     * Async class that processes retrieving of only my messages
     * */
    private class ProcessRetrieveMyMessages extends AsyncTask<String, String, JSONObject> {

        /**
         * Executes before do doInBackground
         * used for initialization
         * */
        List<Address> addresses;
        Geocoder geocoder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            geocoder = new Geocoder(MyMessagesActivity.this, Locale.getDefault());
        }

        /**
         * Works in the background
         * try to request my messages
         * Calls userFunctions.retrieveMyMessages
         * passes userid
         * returns the JSONObject to onPostExecute
         * */
        @Override
        protected JSONObject doInBackground(String... params) {
            userFunctions = new UserFunctions();
            JSONObject json = userFunctions.retrieveMyMessages(user_id);
            return json;
        }

        /**
         * Executes after doInBackground has finished
         * parameter is JSONObject from doInBackground
         * will check if json was successful or error
         * if success then we display each of the messages onto the interface
         * by creating views and setting them on layout
         * else error
         * */
        @Override
        protected void onPostExecute(final JSONObject json) {
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    if(Integer.parseInt(json.getString(KEY_SUCCESS))==1) {
                        int i;

                        result = new ArrayList<MessageInfo>();
                        //String message_id = json.getJSONObject("message").getString("message_id");
                        for(i=0; i<json.names().length()-3; i++) {
                            String value = null;
                            String messageTitle = null;
                            String messageContent = null;
                            String latitude = null;
                            String longitude = null;
                            String messageAddress = null;
                            try {
                                value = json.getString(i + "");
                                messageTitle = json.getJSONObject(i+"").getString("title");
                                messageContent = json.getJSONObject(i+"").getString("content");
                                latitude = json.getJSONObject(i+"").getString("latitude");
                                longitude = json.getJSONObject(i+"").getString("longitude");
                                addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                                messageAddress = addresses.get(0).getAddressLine(0);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(value != null) {
                                //viewid[i] = i;
                                /*
                                TextView rowTextView = new TextView(MyMessagesActivity.this);
                                rowTextView.setLayoutParams(lparams);
                                rowTextView.setText(value);
                                //rowTextView.setId(i);
                                linearLayout.addView(rowTextView);

                                CheckBox checkBox = new CheckBox(MyMessagesActivity.this);
                                checkBox.setLayoutParams(lparams);
                                checkBox.setOnCheckedChangeListener(MyMessagesActivity.this);
                                checkBox.setId(i);

                                linearLayout.addView(checkBox);

                                */


                                MessageInfo ci = new MessageInfo();
                                ci.title = messageTitle;
                                ci.address = messageAddress;
                                ci.content = messageContent;
                                ci.setId(i);
                                result.add(ci);


                            }
                        }

                        ca = new MessageListAdapter(result, json, getApplicationContext());
                        recList.setAdapter(ca);

                        messagesObj = json;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error occured in retrieve all messages", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


