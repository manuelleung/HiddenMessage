package com.hiddenmessageteam.messagecard;

/**
 * Created by Manuel on 12/14/2015.
 */

public class MessageInfo {
    protected String title;
    protected String content;
    protected String address;
    protected int id;
    protected boolean checked;

    void setChecked(boolean b) {
        checked = b;
    }
    boolean getChecked() {
        return checked;
    }
    int getId() {
        return id;
    }
    void setId(int id) {
        this.id = id;
    }
}
