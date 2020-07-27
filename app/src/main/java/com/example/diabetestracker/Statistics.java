package com.example.diabetestracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {

    ArrayList<Sugar> sugarEntries=new ArrayList<>();
    Preferences utils;
    Activity activity;
    TextView t1, t2, t3, t4, t5, t6, t7, t8;
    DatabaseHelper db;
    SQLiteDatabase database;
    ArrayList<String> time = new ArrayList<>();
    ArrayList<Integer> concent = new ArrayList<>();

    Integer count_fast = 0, count_bf = 0, count_blunch = 0, count_alunch = 0, count_bdinner = 0, count_adinner = 0,
            count_random=0, count_other=0, sum_fast = 0, sum_bf = 0, sum_blunch = 0, sum_alunch = 0, sum_bdinner = 0,
            sum_adinner = 0,  sum_random = 0,  sum_other = 0;
    Double avg_fast, avg_bf, avg_blunch, avg_alunch, avg_bdinner, avg_adinner, avg_random, avg_other;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        db = new DatabaseHelper(this);
        database = db.getReadableDatabase();

        activity = this;

        new GetSugarData().execute();

        t1 = findViewById(R.id.fasting);
        t2 = findViewById(R.id.after_bf);
        t3 = findViewById(R.id.before_lunch);
        t4 = findViewById(R.id.after_lunch);
        t5 = findViewById(R.id.before_dinner);
        t6 = findViewById(R.id.after_dinner);
        t7 = findViewById(R.id.random);
        t8 = findViewById(R.id.other);
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
            try {
                SQLiteDatabase database=db.getReadableDatabase();
                String selection=DatabaseContract.SugarTable.COL_EMAIL+" = ?";
                Cursor c = database.query(DatabaseContract.SugarTable.TABLE_NAME, null, selection, new String[] {email}, null, null, null);
                while (c.moveToNext()) {
                    concent.add(c.getInt(1));
                    time.add(c.getString(2));
                }
            } catch (SQLiteException e) {
                Log.d("TAG", e.getMessage());

            }
            for (int i = 0; i < concent.size(); i++) {
                switch (time.get(i)) {
                    case "Fasting":
                        count_fast++;
                        sum_fast += concent.get(i);
                        break;
                    case "After Breakfast":
                        count_bf++;
                        sum_bf += concent.get(i);
                        break;
                    case "Before Lunch":
                        count_blunch++;
                        sum_blunch += concent.get(i);
                        break;
                    case "After Lunch":
                        count_alunch++;
                        sum_alunch += concent.get(i);
                        break;
                    case "Before Dinner":
                        count_bdinner++;
                        sum_bdinner += concent.get(i);
                        break;
                    case "After Dinner":
                        count_adinner++;
                        sum_adinner += concent.get(i);
                        break;
                    case "Random":
                        count_random++;
                        sum_random += concent.get(i);
                        break;
                    case "Other":
                        count_other++;
                        sum_other += concent.get(i);
                        break;
                }
            }

            if (count_fast > 0) {
                avg_fast = Double.valueOf(sum_fast / count_fast);
                t1.setText(String.valueOf(avg_fast));
            } else{
                t1.setText("-");
            }

            if (count_bf > 0) {
                avg_bf = Double.valueOf(sum_bf / count_bf);
                t2.setText(String.valueOf(avg_bf));
            } else{
                t2.setText("-");
            }

            if (count_blunch > 0) {
                avg_blunch = Double.valueOf(sum_blunch / count_blunch);
                t3.setText(String.valueOf(avg_blunch));
            } else{
                t3.setText("-");
            }

            if (count_alunch > 0) {
                avg_alunch = Double.valueOf(sum_alunch / count_alunch);
                t4.setText(String.valueOf(avg_alunch));
            } else{
                t4.setText("-");
            }

            if (count_bdinner > 0) {
                avg_bdinner = Double.valueOf(sum_bdinner / count_bdinner);
                t5.setText(String.valueOf(avg_bdinner));
            } else{
                t5.setText("-");
            }

            if (count_adinner > 0) {

                avg_adinner = Double.valueOf(sum_adinner / count_adinner);
                t6.setText(String.valueOf(avg_adinner));
            } else{
                t6.setText("-");
            }

            if (count_random > 0) {
                avg_random = Double.valueOf(sum_random / count_random);
                t7.setText(String.valueOf(avg_random));
            }
            else{
                t7.setText("-");
            }
            if (count_other > 0) {
                avg_other = Double.valueOf(sum_other / count_other);
                t8.setText(String.valueOf(avg_other));
            }
            else{
                t8.setText("-");
            }

            if(count_other==0 && count_random==0 && count_adinner==0 && count_bdinner==0 && count_alunch==0&& count_blunch==0&& count_fast==0 && count_bf==0){
                Toast.makeText(activity, "No data inserted in Sugar Log", Toast.LENGTH_LONG).show();
            }

        }
    }

        protected Void getSugarEntries() {
            email = utils.getEmail(this);
            sugarEntries = db.getSugarEntries(email);
            return null;
        }

}

