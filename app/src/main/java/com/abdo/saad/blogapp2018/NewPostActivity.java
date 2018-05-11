package com.abdo.saad.blogapp2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class NewPostActivity extends AppCompatActivity {
   private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        toolbar=findViewById(R.id.add_post_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Post");
    }
}
