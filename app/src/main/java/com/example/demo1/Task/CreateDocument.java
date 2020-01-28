package com.example.demo1.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.demo1.R;
import com.example.demo1.UserClass.DemoViewModelSingleton;
import com.example.demo1.UserClass.Error500;
import com.example.demo1.Utils.Tools;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class CreateDocument extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "CreateDocument";
    private static final String URL = "http://10.13.0.34:5656/api/Documents/Create";

    private OnCreateDocumentsListener mListener;

    private WeakReference<Context> mContext;
    private String pathFiles;
    private String fileName;
    private String createDocumentVMAsString;
    private String mensajeError;

    public CreateDocument(Context context, String pathFiles, String fileName, String createDocumentVMAsString) {
        Log.d(TAG, "LLega a CreateDocument");
        Log.d(TAG, "pathFiles: " + pathFiles);
        Log.d(TAG, "fileName: " + fileName);
        Log.d(TAG, "createDocumentVMAsString: " + createDocumentVMAsString);

        this.mContext = new WeakReference<>(context);
        this.pathFiles = pathFiles;
        this.fileName = fileName;
        this.mListener = (OnCreateDocumentsListener) context;
        this.createDocumentVMAsString = createDocumentVMAsString;

    }


    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");

        if (!Tools.isOnline()){
            mListener.onCreateDocumentError(mContext.get().getString(R.string.error_conexion));
            return null;
        }

        final String token = DemoViewModelSingleton.getInstance().getDemoViewModelGuardado().getToken();
        final RequestQueue queue = VolleySingleton.getInstance(mContext.get()).getmRequestQueue();

        final VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d(TAG, "onResponse: call");
                        Log.d(TAG, "statusCode: " + response.statusCode );
                        try {
                            JSONArray obj = new JSONArray(new String(response.data));
                            Log.d(TAG, "obj: " + obj);
                            mListener.onCreateDocumentComplete();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: CALL");
                Log.d(TAG, "getMessage " + error.getMessage());
                Log.d(TAG, ".getCause() " + error.getCause());

                queue.stop();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.d(TAG, "TimeOut error o No ConnectionError");
                    mListener.onCreateDocumentError(mContext.get().getString(R.string.error_conexion));
                    return;
                }
//                Log.d(TAG, ".getCause() " + error.networkResponse.statusCode);

                /** Mapping el error */
                byte[] byteErrorData =  error.networkResponse.data;
                try {
                    mensajeError = new String(byteErrorData, "UTF-8");
                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                Error500 error500 = gson.fromJson(mensajeError, Error500.class);

//                List<Header> headerList  = error.networkResponse.allHeaders;
//                for (Header h : headerList){
//                    Log.d(TAG, "Header.getName(): " + h.getName());
//                    Log.d(TAG, "Header.getValue(): " + h.getValue());
//                    Log.d(TAG, "Header.toString(): " + h.toString());
//                }

                error.printStackTrace();
                error.getMessage();
                //Api Response NOT HTTP 200(OK)
                if (error instanceof ServerError) {
                    mListener.onCreateDocumentError(error500.getDisplayError());
                    Log.d(TAG, "onErrorResponse: " + error500.getError());
                    Log.d(TAG, "Error de Servidor");
                } else {
                    mListener.onCreateDocumentError(error500.getDisplayError());
                    Log.d(TAG, "Error inesperado: + error.networkResponse.statusCode");
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d(TAG, "Headers Agrego Token: " + token);
                params.put("Token", token);
                return params;
            }

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Model", createDocumentVMAsString);
                return params;
            }


            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                Log.d(TAG, "imagen a subir: " + fileName );
                params.put("File", new DataPart(fileName , getByteArrayFromFile(pathFiles)));
                return params;
            }
        };

        //setea el timeout y los retry
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(volleyMultipartRequest);

        return null;
    }

    public interface OnCreateDocumentsListener{
         void onCreateDocumentComplete();
         void onCreateDocumentError(String volleyError);
    }

    private byte[] getByteArrayFromFile(String filepath) {
        //init array with file length
        final File filePdfOriginal = new File(filepath);

        byte[] bytesArray = new byte[(int) filePdfOriginal.length()];

        try {
            FileInputStream fis = new FileInputStream(filePdfOriginal);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "error :" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesArray;
    }


}
