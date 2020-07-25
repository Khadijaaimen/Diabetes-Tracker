package com.example.diabetestracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WeightLog extends AppCompatActivity {

    ArrayList<Weight> weightEntries = new ArrayList<>();
    ListView listView;
    DatabaseHelper db;
    Preferences utils;
    Activity activity;
    AlertDialog.Builder builder;
    Weight clickedWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_log);

        activity = this;
        clickedWeight = new Weight();
        db = new DatabaseHelper(this);
        utils = new Preferences();
        new GetWeightData().execute();

        listView = findViewById(R.id.weight_entry);
    }

    //intent to Weight Entry activity
    public void weightEntry(View v) {
        Intent i = new Intent(this, WeightEntry.class);
        startActivity(i);
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

            final CustomWeightList customWeightList = new CustomWeightList(activity, weightEntries);
            listView.setAdapter(customWeightList);
            registerForContextMenu(listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "You Selected " + weightEntries.get(position).getWeight(), Toast.LENGTH_SHORT).show();
                    clickedWeight.setId(weightEntries.get(position).getId());
                    clickedWeight.setWeight(weightEntries.get(position).getWeight());
                    clickedWeight.setDate(weightEntries.get(position).getDate());
                    clickedWeight.setTime(weightEntries.get(position).getTime());
                    clickedWeight.setEmail(weightEntries.get(position).getEmail());
                }
            });

        }
    }

    protected Void getWeightEntries() {
        String email = utils.getEmail(this);
        weightEntries = db.getWeightEntries(email);
        return null;
    }

    //options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weight_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dlt_weight:
                final AlertDialog.Builder alert = new AlertDialog.Builder(WeightLog.this);
                View v = getLayoutInflater().inflate(R.layout.activity_popup, null);

                alert.setView(v);

                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                TextView tv = v.findViewById(R.id.del);
                tv.setText("Do you want to delete weight records?");

                Button dlt = v.findViewById(R.id.dlt_records);
                Button bcancel = v.findViewById(R.id.cancel);

                dlt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Records Deleted", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                bcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context, menu);
    }

    //operations in context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent ii = new Intent(this, WeightEntry.class);
                ii.putExtra("id", clickedWeight.getId());
                ii.putExtra("weight", clickedWeight.getWeight());
                ii.putExtra("date", clickedWeight.getDate());
                ii.putExtra("time", clickedWeight.getTime());
                startActivity(ii);

                return true;

            case R.id.delete:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean flag = db.deleteWeightRecord(clickedWeight.getEmail(), String.valueOf(clickedWeight.getId()));
                        if (flag)
                            Toast.makeText(getApplicationContext(), "Record Deleted ", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Deletion Unsuccessful", Toast.LENGTH_SHORT).show();
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
            default:
                return false;
        }
    }

}