package com.hiddenmessageteam.SignIn;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.R;

public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    int[] promo;
    LayoutInflater inflater;

    public ViewPagerAdapter(Context context,int[] promo) {

        this.context = context;

        this.promo = promo;

    }

    @Override
    public int getCount() {
        return promo.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables

        ImageView imgPromo;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,
                false);



        // Locate the ImageView in viewpager_item.xml
        imgPromo = (ImageView) itemView.findViewById(R.id.promoImage);
        // Capture position and set to the ImageView


        imgPromo.setImageResource(promo[position]);

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}