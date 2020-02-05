package com.soluciones.demoKit.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.soluciones.demoKit.MainActivity;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;

import java.lang.ref.WeakReference;

public class LoadCapabilitiesTask extends AsyncTask<Void, Void, ScanAttributesCaps> {
    private static final String TAG = "LoadCapabilitiesTask";
    private final WeakReference<MainActivity> mCtxMainClall;

    private Result result;
    private Exception mException = null;

    public LoadCapabilitiesTask (final MainActivity context){
        this.mCtxMainClall = new WeakReference<>(context);
        this.result = new Result();
    }


    @Override
    protected ScanAttributesCaps doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");

        try{
            ScanAttributesCaps caps = mCtxMainClall.get().requestCaps(mCtxMainClall.get(), result);
            return caps;

        } catch (IllegalStateException e) {
            Log.d(TAG, "catch CallIllegalStateException ");
            mException = e;
        } catch (Exception e) {
            Log.d(TAG, "catch Call");
            Log.d(TAG, "doInBackground: " + e.getMessage());
            Log.d(TAG, "doInBackground: " + e.getCause());
            e.printStackTrace();

            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ScanAttributesCaps caps) {
        Log.d(TAG, "onPostExecute: call");
        if (caps != null && result.getCode() == Result.RESULT_OK){
            Log.d(TAG, "onPostExecute: OK");
            // carla las por default
//            mContextRef.get().loadDefaults();

            //carga las opciones al xml
//            mFragment.get().loadCapabilities(caps);
            mCtxMainClall.get().getAllCaps(caps);

        } else {
            mCtxMainClall.get().showResult(mException.getMessage(), result);
            Log.d(TAG, "onPostExecute: FAIL");
            Log.d(TAG, "onPostExecute: " + mException.getMessage());
        }

    }
}
