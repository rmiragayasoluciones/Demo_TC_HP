package com.example.demo1.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.demo1.DocuFiliatoriosActivity;
import com.example.demo1.UserClass.ScanOptionsSelected;
import com.example.demo1.UserClass.ScanUserAttriputes;
import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanletAttributes;
import com.hp.jetadvantage.link.api.scanner.ScannerService;

import java.lang.ref.WeakReference;

public class ScanToDestinationTask extends AsyncTask<Void, Void, String> {
    /* log */
    private static final String TAG = "[ScanSample]" + ScanToDestinationTask.class.getSimpleName();

    private final WeakReference<DocuFiliatoriosActivity> mContextRef;

    private final ScanOptionsSelected mScanSelected = ScanOptionsSelected.getInstance();

    private FileOptionsAttributesCaps mFileOptionsAttributesCaps;

    private String mErrorMsg = null;

    private String filename;

    public ScanToDestinationTask(final DocuFiliatoriosActivity context, String filename) {
        this.mContextRef = new WeakReference<>(context);
        this.filename = filename;
    }

    @Override
    protected String doInBackground(Void... voids) {
        DocuFiliatoriosActivity activity = mContextRef.get();

        try{

            ScanAttributes attributes;

            final ScanAttributesCaps caps = ScanUserAttriputes.getInstance().getCaps();

            if (caps == null) {
                mErrorMsg = "Capabilities not loads";
                return null;
            }

            attributes = buildScanAttributes(caps);
            final ScanletAttributes taskAttribs = new ScanletAttributes.Builder()
                    .setShowSettingsUi(false)
                    .build();


            // Submit the job
            final String rid = ScannerService.submit(activity, attributes, taskAttribs);

            Log.i(TAG, "Job submitted with rid = " + rid);
            return rid;

        } catch (CapabilitiesExceededException e) {
            mErrorMsg = "CapabilitiesExceededException: " + e.getMessage();
        } catch (final IllegalArgumentException e) {
            mErrorMsg = "IllegalArgumentException: " + e.getMessage();
        } catch (final Exception e) {
            mErrorMsg = "Unknown exception: " + e.getMessage();
        }
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        // usar el callback
//        mContextRef.get() callback
    }

    private ScanAttributes buildScanAttributes(ScanAttributesCaps capabilities)
            throws CapabilitiesExceededException {

        final ScanAttributes.BlankImageRemovalMode blankImageRemovalMode;
        if (mScanSelected.getBlankPagesSelected() == null){
            blankImageRemovalMode = ScanAttributes.BlankImageRemovalMode.DEFAULT;
            Log.d(TAG, "Selected blankImageRemovalMode:" + blankImageRemovalMode.name());
        } else {
            blankImageRemovalMode = ScanAttributes.BlankImageRemovalMode.valueOf(mScanSelected.getBlankPagesSelected());
            Log.d(TAG, "Selected blankImageRemovalMode:" + blankImageRemovalMode.name());
        }

        final ScanAttributes.ScanSize ss;
        if (mScanSelected.getPaperSize() == null){
            ss  = ScanAttributes.ScanSize.DEFAULT;
            Log.d(TAG, "Selected Scan Size:" + ss.name());
        } else {
            ss  = ScanAttributes.ScanSize.valueOf(mScanSelected.getPaperSize());
            Log.d(TAG, "Selected Scan Size:" + ss.name());
        }

        final ScanAttributes.JobAssemblyMode jobAssemblyMode;
        if (mScanSelected.getJobBuilderSelected()==null){
            jobAssemblyMode = ScanAttributes.JobAssemblyMode.DEFAULT;
            Log.d(TAG, "Selected JobAssemblyMode:" + jobAssemblyMode.name());
        } else {
            jobAssemblyMode = ScanAttributes.JobAssemblyMode.valueOf(mScanSelected.getJobBuilderSelected());
            Log.d(TAG, "Selected JobAssemblyMode:" + jobAssemblyMode.name());
        }

        final ScanAttributes.ScanPreview scanPreview;
        if (mScanSelected.getScanPreviewSelected() == null){
            scanPreview = ScanAttributes.ScanPreview.DEFAULT;
            Log.d(TAG, "Selected ScanPreview:" + scanPreview.name());
        } else {
            scanPreview = ScanAttributes.ScanPreview.valueOf(mScanSelected.getScanPreviewSelected());
            Log.d(TAG, "Selected ScanPreview:" + scanPreview.name());
        }


        final ScanAttributes.DocumentFormat df = ScanAttributes.DocumentFormat.MTIFF;
        Log.d(TAG, "Selected Doc Format:" + df);

        final ScanAttributes.ColorMode cm = ScanAttributes.ColorMode.DEFAULT;
        Log.d(TAG, "Selected Color Mode:" + cm.name());

        final ScanAttributes.Duplex du = ScanAttributes.Duplex.DEFAULT;
        Log.d(TAG, "Selected Duplex Mode:" + du.name());

        final ScanAttributes.Orientation orientation =  ScanAttributes.Orientation.DEFAULT;
        Log.d(TAG, "Selected Orientation:" + orientation.name());

        final float customLength = 0;

        final float customWidth = 0;

        final ScanAttributes.BackgroundCleanup backgroundCleanup = ScanAttributes.BackgroundCleanup.DEFAULT;

        final ScanAttributes.ContrastAdjustment contrastAdjustment = ScanAttributes.ContrastAdjustment.DEFAULT;

        final ScanAttributes.Resolution resolution = ScanAttributes.Resolution.DEFAULT;
        Log.d(TAG, "Selected Resolution:" + resolution.name());

        final ScanAttributes.DarknessAdjustment darknessAdjustment = ScanAttributes.DarknessAdjustment.DEFAULT;

        final ScanAttributes.ColorDropoutMode colorDropoutMode = ScanAttributes.ColorDropoutMode.DEFAULT;

        final ScanAttributes.CropMode cropMode = ScanAttributes.CropMode.DEFAULT;

        final ScanAttributes.ProgressDialogMode progressDialogMode = ScanAttributes.ProgressDialogMode.OFF;

        final ScanAttributes.OutputQuality outputQuality = ScanAttributes.OutputQuality.DEFAULT;

        final ScanAttributes.TransmissionMode transmissionMode = ScanAttributes.TransmissionMode.DEFAULT;

//        final ScanAttributes.ScanPreview scanPreview = ScanAttributes.ScanPreview.DEFAULT;

//        final ScanAttributes.JobAssemblyMode jobAssemblyMode = ScanAttributes.JobAssemblyMode.DEFAULT;

        final ScanAttributes.SharpnessAdjustment sharpnessAdjustment = ScanAttributes.SharpnessAdjustment.DEFAULT;

        final ScanAttributes.MediaWeightAdjustment mediaWeightAdjustment = ScanAttributes.MediaWeightAdjustment.DEFAULT;

        final ScanAttributes.TextPhotoOptimization textPhotoOptimization = ScanAttributes.TextPhotoOptimization.DEFAULT;

        final ScanAttributes.MediaSource mediaSource = ScanAttributes.MediaSource.DEFAULT;

        final ScanAttributes.MisfeedDetectionMode misfeedDetectionMode = ScanAttributes.MisfeedDetectionMode.DEFAULT;

        final String pdfEncryption = null;

        final FileOptionsAttributes.OcrLanguage ocrLanguage = FileOptionsAttributes.OcrLanguage.DEFAULT;

        final FileOptionsAttributes.PdfCompressionMode compressionMode = FileOptionsAttributes.PdfCompressionMode.DEFAULT;

        final FileOptionsAttributes.TiffCompressionMode tiffCompressionMode = FileOptionsAttributes.TiffCompressionMode.DEFAULT;

        final FileOptionsAttributes.XpsCompressionMode xpsCompressionMode = FileOptionsAttributes.XpsCompressionMode.DEFAULT;


        /* FILE NAME */

        //timeStamp
//        Date date = new Date();
//        Timestamp ts = new Timestamp(date.getTime());
//        String timeStampString = ts.toString().trim();
//        timeStampString = timeStampString.replace(" ", "_");
//        timeStampString = timeStampString.replace(":", "");
//        timeStampString = removeMiliseconds(timeStampString);
//        Log.d(TAG, "timeStamp: " + timeStampString);

        //Nombre del archivo es razonsocial ingresada
//        Log.d(TAG, "nombre " + DemoViewModelSingleton.getInstance().getMetadataCliente().getRazonSocial().trim().toLowerCase());
//        String fileName = DemoViewModelSingleton.getInstance().getMetadataCliente().getRazonSocial().trim().toLowerCase().replace(" ", "_");
//        Log.d(TAG, "file name: " + fileName);


//        String folderName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/SolucionesDockets/";
//        Log.d(TAG, "FOLDER NAME: " + folderName);

        //*******************************************************

        // get file options for defaults
        mFileOptionsAttributesCaps = requestFileOptionsCapabilities(ScanAttributes.ColorMode.DEFAULT, ScanAttributes.DocumentFormat.DEFAULT);

        //*******************************************************

        Log.d(TAG, "FileOptionsAttributes option\npdfEncryption: " + pdfEncryption + "\n"
                + "ocrLanguage: " + ocrLanguage.name() + "\n"
                + "compressionMode: " + compressionMode.name() + "\n"
                + "tiffCompressionMode: " + tiffCompressionMode.name() + "\n"
                + "xpsCompressionMode: " + xpsCompressionMode.name() + "\n");


        FileOptionsAttributes fileOptionsAttributes = new FileOptionsAttributes.Builder()
                .setPdfEncryptionPassword(null)
                .setOcrLanguage(ocrLanguage)
                .setPdfCompressionMode(compressionMode)
                .setTiffCompressionMode(tiffCompressionMode)
                .setXpsCompressionMode(xpsCompressionMode)
                .build(mFileOptionsAttributesCaps);


        ScanAttributes scanAttributesCreated = new ScanAttributes.MeBuilder()

                .setColorMode(cm)
                .setDuplex(du)
                .setDocumentFormat(df)
                .setScanSize(ss)
                .setCustomLength(customLength)
                .setCustomWidth(customWidth)
                .setResolution(resolution)
                .setOrientation(orientation)
                .setScanPreview(scanPreview)
                .setBackgroundCleanup(backgroundCleanup)
                .setContrastAdjustment(contrastAdjustment)
                .setDarknessAdjustment(darknessAdjustment)
                .setBlankImageRemovalMode(blankImageRemovalMode)
                .setColorDropoutMode(colorDropoutMode)
                .setCropMode(cropMode)
                .setProgressDialogMode(progressDialogMode)
                .setOutputQuality(outputQuality)
                .setTransmissionMode(transmissionMode)
                .setJobAssemblyMode(jobAssemblyMode)
                .setSharpnessAdjustment(sharpnessAdjustment)
                .setMediaWeightAdjustment(mediaWeightAdjustment)
                .setTextPhotoOptimization(textPhotoOptimization)
                .setMediaSource(mediaSource)
                .setMisfeedDetectionMode(misfeedDetectionMode)
                .setFileOptionsAttributes(fileOptionsAttributes)
                .setFileName(this.filename)
                .build(capabilities);

        Log.d(TAG, "buildScanAttributes: to string " + scanAttributesCreated.toString());


        return scanAttributesCreated;
    }

    public FileOptionsAttributesCaps requestFileOptionsCapabilities(ScanAttributes.ColorMode colorMode, ScanAttributes.DocumentFormat docFormat) {
        // cache file options capabilities for building FileOptionsAttributes later
        Result result = new Result();
        mFileOptionsAttributesCaps = ScannerService.getFileOptionsCapabilities(mContextRef.get(), colorMode, docFormat, result);
        if (result.getCode() == Result.RESULT_OK) {
            Log.i(TAG, "ColorMode=" + colorMode.name() + ", DocFormat=" + docFormat.name() + " : "
                    + "getOcrLanguageList: " + mFileOptionsAttributesCaps.getOcrLanguageList().toString()
                    + ". getPdfCompressionModeList: " + mFileOptionsAttributesCaps.getPdfCompressionModeList().toString()
                    + ", getTiffCompressionModeList: " + mFileOptionsAttributesCaps.getTiffCompressionModeList().toString()
                    + ", getXpsCompressionModeList: " + mFileOptionsAttributesCaps.getXpsCompressionModeList().toString()
                    + ", isPdfEncryptionPasswordSupported: " + mFileOptionsAttributesCaps.isPdfEncryptionPasswordSupported()
            );
            return mFileOptionsAttributesCaps;
        } else {
            Log.d(TAG, "ScannerService.getFileOptionsCapabilities()");
        }
        return null;
    }
}
