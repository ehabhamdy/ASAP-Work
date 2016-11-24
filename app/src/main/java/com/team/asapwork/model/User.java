package com.team.asapwork.model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ehab on 6/14/16.
 */


@IgnoreExtraProperties
public class User {

    public String username;
    public String photoUrl;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String photoUrl) {
        this.username = username;
        this.photoUrl = photoUrl;
    }

}