package com.team.handycraft;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends ActivityBase {

    private FirebaseAuth mAuth;
    private static final String TAG = "Message";
    protected TextView mSignUpTextView;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignUpTextView = (TextView) findViewById(R.id.sigupText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivitySignUp.class);
                startActivity(intent);
            }
        });


        mEmailField = (EditText) findViewById(R.id.editText);
        mPasswordField = (EditText) findViewById(R.id.editText2);

        btn = (Button) findViewById(R.id.button);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = mEmailField.getText().toString();
                String p = mPasswordField.getText().toString();
                login(e, p);
            }
        });


    }

    private void login(String email, String password) {

        //Check if the email or password fields are empty
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else{
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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
}
