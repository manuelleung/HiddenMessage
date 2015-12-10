package com.hiddenmessageteam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddenmessageteam.database.DatabaseHandler;
import com.hiddenmessageteam.database.NetworkCheck;
import com.hiddenmessageteam.database.UserFunctions;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity implements NetworkCheck.OnTaskCompleted{

    private final static String KEY_SUCCESS = "success";
    private final static String KEY_ERROR = "error";


    ImageView setprofilepic;
    private static final int CAM_REQUEST=1313;
    Bitmap thumbnail;

    String email;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().show();
        }

        db = new DatabaseHandler(this);
        HashMap userDetails = db.getUserDetails();
        String firstName = userDetails.get("fname").toString();
        String lastName = userDetails.get("lname").toString();
        email = userDetails.get("email").toString();

        setprofilepic=(ImageView) findViewById(R.id.profilepic);
        setProfilePic();


        TextView profileName = (TextView) findViewById(R.id.profile_name);
        profileName.setText(firstName.substring(0,1).toUpperCase()+firstName.substring(1)+" "+lastName.substring(0,1).toUpperCase()+lastName.substring(1));

        TextView profileEmail = (TextView) findViewById(R.id.profile_email);
        profileEmail.setText(email);

        Button save = (Button) findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload image
                if(thumbnail!=null) {
                    NetworkCheck checkConnection = new NetworkCheck(getApplicationContext(), EditProfileActivity.this);
                    checkConnection.netAsync();
                }
            }
        });
    }

    public void setProfilePic() {
        if(db.getProfilePic()!=null) {
            byte[] b = db.getProfilePic();
            Bitmap decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length);
            //android.view.ViewGroup.LayoutParams layoutParams = profilePic.getLayoutParams();
            //layoutParams.width = 250;
            //layoutParams.height = 250;
            //profilePic.setLayoutParams(layoutParams);
            setprofilepic.setImageBitmap(decodedByte);

        }
    }


    //TEST RETRIEVEEEEEEEEEEEEEEEEEEEE////////////////////////////////////////////////////////////////////////TEST //////////////////////////////////////////////////////////////////////
    private class ProcessRetrieveImage extends AsyncTask<String, String, JSONObject> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(EditProfileActivity.this, "Uploading image", "Please wait", true, true);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.retrieveProfilePic(email);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            loading.dismiss();
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    String error = json.getString(KEY_ERROR);
                    String result = json.getString(KEY_SUCCESS);

                    // GOOD
                    if(Integer.parseInt(result)==1) {
                        //String encodedImage = json.getString("profile_pic");
                        //byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        //setprofilepic.setImageBitmap(decodedByte);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "could not retrieve image", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Error occured in retrieve image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //TEST ////////////////////////////////////////////////////////////////////////TEST //////////////////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    public void changeProfile(View v){
            Intent camerinter = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camerinter, CAM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQUEST && resultCode!=0) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            thumbnail = getRoundedShape(thumbnail);
            setprofilepic.setImageBitmap(thumbnail);
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        byte[] imageBytes = byteArray.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onConnCompleted(boolean conn) {
        if(conn) {
            new ProcessUploadImage().execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getRoundedShape(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    private class ProcessUploadImage extends AsyncTask<String, String, JSONObject> {
        ProgressDialog loading;
        String imageString;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageString = getStringImage(thumbnail);
            loading = ProgressDialog.show(EditProfileActivity.this, "Uploading image", "Please wait", true, true);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.uploadProfilePic(imageString, email);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            loading.dismiss();
            try {
                if(json.getString(KEY_SUCCESS)!=null) {
                    String error = json.getString(KEY_ERROR);
                    String result = json.getString(KEY_SUCCESS);

                    // GOOD
                    if(Integer.parseInt(result)==1) {
                        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
                        db.addProfilePic(decodedString);
                        //
                        Toast.makeText(getApplicationContext(), "Successfuly Uploaded", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Not Uploaded", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Error occured in upload", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
