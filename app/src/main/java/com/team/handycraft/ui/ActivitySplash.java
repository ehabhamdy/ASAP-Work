package com.team.handycraft.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.handycraft.R;
import com.team.handycraft.model.User;
import com.team.handycraft.utils.Utils;


public class ActivitySplash extends ActivityBase {

    FirebaseDatabase mDatabase;
    DatabaseReference muser;
    static boolean calledAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mDatabase = Utils.getDatabase();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

            muser = mDatabase.getReference().child("users").child(user.getUid());

            muser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        Intent intent = new Intent(ActivitySplash.this, ActivityUserMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ActivitySplash.this, ActivityWorkerMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ActivitySplash.this, "canceled", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            // User is signed out
            Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);

            //Removing HomeActivity from the back stack
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

}



