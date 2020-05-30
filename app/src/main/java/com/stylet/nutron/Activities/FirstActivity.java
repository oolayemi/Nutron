package com.stylet.nutron.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stylet.nutron.R;

public class FirstActivity extends AppCompatActivity {

    private Button login, signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        login = findViewById(R.id.loginbutton);
        signup = findViewById(R.id.signupbutton2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendtoLogin = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(sendtoLogin);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendtoSignup = new Intent(FirstActivity.this, RegisterActivity.class);
                startActivity(sendtoSignup);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            if (user.isEmailVerified()){
                if (user.getDisplayName() != null && user.getPhotoUrl() != null){
                    Intent toMain = new Intent(FirstActivity.this, MainActivity.class);
                    startActivity(toMain);
                    finish();
                }else {
                    Intent toSetup = new Intent(FirstActivity.this, SetUpActivity.class);
                    startActivity(toSetup);
                    finish();
                }
            }else {
                Intent toLogin = new Intent(FirstActivity.this, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(toLogin);
                finish();
            }
        }
    }
}
