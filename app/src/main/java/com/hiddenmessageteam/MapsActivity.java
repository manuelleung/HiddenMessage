package com.hiddenmessageteam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.HandleMessagePost;
import com.hiddenmessageteam.database.MessageRequest;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Manuel on 10/29/2015.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, MessageRequest.onMessageRequestCompleted, View.OnClickListener {


    private GoogleApiClient mGoogleApiClient;
    private boolean goingToMyLocation;
    private GoogleMap mMap;
    private FloatingActionButton plusButton;
    private Intent postMessageIntent;
    private HandleMessagePost messagePost;
    private Intent mapIntent;

    private LatLng loc;
    private String lati="";
    private String longi="";

    private FloatingActionButton refreshButton;

    private NavigationView navView;
    private DrawerLayout drawer;
    private  int countToDelete = 2;
    private int countToDeleteRefresh = 2;

   private final Context context = this;


    private Boolean paused;
    private Intent locationIntent;
    private TextView mD;





    //----------------------------------------------------------------------------------------------
    ShowcaseView showcase;
    Target target_fab,target_refresh ;
    int count=0;


    ImageView setPic;


    DatabaseHandler db;
    HashMap userDetails;

    //---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        goingToMyLocation = true;
        mapIntent = new Intent(this, MapsActivity.class);

        db = new DatabaseHandler(this);
        userDetails = db.getUserDetails();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //initialize
        plusButton = (FloatingActionButton) findViewById(R.id.fab);
        postMessageIntent = new Intent(this, SaveMessageActivity.class);
        messagePost = new HandleMessagePost();
        messagePost.setContext(context);

        //refreshButton = (FloatingActionButton) findViewById(R.id.button_refresh);

        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            startLocationDialog("In order to get to your location,  you need to enable your location");
        }

        /*
        // sync on user request
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear Messages
                HandleMessagePost.clearAll();

                MessageRequest retrieve = new MessageRequest(getApplicationContext(), findViewById(R.id.map), MapsActivity.this);

            }

        });
        */


        plusButtonListener();

        initNavigationBar();

        initTutorial();
    }

    /******************************
     *  P L U S   B U T T O N
     *******************************/

    public void plusButtonListener() {
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                boolean network_enabled = false;

                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch(Exception ex) {}

                if(!gps_enabled && !network_enabled) {
                    startLocationDialog("In order to post a message,  you need to enable your location");
                }
                else {
                    if ((!lati.equals("")) && (!longi.equals(""))) {
                        Bundle bundle = new Bundle();
                        bundle.putString("latitude", "" + loc.latitude);
                        bundle.putString("longitude", "" + loc.longitude);

                        postMessageIntent.putExtras(bundle);
                        startActivityForResult(postMessageIntent, 1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please wait a few seconds to find your location", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
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
        setPic =(ImageView) header.findViewById(R.id.profilepic);
        TextView navName = (TextView) header.findViewById(R.id.nav_name);
        TextView navEmail = (TextView) header.findViewById(R.id.nav_email);
        navName.setText("Hi, " +firstName.substring(0,1).toUpperCase()+firstName.substring(1));
        navEmail.setText(email);

//        if(db.getProfilePic()!=null) {
//            byte[] b = db.getProfilePic();
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length);
//            setPic.setImageBitmap(decodedByte);
//        }

        final Intent goProfile= new Intent(this, ProfileActivity.class);
        setPic.setOnClickListener(new View.OnClickListener() {
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

    /**
     * Initializes the tutorial
     * */
    public void initTutorial() {
        // SetUp User Tutorial -- Will run only once.
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun)
        {
            target_fab = new ViewTarget(R.id.fab,this);
            //target_mood=new ViewTarget(R.id.mood,this);
            //target_refresh=new ViewTarget(R.id.button_refresh,this);
            showcase=new ShowcaseView.Builder(this)
                    .setTarget(Target.NONE)
                    .setContentTitle("Welcome to Hidden Message")
                    .setContentText("An exciting new world awaits. \nPress NEXT to get a quick overview")
                    .setStyle(R.style.finalsc)
                    .setOnClickListener(this)
                    .build();
            showcase.setButtonText("Next");
            //Aligning the showcase_button to the center.
            RelativeLayout.LayoutParams scbuttonpos = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            scbuttonpos.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            scbuttonpos.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
            scbuttonpos.setMargins(margin, margin, margin, 120);
            showcase.setButtonPosition(scbuttonpos);
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
        }
    }

    /**
     * Listener for tutorial clicks
     * */
    @Override
    public void onClick (View v){

        switch(count){
            case 0:
                showcase.setTarget(target_fab);
                showcase.setContentTitle("Add Message");
                showcase.setContentText("TAPPING the PLUS ICON will allow you post a message to your current location");
                break;
            /*
            case 1:
                showcase.setTarget(target_refresh);
                showcase.setContentTitle("Refresh");
                showcase.setContentText("Refresh (IDK)");
                break;
            */
            case 1:
                showcase.hide();
                break;
        }
        count++;
    }
    @Override
    public void onResume() {
        super.onResume();
        // if application is resumed. check for messages again
        // keep syncing the messages
        paused = false;
        backgroundSync();
    }

    @Override
    public void onPause(){
        super.onPause();
        // if application is paused. stop checking/updating for messages
        paused = true;
    }

    // Update messages in the background
    public void backgroundSync() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (paused==false) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Clear Messages
                            HandleMessagePost.clearAll();

                            //Toast.makeText(getApplicationContext(), "Updated messages", Toast.LENGTH_SHORT).show();
                            MessageRequest retrieve = new MessageRequest(getApplicationContext(), findViewById(R.id.map), MapsActivity.this);


                        }
                    });
                    try {
                        Thread.sleep(20000); // 20 sec
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        mMap.setMyLocationEnabled(true);
      //  mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                boolean network_enabled = false;

                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch(Exception ex) {}

                if(!gps_enabled && !network_enabled) {
                    startLocationDialog("In order to see your location,  you need to enable your location");
                }
                else {
                    goingToMyLocation = true;
                }
                return false;
            }
        });
        messagePost.setGoogleMap(mMap);




        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {

            if(goingToMyLocation) {
                loc = new LatLng(location.getLatitude(), location.getLongitude());
                lati = loc.latitude + "";
                longi = loc.longitude + "";
                //messagePost.setLocation(loc);
                if (mMap != null) {
                    goingToMyLocation = false;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14.4f));

                }
            }
        }
    };

    /**
     * Executes when a user finishes posting a new message from SaveMessageActivity
     * Will user the data that the user inputed
     * set all data to a messagePost and display it onto the map
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                messagePost.setMessageId(data.getExtras().get("message_id").toString());
                messagePost.setUserId(data.getExtras().get("user_id").toString());
                messagePost.setUsername(data.getExtras().get("username").toString());
                messagePost.setTitle(data.getExtras().get("title").toString());
                messagePost.setMessage(data.getExtras().get("content").toString());
                messagePost.setLocation(data.getExtras().get("latitude").toString(), data.getExtras().get("longitude").toString());
                messagePost.insertMark();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult



    //----------------------------------------------------------------------------------------------


    /******************************
     *  NAVIGATIONAL DRAWER
     *****************************/

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

    // NAV BAR OPTIONS
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
                    Toast.makeText(getApplicationContext(), "Home clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_friends) {
                    Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_my_messages) {
                    Intent myMessagesIntent = new Intent(getApplicationContext(), MyMessagesActivity.class);
                    startActivity(myMessagesIntent);
                } else if (id == R.id.nav_settings) {
                    Intent settingIntent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(settingIntent);
                }
            }
        }, 250);


        return true;
    }

    /**
     * Method from request all messages interface
     * if request worked then it will set all data requried to message post
     * for each message in db. and insert the mark onto the map
     * */
    @Override
    public void onRequestCompleted(JSONObject json) {

        for(int i=0; i<json.length()-3; i++) {
            try {
                JSONObject object = json.getJSONObject("" + i);
                String message_id = object.getString("message_id");
                String user_id = object.getString("user_id");
                String username = object.getString("username");
                String title = object.getString("title");
                String content = object.getString("content");
                String latitude = object.getString("latitude");
                String longitude = object.getString("longitude");
                final int currentIndex = i;
                messagePost.setMessageId(message_id);
                messagePost.setUserId(user_id);
                messagePost.setUsername(username)g;
                messagePost.setTitle(title);
                messagePost.setMessage(content);
                messagePost.setLocation(latitude, longitude);
                messagePost.insertMark();
                //Log.e("title ", title);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void startLocationDialog(String message) {
        // notify user
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Location");
        dialog.setMessage(message);
        dialog.setPositiveButton("Activate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);


            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub

            }
        });
        dialog.show();
    }



}
