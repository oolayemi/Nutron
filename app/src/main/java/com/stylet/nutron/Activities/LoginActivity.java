package com.stylet.nutron.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stylet.nutron.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private EditText mEmail, mPassword;
    private Button mBtn;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mBtn = findViewById(R.id.signinbutton);
        mProgressBar = findViewById(R.id.loginprogressBar);


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                if (!mEmail.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        hideDialog();
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            if (!user.isEmailVerified()) {
                                                sendEmailVerification(user);
                                                finish();
                                            } else {
                                                if (user.getDisplayName() != null && user.getPhotoUrl() != null) {
                                                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(main);
                                                    showMessage("Login successful");
                                                    finish();
                                                } else {
                                                    Intent toSetup = new Intent(LoginActivity.this, SetUpActivity.class);
                                                    startActivity(toSetup);
                                                    finish();
                                                }
                                                /*if (user.getDisplayName() != null && user.getPhotoUrl() != null) {
                                                    Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(toMain);

                                                    finish();
                                                }*/
                                            }
                                        }
                                    } else {
                                        hideDialog();
                                        showMessage("Login failed: " + task.getException().getMessage());
                                    }
                                }
                            });
                }
                else {
                    hideDialog();
                    showMessage("Input all fields");
                }
            }
        });
        hideSoftKeyboard();

    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private boolean doStringsMatch(String s1, String s2) {
        return s1.equals(s2);
    }
    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog(){
        if (mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void sendEmailVerification(FirebaseUser user){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showMessage("Sent verification mail");
                                Intent verify = new Intent(LoginActivity.this, EmailVerification.class);
                                startActivity(verify);
                            } else {
                                showMessage("Couldn't send verification mail: " + task.getException().getMessage());
                            }
                        }
                    });

    }
}
