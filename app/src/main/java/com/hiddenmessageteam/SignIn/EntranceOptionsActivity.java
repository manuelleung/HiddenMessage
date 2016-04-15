package com.hiddenmessageteam.SignIn;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hiddenmessageteam.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class EntranceOptionsActivity extends AppCompatActivity {

    // Declare Variables
    private ViewPager entrancePager;
    private PagerAdapter adapter;
    private CirclePageIndicator circlePageIndicator;

    protected int [] promo;
    private Timer timer;
    private int page = 0;
    private Button entranceLogin;
    private Button entranceSignup;
    private Intent goLogin;
    private Intent goSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Links to activity_entrance_options (xml)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance_options);

        //
        entranceLogin =(Button) findViewById(R.id.entranceLogin);
        entranceSignup=(Button) findViewById(R.id.entranceSignup);
        promo= new int[] {R.drawable.friendandi,R.drawable.momandher,R.drawable.herandstreet};
        entrancePager =(ViewPager) findViewById(R.id.entrancePager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.entranceIndicator);
        goLogin = new Intent(this, MainActivity.class);
        goSignup= new Intent(this,RegisterActivity.class);

        //Goes to Login activity
        entranceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goLogin);

            }
        });
        //Goes to Sign up activity
        entranceSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goSignup);
            }
        });

        //Send the necessary info to the adapter class.
        adapter=new ViewPagerAdapter(EntranceOptionsActivity.this,promo);

        // Binds the Adapter to the ViewPager
        entrancePager.setAdapter(adapter);

        // ViewPager Indicator
        circlePageIndicator.setViewPager(entrancePager);

        //
        pageSwitcher(5);

    }
    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay

    }
    class RemindTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (page < adapter.getCount()) {
                        entrancePager.setCurrentItem(page++, true);
                    } else {
                        page=0;
                    }
                }
            });
        }
    }
}
