package com.abdo.saad.blogapp2018;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabWidget;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private AppCompatEditText user;
    private AppCompatEditText pass;
    private TextInputLayout userInput, passInput;
    private AppCompatButton login, register;
    private FloatingActionButton fab;
    private FirebaseAuth firebaseAuth;
    private ProgressBar loginProgress;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = findViewById(R.id.username_login);
        pass = findViewById(R.id.password_login);
        userInput = findViewById(R.id.username_TextInputLayout);
        passInput = findViewById(R.id.password_TextInputLayout);
        fab = findViewById(R.id.fab_btn);
        login = findViewById(R.id.appcompat_loginbtn);
        register = findViewById(R.id.appcompat_loginbtn1);
        loginProgress = findViewById(R.id.progressBar2);
        firebaseAuth = FirebaseAuth.getInstance();
        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (user.getText().toString().isEmpty()) {
                    userInput.setErrorEnabled(true);
                    userInput.setError("please Enter your username :");
                } else {
                    userInput.setErrorEnabled(false);
                }
            }
        });
        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (user.getText().toString().isEmpty()) {
                    userInput.setErrorEnabled(true);
                    userInput.setError("please input your username :");
                } else {
                    userInput.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (pass.getText().toString().isEmpty()) {
                    passInput.setErrorEnabled(true);
                    passInput.setError("please input your password :");
                } else {
                    passInput.setErrorEnabled(false);
                }
            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pass.getText().toString().isEmpty()) {
                    passInput.setErrorEnabled(true);
                    passInput.setError("please input your password :");
                } else {
                    passInput.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passInput.setCounterEnabled(true);
        passInput.setCounterMaxLength(8);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgress.setVisibility(View.VISIBLE);
                String loginEmail = user.getText().toString();
                String password = pass.getText().toString();
                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(password)) {
                    firebaseAuth.signInWithEmailAndPassword(loginEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendToMain();
                            } else {
                                String errorMessage=task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,errorMessage,Toast.LENGTH_LONG).show();
                            }
                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRegister();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            sendToMain();
        }
    }
    private void sendToMain(){
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }
    private void sendToRegister(){
        startActivity(new Intent(LoginActivity.this,Registeration.class));
        finish();
    }
}
