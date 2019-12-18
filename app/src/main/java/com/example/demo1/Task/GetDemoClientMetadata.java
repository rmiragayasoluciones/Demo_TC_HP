package com.example.demo1.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.demo1.AperturaCuentaMainActivity;
import com.example.demo1.UserClass.DemoClientMetadataViewModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/** ejemplo Enzo https://api.myjson.com/bins/hld9o */
/** ejemplo Andy https://api.myjson.com/bins/fv7m4*/
/** ejemplo Gast https://api.myjson.com/bins/16r0nw*/

public class GetDemoClientMetadata extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "GetDemoClientMetadata";

    private WeakReference<AperturaCuentaMainActivity> mContextRef;
    private String clientId;

    public GetDemoClientMetadata(AperturaCuentaMainActivity mContextRef, String clientId) {
        this.mContextRef = new WeakReference<>(mContextRef) ;
        this.clientId = clientId;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");
//        RequestQueue queue = Volley.newRequestQueue(mContextRef.get());
        RequestQueue queue = VolleySingleton.getInstance(mContextRef.get()).getmRequestQueue();
        /**
         * FOR DEMO
         * forzar la busqueda al myJson armado
         * */
        if (this.clientId.equalsIgnoreCase("11")){
            this.clientId= "hld9o";
        }else if (this.clientId.equalsIgnoreCase("22")){
            this.clientId = "fv7m4";
        } else if (this.clientId.equalsIgnoreCase("33")){
            this.clientId = "16r0nw";
        } else if (this.clientId.equalsIgnoreCase("44")){
            this.clientId = "525252";
        }else if (this.clientId.equalsIgnoreCase("55")){
            this.clientId = "5656996666666";
        }
        Log.d(TAG, "idCliente= " + this.clientId);
        String url = "https://api.myjson.com/bins/" + this.clientId;
        /** FOR DEMO*/

        JsonObjectRequest jsonObjectRequestrequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response is: "+ response.toString() );
                        createObjectFromResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: call");
                Log.d(TAG, "onErrorResponse: volleyError " + error.toString());
                //Api Response NOT HTTP 200(OK)

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    mContextRef.get().onClientError("TimeOut Error");
                } else if (error instanceof AuthFailureError) {
                    mContextRef.get().onClientError("AuthFailureError Error");
                } else if (error instanceof ServerError) {
                    Log.d(TAG, "status Code: " + error.networkResponse.statusCode);
                    mContextRef.get().volleyResponseError("status Code: " + error.networkResponse.statusCode +
                            "\n esto es un error re feo, ojalá nunca les pase...");
                } else if (error instanceof NetworkError) {
                    mContextRef.get().onClientError("NetworkError Error");
                } else if (error instanceof ParseError) {
                    mContextRef.get().onClientError("ParseError Error");
                } else {
                    mContextRef.get().onClientError("Error status code: " + error.networkResponse.statusCode);
                }

            }
        });
        queue.add(jsonObjectRequestrequest);
        return null;
    }

    /** Crea el DemoViewModel*/
    private void createObjectFromResponse(JSONObject jsonObject){
        Gson gson = new Gson();
        DemoClientMetadataViewModel demoClientMetadataViewModel = gson.fromJson(jsonObject.toString(), DemoClientMetadataViewModel.class);
        Log.d(TAG, demoClientMetadataViewModel.toString());
        mContextRef.get().onClientCorrect(demoClientMetadataViewModel);
        return;
    }
}
