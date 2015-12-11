package com.hiddenmessageteam;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageBox extends LinearLayout {
/*************************************
           Data Field
*************************************/
    
    private TextView titleOfMessage;
    private TextView contentOfMessage;
    
    
    
    
    
    
/*************************************
           Constructor
*************************************/
    public MessageBox(Context context) {
        super(context);
        // Initialize titleOfMessage
        titleOfMessage = new TextView(context);
        titleOfMessage.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        // Initialize contentOfMessage
        contentOfMessage = new TextView(context);
        contentOfMessage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        // Layout preference:
        setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        setOrientation(LinearLayout.VERTICAL);
        
        addView(titleOfMessage);
        addView(contentOfMessage);
    }
    
    
    
    
    
/*************************************
             Mutators
*************************************/
    public void setTitleOfMessage(String titleOfMessage) {
        this.titleOfMessage.setText(titleOfMessage);
    }
    
    public void setContentOfMessage(String contentOfMessage) {
        this.contentOfMessage.setText(contentOfMessage);
    }
  
    
    
    
    
/*************************************
            Acessors
*************************************/
    public String getTitleOfMessage() {
        return this.titleOfMessage.getText().toString();
    }
    
    public String getContentOfMessage() {
        return this.contentOfMessage.getText().toString();
    }   
    
    
    
}   