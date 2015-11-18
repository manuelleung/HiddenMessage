package com.hiddenmessageteam;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.hiddenmessageteam.database.HandleMessagePost;

/**
 * Created by Manuel on 10/29/2015.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FloatingActionButton plusButton;
    private HandleMessagePost messagePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        messagePost = new HandleMessagePost();
        //final Intent messageIntent = new Intent(this, MessageActivity.class);
        plusButton = (FloatingActionButton) findViewById(R.id.fab);
        plusButton.setTranslationX(plusButton.getTranslationX() + 500);
        plusButton.setTranslationY(plusButton.getTranslationY() + 750);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messagePost.insertMark();
                // Intent intent = new Intent(MapsActivity.this, MessageActivity.class);
                //startActivity(intent);
//        /*        Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
//                        Toast.LENGTH_LONG).show();*/
            }
        });
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

        // Add a marker in Sydney and move the camera
        //LatLng currentLocation = new LatLng()
        mMap.setMyLocationEnabled(true);
        LatLng sydney = new LatLng(-34, 151);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //      mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        messagePost.setTitle("Lenny");
        messagePost.setMessage("Hello World!");
        messagePost.setGoogleMap(mMap);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            messagePost.setLocation(loc);
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };
}
