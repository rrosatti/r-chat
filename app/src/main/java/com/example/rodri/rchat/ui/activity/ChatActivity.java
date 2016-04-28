package com.example.rodri.rchat.ui.activity;

import android.app.ListActivity;
import android.os.Bundle;

import com.example.rodri.rchat.R;
import com.firebase.client.Firebase;

/**
 * Created by rodri on 4/28/2016.
 */
public class ChatActivity extends ListActivity {

    private Firebase messagesFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        initialize();



    }

    public void initialize() {
        messagesFirebaseRef = new Firebase("https://r-chat.firebaseio.com/messages");
    }
}
