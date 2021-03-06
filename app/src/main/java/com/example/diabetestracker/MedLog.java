package com.example.diabetestracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;

public class MedLog extends AppCompatActivity {

//    private FusedLocationProviderClient fusedLocationClient;
    ArrayList<Medicine> medEntries = new ArrayList<>();
    ListView listView;
    DatabaseHelper db;
    Preferences utils;
    Activity activity;
    AlertDialog.Builder builder;
    Medicine clickedMedicine;

    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_log);

        activity = this;
        clickedMedicine = new Medicine();
        db = new DatabaseHelper(this);
        utils = new Preferences();
        new MedLog.GetMedData().execute();

        listView = findViewById(R.id.med_entry);
    }

    //intent to Medication Entry activity
    public void medEntry(View v) {
        Intent i = new Intent(this, MedEntry.class);
        startActivity(i);
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
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "You Selected " + medEntries.get(position).getMedication(), Toast.LENGTH_SHORT).show();
                    clickedMedicine.setId(medEntries.get(position).getId());
                    clickedMedicine.setMedication(medEntries.get(position).getMedication());
                    clickedMedicine.setUnit(medEntries.get(position).getUnit());
                    clickedMedicine.setDosage(medEntries.get(position).getDosage());
                    clickedMedicine.setDate(medEntries.get(position).getDate());
                    clickedMedicine.setTime(medEntries.get(position).getTime());
                    clickedMedicine.setEmail(medEntries.get(position).getEmail());
                }
            });

        }
    }

    protected Void getMedEntries() {
        String email = utils.getEmail(this);
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
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean flag = db.deleteMedRecord(clickedMedicine.getEmail(), String.valueOf(clickedMedicine.getId()));
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

                return true;

            default:
                return false;
        }
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

                Intent ii = new Intent(this, MedEntry.class);
                ii.putExtra("id", clickedMedicine.getId());
                ii.putExtra("med", clickedMedicine.getMedication());
                ii.putExtra("dosage", clickedMedicine.getDosage());
                ii.putExtra("date", clickedMedicine.getDate());
                ii.putExtra("time", clickedMedicine.getTime());
                ii.putExtra("unit", clickedMedicine.getUnit());
                startActivity(ii);

                return true;

            case R.id.delete:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean flag = db.deleteMedRecord(clickedMedicine.getEmail(), String.valueOf(clickedMedicine.getId()));
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

    public void find(View v) {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    locationRequestCode);
//
//        } else {
//            GetLocation();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1000: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    GetLocation();
//                    Intent i = new Intent(this, FindPharmacy.class);
//                    startActivity(i);
//                } else {
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
//
//        }
    }
//
//    public void GetLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            {
//                                if (location != null) {
//                                    wayLatitude = location.getLatitude();
//                                    wayLongitude = location.getLongitude();
//                                }
//                            }
//                        }
//                    });
//            return;
//        }
//
//    }
}