package com.example.diabetestracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="Diabetes.db";
    //USER table-Create
    private static final String CREATE_TABLE_USER="CREATE TABLE "+DatabaseContract.UsersTable.TABLE_NAME+
            " ("+DatabaseContract.UsersTable._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            DatabaseContract.UsersTable.COL_NAME+" TEXT NOT NULL, "+
            DatabaseContract.UsersTable.COL_EMAIL+" TEXT NOT NULL, "+
            DatabaseContract.UsersTable.COL_DOB+" TEXT NOT NULL, "+
            DatabaseContract.UsersTable.COL_GENDER+" TEXT NOT NULL, "+
            DatabaseContract.UsersTable.COL_PASSWORD+" TEXT NOT NULL)";
    //SUGAR table-Create
    private static final String CREATE_TABLE_SUGAR="CREATE TABLE "+DatabaseContract.SugarTable.TABLE_NAME+
            " ("+DatabaseContract.SugarTable._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            DatabaseContract.SugarTable.COL_CONCENTRATION+" INTEGER NOT NULL, "+
            DatabaseContract.SugarTable.COL_MEASURED+" TEXT NOT NULL, "+
            DatabaseContract.SugarTable.COL_DATE+" TEXT NOT NULL, "+
            DatabaseContract.SugarTable.COL_TIME+" TEXT NOT NULL, "+
            DatabaseContract.SugarTable.COL_EMAIL+" TEXT NOT NULL)";
    //WEIGHT table-Create
    private static final String CREATE_TABLE_WEIGHT="CREATE TABLE "+DatabaseContract.WeightTable.TABLE_NAME+
            " ("+DatabaseContract.WeightTable._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            DatabaseContract.WeightTable.COL_WEIGHT+" REAL NOT NULL, "+
            DatabaseContract.WeightTable.COL_DATE+" TEXT NOT NULL, "+
            DatabaseContract.WeightTable.COL_TIME+" TEXT NOT NULL, "+
            DatabaseContract.WeightTable.COL_EMAIL+" TEXT NOT NULL)";
    //MEDICATION table-Create
    private static final String CREATE_TABLE_MEDICATION="CREATE TABLE "+DatabaseContract.MedicationsTable.TABLE_NAME+
            " ("+DatabaseContract.MedicationsTable._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            DatabaseContract.MedicationsTable.COL_MEDNAME+" TEXT NOT NULL, "+
            DatabaseContract.MedicationsTable.COL_DOSGAE+" REAL NOT NULL, "+
            DatabaseContract.MedicationsTable.COL_UNIT+" TEXT NOT NULL, "+
            DatabaseContract.MedicationsTable.COL_DATE+" TEXT NOT NULL, "+
            DatabaseContract.MedicationsTable.COL_TIME+" TEXT NOT NULL, "+
            DatabaseContract.MedicationsTable.COL_EMAIL+" TEXT NOT NULL)";
    //constructor
    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_SUGAR);
            db.execSQL(CREATE_TABLE_WEIGHT);
            db.execSQL(CREATE_TABLE_MEDICATION);
        }catch(SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addUser(User user)
    {
        long rowID=0;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DatabaseContract.UsersTable.COL_NAME,user.getName());
        values.put(DatabaseContract.UsersTable.COL_EMAIL,user.getEmail());
        values.put(DatabaseContract.UsersTable.COL_DOB,user.getDOB());
        values.put(DatabaseContract.UsersTable.COL_GENDER,user.getGender());
        values.put(DatabaseContract.UsersTable.COL_PASSWORD,user.getPassword());
        try{
            rowID=db.insert(DatabaseContract.UsersTable.TABLE_NAME,null,values);
        }catch (SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }
        if(rowID==-1)
            return false;
        else
            return true;
    }
    public boolean checkUser(String email)
    {
        String[] columns={DatabaseContract.UsersTable.COL_NAME};
        SQLiteDatabase db=this.getWritableDatabase();
        String[] selectionArgs={email};
        String selection=DatabaseContract.UsersTable.COL_EMAIL+" = ?";
        int cursorcount=0;
        try{
            Cursor cursor=db.query(DatabaseContract.UsersTable.TABLE_NAME,columns,selection,selectionArgs,null,null,null);
            cursorcount=cursor.getCount();
            cursor.close();
            db.close();
        }catch (SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }
        if(cursorcount>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean checkUser(String email,String password)
    {
        String[] columns={DatabaseContract.UsersTable.COL_NAME};
        SQLiteDatabase db=this.getWritableDatabase();
        String selection=DatabaseContract.UsersTable.COL_EMAIL+" = ?"+" AND "+DatabaseContract.UsersTable.COL_PASSWORD+" = ?";
        String[] selectionArgs={email,password};
        int cursorcount=0;
        try{
            Cursor cursor=db.query(DatabaseContract.UsersTable.TABLE_NAME,columns,selection,selectionArgs,null,null,null);
            cursorcount=cursor.getCount();
            cursor.close();
            db.close();
        }catch (SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }
        if(cursorcount>0)
            return true;
        else
            return false;
    }
    public User getUser(String email)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String selection=DatabaseContract.UsersTable.COL_EMAIL+" = ?";
        String[] selectionArgs={email};
        User user=new User();
        int cursorCount=0;
        try{
            Cursor cursor=db.query(DatabaseContract.UsersTable.TABLE_NAME,null,selection,selectionArgs,null,null,null);
            cursorCount=cursor.getCount();
            if(cursorCount>0)
            {
                while(cursor.moveToNext())
                {
                    user.setId(cursor.getInt(0));
                    user.setName(cursor.getString(1));
                    user.setEmail(cursor.getString(2));
                    user.setDOB(cursor.getString(3));
                    user.setGender(cursor.getString(4));
                    user.setPassword(cursor.getString(5));
                }

                Log.d("TAG","Query Sucessfull");

            }
        }catch (SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }
        return user;
    }
    public boolean updateUser(String email,User user)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String selection=DatabaseContract.UsersTable.COL_EMAIL+" = ?";
        String[]selectionArgs={email};
        ContentValues values=new ContentValues();
        values.put(DatabaseContract.UsersTable.COL_NAME,user.getName());
        values.put(DatabaseContract.UsersTable.COL_DOB,user.getDOB());
        values.put(DatabaseContract.UsersTable.COL_GENDER,user.getGender());
        values.put(DatabaseContract.UsersTable.COL_PASSWORD,user.getPassword());
        try {
            db.update(DatabaseContract.UsersTable.TABLE_NAME, values, selection, selectionArgs);
            db.close();
            return true;
        }catch(SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
            return false;
        }
    }

    //sugar
    public boolean addSugar(Sugar sugar)
    {
        long row=0;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DatabaseContract.SugarTable.COL_CONCENTRATION,sugar.getConcentration());
        values.put(DatabaseContract.SugarTable.COL_MEASURED,sugar.getMeasured());
        values.put(DatabaseContract.SugarTable.COL_DATE,sugar.getDate());
        values.put(DatabaseContract.SugarTable.COL_TIME,sugar.getTime());
        values.put(DatabaseContract.SugarTable.COL_EMAIL,sugar.getEmail());
        try{
            row=db.insert(DatabaseContract.SugarTable.TABLE_NAME,null,values);
        }
        catch(SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }
        if(row==-1)
            return false;
        else
            return true;
    }
    public ArrayList<Sugar> getSugarEntries(String email)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String selection=DatabaseContract.SugarTable.COL_EMAIL+" = ?";
        String[] selectionArgs={email};
        String order=DatabaseContract.SugarTable._ID+" DESC";
        //  Sugar sugar=new Sugar();
        int i=0;
        ArrayList<Sugar> sugarEntries=new ArrayList<>();
        try{
            Cursor cursor=db.query(DatabaseContract.SugarTable.TABLE_NAME,null,selection,selectionArgs,null,null,order);
            while(cursor.moveToNext())
            {
                Sugar sugar=new Sugar();
                sugar.setId(cursor.getInt(0));
                sugar.setConcentration(cursor.getInt(1));
                sugar.setMeasured(cursor.getString(2));
                sugar.setDate(cursor.getString(3));
                sugar.setTime(cursor.getString(4));
                sugar.setEmail(cursor.getString(5));
                sugarEntries.add(sugar);
            }
        }catch (SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }
        return sugarEntries;
    }
    public boolean deleteSugarRecord(String email,String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=DatabaseContract.SugarTable.COL_EMAIL+" =? "+" AND "+DatabaseContract.SugarTable._ID+" =? ";
        String[] selectionArgs={email,id};
//        Log.d("TAG",email);
        Log.d("TAG",id);
        int i=0;
        try{
            i=db.delete(DatabaseContract.SugarTable.TABLE_NAME,where,selectionArgs);
            Log.d("TAG",String.valueOf(i));
        }catch(SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }

        if(i>0)
            return true;
        return false;
    }
    public boolean updateSugar(Sugar sugar)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DatabaseContract.SugarTable.COL_CONCENTRATION,sugar.getConcentration());
        values.put(DatabaseContract.SugarTable.COL_DATE,sugar.getDate());
        values.put(DatabaseContract.SugarTable.COL_TIME,sugar.getTime());
        values.put(DatabaseContract.SugarTable.COL_MEASURED,sugar.getMeasured());
        String selection=DatabaseContract.SugarTable._ID+" =?";
        String[] selectionArgs={String.valueOf(sugar.getId())};
        try
        {
            db.update(DatabaseContract.SugarTable.TABLE_NAME,values,selection,selectionArgs);
            db.close();
            return true;
        }catch (SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
            return false;
        }
    }

    //medication
    public boolean addMed(Medicine medicine)
    {
        long row=0;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DatabaseContract.MedicationsTable.COL_MEDNAME,medicine.getMedication());
        values.put(DatabaseContract.MedicationsTable.COL_DOSGAE,medicine.getDosage());
        values.put(DatabaseContract.MedicationsTable.COL_UNIT,medicine.getUnit());
        values.put(DatabaseContract.MedicationsTable.COL_DATE,medicine.getDate());
        values.put(DatabaseContract.MedicationsTable.COL_TIME,medicine.getTime());
        values.put(DatabaseContract.MedicationsTable.COL_EMAIL,medicine.getEmail());
        try{
            row=db.insert(DatabaseContract.MedicationsTable.TABLE_NAME,null,values);
        }
        catch(SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }
        if(row==-1)
            return false;
        else
            return true;
    }
    public ArrayList<Medicine> getMedEntries(String email)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String selection=DatabaseContract.MedicationsTable.COL_EMAIL+" = ?";
        String[] selectionArgs={email};
        String order=DatabaseContract.MedicationsTable._ID+" DESC";
        //  Medicine med=new Medicine();
        int i=0;
        ArrayList<Medicine> medEntries=new ArrayList<>();
        try{
            Cursor cursor=db.query(DatabaseContract.MedicationsTable.TABLE_NAME,null,selection,selectionArgs,null,null,order);
            while(cursor.moveToNext())
            {
                Medicine medicine=new Medicine();
                medicine.setId(cursor.getInt(0));
                medicine.setMedication(cursor.getString(1));
                medicine.setDosage(cursor.getInt(2));
                medicine.setUnit(cursor.getString(3));
                medicine.setDate(cursor.getString(4));
                medicine.setTime(cursor.getString(5));
                medicine.setEmail(cursor.getString(6));
                medEntries.add(medicine);
            }
        }catch (SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }
        return medEntries;
    }
    public boolean deleteMedRecord(String email,String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=DatabaseContract.MedicationsTable.COL_EMAIL+" =? "+" AND "+DatabaseContract.MedicationsTable._ID+" =? ";
        String[] selectionArgs={email,id};
//        Log.d("TAG",email);
        Log.d("TAG",id);
        int i=0;
        try{
            i=db.delete(DatabaseContract.MedicationsTable.TABLE_NAME,where,selectionArgs);
            Log.d("TAG",String.valueOf(i));
        }catch(SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }

        if(i>0)
            return true;
        return false;
    }
    public boolean updateMed(Medicine medicine)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DatabaseContract.MedicationsTable.COL_MEDNAME,medicine.getMedication());
        values.put(DatabaseContract.MedicationsTable.COL_DOSGAE,medicine.getDosage());
        values.put(DatabaseContract.MedicationsTable.COL_DATE,medicine.getDate());
        values.put(DatabaseContract.MedicationsTable.COL_TIME,medicine.getTime());
        values.put(DatabaseContract.MedicationsTable.COL_UNIT,medicine.getUnit());
        String selection=DatabaseContract.MedicationsTable._ID+" =?";
        String[] selectionArgs={String.valueOf(medicine.getId())};
        try {
            db.update(DatabaseContract.MedicationsTable.TABLE_NAME, values, selection, selectionArgs);
            db.close();
            return true;
        } catch (SQLiteException e) {
            Log.d("TAG", e.getMessage());
            return false;
        }
    }

    //weight
    public boolean addWeight(Weight weight) {
        long row = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.WeightTable.COL_WEIGHT, weight.getWeight());
        values.put(DatabaseContract.WeightTable.COL_DATE, weight.getDate());
        values.put(DatabaseContract.WeightTable.COL_TIME, weight.getTime());
        values.put(DatabaseContract.WeightTable.COL_EMAIL, weight.getEmail());
        try {
            row = db.insert(DatabaseContract.WeightTable.TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            Log.d("TAG", e.getMessage());
        }
        if (row == -1)
            return false;
        else
            return true;
    }

    public ArrayList<Weight> getWeightEntries(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = DatabaseContract.WeightTable.COL_EMAIL + " = ?";
        String[] selectionArgs = {email};
        String order = DatabaseContract.WeightTable._ID + " DESC";

        int i = 0;
        ArrayList<Weight> weightEntries = new ArrayList<>();
        try {
            Cursor cursor = db.query(DatabaseContract.WeightTable.TABLE_NAME, null, selection, selectionArgs, null, null, order);
            while (cursor.moveToNext()) {
                Weight weight = new Weight();
                weight.setId(cursor.getInt(0));
                weight.setWeight(cursor.getInt(1));
                weight.setDate(cursor.getString(2));
                weight.setTime(cursor.getString(3));
                weight.setEmail(cursor.getString(4));
                weightEntries.add(weight);
            }
        } catch (SQLiteException e) {
            Log.d("TAG", e.getMessage());
        }
        return weightEntries;
    }

    public boolean deleteWeightRecord(String email,String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=DatabaseContract.WeightTable.COL_EMAIL+" =? "+" AND "+DatabaseContract.WeightTable._ID+" =? ";
        String[] selectionArgs={email,id};
//        Log.d("TAG",email);
        Log.d("TAG",id);
        int i=0;
        try{
            i=db.delete(DatabaseContract.WeightTable.TABLE_NAME,where,selectionArgs);
            Log.d("TAG",String.valueOf(i));
        }catch(SQLiteException e)
        {
            Log.d("TAG",e.getMessage());
        }

        if(i>0)
            return true;
        return false;
    }

    public boolean updateWeight(Weight weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.WeightTable.COL_WEIGHT, weight.getWeight());
        values.put(DatabaseContract.WeightTable.COL_DATE, weight.getDate());
        values.put(DatabaseContract.WeightTable.COL_TIME, weight.getTime());
        String selection = DatabaseContract.MedicationsTable._ID + " =?";
        String[] selectionArgs = {String.valueOf(weight.getId())};
        try {
            db.update(DatabaseContract.WeightTable.TABLE_NAME, values, selection, selectionArgs);
            db.close();
            return true;
        } catch (SQLiteException e) {
            Log.d("TAG", e.getMessage());
            return false;
        }
    }

}