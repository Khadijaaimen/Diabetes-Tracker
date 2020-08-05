package com.example.diabetestracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.diabetestracker.database.DatabaseHelper;
import com.example.diabetestracker.utils.Preferences;
import com.example.diabetestracker.R;
import com.example.diabetestracker.javaClass.User;

import java.util.Calendar;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class EditProfile extends AppCompatActivity {
    User user;
    Intent i;
    EditText name,dob,password;
    RadioButton male,female;
    DatePickerDialog picker;
    private AwesomeValidation awesomeValidation;
    boolean RadioCheck;
    String gender,email;
    Preferences utils;
    DatabaseHelper dbHelper;

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
        if (Build.VERSION.SDK_INT >= 11) {
            dob.setRawInputType(InputType.TYPE_CLASS_TEXT);
            dob.setTextIsSelectable(true);
        } else {
            dob.setRawInputType(InputType.TYPE_NULL);
            dob.setFocusable(true);
        }
    }
    //find editTexts
    private void findViews()
    {
        name=findViewById(R.id.ename);
        dob=findViewById(R.id.edob);
        password=findViewById(R.id.epassword);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
    }
    //Date Picker
    public void pickDate(View v){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(EditProfile.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
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
        name.setText(user.getName());
        dob.setText(user.getDOB());
        password.setText(user.getPassword());
        if(user.getGender()=="Male")
            male.setChecked(true);
        else
            female.setChecked(true);
    }
    //validate
    public void validate()
    {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //validate name
        awesomeValidation.addValidation(this,R.id.ename,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$",R.string.error_name1);
        //Validate DOB
        awesomeValidation.addValidation(this,R.id.edob,"^[0-9]{1,2}[/][0-9]{1,2}[/][0-9]{4}$",R.string.error_dob1);
        //Validate Password
        if(!String.valueOf(password.getText()).isEmpty())
        {
            if(String.valueOf(password.getText()).length()<5)
            {
                awesomeValidation.addValidation(this,R.id.epassword,"^[a-zA-Z0-9]{5,}$",R.string.error_password2);
            }
            else{
                awesomeValidation.addValidation(this,R.id.epassword,"^[a-zA-Z0-9]+$",R.string.error_password1);
            }
        }
        else {
            awesomeValidation.addValidation(this,R.id.epassword,"^[a-zA-Z0-9]{5,}$",R.string.error_password3);
        }
        //Validate Gender
        RadioCheck=TRUE;

        RadioGroup gender=(RadioGroup)findViewById(R.id.Group);
        if(gender.getCheckedRadioButtonId()==-1)
        {
            female.setError(getString(R.string.error_gender1));//Set error to last Radio button
            RadioCheck=FALSE;
        }
        else{
            female.setError(null);
            RadioCheck=TRUE;
        }

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
        user.setName(name.getText().toString().trim());
        user.setDOB(dob.getText().toString().trim());
        user.setGender(gender);
        user.setPassword(password.getText().toString().trim());
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
                Intent i2=new Intent(this, Tabs.class);
                startActivity(i2);
            }
        }
    }
}
