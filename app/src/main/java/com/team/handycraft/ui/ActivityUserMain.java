package com.team.handycraft.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.handycraft.R;
import com.team.handycraft.model.User;

public class ActivityUserMain extends ActivityBase {

    private static final String TAG = "Message";
    private FirebaseDatabase mDatabase;
    private TextView userTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        userTextView = (TextView) findViewById(R.id.usertextView);

        mDatabase = FirebaseDatabase.getInstance();

        //showProgressDialog();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            mDatabase.getReference().child("users").child(user.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);
                            //Toast.makeText(HomeActivity.this, user.username, Toast.LENGTH_SHORT).show();
                            userTextView.setText(user.username);

                            //Toast.makeText(ActivityUserMain.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                //Toast.makeText(ActivityUserMain.this, child.get, Toast.LENGTH_SHORT).show();
                            }
                            //hideProgressDialog();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });
        } else{
            Intent intent = new Intent(ActivityUserMain.this, ActivityLogin.class);

            //Removing HomeActivity from the back stack
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }

        FloatingActionButton newOrderBtn = (FloatingActionButton) findViewById(R.id.fab_new_order);

        newOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityUserMain.this, ActivityPlaceOrder.class));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, ActivityLogin.class));
                return true;
            case R.id.action_user_profile:
                // TODO: 6/21/16 implement user profile activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
