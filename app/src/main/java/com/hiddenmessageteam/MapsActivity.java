package com.hiddenmessageteam;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.hiddenmessageteam.database.HandleMessagePost;
import com.hiddenmessageteam.database.MessageRequest;

import org.json.JSONObject;

/**
 * Created by Manuel on 10/29/2015.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, MessageRequest.onMessageRequestCompleted {

    private GoogleMap mMap;
    private FloatingActionButton plusButton;
    private Intent postMessageIntent;
    private HandleMessagePost messagePost;

    private LatLng loc;
    private String lati="";
    private String longi="";

    private FloatingActionButton refreshButton;

    private NavigationView navView;
    private DrawerLayout drawer;


    private Boolean paused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialize
        plusButton = (FloatingActionButton) findViewById(R.id.fab);
        postMessageIntent = new Intent(this, SaveMessageActivity.class);
        messagePost = new HandleMessagePost();

        refreshButton = (FloatingActionButton) findViewById(R.id.button_refresh);



        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        // sync on user request
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageRequest retrieve = new MessageRequest(getApplicationContext(), findViewById(R.id.map), MapsActivity.this);
            }
        });


        navView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView.setNavigationItemSelectedListener(this);

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
        messagePost.setGoogleMap(mMap);



        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            loc = new LatLng(location.getLatitude(), location.getLongitude());
            lati = loc.latitude+"";
            longi = loc.longitude+"";
            //messagePost.setLocation(loc);
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_my_messages) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestCompleted(JSONObject json) {

        for(int i=0; i<json.length()-3; i++) {
            try {
                JSONObject object = json.getJSONObject("" + i);
                String title = object.getString("title");
                String content = object.getString("content");
                String latitude = object.getString("latitude");
                String longitude = object.getString("longitude");

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
}
