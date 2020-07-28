package com.example.diabetestracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;


public class MedLog extends AppCompatActivity {
    ArrayList<Medicine> medEntries = new ArrayList<>();
    SwipeMenuListView listView;
    DatabaseHelper db;
    SQLiteDatabase database;
    Preferences utils;
    com.github.clans.fab.FloatingActionButton fab;
    AlertDialog.Builder builder;
    Activity activity;
    Medicine med;
    String id,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_log);
        Intent i = getIntent();
        activity = this;
        listView = findViewById(R.id.medList);
        med=new Medicine();
        new GetMedData().execute();
        utils = new Preferences();
        db = new DatabaseHelper(this);
        fab=findViewById(R.id.medfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2=new Intent(MedLog.this,MedEntry.class);
                startActivity(i2);
            }
        });
    }

    public class GetMedData extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return getMedEntries();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            CustomMedList customMedList = new CustomMedList(activity, medEntries);
            listView.setAdapter(customMedList);
            registerForContextMenu(listView);
            listView.setMenuCreator(creator);
            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                    switch (index) {
                        case 0:
                            if(medEntries.size() > position) {
                                Toast.makeText(getApplicationContext(), "You Selected " + medEntries.get(position).getMedication(), Toast.LENGTH_SHORT).show();
                                Intent i2 = new Intent(MedLog.this, MedEntry.class);
                                i2.putExtra("med", medEntries.get(position).getMedication());
                                i2.putExtra("dosage", medEntries.get(position).getDosage());
                                i2.putExtra("unit", medEntries.get(position).getUnit());
                                i2.putExtra("date", medEntries.get(position).getDate());
                                i2.putExtra("time", medEntries.get(position).getTime());
                                i2.putExtra("id", medEntries.get(position).getId());
                                startActivity(i2);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Enter Any Data", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        case 1:
                            id=String.valueOf(medEntries.get(position).getId());
                            email=medEntries.get(position).getEmail().trim();
                            boolean flag=db.deleteMedRecord(email,id);
                            if(flag)
                            {
                                Toast.makeText(getApplicationContext(), "Record Deleted ", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                            }
                            return true;
                        default:
                            return false;
                    }
                }

            });
        }
    }
    protected Void getMedEntries() {
        String email = utils.getEmail(MedLog.this);
        Log.d("TAG", email);
        medEntries = db.getMedEntries(email);
        return null;
    }

    //options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.med_log, menu);
        return true;
    }

    //operations in options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dlt_med:
                builder=new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            database = db.getReadableDatabase();
                            database.execSQL("delete from medication");
                            Toast.makeText(getApplicationContext(), "Data Deleted ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MedLog.this, Tabs.class);
                            startActivity(intent);}
                        catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Deletion unsuccessful", Toast.LENGTH_SHORT).show();
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
                return true;

            default:
                return false;
        }
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(R.color.green);
            // set item width
            openItem.setWidth(120);
            // set item icon
            openItem.setIcon(R.drawable.ic_baseline_edit_24);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(120);
            // set a icon
            deleteItem.setIcon(R.drawable.ic_baseline_delete_24);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

// set creator

}

