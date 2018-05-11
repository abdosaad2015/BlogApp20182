package com.abdo.saad.blogapp2018;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CircleImageView imageView;
    private Uri mainImageURI = null;
    private Button btnSaveSetting;
    private AppCompatEditText setupName;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    private String userId;
    private boolean isChanged = false;
    private String userName;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        toolbar = findViewById(R.id.toolbar4);
        relativeLayout=findViewById(R.id.layout_setup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Setting");
        imageView = findViewById(R.id.circle_image_view);
        btnSaveSetting = findViewById(R.id.btn_save_setting);
        setupName = findViewById(R.id.setupName);
        progressBar = findViewById(R.id.setup_progressbar);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = auth.getCurrentUser().getUid();
        relativeLayout.setActivated(false);
        progressBar.setVisibility(View.VISIBLE);
        firestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        setupName.setText(name);
                        mainImageURI = Uri.parse(image);
                        RequestOptions placeHolder = new RequestOptions();
                        placeHolder.placeholder(R.drawable.defualt_image);
                        Glide.with(SetupActivity.this).setDefaultRequestOptions(placeHolder).load(image).into(imageView);
                    }
                } else {
                    Toast.makeText(SetupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                relativeLayout.setActivated(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(SetupActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        String[] perimmision = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                        ActivityCompat.requestPermissions(SetupActivity.this, perimmision, 25);
                    } else {
                        pickImage();
                    }
                } else {
                    pickImage();
                }
            }
        });
        btnSaveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = setupName.getText().toString().trim();
                if (isChanged) {
                    if (!TextUtils.isEmpty(userName) && mainImageURI != null) {
                        StorageReference image_path = storageReference.child("profile_images")
                                .child(userId + ".jpg");
                        progressBar.setVisibility(View.VISIBLE);
                        image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storeFireStore(task, userName);
                                } else {
                                    Toast.makeText(SetupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                } else storeFireStore(null, userName);
            }
        });
    }

    private void storeFireStore(@NonNull Task<UploadTask.TaskSnapshot> task, String name) {
        Uri download_uri;
        if (task != null) {
            download_uri = task.getResult().getDownloadUrl();
        } else {
            download_uri = mainImageURI;
        }
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("image", download_uri.toString());
        firestore.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SetupActivity.this, "The user settings are updated", Toast.LENGTH_LONG).show();
                    sendToMain();
                } else {
                    Toast.makeText(SetupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendToMain() {
        startActivity(new Intent(SetupActivity.this, MainActivity.class));
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 25 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                imageView.setImageURI(mainImageURI);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void pickImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupActivity.this);
    }
}
