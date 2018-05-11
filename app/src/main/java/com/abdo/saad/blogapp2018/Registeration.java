package com.abdo.saad.blogapp2018;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Registeration extends AppCompatActivity {
    private AppCompatEditText email, password, cpassword;
    private AppCompatButton regiterBtn, loginBtn;
    private ProgressBar bar;
    private FirebaseAuth auth;
    private TextInputLayout emailInput, passInput, cpassInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        email = findViewById(R.id.username_register_appcom);
        password = findViewById(R.id.password_register_appcomp);
        cpassword = findViewById(R.id.cpassword_register_appcomp);
        regiterBtn = findViewById(R.id.btn_register_appcomp);
        loginBtn = findViewById(R.id.btn_r_login_appcomp);
        emailInput = findViewById(R.id.username_register_txtinput);
        passInput = findViewById(R.id.password_register_textinput);
        cpassInput = findViewById(R.id.cpassword_register_txtinput);
        bar = findViewById(R.id.progressBar3);
        auth = FirebaseAuth.getInstance();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        regiterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt = email.getText().toString().trim();
                String passTxt = password.getText().toString().trim();
                String cpassTxt = cpassword.getText().toString().trim();
                if (!TextUtils.isEmpty(emailTxt) && !TextUtils.isEmpty(passTxt) && !TextUtils.isEmpty(cpassTxt)) {
                    if (passTxt.equals(cpassTxt)) {
                        cpassInput.setErrorEnabled(false);
                        bar.setVisibility(View.VISIBLE);
                        auth.createUserWithEmailAndPassword(emailTxt, passTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sendToSetup();
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(Registeration.this, errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        bar.setVisibility(View.INVISIBLE);
                    } else {
                        cpassInput.setErrorEnabled(true);
                        cpassInput.setError("*Password Not Matched");
                    }
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogin();
            }
        });

    }

    private void sendToSetup() {
     startActivity(new Intent(Registeration.this,SetupActivity.class));
     finish();
    }

    private void sendToLogin() {
        startActivity(new Intent(Registeration.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        startActivity(new Intent(Registeration.this, MainActivity.class));
        finish();
    }
}
