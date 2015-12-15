package com.hiddenmessageteam.friendlist;

/**
 * Created by Manuel on 12/14/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiddenmessageteam.R;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List<FriendInfo> friendList;

    private Context context;

    public FriendsAdapter(List<FriendInfo> friendList, Context context) {
        this.friendList = friendList;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return friendList.size();
    }



    @Override
    public void onBindViewHolder(FriendsViewHolder friendsViewHolder, int i) {
        FriendInfo ci = friendList.get(i);
        friendsViewHolder.vName.setText(ci.name);
        friendsViewHolder.vEmail.setText(ci.email);
        if(ci.profilePicture!=null) {
            friendsViewHolder.vProfilePicture.setImageBitmap(ci.profilePicture);
        }
        if(ci.status.equals("pending")) {
            friendsViewHolder.vStatusText.setText("Pending");
            friendsViewHolder.vStatusText.setVisibility(View.VISIBLE);
        }else if(ci.status.equals("accept")) {
            friendsViewHolder.vStatusButton.setText("Accept");
            friendsViewHolder.vStatusButton.setVisibility(View.VISIBLE);
        } else if(ci.status.equals("friend")) {
            // do nothing its already a friend
        }
    }


    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_card_friends, viewGroup, false);


        return new FriendsViewHolder(itemView);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vEmail;
        protected ImageView vProfilePicture;
        protected TextView vStatusText;
        protected Button vStatusButton;

        public FriendsViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.name);
            vEmail = (TextView)  v.findViewById(R.id.email);
            vProfilePicture = (ImageView) v.findViewById(R.id.profile_pic);
            vStatusText = (TextView) v.findViewById(R.id.status_text);
            vStatusButton = (Button) v.findViewById(R.id.status_button);
        }
    }
}