package com.example.rodri.rchat.ui.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodri.rchat.R;
import com.example.rodri.rchat.classes.Friend;
import com.example.rodri.rchat.ui.adapter.FriendsFirebaseListAdapter;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

/**
 * Created by rodri on 4/27/2016.
 */
public class FriendsActivity extends ListActivity {

    private Firebase friendsFirebaseRef;
    private Firebase usersFirebase;
    private Firebase ref;
    private FriendsFirebaseListAdapter<Friend> firebaseListAdapter;
    private String userEmail;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        //Toast.makeText(this, "user email: " + userEmail, Toast.LENGTH_LONG).show();

        Query friendsQuery = friendsFirebaseRef.orderByChild("userName").equalTo(userEmail);
        /**Firebase temp = new Firebase("https://r-chat.firebaseio.com/users/-KGHhOCmleAP-aMWbetq/name");
        String test = temp.push().toString();
        System.out.println("test: " + test);**/


        firebaseListAdapter = new FriendsFirebaseListAdapter<Friend>(friendsQuery, Friend.class, R.layout.friend_layout, FriendsActivity.this) {
            @Override
            protected void populateView(View v, Friend model) {
                //userName = getUserNameFromFirebase(model.getFriendName());
                ((TextView) v.findViewById(R.id.txtFriendName)).setText(model.getFriendName());
            }
        };
        setListAdapter(firebaseListAdapter);

    }

    public void initialize() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userEmail = extras.getString("USER_EMAIL");
        }

        friendsFirebaseRef = new Firebase("https://r-chat.firebaseio.com/friends");
        usersFirebase = new Firebase("https://r-chat.firebaseio.com/users");
        ref = new Firebase("https://r-chat.firebaseio.com/");
    }

    public String getUserNameFromFirebase(String userEmail) {
        Query userQuery = usersFirebase.orderByChild("email").equalTo(userEmail);
        /**ref.child("users/email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //User user = dataSnapshot.getValue(User.class);
                Toast.makeText(FriendsActivity.this, "WTF!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String userKey = dataSnapshot.getKey();
                ref.child("users/" + userKey + "/name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userName = dataSnapshot.getValue(String.class);
                        Toast.makeText(FriendsActivity.this, userName, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Toast.makeText(FriendsActivity.this, "before return: " + userName, Toast.LENGTH_LONG).show();

        return userName;
    }
}
