package com.hiddenmessageteam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;
import java.util.HashMap;

/**
 * Created by Manuel on 10/30/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "local_admin_webservice";

    private static final String TABLE_LOGIN = "login";

    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "fname";
    private static final String KEY_LASTNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "uname";
    private static final String KEY_UID = "user_id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_PROFILE_PIC = "profile_pic";

    /**
     * Constructor
     * */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create table
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRSTNAME + " TEXT,"
                + KEY_LASTNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_USERNAME + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT,"
                + KEY_PROFILE_PIC + " BLOB" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    /**
     * Drop table and Re Create it
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        //remake
        onCreate(db);
    }

    /**
     * Add user row to table
     * */
    public void addUser(String fname, String lname, String email, String username, String user_id, String created_at) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_FIRSTNAME, fname);
        values.put(KEY_LASTNAME, lname);
        values.put(KEY_EMAIL, email);
        values.put(KEY_USERNAME, username);
        values.put(KEY_UID, user_id);
        values.put(KEY_CREATED_AT, created_at);
        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }

    public void addProfilePic(byte[] profile_pic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PROFILE_PIC, profile_pic);
        db.update(TABLE_LOGIN, values, null, null);
        //db.insert(TABLE_LOGIN, null, values);
        db.close();
    }

    public byte[] getProfilePic() {
        String selectQuery = "SELECT "+KEY_PROFILE_PIC+" FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        byte[] result = cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_PROFILE_PIC));
        cursor.close();
        db.close();
        return result;
    }

    /**
     * Get user data
     * return hashmap of each column
     * */
    public HashMap getUserDetails() {
        HashMap user = new HashMap();
        String selectQuery = "SELECT * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            user.put("fname", cursor.getString(1));
            user.put("lname", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("uname", cursor.getString(4));
            user.put("user_id", cursor.getString(5));
            user.put("created_at", cursor.getString(6));
        }
        cursor.close();
        db.close();
        return user;
    }

    /**
     * Getting user login status
     * return true if there is a user row in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        return rowCount;
    }


    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}
