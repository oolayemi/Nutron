package com.stylet.nutron.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stylet.nutron.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText suEmail, suPassword, suConfirmPassword;
    private Button suBtn;
    private TextView login;
    private ProgressBar suProgressBar;

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        suEmail = findViewById(R.id.signupemail);
        suPassword = findViewById(R.id.signuppassword);
        suConfirmPassword = findViewById(R.id.signupconfirmpassword);
        login = findViewById(R.id.login);
        suProgressBar = findViewById(R.id.signupprogressBar);

        suBtn = findViewById(R.id.signupbutton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        suBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Log.d(TAG, "onClick: attempting to sign up");

                if (!suEmail.getText().toString().isEmpty()
                    && !suPassword.getText().toString().isEmpty()
                    && !suConfirmPassword.getText().toString().isEmpty()){

                        if (doStringsMatch(suPassword.getText().toString(), suConfirmPassword.getText().toString())){
                            showDialog();
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(suEmail.getText().toString(), suPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        hideDialog();
                                        /*Snackbar.make(v, "Account has been created with " + suEmail.getText().toString() +". Pls proceed to verify your account", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();*/
                                        sendEmailVerification();
                                        finish();
                                    }else {
                                        hideDialog();
                                        showMessage("Unable to create account: " + task.getException().getMessage());

                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                else {
                    Toast.makeText(RegisterActivity.this, "You must fill out all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        hideSoftKeyboard();

    }

    private void sendEmailVerification(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //showMessage("Sent verification mail");
                                Intent verify = new Intent(RegisterActivity.this, EmailVerification.class);
                                startActivity(verify);
                            } else {
                                showMessage("Couldn't send verification mail: " + task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    private void registerNewEmail(String email, String password){
        showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "onComplete: " + task.isSuccessful());

                if (task.isSuccessful()){
                    //Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    showMessage("onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    sendEmailVerification();
                    Intent setup = new Intent(RegisterActivity.this, EmailVerification.class);
                    //startActivity(setup);
                    finish();
                    FirebaseAuth.getInstance().signOut();
                }else {
                    Toast.makeText(RegisterActivity.this, "Unable to register", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        });
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private boolean doStringsMatch(String s1, String s2) {
        return s1.equals(s2);
    }
    private void showDialog(){
        suProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog(){
        if (suProgressBar.getVisibility() == View.VISIBLE){
                suProgressBar.setVisibility(View.INVISIBLE);
        }
    }


}
