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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.diabetestracker.utils.Preferences;
import com.example.diabetestracker.R;
import com.example.diabetestracker.javaClass.Weight;
import com.example.diabetestracker.database.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeightEntry extends AppCompatActivity {
    DatePickerDialog picker;
    EditText eweight,edate,etime;
    private AwesomeValidation awesomeValidation;
    Weight weight;
    Preferences utils;
    AlertDialog.Builder builder;
    int hour, minute;
    DatabaseHelper dbHelper;
    Intent i;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_entry);
        i=getIntent();
        getEditTexts();
        weight=new Weight();
        utils=new Preferences();
        dbHelper=new DatabaseHelper(this);
        if(i.hasExtra("weight"))
        {
            updateWeight();
            ImageView img=findViewById(R.id.image);
            img.setImageResource(R.drawable.edit_entry);
        }

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
                picker = new DatePickerDialog(WeightEntry.this,
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
                mTimePicker = new TimePickerDialog(WeightEntry.this, new TimePickerDialog.OnTimeSetListener() {
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
        if(i.hasExtra("weight"))
            builder.setMessage("Exit without changing?");
        else
            builder.setMessage("Exit without saving?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(i.getIntExtra("flag",0)==1)//if activity started from Mainactivity
                {
                    Intent i2=new Intent(WeightEntry.this, Tabs.class);
                    startActivity(i2);
                    finish();
                }
                else
                {
                    Intent i2=new Intent(WeightEntry.this, WeightLog.class);
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

    private void getEditTexts()
    {
        eweight=findViewById(R.id.weight);
        edate=findViewById(R.id.date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        edate.setText(dateFormat.format(new Date()));
        etime=findViewById(R.id.time);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        etime.setText(timeFormat.format(new Date()));
    }

    private void validate()
    {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        if (String.valueOf(eweight.getText()).isEmpty()) {
            awesomeValidation.addValidation(this, R.id.weight, "^[0-9]{1,3}[.]{0,1}[0-9]{0,2}$", R.string.error_weight1);
        } else {
            awesomeValidation.addValidation(this, R.id.weight, "^[0-9]{1,3}[.]{0,1}[0-9]{0,2}$", R.string.error_weight2);
        }
        //validate date
        awesomeValidation.addValidation(this,R.id.date,"^[0-9]{4}[/][0-9]{1,2}[/][0-9]{1,2}$",R.string.error_date2);
        //validate time
        awesomeValidation.addValidation(this,R.id.time,"((1[0-2]|0?[1-9]):([0-5][0-9]) ?([AaPp][Mm]))",R.string.error_time2);
    }

    public void submitWeight(View v)
    {
        validate();
        if(awesomeValidation.validate())
        {
            setWeight();
            boolean flag;
            if(i.hasExtra("weight"))
            {
                weight.setId(i.getIntExtra("id",0));
                flag=dbHelper.updateWeight(weight);
                if(flag)
                {
                    Toast.makeText(this,"Update successful",Toast.LENGTH_LONG).show();
                    Intent ii=new Intent(this, WeightLog.class);
                    startActivity(ii);
                    finish();
                }
                else
                    Toast.makeText(this,"Update Failed",Toast.LENGTH_LONG).show();
            }
            else
            {
                flag= dbHelper.addWeight(weight);
                if(flag)
                {
                    Toast.makeText(this,"Entry successful",Toast.LENGTH_LONG).show();
                    Intent ii=new Intent(this, WeightLog.class);
                    startActivity(ii);
                    finish();
                }
                else
                    Toast.makeText(this,"Entry failed",Toast.LENGTH_LONG).show();
            }

        }
    }
    private void setWeight() {
        weight.setWeight(Integer.parseInt(eweight.getText().toString()));
        weight.setDate(edate.getText().toString());
        weight.setTime(etime.getText().toString());
        weight.setEmail(utils.getEmail(this));
    }
    private void updateWeight()
    {
        eweight.setText(String.valueOf(i.getDoubleExtra("weight",0)));
        edate.setText(i.getStringExtra("date"));
        etime.setText(i.getStringExtra("time"));
    }
}
