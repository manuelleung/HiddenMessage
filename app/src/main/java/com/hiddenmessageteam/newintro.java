package com.hiddenmessageteam;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiddenmessageteam.database.DatabaseHandler;

public class newintro extends AppCompatActivity {
    private Intent goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newintro);
        goHome=new Intent(this,MainActivity.class);
        // Set the Title to the want typeface.
        TextView title = (TextView) findViewById(R.id.intro_title);
        Typeface changetitle = Typeface.createFromAsset(getAssets(), "fonts/LucidaCalligraphyItalic.ttf");
        title.setTypeface(changetitle);

        TextView slogan = (TextView) findViewById(R.id.intro_slogan);
        changetitle = Typeface.createFromAsset(getAssets(), "fonts/HARRINGT.TTF");
        slogan.setTypeface(changetitle);

        TextView windcore = (TextView) findViewById(R.id.intro_windcore);
        changetitle = Typeface.createFromAsset(getAssets(), "fonts/colonna.ttf");
        windcore.setTypeface(changetitle);


        RelativeLayout in=(RelativeLayout) findViewById(R.id.intro_back);

        final Animation animation_in= AnimationUtils.loadAnimation(getBaseContext(), R.anim.wait);
        in.setAnimation(animation_in);
        animation_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override

            public void onAnimationEnd(Animation animation) {
                finish();

                /*CHECK IF AN USER ALREADY LOGGED IN*/
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                if (db.getRowCount() > 0) {
                    Intent loggedin = new Intent(getApplicationContext(), MapsActivity.class);
                    loggedin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loggedin);
                    overridePendingTransition(0, 0);
                } else {
                    startActivity(goHome);
                    overridePendingTransition(0, 0); //Smoother transition between activity.
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }




}
