package com.example.rodri.rchat;

import com.firebase.client.Firebase;

/**
 * Created by rodri on 4/27/2016.
 */
public class FirebaseApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
