package com.example.demo1.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.demo1.DocuFiliatoriosActivity;
import com.example.demo1.MainActivity;
import com.example.demo1.ProductoActivity;
import com.example.demo1.QRandBarCodeActivity;
import com.example.demo1.R;
import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.SsdkUnsupportedException;
import com.hp.jetadvantage.link.api.config.ConfigService;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.scanner.ScannerService;

import java.lang.ref.WeakReference;

public class InitializationTask extends AsyncTask<Void, Void, InitializationTask.InitStatus> {

    private static final String TAG = "InitializationTask";

    private Exception mException = null;

    private final WeakReference<MainActivity> mContextRef;
    private final WeakReference<DocuFiliatoriosActivity> mContextRef2;
    private final WeakReference<ProductoActivity> mContextRef3;
    private final WeakReference<QRandBarCodeActivity> mContextRef4;

    public InitializationTask(MainActivity context){
        this.mContextRef = new WeakReference<>(context);
        this.mContextRef2 = null;
        this.mContextRef3 = null;
        this.mContextRef4 = null;
    }

    public InitializationTask(DocuFiliatoriosActivity context){
        this.mContextRef = null;
        this.mContextRef2 = new WeakReference<>(context);
        this.mContextRef3 = null;
        this.mContextRef4 = null;
    }

    public InitializationTask(ProductoActivity context){
        this.mContextRef = null;
        this.mContextRef2 = null;
        this.mContextRef3 = new WeakReference<>(context);
        this.mContextRef4 = null;
    }

    public InitializationTask(QRandBarCodeActivity context){
        this.mContextRef = null;
        this.mContextRef2 = null;
        this.mContextRef3 = null;
        this.mContextRef4 = new WeakReference<>(context);
    }

    @Override
    protected InitStatus doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");
        InitStatus status = InitStatus.NO_ERROR;

        try{
            // initialize the JetAdvantageLink with app context
            if (mContextRef != null) {
                JetAdvantageLink.getInstance().initialize(mContextRef.get());
            } else if (mContextRef2 != null) {
                JetAdvantageLink.getInstance().initialize(mContextRef2.get());
            } else if (mContextRef3 != null) {
                JetAdvantageLink.getInstance().initialize(mContextRef3.get());
            } else if (mContextRef4 != null) {
                JetAdvantageLink.getInstance().initialize(mContextRef4.get());
            }

        }catch (final SsdkUnsupportedException e){
            Log.e(TAG, "SDK is not supported!", e);
            mException = e;
            status = InitStatus.INIT_EXCEPTION;
        } catch (final SecurityException e){
            Log.e(TAG, "Security exception!", e);
            mException = e;
            status = InitStatus.INIT_EXCEPTION;
        }

        // Check if ScannerService is supported
        if (mContextRef != null) {
            if (status == InitStatus.NO_ERROR && (!ScannerService.isSupported(mContextRef.get()) || !JobService.isSupported(mContextRef.get()) || !ConfigService.isSupported(mContextRef.get()))) {
                // ScannerService is not supported on this device
                status = InitStatus.NOT_SUPPORTED;
            }
        } else if (mContextRef2 != null) {
            if (status == InitStatus.NO_ERROR && (!ScannerService.isSupported(mContextRef2.get()) || !JobService.isSupported(mContextRef2.get()))) {
                // ScannerService is not supported on this device
                status = InitStatus.NOT_SUPPORTED;
            }
        } else if (mContextRef3 != null) {
            if (status == InitStatus.NO_ERROR && (!ScannerService.isSupported(mContextRef3.get()) || !JobService.isSupported(mContextRef3.get()))) {
                // ScannerService is not supported on this device
                status = InitStatus.NOT_SUPPORTED;
            }
        } else if (mContextRef4 != null) {
            if (status == InitStatus.NO_ERROR && (!ScannerService.isSupported(mContextRef4.get()) || !JobService.isSupported(mContextRef4.get()))) {
                // ScannerService is not supported on this device
                status = InitStatus.NOT_SUPPORTED;
            }
        }
        return status;
    }


    @Override
    protected void onPostExecute(final InitializationTask.InitStatus status) {
        if (mContextRef!=null){
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

    }

    public enum InitStatus {
        INIT_EXCEPTION,
        NOT_SUPPORTED,
        NO_ERROR
    }
}
