package com.team.asapwork.business;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ehab on 6/28/16.
 */
public class FirebaseCommunication {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    public FirebaseCommunication() {
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

}
