package com.team.handycraft.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.handycraft.R;
import com.team.handycraft.model.Craftsman;

public class ActivityWorkerMain extends ActivityBase {

    Toolbar mToolbar;

    TextView name;
    TextView phone;
    TextView craft;
    TextView location;
    TextView mEmailTv;


    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        name = (TextView) findViewById(R.id.txtname);
        phone = (TextView) findViewById(R.id.txtmobile);
        craft = (TextView) findViewById(R.id.txtcraft);
        location = (TextView) findViewById(R.id.txtloca);
        mEmailTv = (TextView) findViewById(R.id.txtmail);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

            final String userId = getUid();

            final String email = user.getEmail();
            mDatabase.child("craftsmen").child(userId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            Craftsman worker = dataSnapshot.getValue(Craftsman.class);

                            name.setText(worker.username);
                            phone.setText(worker.phone);
                            craft.setText(worker.craft);
                            location.setText(worker.location);
                            mEmailTv.setText(email);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });
        } else{
            Intent intent = new Intent(ActivityWorkerMain.this, ActivityLogin.class);

            //Removing HomeActivity from the back stack
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, ActivityLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
