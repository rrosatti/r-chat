package com.example.rodri.rchat.ui.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodri.rchat.R;
import com.example.rodri.rchat.classes.Message;
import com.example.rodri.rchat.ui.adapter.MessagesFirebaseListAdapter;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

/**
 * Created by rodri on 4/28/2016.
 */
public class ChatActivity extends ListActivity {

    private Firebase messagesFirebaseRef;
    private Firebase firebaseRef;
    private Query test;
    private Query messagesQuery;
    private MessagesFirebaseListAdapter messagesFirebaseListAdapter;
    private Bundle extras;
    private String friendEmail = null;
    private String userEmail;
    private int friendMessageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        initialize();

        messagesFirebaseListAdapter = new MessagesFirebaseListAdapter<Message>(messagesFirebaseRef, Message.class, R.layout.message_layout, this) {
            @Override
            protected void populateView(View v, Message model) {
                if (model.getUserFrom().toString() == friendEmail) {
                    Toast.makeText(ChatActivity.this, "friend email: " + friendEmail, Toast.LENGTH_LONG).show();
                    ((TextView) v.findViewById(R.id.txtMessage)).setTextAppearance(ChatActivity.this, friendMessageLayout);
                } else {
                    ((TextView) v.findViewById(R.id.txtMessage)).setText(model.getMessage());
                }
            }
        };
        setListAdapter(messagesFirebaseListAdapter);


    }

    public void initialize() {
        messagesFirebaseRef = new Firebase("https://r-chat.firebaseio.com/messages");
        firebaseRef = new Firebase("https://r-chat.firebaseio.com/");

        extras = getIntent().getExtras();
        if (extras != null) {
            friendEmail = extras.getString("FRIEND_EMAIL");
        }

        firebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    userEmail = ((String) authData.getProviderData().get("email"));
                } else {
                    userEmail = null;
                }

            }
        });

        messagesQuery = messagesFirebaseRef.orderByChild("userFrom").equalTo(userEmail);
        messagesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        friendMessageLayout = R.style.FriendMessage;

    }



}
