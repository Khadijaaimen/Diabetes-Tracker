package com.example.diabetestracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.Calendar;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class EditProfile extends AppCompatActivity {

    User user;
    String name, pass, dob;
    Intent i;
    Preferences utils;
    DatabaseHelper dbHelper;
    boolean RadioCheck;

    //editviews
    DatePickerDialog picker;
    EditText ename,eDOB,epassword;
    String gender,email;
    RadioButton male,female;

    //for validation
    private AwesomeValidation awesomeValidation;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        i=getIntent();
        user=new User();
        setUser();
        findViews();
        setViews();
        dbHelper=new DatabaseHelper(this);
        utils=new Preferences();

        //for validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        ename = findViewById(R.id.e1);
        epassword = findViewById(R.id.e3);
        eDOB = findViewById(R.id.e2);

        eDOB.setShowSoftInputOnFocus(false);

        //datepicker dialog
        eDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(EditProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eDOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

    }

    // find edittexts
    private void findViews()
    {
        ename = findViewById(R.id.e1);
        epassword = findViewById(R.id.e3);
        eDOB = findViewById(R.id.e2);
        male=findViewById(R.id.Male);
        female=findViewById(R.id.Female);
    }

    //take values from intent
    private void setUser()
    {
        user.setName(i.getStringExtra("name"));
        user.setDOB(i.getStringExtra("dob"));
        user.setGender(i.getStringExtra("gender"));
        gender=i.getStringExtra("gender");
        user.setPassword(i.getStringExtra("password"));
    }

    //set values of views
    private void setViews()
    {
        ename.setText(user.getName());
        eDOB.setText(user.getDOB());
        epassword.setText(user.getPassword());
        if(user.getGender()=="Male")
            male.setChecked(true);
        else
            female.setChecked(true);
    }

    //validate
    public void validate(){

        // validate name
        awesomeValidation.addValidation(this, R.id.e1,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$",R.string.error_name1);

        //Validate DOB
        awesomeValidation.addValidation(this,R.id.e2,"^[0-9]{1,2}[/][0-9]{0,2}[/][0-9]{4}$",R.string.error_dob1);

//      //Validate Gender

        RadioCheck=TRUE;

        RadioGroup gender=(RadioGroup)findViewById(R.id.Group);
        if(gender.getCheckedRadioButtonId()==-1)
        {
            female.setError(getString(R.string.error_gender1));//Set error to last Radio button
            Toast.makeText(this, "Gender Required", Toast.LENGTH_SHORT).show();
            RadioCheck=FALSE;
        }
        else{
            female.setError(null);
            RadioCheck=TRUE;
        }

        // Validate Password
        if(!String.valueOf(epassword.getText()).isEmpty())
        {
            if(String.valueOf(epassword.getText()).length()<5)
            {
                awesomeValidation.addValidation(this,R.id.e3,"^[a-zA-Z0-9]{5,}$",R.string.error_password2);
            }
            else{
                awesomeValidation.addValidation(this,R.id.e3,"^[a-zA-Z0-9]+$",R.string.error_password1);
            }
        }
        else {
            awesomeValidation.addValidation(this,R.id.e3,"^[a-zA-Z0-9]{5,}$",R.string.error_password3);
        }

        awesomeValidation.validate();

//
    }

    //Gender Radiobox
    public void pickGender(View v)
    {
        switch(v.getId())
        {
            case R.id.male:
                gender="Male";
                break;
            case R.id.female:
                gender="Female";
                break;
            default:
                gender="";
        }
    }

    //set new user values
    private  void setNewUserValues()
    {
        user.setName(ename.getText().toString().trim());
        user.setDOB(eDOB.getText().toString().trim());
        user.setGender(gender);
        user.setPassword(epassword.getText().toString().trim());
        email=utils.getEmail(this);
    }

    public void update(View v)
    {
        validate();
        if(awesomeValidation.validate()&&RadioCheck==TRUE)
        {
            setNewUserValues();
            boolean flag=dbHelper.updateUser(email,user);
            if(flag)
            {
                Toast.makeText(this,"Data Inserted",Toast.LENGTH_LONG).show();
                Intent i2=new Intent(this, EditProfile.class);
                startActivity(i2);
            }
        }
    }

}

