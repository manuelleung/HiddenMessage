package com.hiddenmessageteam.database;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hiddenmessageteam.R;

import java.util.ArrayList;

/**
 * Created by Leibniz H. Berihuete on 11/12/2015.
 */
public class HandleMessagePost {
/***************************
        Data Fields
***************************/
    private String title;
    private String message;
    private GoogleMap googleMap;
    private LatLng location;
    private static ArrayList<Marker> markerList = new ArrayList<>();
    private int ID;
    private String uid;
    private Marker marker;



/**************************
     Default Constructor
 **************************/
    public HandleMessagePost() {
        ID = -1;
        setTitle("");
        setMessage("");
    }

/**************************
       Mutators
**************************/
    public void setUid(String uid){this.uid = uid;}
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void setLocation(String latitude, String longitude) {
        location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }





/**************************
         Accessors
 **************************/
    public String getUid(){return uid;}

    public String getTitle() {
        return this.title;
    }

    public String getMessage() {
        return this.message;
    }

    public GoogleMap getGoogleMap(){
        return  this.googleMap;
    }

    public LatLng getLocation() {
        return this.location;
    }

    public Marker getMarker(int ID) {
        return HandleMessagePost.markerList.get(ID);
    }

    public ArrayList<Marker> getMarkerList() {
        return HandleMessagePost.markerList;
    }

    public Marker getMarker() {
        return marker;
    }



/**************************
      insertMark Method
 **************************/
    public void insertMark() {
      /*  int num = 1 + new java.util.Random().nextInt(2);

        if(num == 1) {
            ID = markerList.size()+1;
            Marker marker = googleMap.addMarker(new MarkerOptions().position(location).title(title  + "ID: " + ID).snippet(message).icon(BitmapDescriptorFactory.fromResource(R.drawable.message_icon2)));
            markerList.add(marker);
        }
        else {*/
            ID = markerList.size()+1;
            marker = googleMap.addMarker(new MarkerOptions().position(location).title(title).snippet(message).icon(BitmapDescriptorFactory.fromResource(R.drawable.message_icon)));
            markerList.add(marker);
        /*}*/



    }

    public static void clearAll() {
        for(int i = 0; i < HandleMessagePost.markerList.size(); i++) {
            HandleMessagePost.markerList.get(i).remove();
        }
        HandleMessagePost.markerList.clear();
    }


}
