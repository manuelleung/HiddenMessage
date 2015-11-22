package com.hiddenmessageteam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class intro extends AppCompatActivity {

    private Intent goHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        goHome=new Intent(this,HomeActivity.class);
        final ImageView hm= (ImageView) findViewById(R.id.hmlogo);
        final ImageView wc= (ImageView) findViewById(R.id.wclogo);
        final Animation animation_wait= AnimationUtils.loadAnimation(getBaseContext(), R.anim.wait);
        final Animation animation_in= AnimationUtils.loadAnimation(getBaseContext(),R.anim.fadein);
        final Animation animation_out= AnimationUtils.loadAnimation(getBaseContext(),R.anim.fadeout);
        wc.startAnimation(animation_wait);
        wc.startAnimation(animation_out);
        animation_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                hm.startAnimation(animation_in);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                wc.setAlpha(1);
                finish();
                startActivity(goHome);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

}
