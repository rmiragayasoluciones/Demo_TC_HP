package com.example.demo1.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.demo1.DocumentPreviewActivity;
import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.SsdkUnsupportedException;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.printer.PrinterService;

import java.lang.ref.WeakReference;

public class InitializationPrintTask extends AsyncTask<Void, Void, InitializationPrintTask.InitStatus> {
    /* log */
    private static final String TAG = "InitializationPrintTask";

    private Exception mException = null;

//    private final WeakReference<PrintFileActivityTest> mContextRef;
    private final WeakReference<DocumentPreviewActivity> mContextRef;

//    public InitializationPrintTask(PrintFileActivityTest context) {
//        this.mContextRef = new WeakReference<>(context);
//    }

    public InitializationPrintTask(DocumentPreviewActivity context) {
        this.mContextRef = new WeakReference<>(context);
    }

    @Override
    protected InitStatus doInBackground(final Void... params) {
        InitStatus status = InitStatus.NO_ERROR;

        try {
            // initialize the JetAdvantageLink with app context
            JetAdvantageLink.getInstance().initialize(mContextRef.get());
        } catch (final SsdkUnsupportedException e) {
            Log.e(TAG, "SDK is not supported!", e);
            mException = e;
            status = InitStatus.INIT_EXCEPTION;
        } catch (final SecurityException e) {
            Log.e(TAG, "Security exception!", e);
            mException = e;
            status = InitStatus.INIT_EXCEPTION;
        }

        // Check if PrinterService is supported
        if (status == InitStatus.NO_ERROR
                && (!PrinterService.isSupported(mContextRef.get())
                || !JobService.isSupported(mContextRef.get()))) {
            // PrinterService is not supported on this device
            status = InitStatus.NOT_SUPPORTED;
        }

        return status;
    }

    @Override
    protected void onPostExecute(final InitializationPrintTask.InitStatus status) {
        if (status == InitStatus.NO_ERROR) {
//            mContextRef.get().handleComplete();
            return;
        }

        switch (status) {
            case INIT_EXCEPTION:
//                mContextRef.get().handleException(mException);
                break;
            case NOT_SUPPORTED:
//                mContextRef.get().handleException(new Exception("Error"));
                break;
            default:
//                mContextRef.get().handleException(new Exception("Error"));
        }
    }

    public enum InitStatus {
        INIT_EXCEPTION,
        NOT_SUPPORTED,
        NO_ERROR
    }
}