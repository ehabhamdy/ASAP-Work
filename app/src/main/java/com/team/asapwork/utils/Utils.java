package com.team.asapwork.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ehab on 7/1/16.
 */
public class Utils {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
        }
        return mDatabase;
    }
}
