package com.stylet.nutron.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stylet.nutron.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpActivity extends AppCompatActivity {

    TextView status;

    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private String user_id;

    private boolean isChanged = false;

    private EditText setupName;
    private ProgressBar setupProgress;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Setup");

        setupImage = findViewById(R.id.setup_image);
        setupName = findViewById(R.id.setup_name);
        Button setupBtn = findViewById(R.id.setup_btn);
        setupProgress = findViewById(R.id.setup_progress);

        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!setupName.getText().toString().isEmpty() && mainImageURI != null){
                    setupProgress.setVisibility(View.VISIBLE);
                    if (user != null) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(setupName.getText().toString())
                                .setPhotoUri(mainImageURI)
                                .build();

                        user.updateProfile(profileChangeRequest)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            storeFirestore(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString());
                                            setupProgress.setVisibility(View.INVISIBLE);
                                            Toast.makeText(SetUpActivity.this, "Patient settings updated successfully", Toast.LENGTH_SHORT).show();
                                            Intent toMain = new Intent(SetUpActivity.this, MainActivity.class);
                                            startActivity(toMain);
                                            finish();
                                        } else {
                                            Toast.makeText(SetUpActivity.this, "Patient settings not updated: " + task.getException().getMessage(), Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });



                    }
                }else {
                    Toast.makeText(SetUpActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(SetUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(SetUpActivity.this, "Awaiting permission...", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetUpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
            }
        });

    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                //.setMinCropResultSize(512, 512)
                .setAspectRatio(1, 1)
                .start(SetUpActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void storeFirestore(String user_id, String username, String usermail, String userimageuri){

        HashMap<String, String> user_details = new HashMap<>();
        user_details.put("user_id", user_id);
        user_details.put("username", username);
        user_details.put("useremail", usermail);
        user_details.put("userimage", userimageuri);


        firebaseFirestore.collection("Doctor").document(user_id).set(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SetUpActivity.this, "The user Settings are updated.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SetUpActivity.this, "The user Settings not updated.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
