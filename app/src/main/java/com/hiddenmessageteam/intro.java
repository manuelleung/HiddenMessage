package com.hiddenmessageteam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class intro extends AppCompatActivity {

    private Intent goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        //Initialize intent
        goHome=new Intent(this,MainActivity.class);
        // Initialize id
        final ImageView hm= (ImageView) findViewById(R.id.hmlogo);
        final ImageView wc= (ImageView) findViewById(R.id.wclogo);
        //Initialize Animation though res-anim-xml.
        final Animation animation_wait= AnimationUtils.loadAnimation(getBaseContext(), R.anim.wait);
        final Animation animation_in= AnimationUtils.loadAnimation(getBaseContext(),R.anim.fadein);
        final Animation animation_out= AnimationUtils.loadAnimation(getBaseContext(),R.anim.fadeout);

        wc.startAnimation(animation_wait); // Wc logo will flash for 5 sce.
        wc.startAnimation(animation_out); //Wc lofo will start to fade out.
        //Setting a listener for wc sceond animation.
        animation_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                hm.startAnimation(animation_in);
            }
            @Override
            //When the second animation end, go to Home Activity
            public void onAnimationEnd(Animation animation) {
                wc.setAlpha(1);
                finish();
                startActivity(goHome);
                overridePendingTransition(0, 0); //Smoother transition between activity.
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
}
