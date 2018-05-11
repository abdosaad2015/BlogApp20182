package com.abdo.saad.blogapp2018;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private FloatingActionButton addPostBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar2);
        addPostBtn=findViewById(R.id.fab_add_btn);
        auth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AWZAN App");
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NewPostActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOut();
                return true;
            case R.id.account_setting:
                sendToSetup();
                return true;

            default:
                return false;
        }
    }

    private void sendToSetup() {
        startActivity(new Intent(MainActivity.this, SetupActivity.class));
    }

    private void logOut() {
        auth.signOut();
        sindToLogin();

    }

    private void sindToLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent
                    (MainActivity.this, LoginActivity.class));
        }

    }
}


