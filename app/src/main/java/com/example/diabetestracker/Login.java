package com.example.diabetestracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.snackbar.Snackbar;

import static java.lang.Boolean.FALSE;

public class Login extends AppCompatActivity {

    EditText eemail, epassword;
    String email, password;
    Preferences utils;
    User user;
    DatabaseHelper dbHelper;
    private AwesomeValidation awesomeValidation;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        checkBox = findViewById(R.id.c1);
        Intent i = getIntent();
        eemail = (EditText) findViewById(R.id.e1);
        epassword = (EditText) findViewById(R.id.e2);
        //object of Preferences class
        utils = new Preferences();
        user = new User();
        dbHelper = new DatabaseHelper(this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

    }

    private boolean EmailisEmpty()
    {
        email=String.valueOf(eemail.getText());
        if(email.isEmpty()) {

            awesomeValidation.addValidation(this,R.id.e1,"^[A-Za-z]+[A-Za-z0-9+_.-]*@(.+)$",R.string.error_email2);
            //change tint color
            eemail.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            return true;
        }

        else {
            eemail.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
            return false;
        }

    }
    private boolean PasswordisEmpty()
    {

        password=String.valueOf(epassword.getText());
        if(password.isEmpty()) {
            awesomeValidation.addValidation(this,R.id.e2,"^[a-zA-Z0-9]{5,}$",R.string.error_password3);
            epassword.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            return true;
        }
        else
        {
            epassword.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
            return false;
        }

    }
    private void getValues()
    {
        user.setEmail(eemail.getText().toString().trim());
        user.setPassword(epassword.getText().toString().trim());
    }

    //intent to Tabs activity
    public void submit(View view)
    {
        EmailisEmpty();
        PasswordisEmpty();
        awesomeValidation.validate();
        if(EmailisEmpty()==FALSE)
        {
            if(PasswordisEmpty()==FALSE)
            {
                if(awesomeValidation.validate())
                {
                    getValues();
                    //database working
                    if(dbHelper.checkUser(user.getEmail(),user.getPassword()))
                    {
                        //after database validation

                        utils.saveEmail(user.getEmail(),this);

                        if(utils.getEmail(this)!=null || !utils.getEmail(this).equals(""))
                        {
                            Intent intent=new Intent(this, Tabs.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else
                    {
                        Snackbar.make(view, "Invalid Email or Password", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        }
        else
        {
            epassword.setText("");

            //epassword.setHintTextColor(Color.GRAY);
        }
    }

    //intent to Signup activity
    public void account2(View v) {
        Intent i = new Intent(Login.this, Signup.class);
        startActivity(i);
    }
}
