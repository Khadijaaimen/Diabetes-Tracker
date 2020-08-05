package com.example.diabetestracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.diabetestracker.utils.Preferences;
import com.example.diabetestracker.R;


public class Welcome extends AppCompatActivity {

    CheckBox checkBox;
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
            if (Preferences.getEmail(this) != null && !Preferences.getEmail(this).equals("")) {
                Intent i3 = new Intent(this, Tabs.class);
                startActivity(i3);
                finish();
            }
        else {
            Intent i = new Intent(Welcome.this, Login.class);
            startActivity(i);
        }
    }
}