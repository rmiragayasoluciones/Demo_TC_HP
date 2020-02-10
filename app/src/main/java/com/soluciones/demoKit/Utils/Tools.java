package com.soluciones.demoKit.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;
import com.soluciones.demoKit.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Tools {
    private static final String TAG = "Tools";

    private static final String VIEWPAGERPOSITION = "sharedpref";
    public static final String TOKEN = "demoToken";
    private static final String URL = "url";


    @SuppressLint("ApplySharedPref")
    public static void saveUrlonSharedPreference(Context context, String url){
        Log.d(TAG, "saveUrlonSharedPreference: call");
        if (url.isEmpty()){
            url = "https://apikitdemohp.soluciones.com.ar/api";
        }
        Log.d(TAG, "se guarda la url : " + url);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Tools.URL, url);
        editor.commit();
    }

    public static String getUrlFromConfirg(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Log.d(TAG, "url guardada: " + sharedPreferences.getString(Tools.URL, ""));
        return sharedPreferences.getString(Tools.URL, "https://apikitdemohp.soluciones.com.ar/api");
    }

    @SuppressLint("ApplySharedPref")
    public static void saveViewPagerPosition(Context context, int position){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Tools.VIEWPAGERPOSITION, position);
        editor.commit();
    }

    public static int getViewPagerPosition(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Log.d(TAG, "url guardada: " + sharedPreferences.getInt(Tools.VIEWPAGERPOSITION, 0));
        return sharedPreferences.getInt(Tools.VIEWPAGERPOSITION, 0);
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

    public static void showSnackbar(View layout){
        Log.d(TAG, "showSnackbar: call");
        Snackbar snackbar = Snackbar.make(layout, layout.getContext().getString(R.string.scan20hojas), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

//    public static String replaceSpecialCharacters(String json){
//            json = json.replaceAll("á", "\u00E1");
//            json = json.replaceAll("é", "\u00E9");
//            json = json.replaceAll("í", "\u00ED");
//            json = json.replaceAll("ó", "\u00F3");
//            json = json.replaceAll("ú", "\u00FA");
//            json = json.replaceAll("ñ", "\u00F1");
//        return json;
//
//    }

}
