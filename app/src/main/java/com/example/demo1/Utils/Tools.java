package com.example.demo1.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

public class Tools {
    private static final String TAG = "Tools";

    public static final String SHAREDPREF = "sharedpref";
    public static final String TOKEN = "demoToken";
    public static final String URL = "url";


    @SuppressLint("ApplySharedPref")
    public static void saveUrlonSharedPreference(Context context, String url){
        Log.d(TAG, "saveUrlonSharedPreference: call");
        if (url.isEmpty()){
            url = "http://10.13.0.34:5656/api";
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Tools.URL, url);
        editor.commit();
    }

    public static String getUrlFromConfirg(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Log.d(TAG, "url guardada: " + sharedPreferences.getString(Tools.URL, ""));
        return sharedPreferences.getString(Tools.URL, "http://10.13.0.34:5656/api");
    }


}
