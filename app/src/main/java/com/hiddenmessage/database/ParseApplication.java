package com.hiddenmessage.database;

import android.app.Application;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by manuel on 10/21/2015.
 */
public class ParseApplication extends Application {
    public static final String APPLICATION_ID = "zCnzgrfqZOCjRucrnzyHv3yc6akrU1EF9cl19jDI";
    public static final String CLIENT_KEY = "VDko1czxZ1LiqshcknW4vnAqfXLHIqmuuxkKg9YU";

    /* Parse DB initialization
    **
    **/
    @Override
    public void onCreate() {
        super.onCreate();

        // Register parse models
        ParseObject.registerSubclass(MessageModel.class);

        // Initialize and register parse db
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Test sending to db
        // Uncomment to test again
        //messageSendTest();
    }

    /* Message to DB test
    ** Creates a message and saves it into parse db
     */
    public void messageSendTest() {
        // Sends a TEST object to Parse DB
        // MessageModel( "title", "content", MessageModel.Type )

        final MessageModel testMessage = new MessageModel();

        testMessage.setTitle("Ate at this McDonalds");
        testMessage.setContent("9.5/10 would eat again");
        testMessage.setType(MessageType.WORLD);
        // No user accounts yet
        //testMessage.setOwner(ParseUser.getCurrentUser());

        // Save message in db. Two different ways to save to db
        // 1. saveInBackground executes immediately
        // 2. saveEventually will store the update on the device and push to the server once internet access is available
        testMessage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getApplicationContext(), "Success, message was uploaded!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
