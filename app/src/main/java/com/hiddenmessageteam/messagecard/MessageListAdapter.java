package com.hiddenmessageteam.messagecard;

/**
 * Created by Manuel on 12/14/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.R;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    private List<MessageInfo> messageList;

    private static Context context;

    private JSONObject messagesObj;

    private HashMap<String, String> user_id_array = new HashMap<String, String>();
    private HashMap<String, String> message_id_array = new HashMap<String, String>();



    public MessageListAdapter(List<MessageInfo> messageList, JSONObject messagesObj, Context context) {
        this.messageList = messageList;
        this.context = context;
        this.messagesObj = messagesObj;

    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public HashMap<String, String> getUserIdArray() {
        return user_id_array;
    }
    public HashMap<String, String> getMessageIdArray() {
        return message_id_array;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder messageViewHolder, int i) {
        final MessageInfo ci = messageList.get(i);

        messageViewHolder.vTitle.setText(ci.title);
        messageViewHolder.vAddress.setText(ci.address);
        messageViewHolder.vContent.setText(ci.content);

        messageViewHolder.vCheckbox.setOnCheckedChangeListener(null);
        messageViewHolder.vCheckbox.setChecked(ci.getChecked());
        messageViewHolder.vCheckbox.setId(ci.getId());

        messageViewHolder.vCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ci.setChecked(true);

                    try {
                        user_id_array.put(buttonView.getId()+"", messagesObj.getJSONObject(buttonView.getId()+"").getString("user_id"));
                        message_id_array.put(buttonView.getId()+"", messagesObj.getJSONObject(buttonView.getId() + "").getString("message_id"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    ci.setChecked(false);

                    user_id_array.remove(buttonView.getId() + "");
                    message_id_array.remove(buttonView.getId() + "");
                }
            }
        });


    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_card_message, viewGroup, false);

        return new MessageViewHolder(itemView);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        protected TextView vContent;
        protected TextView vTitle;
        protected TextView vAddress;
        protected CheckBox vCheckbox;

        public MessageViewHolder(View v) {
            super(v);

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            vTitle = (TextView) v.findViewById(R.id.title);
            vAddress = (TextView) v.findViewById(R.id.address);
            vContent = (TextView) v.findViewById(R.id.content);
            vCheckbox = (CheckBox) v.findViewById(R.id.checkbox);
        }
    }
}