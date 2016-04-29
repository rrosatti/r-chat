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

import com.example.rodri.rchat.classes.Message;
import com.example.rodri.rchat.R;
import com.example.rodri.rchat.ui.adapter.FriendsFirebaseListAdapter;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class MainActivity extends ListActivity {

    private Firebase messagesFirebaseRef;
    private Firebase userFirebaseRef;
    EditText etMessage;
    Button btSendMessage;
    Button btLogin;
    Button btLogout;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();


        btSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username != null) {
                    String message = etMessage.getText().toString();
                    //messagesFirebaseRef.push().setValue(new Message(username, message));
                    etMessage.setText("");
                }

            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
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

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesFirebaseRef.unauth();
                findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
                findViewById(R.id.logoutButton).setVisibility(View.INVISIBLE);
            }
        });

        FriendsFirebaseListAdapter messagesListAdapter = new FriendsFirebaseListAdapter<Message>(messagesFirebaseRef, Message.class, R.layout.message_layout, this) {

            @Override
            protected void populateView(View v, Message model) {
                //((TextView)v.findViewById(R.id.authorTextView)).setText(model.getName());
                ((TextView)v.findViewById(R.id.txtMessage)).setText(model.getMessage());
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

        etMessage = (EditText) findViewById(R.id.messageEditText);
        btSendMessage = (Button) findViewById(R.id.sendMessageButton);
        btLogin = (Button) findViewById(R.id.loginButton);
        btLogout = (Button) findViewById(R.id.logoutButton);
    }
}
