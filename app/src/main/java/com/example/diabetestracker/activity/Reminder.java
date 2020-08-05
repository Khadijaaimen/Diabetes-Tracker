package com.example.diabetestracker.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.diabetestracker.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Reminder extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String field;
    EditText etime;
    Spinner spinner;
    TimePickerDialog timepicker;

    //for validation
    public AwesomeValidation awesomeValidation;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //for validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        etime=(EditText) findViewById(R.id.time);

        spinner = findViewById(R.id.rem);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reminder, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //back button Onclick
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        field=parent.getItemAtPosition(position).toString();

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Time Picker
    public void pickTime(View v){
        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);
        String am_pm=" ";
        if(cldr.get(Calendar.AM_PM)==Calendar.AM)
            am_pm="am";
        else if(cldr.get(Calendar.AM_PM)==Calendar.PM)
            am_pm="pm";
        // time picker dialog
        timepicker = new TimePickerDialog(Reminder.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        String time=sHour+":"+sMinute;
                        SimpleDateFormat fmt=new SimpleDateFormat("hh:mm");
                        Date date=null;
                        try{
                            date=fmt.parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                        String formattedTime=fmtOut.format(date);
                        etime.setText(formattedTime);
                    }
                }, hour, minutes, false);
        timepicker.show();
    }
    public void setAlarm(View v) {
        int hour, minute;
        hour=Integer.parseInt(String.valueOf(etime.getText()).substring(0,2));
        minute=Integer.parseInt(String.valueOf(etime.getText()).substring(3,5));
        String am_pm=String.valueOf(etime.getText()).substring(6);
        boolean t;
        if(am_pm=="AM")
            t=false;
        else
            t=true;
        Snackbar.make(v, String.valueOf(hour)+":"+String.valueOf(minute), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, field)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minute)
                .putExtra(AlarmClock.EXTRA_IS_PM,t);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}