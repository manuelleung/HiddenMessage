package com.hiddenmessageteam.database;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Manuel on 11/18/2015.
 */
public class UserFunctions {
    private JSONParser jsonParser;

    /* LOCAL SERVER */
    /*
    private static String URL_LOGIN = "http://10.0.2.2:8080/hiddendb/";
    //private static String URL_REGISTER = "http://10.0.2.2:8080/hiddendb/";
    private static String URL_FORGOT = "http://10.0.2.2:8080/hiddendb/";
    private static String URL_CHANGE = "http://10.0.2.2:8080/hiddendb/";
    private static String URL_POST = "http://10.0.2.2:8080/hiddendb/";
    private static String URL_RETRIEVE_MESSAGE = "http://10.0.2.2:8080/hiddendb/";
    */
    /* REMOTE SERVER */

    private static String URL_LOGIN = "http://hidden-message.me/webservice/index.php";
    private static String URL_REGISTER = "http://hidden-message.me/webservice/index.php";
    private static String URL_FORGOT = "http://hidden-message.me/webservice/index.php";
    private static String URL_CHANGE = "http://hidden-message.me/webservice/index.php";
    private static String URL_POST = "http://hidden-message.me/webservice/index.php";
    private static String URL_RETRIEVE_MESSAGE = "http://hidden-message.me/webservice/index.php";

    private static String TAG_LOGIN = "login";
    private static String TAG_REGISTER = "register";
    private static String TAG_FORGOT = "forgotpass";
    private static String TAG_CHANGE = "changepass";
    private static String TAG_NEWMESSAGE = "newmessage";
    private static String TAG_RETRIEVE_ALL_MESSAGES = "allmessages";
    private static String TAG_RETRIEVE_MY_MESSAGES = "mymessages";
    private static String TAG_DELETE_MESSAGE = "deletemessage";

    /**
     * Constructor
     * */
    public UserFunctions() {
        jsonParser = new JSONParser();
    }

    /**
     * Makes a request to login
     * passes tag, email, password to php
     * returns JSONObject from php
     * */
    public JSONObject userLogin(String email, String password) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_LOGIN);
        params.put("email", email);
        params.put("password", password);
        JSONObject json = jsonParser.makeHttpRequest(URL_LOGIN, "POST", params);
        return json;
    }

    /**
     * Makes a request to change password
     * passes tag, email, new password to php
     * returns JSONObject from php
     * */
    public JSONObject changePassword(String newpas, String email) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_CHANGE);
        params.put("newpas", newpas);
        params.put("email", email);
        JSONObject json = jsonParser.makeHttpRequest(URL_CHANGE, "POST", params);
        return json;
    }

    /**
     * Makes a request to forgot password
     * passes tag, email to php
     * returns JSONObject from php
     * */
    public JSONObject forgotPassword(String email) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_FORGOT);
        params.put("email", email);
        JSONObject json = jsonParser.makeHttpRequest(URL_FORGOT, "POST", params);
        return json;
    }

    /**
     * Makes a request to register
     * passes tag, first name, last name, email, username, password to php
     * returns JSONObject from php
     * */
    public JSONObject userRegister(String fname, String lname, String email, String uname, String password) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_REGISTER);
        params.put("fname", fname);
        params.put("lname", lname);
        params.put("email", email);
        params.put("uname", uname);
        params.put("password", password);
        JSONObject json = jsonParser.makeHttpRequest(URL_REGISTER, "POST", params);
        return json;
    }

    /**
     * Clears the user row from local db
     * returns boolean
     * */
    public boolean userLogout(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

    /**
     * Makes a request to post message
     * passes tag, userid, title, content, lat, long to php
     * returns JSONObject from php
     * */
    public JSONObject postMessage(String user_id, String title, String content, String latitude, String longitude){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_NEWMESSAGE);
        params.put("user_id", user_id);
        params.put("title", title);
        params.put("content", content);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        JSONObject json = jsonParser.makeHttpRequest(URL_POST, "POST", params);
        return json;
    }

    /**
     * Makes a request to retrieve all messages
     * passes tag to php
     * returns JSONObject from php
     * */
    public JSONObject retrieveAllMessages() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_RETRIEVE_ALL_MESSAGES);
        JSONObject json = jsonParser.makeHttpRequest(URL_RETRIEVE_MESSAGE, "POST", params);
        return json;
    }

    /**
     * Makes a request to retrieve only my messages
     * passes tag, userid to php
     * returns JSONObject from php
     * */
    public JSONObject retrieveMyMessages(String user_id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_RETRIEVE_MY_MESSAGES);
        params.put("user_id", user_id);
        JSONObject json = jsonParser.makeHttpRequest(URL_RETRIEVE_MESSAGE, "POST", params);
        return json;
    }

    /**
     * Makes a request to delete
     * passes tag, userid, messageid to php
     * returns JSONObject from php
     * */
    public JSONObject deleteMessage(String user_id, String message_id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_DELETE_MESSAGE);
        params.put("user_id", user_id);
        params.put("message_id", message_id);
        JSONObject json = jsonParser.makeHttpRequest(URL_RETRIEVE_MESSAGE, "POST", params);
        return json;
    }
}