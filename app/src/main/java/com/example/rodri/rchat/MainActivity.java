package com.example.rodri.rchat;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ListActivity {

    private Firebase mRef;
    EditText messageEditText;
    Button sendMessageButton;
    Button loginButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        mRef = new Firebase("https://r-chat.firebaseio.com/");

        messageEditText = (EditText) findViewById(R.id.messageEditText);
        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        loginButton = (Button) findViewById(R.id.loginButton);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = messageEditText.getText().toString();
                mRef.push().setValue(new ChatMessage(username, message));
                messageEditText.setText("");

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage(R.string.login_message)
                        .setTitle(R.string.login_title);

                LayoutInflater inflater = getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_signin, null));

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog alertDialog = (AlertDialog) dialog;
                        final String email = ((TextView) alertDialog.findViewById(R.id.emailEditText)).getText().toString();
                        final String password = ((TextView) alertDialog.findViewById(R.id.passwordEditText)).getText().toString();

                        mRef.createUser(email, password, new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                                mRef.authWithPassword(email, password, null);
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                mRef.authWithPassword(email, password, null);
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }


        });

        FirebaseListAdapter mListAdapter = new FirebaseListAdapter<ChatMessage>(mRef, ChatMessage.class, R.layout.message_layout, this) {

            @Override
            protected void populateView(View v, ChatMessage model) {
                ((TextView)v.findViewById(R.id.authorTextView)).setText(model.getName());
                ((TextView)v.findViewById(R.id.messageTextView)).setText(model.getMessage());
            }

        };
        setListAdapter(mListAdapter);

        mRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if(authData != null) {
                    username = ((String)authData.getProviderData().get("email"));
                    findViewById(R.id.loginButton).setVisibility(View.INVISIBLE);
                } else {
                    username = null;
                    findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
                }
            }
        });


    }
}
