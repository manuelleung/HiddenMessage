package com.hiddenmessageteam.SignIn;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiddenmessageteam.MapsActivity;
import com.hiddenmessageteam.R;
import com.hiddenmessageteam.SignIn.EntranceOptionsActivity;
import com.hiddenmessageteam.database.DatabaseHandler;

public class IntroActivity extends AppCompatActivity
{

    // Declare Variables
    private Intent goHome;
    private RelativeLayout introBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Links to activity_intro
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //
        goHome=new Intent(this,EntranceOptionsActivity.class);
        introBack=(RelativeLayout) findViewById(R.id.introBack);
        TextView introTitle = (TextView) findViewById(R.id.introTitle);
        TextView introSlogan = (TextView) findViewById(R.id.introSlogan);

        // Setting to the wanted typeface.
        Typeface changeTitle = Typeface.createFromAsset(getAssets(), "fonts/LucidaCalligraphyItalic.ttf");
        introTitle.setTypeface(changeTitle);

        changeTitle = Typeface.createFromAsset(getAssets(), "fonts/HARRINGT.TTF");
        introSlogan.setTypeface(changeTitle);

//        TextView windcore = (TextView) findViewById(R.id.introwindcore);
//        changeTitle = Typeface.createFromAsset(getAssets(), "fonts/colonna.ttf");
//        windcore.setTypeface(changeTitle);

        setBackground();
    }


    private void setBackground()
    {
        final Animation animationIntro= AnimationUtils.loadAnimation(getBaseContext(), R.anim.wait);
        introBack.setAnimation(animationIntro);
        animationIntro.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();

                /*CHECK IF AN USER ALREADY LOGGED IN*/
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                if (db.getRowCount() > 0) {
                    Intent loggedIn = new Intent(getApplicationContext(), MapsActivity.class);
                    loggedIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loggedIn);
                    overridePendingTransition(0, 0);
                } else {
                    startActivity(goHome);
                    overridePendingTransition(0, 0); //Smoother transition between activity.
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}});
    }




}
