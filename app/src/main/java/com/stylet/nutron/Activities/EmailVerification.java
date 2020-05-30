package com.stylet.nutron.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stylet.nutron.R;

public class EmailVerification extends AppCompatActivity {

    private Button refresh;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        refresh = findViewById(R.id.refresh);
        mAuth = FirebaseAuth.getInstance();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    if (user.isEmailVerified()) {
                        if (user.getDisplayName() != null && user.getPhotoUrl() != null) {
                            Intent main = new Intent(EmailVerification.this, MainActivity.class);
                            startActivity(main);
                        } else {
                            Intent toSetup = new Intent(EmailVerification.this, SetUpActivity.class);
                            startActivity(toSetup);
                        }
                    }else {
                        Toast.makeText(EmailVerification.this, "Email not verified yet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                if (user.getDisplayName() != null && user.getPhotoUrl() != null) {
                    Intent main = new Intent(EmailVerification.this, MainActivity.class);
                    startActivity(main);
                } else {
                    Intent toSetup = new Intent(EmailVerification.this, SetUpActivity.class);
                    startActivity(toSetup);
                }
            }
        }
    }
}
