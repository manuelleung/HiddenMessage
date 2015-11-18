package com.hiddenmessageteam.database;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by manuel on 10/21/2015.
 */

@ParseClassName("MessageModel")
public class MessageModel extends ParseObject {

    // Default constructor is required
    public MessageModel() {
        super();
    }

    // Constructor that contains core properties
    public MessageModel(String name, String title, String content, MessageType type) {
        super();
        setName(name);
        setTitle(title);
        setContent(content);
        setType(type);
        // No user accounts yet
        //setOwner(ParseUser.getCurrentUser());
    }

    public void setName(String name) {
        put("name", name);
    }

    // Get name
    public String getTitle() {
        return getString("title");
    }

    // Set name
    public void setTitle(String title) {
        put("title", title);
    }

    //
    public String getContent() {
        return getString("content");
    }

    // Set body
    public void setContent(String content) {
        put("content", content);
    }

    // Get the user for this item
    public ParseUser getOwner() {
        return getParseUser("owner");
    }

    // Associate each item with a user
    public void setOwner(ParseUser user) {
        put("owner", user);
    }

    // Set the type (WORLD or SCAVENGER)
    public MessageType getType() {
        return MessageType.valueOf(getString("type"));
    }

    // Get type
    public void setType(MessageType type) {
        put("type", type.toString());
    }
}
