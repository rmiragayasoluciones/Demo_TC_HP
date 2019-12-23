package com.example.demo1.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.example.demo1.DocuFiliatoriosActivity;

import java.lang.ref.WeakReference;
import java.util.List;

public class CreateDocument extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "CreateDocument";
    private WeakReference<DocuFiliatoriosActivity> mContext;
    private List<String> listaPathFiles;

    public CreateDocument(DocuFiliatoriosActivity context, List<String> listaPathFiles) {
        this.mContext = new WeakReference<>(context);
        this.listaPathFiles = listaPathFiles;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");

        RequestQueue queue = VolleySingleton.getInstance(mContext.get()).getmRequestQueue();

        return null;
    }

}
