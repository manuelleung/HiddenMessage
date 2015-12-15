package com.hiddenmessageteam.database;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hiddenmessageteam.MapsActivity;
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
    private static ArrayList<String> messageList = new ArrayList<>();
    private static ArrayList<String> titleList = new ArrayList<>();
    private int ID;

    private String user_id;
    private String message_id;

    private String username;

    private Marker marker;
    private Context context;
    MapsActivity mapsActivity;


/**************************
     Default Constructor
 **************************/
    public HandleMessagePost() {
        this.context = context;
        ID = -1;
        setTitle("");
        setMessage("");
    }

/**************************
       Mutators
**************************/
    public void setUserId(String user_id) {
        this.user_id = user_id;
    }
    public void setMessageId(String message_id) {
        this.message_id = message_id;
    }
    public void setUsername(String username) {
        this.username = username;
    }

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

    public void setContext(Context context) {
        this.context = context;
    }





/**************************
         Accessors
 **************************/
    public String getUserId(){return user_id;}

    public String getMessageId(){return message_id;}

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

    public Context getContext() {
        return this.context;
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
//        }
//        else {*/
            ID = markerList.size()+1;
            marker = googleMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.message_icon)));
            markerList.add(marker);
            titleList.add(title);
            messageList.add(message);

            final Marker myMarker = marker;


            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    /****************************
                     *  Marker DIalog Box pop up
                     ****************************/
                    for(int i = 0; i < HandleMessagePost.markerList.size(); i++) {
                        if(marker.equals(HandleMessagePost.markerList.get(i))) {
                            final Dialog readmessage_Dialog = new Dialog(context);
                            readmessage_Dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
                            readmessage_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            readmessage_Dialog.setContentView(R.layout.message_reading_dialog);
                            readmessage_Dialog.setTitle("Rashed: " + HandleMessagePost.markerList.get(i).getTitle());
                            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                            layoutParams.copyFrom(readmessage_Dialog.getWindow().getAttributes());
                            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                            readmessage_Dialog.show();
                            readmessage_Dialog.getWindow().setAttributes(layoutParams);
                            TextView settitle=(TextView) readmessage_Dialog.findViewById(R.id.readmessagedialog_title);
                            TextView setname=(TextView) readmessage_Dialog.findViewById(R.id.readmessagedialog_name);
                            TextView setbody=(TextView) readmessage_Dialog.findViewById(R.id.readmessagedialog_showmessage);

                            setname.setText(""+username);
                            settitle.setText(HandleMessagePost.markerList.get(i).getTitle());
                            setbody.setText(HandleMessagePost.markerList.get(i).getSnippet());
                            Button cancelButton = (Button)readmessage_Dialog.findViewById(R.id.readmessagedialog_CancelButton);
                            readmessage_Dialog.setCanceledOnTouchOutside(false);
                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    readmessage_Dialog.dismiss();
                                }
                            });
                        }
                    }




                    return false;
                }
            });
//        /*}*/






    }

    /**
     * Clears all the marks from map
     * */
    public static void clearAll() {
        for(int i = 0; i < HandleMessagePost.markerList.size(); i++) {
            HandleMessagePost.markerList.get(i).remove();
        }
        HandleMessagePost.markerList.clear();
    }


}
