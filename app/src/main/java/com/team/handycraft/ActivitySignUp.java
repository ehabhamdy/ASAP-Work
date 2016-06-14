package com.team.handycraft;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.handycraft.model.User;

public class ActivitySignUp extends ActivityBase {

    EditText mUsernameField;
    EditText mEmailField;
    EditText mPasswordField;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    private static final String TAG = "Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mEmailField = (EditText) findViewById(R.id.emailEditText);
        mPasswordField = (EditText) findViewById(R.id.passwordEditText);
        mUsernameField = (EditText) findViewById(R.id.usernameEditText);

        final Button signupBtn = (Button) findViewById(R.id.signupButton);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameField.getText().toString();
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();

                //create new user
                signUp(username, email, password);
                //Toast.makeText(SignUpActivity.this, email, Toast.LENGTH_SHORT).show();
                Log.i(TAG, email);

            }
        });




    }

    /*   @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }*/

    public void signUp(final String username, String email, String password){

        //Check if the email or password fields are empty
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser(), username);
                        }else {
                            Toast.makeText(ActivitySignUp.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    private void onAuthSuccess(FirebaseUser user, String username) {
        //String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username);

        // Go to MainActivity
        Intent intent = new Intent(ActivitySignUp.this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name) {
        User user = new User(name);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END basic_write]

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

}
