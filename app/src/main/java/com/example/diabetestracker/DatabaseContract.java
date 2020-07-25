package com.example.diabetestracker;

import android.provider.BaseColumns;

public class DatabaseContract {
    public DatabaseContract(){ }
    /*Innner Class  that defines USER table contents*/
    public static abstract class UsersTable implements BaseColumns{
        public static final String TABLE_NAME="users";
        public static final String COL_NAME="name";
        public static final String COL_EMAIL="email";
        public static final String COL_DOB="dob";
        public static final String COL_GENDER="gender";
        public static final String COL_PASSWORD="password";
    }
    /*Innner Class  that defines SUGAR table contents*/
    public static abstract class SugarTable implements BaseColumns{
        public static final String TABLE_NAME="sugar";
        public static final String COL_CONCENTRATION="concentration";
        public static final String COL_MEASURED="measured";
        public static final String COL_DATE="date";
        public static final String COL_TIME="time";
        public static final String COL_EMAIL="email";
    }
    /*Innner Class  that defines WEIGHT table contents*/
    public static abstract class WeightTable implements BaseColumns{
        public static final String TABLE_NAME="weight";
        public static final String COL_WEIGHT="weight";
        public static final String COL_DATE="date";
        public static final String COL_TIME="time";
        public static final String COL_EMAIL="email";
    }
    /*Innner Class  that defines Medications table contents*/
    public static abstract class MedicationsTable implements BaseColumns{
        public static final String TABLE_NAME="medication";
        public static final String COL_MEDNAME="med_name";
        public static final String COL_DOSGAE="dosage";
        public static final String COL_UNIT="unit";
        public static final String COL_DATE="date";
        public static final String COL_TIME="time";
        public static final String COL_EMAIL="email";
    }
}