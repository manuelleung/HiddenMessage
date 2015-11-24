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

    public UserFunctions() {
        jsonParser = new JSONParser();
    }

    public JSONObject userLogin(String email, String password) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_LOGIN);
        params.put("email", email);
        params.put("password", password);
        JSONObject json = jsonParser.makeHttpRequest(URL_LOGIN, "POST", params);
        return json;
    }

    public JSONObject changePassword(String newpas, String email) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_CHANGE);
        params.put("newpas", newpas);
        params.put("email", email);
        JSONObject json = jsonParser.makeHttpRequest(URL_CHANGE, "POST", params);
        return json;
    }

    public JSONObject forgotPassword(String email) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_FORGOT);
        params.put("email", email);
        JSONObject json = jsonParser.makeHttpRequest(URL_FORGOT, "POST", params);
        return json;
    }

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

    public boolean userLogout(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

    public JSONObject postMessage(String name, String title, String content, String latitude, String longitude){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_NEWMESSAGE);
        params.put("name", name);
        params.put("title", title);
        params.put("content", content);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        JSONObject json = jsonParser.makeHttpRequest(URL_POST, "POST", params);
        return json;
    }

    public JSONObject retrieveAllMessages() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_RETRIEVE_ALL_MESSAGES);
        JSONObject json = jsonParser.makeHttpRequest(URL_RETRIEVE_MESSAGE, "POST", params);
        return json;
    }
}