package com.example.diabetestracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class Reminder extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String field;
    TimePicker tp;
    Spinner spinner;

    //for validation
    public AwesomeValidation awesomeValidation;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //for validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        tp=(TimePicker)findViewById(R.id.Time);
        tp.setIs24HourView(false);

        spinner = findViewById(R.id.rem);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reminder, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        field=parent.getItemAtPosition(position).toString();

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setAlarm(View v){

        int hour, minute;
        hour = tp.getCurrentHour();
        minute = tp.getCurrentMinute();

        Snackbar.make(v, String.valueOf(hour)+":"+String.valueOf(minute), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, field)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minute);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}