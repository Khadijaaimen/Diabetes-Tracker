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
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;


public class WeightLog extends AppCompatActivity {
    ArrayList<Weight> weightEntries = new ArrayList<>();
    SwipeMenuListView listView;
    DatabaseHelper db;
    SQLiteDatabase database;
    Preferences utils;
    com.github.clans.fab.FloatingActionButton fab;
    AlertDialog.Builder builder;
    Activity activity;
    Weight weight;
    String id,email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_log);
        Intent i = getIntent();
        activity = this;
        listView = findViewById(R.id.weightList);
        weight=new Weight();
        new GetWeightData().execute();
        utils = new Preferences();
        db = new DatabaseHelper(this);

        fab=findViewById(R.id.weightFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2=new Intent(WeightLog.this, WeightEntry.class);
                startActivity(i2);
            }
        });
    }

    public class GetWeightData extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return getWeightEntries();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            CustomWeightList customWeightList = new CustomWeightList(activity, weightEntries);
            listView.setAdapter(customWeightList);
            registerForContextMenu(listView);

            listView.setMenuCreator(creator);
            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                    switch (index) {
                        case 0:
                                Toast.makeText(getApplicationContext(), "You Selected " + weightEntries.get(position).getWeight(), Toast.LENGTH_SHORT).show();
                                Intent i2 = new Intent(WeightLog.this, WeightEntry.class);
                                i2.putExtra("weight", weightEntries.get(position).getWeight());
                                i2.putExtra("date", weightEntries.get(position).getDate());
                                i2.putExtra("time", weightEntries.get(position).getTime());
                                i2.putExtra("id", weightEntries.get(position).getId());
                                startActivity(i2);

                            return true;
                        case 1:
                            id=String.valueOf(weightEntries.get(position).getId());
                            email=weightEntries.get(position).getEmail().trim();
                            boolean flag=db.deleteWeightRecord(email,id);
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
    protected Void getWeightEntries() {
        String email = utils.getEmail(WeightLog.this);
        Log.d("TAG", email);
        weightEntries = db.getWeightEntries(email);
        return null;
    }

    //options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weight_log, menu);
        return true;
    }

    //operations in options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dlt_weight:
                builder=new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            database = db.getReadableDatabase();
                            database.execSQL("delete from weight");
                            Toast.makeText(getApplicationContext(), "Data Deleted ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(WeightLog.this, Tabs.class);
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

