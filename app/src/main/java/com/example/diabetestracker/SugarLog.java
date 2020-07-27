package com.example.diabetestracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SugarLog extends AppCompatActivity {
    ArrayList<Sugar> sugarEntries=new ArrayList<>();
    ListView listView;
    DatabaseHelper db;
    SQLiteDatabase database;
    Preferences utils;
    Activity activity;
    com.github.clans.fab.FloatingActionButton fab;
    AlertDialog.Builder builder;
    Sugar clickedSugar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_log);
        Intent i=getIntent();
        activity=this;
        listView=findViewById(R.id.sugarList);
        clickedSugar=new Sugar();
        db=new DatabaseHelper(this);
        utils=new Preferences();
        new GetSugarData().execute();
        fab=findViewById(R.id.sugarfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2=new Intent(SugarLog.this,SugarEntry.class);
                startActivity(i2);
            }
        });
        //  getSugarEntries();

    }
    public class GetSugarData extends AsyncTask{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            return getSugarEntries();
        }
        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);

            CustomSugarList customSugarList=new CustomSugarList(activity,sugarEntries);
            listView.setAdapter(customSugarList);
            registerForContextMenu(listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(sugarEntries.size() > position) {
                        Toast.makeText(getApplicationContext(), "You Selected " + sugarEntries.get(position).getConcentration(), Toast.LENGTH_SHORT).show();
                        clickedSugar.setId(sugarEntries.get(position).getId());
                        clickedSugar.setConcentration(sugarEntries.get(position).getConcentration());
                        clickedSugar.setDate(sugarEntries.get(position).getDate());
                        clickedSugar.setTime(sugarEntries.get(position).getTime());
                        clickedSugar.setEmail(sugarEntries.get(position).getEmail());
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Enter Data first ", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    protected Void getSugarEntries()
    {
        String email=utils.getEmail(this);
        sugarEntries=db.getSugarEntries(email);
        return null;
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                Intent ii=new Intent(this,SugarEntry.class);
                ii.putExtra("id",clickedSugar.getId());
                ii.putExtra("conc",clickedSugar.getConcentration());
                ii.putExtra("date",clickedSugar.getDate());
                ii.putExtra("time",clickedSugar.getTime());
                ii.putExtra("measured",clickedSugar.getMeasured());
                startActivity(ii);

                return true;
            case R.id.delete:
                builder=new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean flag= db.deleteSugarRecord(clickedSugar.getEmail(),String.valueOf(clickedSugar.getId()));
                        if(flag)
                            Toast.makeText(getApplicationContext(),"Record Deleted ",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(),"Deletion Unsuccessful",Toast.LENGTH_SHORT).show();
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
            default:
                return super.onContextItemSelected(item);
        }
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
            case R.id.dlt_sugar:
                builder=new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete Blood Sugar records?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                        database = db.getReadableDatabase();
                        database.execSQL("delete from sugar");
                        Toast.makeText(getApplicationContext(), "Data Deleted ", Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(SugarLog.this, Tabs.class);
                      startActivity(intent);}
                        catch (Exception e){
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
}