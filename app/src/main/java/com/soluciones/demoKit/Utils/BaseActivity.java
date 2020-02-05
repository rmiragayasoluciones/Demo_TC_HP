package com.soluciones.demoKit.Utils;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException( Thread paramThread, Throwable paramThrowable ) {
                        //Do your own error handling here

                        Log.d(TAG, "paramThread.getStackTrace(): " + paramThread.getStackTrace().toString());
                        Log.d(TAG, " paramThrowable.getCause(): " + paramThrowable.getCause());

                        if (oldHandler != null){
                            Log.d(TAG, "oldHandler != null: call");
                            oldHandler.uncaughtException( paramThread, paramThrowable);
                            //Delegates to Android's error handling
                        } else{
                            Log.d(TAG, "System.exit(2): call");
                            System.exit(2); //Prevents the service/app from freezing
                        }

                    }
                });
    }


    private void sendReport(String reporte){
        Log.d(TAG, "sendReport: call");

        String url= "https://jsonplaceholder.typicode.com/posts";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        
        try {
            Log.d(TAG, "try call");
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", reporte);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Response:  " + response.toString());
                    Toast.makeText(getApplicationContext(), "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();
                    System.exit(2);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "error: call " + error.networkResponse.statusCode);
                    onBackPressed();
                    System.exit(2);
                }
            });

            requestQueue.add(jsonObjectRequest);


        } catch (JSONException e){
            e.printStackTrace();
        }



       
        


    }
}
