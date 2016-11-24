package com.team.asapwork.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team.asapwork.R;
import com.team.asapwork.model.User;

/**
 * Created by Ehab on 11/20/2016.
 */

public class ActivityUserProfile extends ActivityBase {

    public static final int RC_PHOTO_PICKER = 2;

    Button mPhotoPickerButton;
    String downloadUri;
    String userName;
    ImageView mProfileImageView;
    TextView mUserNameTextView;

    FirebaseUser currentUser;
    String userId;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersReference;


    private FirebaseStorage mFirebaseStorage;
    private StorageReference mUserPhotosStorageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mFirebaseStorage = FirebaseStorage.getInstance();
        mUserPhotosStorageReference = mFirebaseStorage.getReference().child("user_photos");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersReference = mFirebaseDatabase.getReference().child("users");

        mPhotoPickerButton = (Button) findViewById(R.id.select_image_btn);
        mProfileImageView = (ImageView) findViewById(R.id.profile_image);
        mUserNameTextView = (TextView) findViewById(R.id.username_textview);

        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentUser.getUid();

        mUsersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName = user.username;
                mUserNameTextView.setText(user.username);
                if(user.photoUrl == null)
                    mProfileImageView.setImageResource(R.drawable.default_thumbnail);
                else
                    Glide.with(mProfileImageView.getContext()).load(user.photoUrl).into(mProfileImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            // get reference to store the file
            StorageReference photoRef = mUserPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
            // upload file to Firebase Storage
            photoRef.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUri = taskSnapshot.getDownloadUrl().toString();

                    User userUpdatedInfo = new User(userName, downloadUri);
                    mUsersReference.child(userId).setValue(userUpdatedInfo);

                    mUserNameTextView.setText(userName);
                    Glide.with(mProfileImageView.getContext()).load(downloadUri).into(mProfileImageView);
                }
            });
        }
    }

}
