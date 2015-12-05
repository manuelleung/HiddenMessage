package com.hiddenmessageteam;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.widget.RelativeLayout;

/**
 * Created by Leibniz H. Berihuete on 11/19/2015.
 */
public class BackgroundAnimation {
    private ValueAnimator anim;
    private int colorFrequency;
    private boolean reverseColor;


    public BackgroundAnimation(final RelativeLayout relativeLayout) {
        colorFrequency = 90;
        reverseColor = false;

        ValueAnimator anim = ValueAnimator.ofFloat(0,1).setDuration(100000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(colorFrequency >=200) {
                    reverseColor = true;
                }

                if(colorFrequency <=90) {
                    reverseColor = false;
                }

                if(reverseColor) {
                    colorFrequency--;
                }
                else {
                    colorFrequency++;
                }

                relativeLayout.setBackgroundColor(Color.argb(130, colorFrequency, 150, 150));

            }
        });

        //anim.setDuration(1);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();

    }




}
