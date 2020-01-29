package com.example.demo1.Task;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.demo1.DocumentPreviewActivity;
import com.example.demo1.UserClass.ScanUserAttriputes;
import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.api.printer.PrintAttributes;
import com.hp.jetadvantage.link.api.printer.PrintAttributesCaps;
import com.hp.jetadvantage.link.api.printer.PrinterService;
import com.hp.jetadvantage.link.api.printer.PrintletAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Async task to request print.
 */
public class RequestPrintTask extends AsyncTask<Void, Void, String> {
    /* log */
    private static final String TAG = "RequestPrintTask";

//    private final WeakReference<PrintFileActivityTest> mContextRef;
    private final WeakReference<DocumentPreviewActivity> mContextRef;

//    private final SharedPreferences mPrefs;
    private String filePath;

    private String mErrorMsg = null;

//    public RequestPrintTask(final PrintFileActivityTest context, String filePathToPrint) {
//        this.mContextRef = new WeakReference<>(context);
//        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//        this.filePath = filePathToPrint;
//    }

    public RequestPrintTask(final DocumentPreviewActivity context, String filePathToPrint) {
        this.mContextRef = new WeakReference<>(context);
//        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        this.filePath = filePathToPrint;
    }

    @Override
    protected String doInBackground(final Void... params) {
        try {
            final boolean settingsUi = false;
            Log.i(TAG, "Settings UI:" + settingsUi);

            PrintAttributes attributes = null;

            if (!settingsUi) {
                //todo crear esto
                final PrintAttributesCaps caps = ScanUserAttriputes.getInstance().getPrintCaps();

                if (caps == null) {
                    mErrorMsg = "Caps no cargadas";
                    return null;
                }

                // Build PrintAttributes based on preferences values
                // can print from any InputStream, here FileInputStream as example
                InputStream printStream = new FileInputStream(new File(filePath));

                // building with common print attributes set
                attributes = new PrintAttributes.PrintFromStreamBuilder(printStream)
                                    .setCollateMode(PrintAttributes.CollateMode.DEFAULT)
                                    .setColorMode(PrintAttributes.ColorMode.MONO)
                                    .setDuplex(PrintAttributes.Duplex.NONE)
                                    .setAutoFit(PrintAttributes.AutoFit.DEFAULT)
                                    .setStapleMode(PrintAttributes.StapleMode.DEFAULT)
                                    .setPaperSource(PrintAttributes.PaperSource.DEFAULT)
                                    .setPaperSize(PrintAttributes.PaperSize.DEFAULT)
                                    .setPaperType(PrintAttributes.PaperType.DEFAULT)
                                    .setDocumentFormat(PrintAttributes.DocumentFormat.AUTO)
                                    .setCopies(1)
                                    .setJobName("test")
                                    .build(caps);

            }

            final PrintletAttributes taskAttribs = new PrintletAttributes.Builder()
                    .setShowSettingsUi(settingsUi)
                    .build();

            // Submit the job
            String rid = PrinterService.submit(mContextRef.get(), attributes, taskAttribs);
            Log.i(TAG, "Job submitted with rid = " + rid);
            return rid;
        } catch (final CapabilitiesExceededException e) {
            mErrorMsg = "CapabilitiesExceededException: " + e.getMessage();
        } catch (final IllegalArgumentException e) {
            mErrorMsg = "IllegalArgumentException: " + e.getMessage();
        } catch (final Exception e) {
            mErrorMsg = "Unknown exception: " + e.getMessage();
        }

        return null;
    }

    @Override
    protected void onPostExecute(final String rid) {
        super.onPostExecute(rid);
        if (!TextUtils.isEmpty(rid)) {
            mContextRef.get().setRid(rid);
        } else if (mErrorMsg != null) {
            Log.e(TAG, mErrorMsg);
            mContextRef.get().showResult(mErrorMsg);
        }
    }
}
