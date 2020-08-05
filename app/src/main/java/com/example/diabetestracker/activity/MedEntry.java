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
import com.example.diabetestracker.R;
import com.example.diabetestracker.database.DatabaseHelper;
import com.example.diabetestracker.javaClass.Medicine;
import com.example.diabetestracker.utils.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MedEntry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText emed, edosage, edate, etime;
    DatePickerDialog picker;
    Medicine medicine;
    private AwesomeValidation awesomeValidation;
    Preferences utils;
    DatabaseHelper dbHelper;
    Intent i;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    AlertDialog.Builder builder;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_entry);
        i=getIntent();

        //spinner
        spinner=findViewById(R.id.unit);
        adapter=ArrayAdapter.createFromResource(this,R.array.unit ,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Initializing variables
        getEditTexts();
        medicine=new Medicine();
        utils=new Preferences();
        dbHelper=new DatabaseHelper(this);

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
                picker = new DatePickerDialog(MedEntry.this,
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
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MedEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time=selectedHour+":"+selectedMinute;
                        SimpleDateFormat  fmt=new SimpleDateFormat("hh:mm");
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
        if(i.hasExtra("med"))
        {
            updateMed();
            ImageView img=findViewById(R.id.image);
            img.setImageResource(R.drawable.edit_entry);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

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
        if(i.hasExtra("med"))
            builder.setMessage("Exit without changing?");
        else
            builder.setMessage("Exit without saving?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(i.getIntExtra("flag",0)==1)//if activity started from Mainactivity
                {
                    Intent i2=new Intent(MedEntry.this, Tabs.class);
                    startActivity(i2);
                    finish();
                }
                else
                {
                    Intent i2=new Intent(MedEntry.this, MedLog.class);
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
        medicine.setUnit(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void getEditTexts()
    {
        emed=findViewById(R.id.medication);
        edate=findViewById(R.id.date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        edate.setText(dateFormat.format(new Date()));
        etime=findViewById(R.id.time);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        etime.setText(timeFormat.format(new Date()));
        edosage=findViewById(R.id.dosage);
    }

    private void validate()
    {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //validate Medicine name
        if(String.valueOf(emed.getText()).isEmpty())
        {
            awesomeValidation.addValidation(this,R.id.medication,"^[A-Za-z0-9 ]+[\\w .-]*",R.string.error_medication);
        }
        //validate dosgae
        if(String.valueOf(edosage.getText()).isEmpty())
        {
            awesomeValidation.addValidation(this,R.id.dosage,"^[0-9]{1,}$",R.string.error_dosage2);
        }
        //validate date
        awesomeValidation.addValidation(this,R.id.date,"^[0-9]{4}[/][0-9]{1,2}[/][0-9]{1,2}$",R.string.error_date2);
        //validate time
        awesomeValidation.addValidation(this,R.id.time,"((1[0-2]|0?[1-9]):([0-5][0-9]) ?([AaPp][Mm]))",R.string.error_time2);


    }
    private void setMedicine()
    {
        medicine.setMedication(emed.getText().toString().trim());
        medicine.setDosage(Integer.parseInt(edosage.getText().toString().trim()));
        medicine.setDate(edate.getText().toString().trim());
        medicine.setTime(etime.getText().toString().trim());
        medicine.setEmail(utils.getEmail(this));
    }
    public void submitMed(View v)
    {
        validate();
        if(awesomeValidation.validate())
        {
            setMedicine();        //set data in Medicine object
            boolean flag;
            if(i.hasExtra("med"))       //if data is to be updated
            {
                medicine.setId(i.getIntExtra("id",0));
                flag=dbHelper.updateMed(medicine);
                if(flag)        //if update is successful
                {
                    Toast.makeText(this,"Update successful",Toast.LENGTH_LONG).show();
                    Intent ii=new Intent(this,MedLog.class);
                    startActivity(ii);
                    finish();
                }else
                {
                    Toast.makeText(this,"Update failed",Toast.LENGTH_LONG).show();
                }

            }
            else        //if new entry is made in Medications
            {
                flag =dbHelper.addMed(medicine);
                if(flag)        //if new entry is successful
                {
                    Toast.makeText(this,"Entry successful",Toast.LENGTH_LONG).show();
                    Intent ii=new Intent(this, MedLog.class);
                    startActivity(ii);
                    finish();
                }else
                {
                    Toast.makeText(this,"Entry failed",Toast.LENGTH_LONG).show();
                }
            }


        }
    }
    private void updateMed()
    {
        emed.setText(i.getStringExtra("med"));
        edosage.setText(String.valueOf(i.getIntExtra("dosage",0)));
        edate.setText(i.getStringExtra("date"));
        etime.setText(i.getStringExtra("time"));
        int position=adapter.getPosition(i.getStringExtra("unit"));
        spinner.setSelection(position);
    }
}

