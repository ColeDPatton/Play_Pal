package com.example.p19;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BufferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(BufferActivity.this, UserProfileActivity.class);
        intent.putExtra("user", getIntent().getStringExtra("user"));
        startActivity(intent);
    }
}
