package com.hiddenmessageteam.database;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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


/**************************
     Default Constructor
 **************************/
    public HandleMessagePost() {
        setTitle("");
        setMessage("");
    }

/**************************
       Mutators
**************************/
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



/**************************
      insertMark Method
 **************************/
    public void insertMark() {
        googleMap.addMarker(new MarkerOptions().position(location).title(title).snippet(message));
    }

}
