package com.hiddenmessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hiddenmessage.database.MessageModel;
import com.hiddenmessage.database.MessageType;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        final EditText name = (EditText) findViewById(R.id.name);

        final EditText title = (EditText) findViewById(R.id.title);

        final EditText content = (EditText) findViewById(R.id.content);



        Button post = (Button) findViewById(R.id.post);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MessageModel testMessage = new MessageModel();
                testMessage.setName(name.getText().toString());
                testMessage.setTitle(title.getText().toString());
                testMessage.setContent(content.getText().toString());
                testMessage.setType(MessageType.WORLD);
                testMessage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getApplicationContext(), "Success, message was uploaded!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }


}
