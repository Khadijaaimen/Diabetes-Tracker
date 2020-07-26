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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.Calendar;

public class SugarEntry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText econcentration,edate,etime;
    DatePickerDialog picker;
    Sugar sugar;
    private AwesomeValidation awesomeValidation;
    Preferences utils;
    DatabaseHelper dbHelper;
    Intent i;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_entry);
        i=getIntent();

        spinner=findViewById(R.id.Measured);
        adapter=ArrayAdapter.createFromResource(this,R.array.measured,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        getEditTexts();
        sugar=new Sugar();
        utils=new Preferences();
        dbHelper=new DatabaseHelper(this);

        edate.setShowSoftInputOnFocus(false);

        //datepicker
        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SugarEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

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
                mTimePicker = new TimePickerDialog(SugarEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //if intent is coming from Edit button
        if(i.hasExtra("conc"))
        {
            updateSugar();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sugar.setMeasured(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void getEditTexts()
    {
        econcentration=(EditText)findViewById(R.id.concentration);
        edate=(EditText)findViewById(R.id.Date);
        etime=(EditText)findViewById(R.id.Time);
    }

    private void validate()
    {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        if(String.valueOf(econcentration.getText()).isEmpty())
        {
            awesomeValidation.addValidation(this,R.id.concentration,"^[0-9]{1,3}$",R.string.error_concentration);
        }
        if(String.valueOf(edate.getText()).isEmpty())
        {
            awesomeValidation.addValidation(this,R.id.Date,"^[0-9]{1,2}[/][0-9]{1,2}[/][0-9]{4}$",R.string.error_date2);
        }
        if(String.valueOf(etime.getText()).isEmpty())
        {
            awesomeValidation.addValidation(this,R.id.Time,"^([0-1][0-9]|[2][0-3]):([0-5][0-9])$",R.string.error_time2);
        }


    }
    private void setSugar()
    {
        sugar.setConcentration(Integer.parseInt(econcentration.getText().toString().trim()));
        sugar.setDate(edate.getText().toString().trim());
        sugar.setTime(etime.getText().toString().trim());
        sugar.setEmail(utils.getEmail(this));
    }
    public void submitSugar(View v)
    {
        validate();
        if(awesomeValidation.validate())
        {
            setSugar();
            boolean flag;
            if(i.hasExtra("conc"))
            {
                sugar.setId(i.getIntExtra("id",0));
                flag=dbHelper.updateSugar(sugar);
            }
            else
            {
                flag=dbHelper.addSugar(sugar);

            }
            if(flag)
            {
                Toast.makeText(this,"Record Entered",Toast.LENGTH_LONG).show();
                Intent i2=new Intent(this,SugarLog.class);
                startActivity(i2);
            }

        }
    }
    private void updateSugar()
    {
        econcentration.setText(String.valueOf(i.getIntExtra("conc",0)));
        edate.setText(i.getStringExtra("date"));
        etime.setText(i.getStringExtra("time"));
        int position=adapter.getPosition(i.getStringExtra("measured"));
        spinner.setSelection(position);
    }
}