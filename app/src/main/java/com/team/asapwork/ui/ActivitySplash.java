package com.team.asapwork.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.asapwork.R;
import com.team.asapwork.model.User;
import com.team.asapwork.utils.Utils;


public class ActivitySplash extends ActivityBase {

    FirebaseDatabase mDatabase;
    DatabaseReference muser;
    static boolean calledAlready = false;
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    mDatabase = Utils.getDatabase();

                    muser = mDatabase.getReference().child("users").child(user.getUid());

                    if (user.isEmailVerified()) {

                        muser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if (user != null) {
                                    goToActivity(ActivityUserMain.class);
                                    //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                                } else {
                                    goToActivity(ActivityWorkerMain.class);
                                    //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(ActivitySplash.this, "canceled", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        goToActivity(ActivityLogin.class);
                    }
                } else {
                    // User signed out or No Network Connection
                    goToActivity(ActivityLogin.class);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }


            }
        }, SPLASH_TIME_OUT);


    }

    private void goToActivity(Class activity) {
        Intent intent = new Intent(ActivitySplash.this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}



