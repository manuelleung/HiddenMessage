package com.hiddenmessageteam.friendlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hiddenmessageteam.R;
import com.hiddenmessageteam.SearchFriendActivity;
import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Manuel on 12/9/2015.
 */
public class FriendsActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted{


    private static final String KEY_SUCCESS = "success";


    private Button searchButton;

    /*
    private LinearLayout linearLayoutPending;
    private LinearLayout.LayoutParams lparamsPending;

    private LinearLayout linearLayoutFriends;
    private LinearLayout.LayoutParams lparamsFriends;
*/

    //private Button rowButton;

    //boolean friendsListedBool = true;

    List<FriendInfo> result;

    private RecyclerView recList;

    private LinearLayoutManager llm;

    private FriendsAdapter ca;

    String target_id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().show();
        }

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recList.setLayoutManager(llm);



        /*
        linearLayoutPending = (LinearLayout) findViewById(R.id.list_pending_friends);
        lparamsPending = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        linearLayoutFriends = (LinearLayout) findViewById(R.id.list_my_friends);
        lparamsFriends = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

*/

        //setFriendListener();
    }

    public void setFriendListener() {
        NetworkCheck n = new NetworkCheck(getApplicationContext(), this);
        n.netAsync();
    }

    @Override
    public void onResume() {
        super.onResume();
        setFriendListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
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
            case R.id.search:
                Intent intent = new Intent(getApplicationContext(), SearchFriendActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessListFriends().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }
    }


    private class ProcessListFriends extends AsyncTask<String, String, JSONObject> {
        String user_id;
        HashMap myDetails;
        DatabaseHandler databaseHandler;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseHandler = new DatabaseHandler(getApplicationContext());
            myDetails = databaseHandler.getUserDetails();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            user_id = myDetails.get("user_id").toString();
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.listFriends(user_id);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    if (Integer.parseInt(json.getString(KEY_SUCCESS)) == 1) {
                        //Toast.makeText(getApplicationContext(), "Friends listed", Toast.LENGTH_SHORT).show();

                        result = new ArrayList<FriendInfo>();

                        int i;
                        for(i=0; i<json.names().length()-3; i++) {
                            String value = null;
                            try {
                                value = json.getString(i+"");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(value != null) {
                                //viewid[i] = i;
                                JSONObject user = json.getJSONObject(i + "");
                                target_id = user.getString("user_id");

                                String status = user.getString("status");
                                String action_user_id = user.getString("action_user_id");


                                String friendEmail = user.getString("email");
                                String friendName = user.getString("firstname") +" "+ user.getString("lastname");

                                String encodedImage = user.getString("profile_pic");
                                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);

                                //setprofilepic.setImageBitmap(decodedByte);
                                /*
                                //pending users
                                if(user.getString("status").equals("0")) {

                                    TextView rowTextView = new TextView(FriendsActivity.this);
                                    rowTextView.setLayoutParams(lparamsPending);
                                    rowTextView.setText(value);

                                    linearLayoutPending.addView(rowTextView);

                                    rowButton = new Button(FriendsActivity.this);
                                    rowButton.setLayoutParams(lparamsPending);
                                    rowButton.setId(Integer.parseInt(target_id));

                                    if(user.getString("action_user_id").equals(user_id)) {
                                        rowButton.setText("Waiting for Reply");
                                        linearLayoutPending.addView(rowButton);
                                    }
                                    else {
                                        rowButton.setText("Accept");
                                        linearLayoutPending.addView(rowButton);
                                        setAcceptListener();
                                    }

                                }//added users
                                else if(user.getString("status").equals("1")) {
                                    TextView friendsRowTextView = new TextView(FriendsActivity.this);
                                    friendsRowTextView.setLayoutParams(lparamsFriends);
                                    friendsRowTextView.setText(value);
                                    linearLayoutFriends.addView(friendsRowTextView);
                                }


                                */



                                FriendInfo ci = new FriendInfo();
                                ci.email = friendEmail;
                                ci.name = friendName;
                                if(!encodedImage.equals("null")) {
                                    ci.profilePicture = decodedByte;
                                }

                                if(user_id.equals(action_user_id) && status.equals("0")) {
                                    ci.status = "pending";
                                } else if(!user_id.equals(action_user_id) && status.equals("0")) {
                                    ci.status = "accept";
                                } else if(status.equals("1")) {
                                    ci.status = "friend";
                                }

                                result.add(ci);



                            }
                        }
                        ca = new FriendsAdapter(result, getApplicationContext());
                        recList.setAdapter(ca);


                    } else {
                        Toast.makeText(getApplicationContext(), "Error occures in listing friends " + json.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    public void setAcceptListener() {
        rowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pressed "+v.getId(), Toast.LENGTH_SHORT).show();
                FriendRequest friendRequest = new FriendRequest(getApplicationContext());
                friendRequest.acceptFriend(v.getId()+"", true);
            }
        });
    }
    */

}
