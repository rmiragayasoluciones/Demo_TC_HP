package com.soluciones.demoKit.Task;

import android.os.AsyncTask;

import com.soluciones.demoKit.DocumentPreviewActivity;
import com.soluciones.demoKit.UserClass.ScanUserAttriputes;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.printer.PrintAttributesCaps;

import java.lang.ref.WeakReference;

public class LoadPrintCapabilitiesTask extends AsyncTask<Void, Void, PrintAttributesCaps> {

//    private final WeakReference<PrintFileActivityTest> mContextRef;
    private final WeakReference<DocumentPreviewActivity> mContextRef;

    private Result result;

//    public LoadPrintCapabilitiesTask(final PrintFileActivityTest context) {
//        this.mContextRef = new WeakReference<>(context);
//        this.result = new Result();
//    }

    public LoadPrintCapabilitiesTask(final DocumentPreviewActivity context) {
        this.mContextRef = new WeakReference<>(context);
        this.result = new Result();
    }

    @Override
    protected PrintAttributesCaps doInBackground(final Void... params) {
        return mContextRef.get().requestCaps(mContextRef.get(), result);
    }

    @Override
    protected void onPostExecute(final PrintAttributesCaps caps) {
        super.onPostExecute(caps);

        if (result.getCode() == Result.RESULT_OK) {
            ScanUserAttriputes.getInstance().setPrintCaps(caps);
        }
    }
}