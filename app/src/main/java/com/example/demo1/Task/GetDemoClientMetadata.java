package com.example.demo1.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.demo1.AperturaCuentaMainActivity;
import com.example.demo1.R;
import com.example.demo1.UserClass.DemoViewModelSingleton;
import com.example.demo1.UserClass.MetadataCliente;
import com.example.demo1.Utils.Tools;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

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
        RequestQueue queue = VolleySingleton.getInstance(mContextRef.get()).getmRequestQueue();
        /**
         * FOR DEMO
         * forzar la busqueda al myJson armado
         * */
        Log.d(TAG, "idCliente= " + this.clientId);
        final String token = DemoViewModelSingleton.getInstance().getDemoViewModelGuardado().getToken();
        String preUrl = Tools.getUrlFromConfirg(mContextRef.get());
        String url = preUrl + "/Demos/GetDemoClientMetadata/" + token + "&" + this.clientId;
        /** FOR DEMO*/

        final JsonObjectRequest jsonObjectRequestrequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response is: "+ response.toString() );
                        createObjectFromResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: volleyError " + error.toString());
                //Api Response NOT HTTP 200(OK)
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    mContextRef.get().volleyResponseError(mContextRef.get().getString(R.string.error_conexion));
                } else if (error instanceof ServerError) {
                    mContextRef.get().volleyResponseError("Usuario no encontrado");
                } else {
                    mContextRef.get().volleyResponseError("Error inesperado: " + error.networkResponse.statusCode);
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Token", token);
                return params;
            }
        };

        jsonObjectRequestrequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequestrequest);
        return null;
    }

    /** Crea el DemoViewModel*/
    private void createObjectFromResponse(JSONObject jsonObject){
        Gson gson = new Gson();
        MetadataCliente metadataCliente = gson.fromJson(jsonObject.toString(), MetadataCliente.class);
        Log.d(TAG, metadataCliente.toString());
        mContextRef.get().onClientCorrect(metadataCliente);
        return;
    }
}
