package com.example.demo1.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public static String convertDateTimeInDateString(String dateTimeString) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {
            return "";
        }

        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        return convetDateFormat.format(date);
    }

    public static String convertDateStringInDateTime(long milliseconds){
        Log.d(TAG, "milliseconds " + milliseconds);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        Log.d(TAG, "sdf.format(calendar.getTime(): " + sdf.format(calendar.getTime()));
        return sdf.format(calendar.getTime());

    }


}
