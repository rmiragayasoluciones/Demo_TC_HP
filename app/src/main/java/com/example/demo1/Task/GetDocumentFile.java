package com.example.demo1.Task;

import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.demo1.DocumentPreviewActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class GetDocumentFile extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "GetDocumentFile";

    private WeakReference<DocumentPreviewActivity> mContext;
    private PdfDocument pdf;
    private String tokenCliente, idFile;


    public GetDocumentFile(DocumentPreviewActivity mContext , final String tokenCliente, String idFile) {
        this.mContext = new WeakReference<>(mContext);
        this.tokenCliente = tokenCliente;
        this.idFile = idFile;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");
        RequestQueue queue = VolleySingleton.getInstance(mContext.get()).getmRequestQueue();
        Log.d(TAG, "tokenCliente= " + this.tokenCliente);
        Log.d(TAG, "idFile= " + this.idFile);
        String url = "http://10.13.0.34:5656/api/Documents/GetDocumentFile/" + this.idFile;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse Ok Json: " + response.toString());
                        //todo call mContext para mandar la respuesta a la activity

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: cell");
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Token", tokenCliente);
                return params;
            }
        };
        queue.add(jsonObjectRequest);

        return null;
    }
}
