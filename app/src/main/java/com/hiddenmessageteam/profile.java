package com.hiddenmessageteam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class profile extends AppCompatActivity {
    ImageView setprofilepic;
    private static final int CAM_REQUEST=1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setprofilepic=(ImageView) findViewById(R.id.profilepic);
    }
    public void changeProfile(View v){
            Intent camerinter = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camerinter, CAM_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQUEST) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            setprofilepic.setImageBitmap(thumbnail);
        }
    }
}
