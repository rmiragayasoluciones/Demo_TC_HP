package com.example.demo1.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.demo1.MainActivity;
import com.example.demo1.R;
import com.example.demo1.UserClass.DemoViewModel;
import com.example.demo1.UserClass.DemoViewModelSingleton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

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
        /* FOR DEMO HARCODEO 06b39eb6-fba6-477c-a5e1-57d95f8a8b1b */
//        this.tokenCliente = "tokenexp";
        /* FOR DEMO HARCODEO 06b39eb6-fba6-477c-a5e1-57d95f8a8b1b */
//        String url = "http://10.13.0.34:5656/api/Demos/GetDemo/" + this.tokenCliente;
        String url = "http://10.13.0.34:5656/api/Demos/GetDemo/" + this.tokenCliente;

        JsonObjectRequest jsonObjectRequestrequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response is: "+ response.toString());
                        saveInSingleton(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Api Response NOT HTTP 200(OK)
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                mContextRef.get().onTokenError(error.getMessage());
            }
        });
        queue.add(jsonObjectRequestrequest);
        return null;

    }

    /** Crea el DemoViewModel y guarda en un singleton */
    private void saveInSingleton(JSONObject jsonObject){
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























