package com.example.diabetestracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout .activity_welcome);
        getSupportActionBar().hide();
    }

    public void signup(View v)
    {
        Intent i = new Intent(Welcome.this, Signup.class);
        startActivity(i);
    }

    public void login(View v){
        Intent i = new Intent(Welcome.this, Login.class);
        startActivity(i);
    }
}