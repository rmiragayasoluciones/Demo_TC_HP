package com.example.demo1.UserClass;

import android.os.AsyncTask;

import com.example.demo1.MainActivity;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScannerService;

import java.lang.ref.WeakReference;

public class LoadDefaultsTask extends AsyncTask<Void, Void, ScanAttributes> {
    private static final String TAG = "LoadDefaultsTask";
    private final WeakReference<MainActivity> mCtxMainClall;

    private Result result;

    public LoadDefaultsTask(final MainActivity context) {
        this.mCtxMainClall = new WeakReference<>(context);
        this.result = new Result();
    }

    @Override
    protected ScanAttributes doInBackground(Void... voids) {
        return ScannerService.getDefaults(mCtxMainClall.get(), result);
    }

    @Override
    protected void onPostExecute(ScanAttributes defaultcaps) {
        super.onPostExecute(defaultcaps);
        mCtxMainClall.get().getDefaultCaps(defaultcaps);

    }
}
