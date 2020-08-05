package com.example.diabetestracker.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.example.diabetestracker.utils.Preferences;
import com.example.diabetestracker.R;
import com.example.diabetestracker.javaClass.Sugar;
import com.example.diabetestracker.database.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SugarEntry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText econcentration,edate,etime;
    DatePickerDialog picker;
    Sugar sugar;
    private AwesomeValidation awesomeValidation;
    Preferences utils;
    DatabaseHelper dbHelper;
    Intent i;
    int hour, minute;
    AlertDialog.Builder builder;
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

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //to disable keyboard popup
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
                                String month,day;
                                month=String.valueOf(monthOfYear+1);
                                day=String.valueOf(dayOfMonth);
                                if(monthOfYear < 10){
                                    month = "0" + month;
                                }
                                if(dayOfMonth < 10){
                                    day  = "0" + day ;
                                }
                                edate.setText(year + "/" + month + "/" + day);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        //to disable keyboard popup
        etime.setShowSoftInputOnFocus(false);

        //timepicker
        etime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SugarEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time=selectedHour+":"+selectedMinute;
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
                }, hour, minute, false);
                mTimePicker.show();
            }
        });

        //if intent is coming from Edit button
        if(i.hasExtra("conc"))
        {
            updateSugar();
            ImageView img=findViewById(R.id.image);
            img.setImageResource(R.drawable.edit_entry);
        }
    }

    //back button Onclick
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)//back button Onclick
        {
            showAlert();
        }
        return super.onOptionsItemSelected(item);
    }
    //Back button click
    @Override
    public void onBackPressed()
    {
        showAlert();
    }
    //Show alert dialogue box
    private void showAlert()
    {
        builder=new AlertDialog.Builder(this);
        if(i.hasExtra("conc"))
            builder.setMessage("Exit without changing?");
        else
            builder.setMessage("Exit without saving?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(i.getIntExtra("flag",0)==1)//if activity started from Mainactivity
                {
                    Intent i2=new Intent(SugarEntry.this, Tabs.class);
                    startActivity(i2);
                    finish();
                }
                else
                {
                    Intent i2=new Intent(SugarEntry.this, SugarLog.class);
                    startActivity(i2);
                    finish();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        edate.setText(dateFormat.format(new Date()));
        etime=(EditText)findViewById(R.id.Time);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        etime.setText(timeFormat.format(new Date()));
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

    //submit button - Onclick
    public void submitSugar(View v)
    {
        validate();
        if(awesomeValidation.validate())
        {
            setSugar();        //set data in Medicine object
            boolean flag;
            if(i.hasExtra("conc"))       //if data is to be updated
            {
                sugar.setId(i.getIntExtra("id",0));
                flag=dbHelper.updateSugar(sugar);
                if(flag)        //if update is successful
                {
                    Toast.makeText(this,"Update successful",Toast.LENGTH_LONG).show();
                    Intent ii=new Intent(this, SugarLog.class);
                    startActivity(ii);
                    finish();
                }else
                {
                    Toast.makeText(this,"Update failed",Toast.LENGTH_LONG).show();
                }

            }
            else        //if new entry is made in Medications
            {
                flag =dbHelper.addSugar(sugar);
                if(flag)        //if new entry is successful
                {
                    Toast.makeText(this,"Entry successful",Toast.LENGTH_LONG).show();
                    Intent ii=new Intent(this, SugarLog.class);
                    startActivity(ii);
                    finish();
                }else
                {
                    Toast.makeText(this,"Entry failed",Toast.LENGTH_LONG).show();
                }
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