package com.example.rodri.rchat.ui.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rodri.rchat.classes.ChatMessage;
import com.example.rodri.rchat.R;
import com.example.rodri.rchat.classes.User;
import com.example.rodri.rchat.ui.adapter.FirebaseListAdapter;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class MainActivity extends ListActivity {

    private Firebase messagesFirebaseRef;
    private Firebase userFirebaseRef;
    EditText messageEditText;
    Button sendMessageButton;
    Button loginButton;
    Button logoutButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        initialize();


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username != null) {
                    String message = messageEditText.getText().toString();
                    messagesFirebaseRef.push().setValue(new ChatMessage(username, message));
                    messageEditText.setText("");
                }

            }
        });

        userFirebaseRef.push().setValue(new User("Rodrigo", "r.rosatti@rchat.com"));
        userFirebaseRef.push().setValue(new User("Lipe", "l.zanelatto@rchat.com"));

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

                        messagesFirebaseRef.createUser(email, password, new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                                messagesFirebaseRef.authWithPassword(email, password, null);
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                messagesFirebaseRef.authWithPassword(email, password, null);
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }


        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesFirebaseRef.unauth();
                findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
                findViewById(R.id.logoutButton).setVisibility(View.INVISIBLE);
            }
        });

        FirebaseListAdapter messagesListAdapter = new FirebaseListAdapter<ChatMessage>(messagesFirebaseRef, ChatMessage.class, R.layout.message_layout, this) {

            @Override
            protected void populateView(View v, ChatMessage model) {
                ((TextView)v.findViewById(R.id.authorTextView)).setText(model.getName());
                ((TextView)v.findViewById(R.id.messageTextView)).setText(model.getMessage());
            }

        };
        setListAdapter(messagesListAdapter);

        messagesFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    username = ((String) authData.getProviderData().get("email"));
                    findViewById(R.id.loginButton).setVisibility(View.INVISIBLE);
                    findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
                } else {
                    username = null;
                    findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
                }
            }
        });


    }

    public void initialize() {
        messagesFirebaseRef = new Firebase("https://r-chat.firebaseio.com/messages");
        userFirebaseRef = new Firebase("https://r-chat.firebaseio.com/users");

        messageEditText = (EditText) findViewById(R.id.messageEditText);
        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);
    }
}
