package com.example.rodri.rchat.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodri.rchat.R;
import com.example.rodri.rchat.classes.Friend;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by rodri on 4/26/2016.
 */
public class LoginActivity extends Activity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btLogin;
    private Firebase loginFirebaseRef;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initialize();


        //loginFirebaseRef.unauth();

        loginFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    userEmail = ((String) authData.getProviderData().get("email"));
                    Intent showFriends = new Intent(LoginActivity.this, FriendsActivity.class);
                    showFriends.putExtra("USER_EMAIL", userEmail);
                    startActivity(showFriends);
                    finish();
                } else {
                    userEmail = null;
                }

            }
        });


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final String pass = etPassword.getText().toString();

                loginFirebaseRef.createUser(email, pass, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        loginFirebaseRef.authWithPassword(email, pass, null);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        loginFirebaseRef.authWithPassword(email, pass, null);
                        //Toast.makeText(LoginActivity.this, "Try again", Toast.LENGTH_LONG).show();
                    }
                });

                /**loginFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(AuthData authData) {
                        if (authData != null) {
                            userEmail = ((String) authData.getProviderData().get("email"));
                            Intent showFriends = new Intent(LoginActivity.this, FriendsActivity.class);
                            showFriends.putExtra("USER_EMAIL", userEmail);
                            startActivity(showFriends);
                            finish();
                        } else {
                            userEmail = null;
                        }

                    }
                });*/

            }
        });

    }

    public void initialize() {
        etEmail = (EditText) findViewById(R.id.txtEmail);
        etPassword = (EditText) findViewById(R.id.txtPassword);
        btLogin = (Button) findViewById(R.id.btLogin);

        loginFirebaseRef = new Firebase("https://r-chat.firebaseio.com/");

    }
}
