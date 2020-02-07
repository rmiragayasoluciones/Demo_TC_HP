package com.soluciones.demoKit.Utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.soluciones.demoKit.ReporteDeErrores;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException( Thread paramThread, Throwable paramThrowable ) {
                        Log.d(TAG, "uncaughtException: call");
                        handleUncaughtException (paramThread, paramThrowable);
                    }
                });
    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        Log.d(TAG, "e.toString(): " + e.toString());
        Log.d(TAG, "e.getCause(): " + e.getCause());
        Log.d(TAG, "e.getMessage(): " + e.getMessage());
        Writer writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String s = writer.toString();
        Log.d(TAG, "e.fillInStackTrace(): " + s);

        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        Intent intent = new Intent (this, ReporteDeErrores.class);
        intent.putExtra("fillInStackTrace" , s);
        startActivity (intent);

        finish();
    }
}
