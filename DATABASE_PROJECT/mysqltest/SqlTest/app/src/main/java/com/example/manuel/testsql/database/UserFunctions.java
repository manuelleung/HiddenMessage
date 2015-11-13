package com.example.manuel.testsql.database;

/**
 * Created by Manuel on 10/30/2015.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import android.content.Context;


public class UserFunctions {
    private JSONParser jsonParser;

    /* LOCAL SERVER */
    /*
    private static String URL_LOGIN = "http://10.0.2.2:8080/hiddendb/";
    private static String URL_REGISTER = "http://10.0.2.2:8080/hiddendb/";
    private static String URL_FORGOT = "http://10.0.2.2:8080/hiddendb/";
    private static String URL_CHANGE = "http://10.0.2.2:8080/hiddendb/";
    */
    private static String URL_LOGIN = "http://hidden-message.me/webservice/index.php";
    private static String URL_REGISTER = "http://hidden-message.me/webservice/index.php";
    private static String URL_FORGOT = "http://hidden-message.me/webservice/index.php";
    private static String URL_CHANGE = "http://hidden-message.me/webservice/index.php";
    private static String URL_POST = "http://hidden-message.me/webservice/index.php";

    private static String TAG_LOGIN = "login";
    private static String TAG_REGISTER = "register";
    private static String TAG_FORGOT = "forgotpass";
    private static String TAG_CHANGE = "changepass";

    private static String TAG_NEWMESSAGE = "newmessage";

    public UserFunctions() {
        jsonParser = new JSONParser();
    }

    public JSONObject loginUser(String email, String password) {
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

    public JSONObject forgotPassword(String forgotpassword) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_FORGOT);
        params.put("forgotpassword", forgotpassword);
        JSONObject json = jsonParser.makeHttpRequest(URL_FORGOT, "POST", params);
        return json;
    }

    public JSONObject registerUser(String fname, String lname, String email, String uname, String password) {
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

    public boolean logoutUser(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

    public JSONObject postMessage(String name, String title, String content){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tag", TAG_NEWMESSAGE);
        params.put("name", name);
        params.put("title", title);
        params.put("content", content);
        JSONObject json = jsonParser.makeHttpRequest(URL_POST, "POST", params);
        return json;
    }
}
