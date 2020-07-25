package com.example.diabetestracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.Calendar;

public class WeightEntry extends AppCompatActivity{

    DatePickerDialog picker;

    Intent i;
    Preferences utils;
    Weight weight;
    DatabaseHelper dbHelper;

    //edit view
    EditText eweight, edate, etime;
    String sweight, date, time;

    //for validation
    private AwesomeValidation awesomeValidation;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_entry);

        i=getIntent();

        //for validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        eweight = findViewById(R.id.weight);
        edate = findViewById(R.id.date);
        edate.setShowSoftInputOnFocus(false);

        getEditTexts();
        weight=new Weight();
        utils=new Preferences();
        dbHelper=new DatabaseHelper(this);
        //if intent is coming from Edit button
        if(i.hasExtra("weight"))
        {
            updateWeight();
        }

        //datepicker
        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(WeightEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        etime = findViewById(R.id.time);
        etime.setShowSoftInputOnFocus(false);

        //timepicker
        etime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeightEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    private void getEditTexts(){
        eweight = findViewById(R.id.weight);
        edate = findViewById(R.id.date);
        etime = findViewById(R.id.time);
    }

    private void setWeight(){
        weight.setWeight(Integer.parseInt(eweight.getText().toString().trim()));
        weight.setDate(edate.getText().toString().trim());
        weight.setTime(etime.getText().toString().trim());
        weight.setEmail(utils.getEmail(this));
    }

    //validation
    private void validate(){
        sweight = eweight.getText().toString();
        date = edate.getText().toString();
        time = etime.getText().toString();

        // validate concentration
        if(sweight.isEmpty()) {
            awesomeValidation.addValidation(this, R.id.weight, "^[0-9]{1,3}", R.string.error_weight);
        }

        //validate date
        if(date.isEmpty()){
            awesomeValidation.addValidation(this,R.id.date,"^[0-9]{1,2}[/][0-9]{0,2}[/][0-9]{4}$",R.string.error_date2);
        }

        //validate time
        if(time.isEmpty()) {
            awesomeValidation.addValidation(this, R.id.time, "^[0-9]{1,2}[:][0-9]{1,2}$", R.string.error_time2);
        }
        awesomeValidation.validate();
    }

    public void submitWeight(View v){
        validate();
        if(awesomeValidation.validate())
        {
            setWeight();
            boolean flag;
            if(i.hasExtra("weight"))
            {
                weight.setId(i.getIntExtra("id",0));
                flag=dbHelper.updateWeight(weight);
            }
            else
            {
                flag= dbHelper.addWeight(weight);
            }
            if(flag)
            {
                Intent ii=new Intent(this, WeightLog.class);
                Toast.makeText(this,"Record Entered",Toast.LENGTH_LONG).show();
                startActivity(ii);
            }
            else
                Toast.makeText(this,"Insertion Failed",Toast.LENGTH_LONG).show();
        }
    }

    private void updateWeight()
    {
        eweight.setText(String.valueOf(i.getIntExtra("weight",0)));
        edate.setText(i.getStringExtra("date"));
        etime.setText(i.getStringExtra("time"));
    }
}