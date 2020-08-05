package com.example.diabetestracker.activity;

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
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.diabetestracker.customList.CustomSugarList;
import com.example.diabetestracker.database.DatabaseHelper;
import com.example.diabetestracker.utils.Preferences;
import com.example.diabetestracker.R;
import com.example.diabetestracker.javaClass.Sugar;

import java.util.ArrayList;


public class SugarLog extends AppCompatActivity {
    ArrayList<Sugar> sugarEntries = new ArrayList<>();
    SwipeMenuListView listView;
    DatabaseHelper db;
    SQLiteDatabase database;
    Preferences utils;
    com.github.clans.fab.FloatingActionButton fab;
    AlertDialog.Builder builder;
    Activity activity;
    Sugar sugar;
    String id,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_log);

        Intent i = getIntent();
        activity = this;
        listView = findViewById(R.id.sugarList);
        sugar=new Sugar();
        utils = new Preferences();
        db = new DatabaseHelper(this);
        getSugarEntries();
        if(!sugarEntries.isEmpty()) {
            new GetSugarData().execute();
        }
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab=findViewById(R.id.sugarfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2=new Intent(SugarLog.this, SugarEntry.class);
                startActivity(i2);
            }
        });
    }

    public class GetSugarData extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return getSugarEntries();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            CustomSugarList customSugarList = new CustomSugarList(activity, sugarEntries);
            listView.setAdapter(customSugarList);
            registerForContextMenu(listView);
            listView.setMenuCreator(creator);
            //on item swipe
            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            Intent i2 = new Intent(SugarLog.this, SugarEntry.class);
                            i2.putExtra("conc", sugarEntries.get(position).getConcentration());
                            i2.putExtra("measured", sugarEntries.get(position).getMeasured());
                            i2.putExtra("date", sugarEntries.get(position).getDate());
                            i2.putExtra("time", sugarEntries.get(position).getTime());
                            i2.putExtra("id", sugarEntries.get(position).getId());
                            startActivity(i2);
                            finish();
                            return true;
                        case 1:     //delete this record
                            id = String.valueOf(sugarEntries.get(position).getId());
                            email = sugarEntries.get(position).getEmail().trim();
                            builder = new AlertDialog.Builder(SugarLog.this);
                            builder.setMessage("Are you sure you want to delete this record?");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean flag = db.deleteSugarRecord(email, id);
                                    if (flag) {
                                        Toast.makeText(getApplicationContext(), "Record Deleted ", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            return true;
                        default:
                            return false;
                        //add alert dialogue
                    }
                }
            });
            //on item click
            listView.setOnItemClickListener(new SwipeMenuListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(SugarLog.this,"Swipe left to Edit or Delete",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    protected Void getSugarEntries() {
        String email = utils.getEmail(SugarLog.this);
        Log.d("TAG", email);
        sugarEntries = db.getSugarEntries(email);
        return null;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.dlt_sugar);
        if(sugarEntries.isEmpty())        //if medication log is empty-disable clear log options menu
            item.setEnabled(false);
        else                            //if medication log is not empty-enable clear log options menu
            item.setEnabled(true);
        super.onPrepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }


    //options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sugar_log, menu);
        return true;
    }

    //operations in options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.dlt_sugar:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to clear log?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            database = db.getReadableDatabase();
                            database.execSQL("delete from sugar");
                            Toast.makeText(getApplicationContext(), "Log Cleared", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SugarLog.this, Tabs.class);
                            startActivity(intent);
                        } catch (Exception e) {
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
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;

            case R.id.level:
                Intent i1 = new Intent(this, TargetLevels.class);
                startActivity(i1);
                return true;

            case R.id.warning:
                Intent i2 = new Intent(this, SugarWarning.class);
                startActivity(i2);
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


