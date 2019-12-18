package com.example.demo1.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.demo1.MainActivity;
import com.example.demo1.R;
import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.SsdkUnsupportedException;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.scanner.ScannerService;

import java.lang.ref.WeakReference;

public class InitializationTask extends AsyncTask<Void, Void, InitializationTask.InitStatus> {

    private static final String TAG = "InitializationTask";

    private Exception mException = null;

    private final WeakReference<MainActivity> mContextRef;

    public InitializationTask(MainActivity context){
        this.mContextRef = new WeakReference<>(context);
    }

    @Override
    protected InitStatus doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");
        InitStatus status = InitStatus.NO_ERROR;

        try{
            // initialize the JetAdvantageLink with app context
            JetAdvantageLink.getInstance().initialize(mContextRef.get());
        }catch (final SsdkUnsupportedException e){
            Log.e(TAG, "SDK is not supported!", e);
            mException = e;
            status = InitStatus.INIT_EXCEPTION;
        } catch (final SecurityException e){
            Log.e(TAG, "Security exception!", e);
            mException = e;
            status = InitStatus.INIT_EXCEPTION;
        }

        if (status == InitStatus.NO_ERROR
                && (!ScannerService.isSupported(mContextRef.get())
                || !JobService.isSupported(mContextRef.get()))) {
            // ScannerService is not supported on this device
            status = InitStatus.NOT_SUPPORTED;
        }

        return status;
    }


    @Override
    protected void onPostExecute(final InitializationTask.InitStatus status) {
        if (status == InitStatus.NO_ERROR) {
            mContextRef.get().handleComplete();
            return;
        }

        switch (status) {
            case INIT_EXCEPTION:
                mContextRef.get().handleException(mException);
                break;
            case NOT_SUPPORTED:
                mContextRef.get().handleException(new Exception(mContextRef.get().getString(R.string.service_not_supported)));
                break;
            default:
                mContextRef.get().handleException(new Exception(mContextRef.get().getString(R.string.unknown_error)));
        }
    }

    public enum InitStatus {
        INIT_EXCEPTION,
        NOT_SUPPORTED,
        NO_ERROR
    }
}
