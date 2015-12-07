package com.hiddenmessageteam;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hiddenmessageteam.database.DatabaseHandler;

/**
 * Created by Manuel on 12/5/2015.
 */
public class ProfileActivity extends AppCompatActivity {


    ImageView profilePic;
    DatabaseHandler db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHandler(this);

        profilePic = (ImageView) findViewById(R.id.profilepic);
        setProfilePic();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfilePic();

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
            case R.id.action_edit:
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void setProfilePic() {
        if(db.getProfilePic()!=null) {
            byte[] b = db.getProfilePic();
            Bitmap decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length);
            //android.view.ViewGroup.LayoutParams layoutParams = profilePic.getLayoutParams();
            //layoutParams.width = 250;
            //layoutParams.height = 250;
            //profilePic.setLayoutParams(layoutParams);
            profilePic.setImageBitmap(decodedByte);

        }
    }
}
