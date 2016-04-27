package com.example.rodri.rchat.ui.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodri.rchat.R;
import com.example.rodri.rchat.classes.Friend;
import com.example.rodri.rchat.ui.adapter.FirebaseListAdapter;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

/**
 * Created by rodri on 4/27/2016.
 */
public class FriendsActivity extends ListActivity {

    private Firebase friendsFirebaseRef;
    private FirebaseListAdapter<Friend> firebaseListAdapter;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        Toast.makeText(this, "user email: " + userEmail, Toast.LENGTH_LONG).show();

    }

    public void initialize() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userEmail = extras.getString("USER_EMAIL");
        }

        friendsFirebaseRef = new Firebase("https://r-chat.firebaseio.com/friends");

        Query friendsQuery = friendsFirebaseRef.orderByChild("friend").equalTo(userEmail);

        firebaseListAdapter = new FirebaseListAdapter<Friend>(friendsFirebaseRef, Friend.class, R.layout.friend_layout, FriendsActivity.this) {
            @Override
            protected void populateView(View v, Friend model) {
                ((TextView) v.findViewById(R.id.txtFriendName)).setText(model.getFriendName());
            }
        };
    }
}
