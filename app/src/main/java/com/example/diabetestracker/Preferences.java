package com.example.diabetestracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    public static String KEY_EMAIL = "email";
    //constructor
    public Preferences(){}

    //save email in shared preferences
    public static boolean saveEmail(String email, Context context)
    {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor=prefs.edit();
        prefsEditor.putString(KEY_EMAIL,email);
        prefsEditor.apply();
        return true;
    }
    //get Email from shared preferences
    public static String getEmail(Context context)
    {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_EMAIL,null);
    }
}