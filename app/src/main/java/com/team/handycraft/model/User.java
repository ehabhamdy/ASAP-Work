package com.team.handycraft.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ehab on 6/14/16.
 */


@IgnoreExtraProperties
public class User {

    public String username;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username) {
        this.username = username;
    }

}