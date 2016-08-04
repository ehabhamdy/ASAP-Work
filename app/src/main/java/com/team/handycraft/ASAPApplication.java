package com.team.handycraft;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ehab on 8/4/16.
 */
public class ASAPApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
