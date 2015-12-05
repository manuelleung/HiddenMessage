package com.hiddenmessageteam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiddenmessageteam.database.DatabaseHandler;

import java.util.HashMap;

public class profile extends AppCompatActivity {
    ImageView setprofilepic;
    private static final int CAM_REQUEST=1313;
    Bitmap thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setprofilepic=(ImageView) findViewById(R.id.profilepic);

        DatabaseHandler db = new DatabaseHandler(this);
        HashMap userDetails = db.getUserDetails();
        String firstName = userDetails.get("fname").toString();
        String lastName = userDetails.get("lname").toString();
        String email = userDetails.get("email").toString();

        TextView profileName = (TextView) findViewById(R.id.profile_name);
        profileName.setText(firstName+" "+lastName);

        TextView profileEmail = (TextView) findViewById(R.id.profile_email);
        profileEmail.setText(email);


    }
    public void changeProfile(View v){
            Intent camerinter = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camerinter, CAM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQUEST) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            setprofilepic.setImageBitmap(thumbnail);
        }
    }
}