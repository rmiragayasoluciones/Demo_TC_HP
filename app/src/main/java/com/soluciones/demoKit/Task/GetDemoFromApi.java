package com.soluciones.demoKit.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.soluciones.demoKit.MainActivity;
import com.soluciones.demoKit.R;
import com.soluciones.demoKit.UserClass.DemoViewModel;
import com.soluciones.demoKit.UserClass.DemoViewModelSingleton;
import com.soluciones.demoKit.Utils.Tools;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * hace GetDemo con el token a la api
 *
 */

public class GetDemoFromApi extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "GetDemoFromApi";

    private final WeakReference<MainActivity> mContextRef;
    private String tokenCliente;

    public GetDemoFromApi(final WeakReference<MainActivity> context, final String token) {
        this.mContextRef = context;
        this.tokenCliente = token;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");
        RequestQueue queue = VolleySingleton.getInstance(mContextRef.get()).getmRequestQueue();
        Log.d(TAG, "tokenCliente= " + this.tokenCliente);
        String preUrl = Tools.getUrlFromConfirg(mContextRef.get());
        String url = preUrl + "/Demos/GetDemo/" + this.tokenCliente;

        Log.d(TAG, "doInBackground: get demo: " + url);

        JsonObjectRequest jsonObjectRequestrequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        saveInSingleton(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Api Response NOT HTTP 200(OK)
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError){
                    Log.d(TAG, "onErrorResponse: dio timeout");
                    mContextRef.get().onTokenError(mContextRef.get().getString(R.string.error_conexion));
                    return;
                }
                mContextRef.get().onTokenError(error.getMessage());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Token", tokenCliente);
                return params;
            }
        };
        queue.add(jsonObjectRequestrequest);
        return null;

    }

    /** Crea el DemoViewModel y guarda en un singleton */
    private void saveInSingleton(JSONObject jsonObject){
        Log.d(TAG, "jsonObject");

        String tokenExp = null;
        try {
            tokenExp = jsonObject.get("tokenExpirationTime").toString();
            Log.d(TAG, "tokenExpirationTime: " + tokenExp);
            jsonObject.put("tokenExpirationTime", Tools.convertDateTimeInDateString(jsonObject.get("tokenExpirationTime").toString()));
            jsonObject.put("creationTime", Tools.convertDateTimeInDateString(jsonObject.get("creationTime").toString()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        DemoViewModel demoViewModel = gson.fromJson(jsonObject.toString(), DemoViewModel.class);
        DemoViewModelSingleton demoViewModelSingleton = DemoViewModelSingleton.getInstance(demoViewModel);
        Log.d(TAG, "################################");
        Log.d(TAG, "saveInSingleton: " + demoViewModelSingleton.getDemoViewModelGuardado().toString());
        Log.d(TAG, "################################");
        verificarEstadoDemo(demoViewModel);

    }

    /** verifica el estado de la demo una vez recibida de la api*/
    private void verificarEstadoDemo(DemoViewModel demoViewModel){
        //esta activa?
        if (!demoViewModel.isActive()){
            mContextRef.get().onTokenError(mContextRef.get().getString(R.string.token_no_activo));
            //está expirada?
        } else if (demoViewModel.isExpired()){
            mContextRef.get().onTokenError(mContextRef.get().getString(R.string.token_expirado));
            //toodo perfecto, vuelve a Main
        } else {

            mContextRef.get().onApiResponseComplete();
        }
    }


}























