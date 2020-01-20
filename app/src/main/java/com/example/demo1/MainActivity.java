package com.example.demo1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo1.Dialogs.NoConfigDialog;
import com.example.demo1.Task.ConfigReaderTask;
import com.example.demo1.Task.InitializationTask;
import com.example.demo1.Task.LoadCapabilitiesTask;
import com.example.demo1.UserClass.LoadDefaultsTask;
import com.example.demo1.UserClass.ScanUserAttriputes;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScannerService;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private static final int METODOS_COMPLETADOS = 2;

    private ImageView logo;
    private TextView poweredBy;
    private int metodos = 0;

    /* HP InitializationTask */
    private InitializationTask mInitializationTask;
    private ScanAttributesCaps mCapabilities;
    private FileOptionsAttributesCaps mFileOptionsAttributesCaps;
    private ScanUserAttriputes scanUserAttriputes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logohp);
        poweredBy = findViewById(R.id.textoPoweredBySoluciones);


        /** for hp */
        scanUserAttriputes = ScanUserAttriputes.getInstance();

        splashAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();


        mInitializationTask = new InitializationTask(this);
        mInitializationTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mInitializationTask.cancel(true);
        mInitializationTask = null;
    }

    private void splashAnimation(){
        logo.setAlpha(0f);
        logo.setVisibility(View.VISIBLE);

        logo.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        textAnimation();
                    }
                });
    }

    private void textAnimation(){
        ObjectAnimator animaton = ObjectAnimator.ofFloat(poweredBy,"translationY", 50f);
        animaton.setDuration(1300);
        animaton.setInterpolator(new AccelerateDecelerateInterpolator());
        poweredBy.setAlpha(0f);
        poweredBy.setVisibility(View.VISIBLE);

        animaton.start();
        poweredBy.animate()
                .alpha(1f)
                .setDuration(1300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startNextActivity();
                            }
                        }, 1000);

                    }
                });
    }

    public void handleComplete(){
        // el initStatus vuelve correctamente
        Log.d(TAG, "handleComplete: initStatus vuelve correctamente");
        //busca las capabilities del Equipo
        //busca la configCargada
        new ConfigReaderTask(this).execute();
    }

    public void handleException (final Exception e){
        // el initStatus vuelve con Error
        Log.e(TAG, e.getMessage());
    }


    private void startNextActivity() {
        Log.d(TAG, "startNextActivity: call");
        metodos++;
        Log.d(TAG, "startNextActivity: metodos hechos " + metodos);
        if (metodos != METODOS_COMPLETADOS){
            Log.d(TAG, "startNextActivity: return");
            return;
        }
        Log.d(TAG, "startNextActivity: inicia");

        Intent intent = new Intent(this, AppSelectionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void notTokenLoaded(String mensajeError){
        NoConfigDialog noConfigDialog;
        if (mensajeError == null){
            noConfigDialog = new NoConfigDialog(this, null);
        } else {
            noConfigDialog = new NoConfigDialog(this, mensajeError);
        }
        noConfigDialog.setCancelable(false);
        noConfigDialog.show(getSupportFragmentManager(), "noConfigLoaded");
    }

    public void onConfigError(){
        Log.d(TAG, "onConfigError: error en la configuracion (no/mal cargada)");
        notTokenLoaded(null);
    }

    public void onTokenError(String mensajeError){
        Log.d(TAG, "onTokenError: error en la respuesta del token (no existe, cenvido, etc)");
        notTokenLoaded(mensajeError);
    }

    //vuelve de buscar la config del equipo
    public void onApiResponseComplete() {
        Log.d(TAG, "llega con el json respuesta de la Api nuestra");
        new LoadCapabilitiesTask(this).execute();
    }

    public void getAllCaps(ScanAttributesCaps caps){
        Log.d(TAG, "getAllCaps: call");
        scanUserAttriputes.setCaps(caps);
        new LoadDefaultsTask(this).execute();
    }

    public void getDefaultCaps(ScanAttributes defaultCaps){
        Log.d(TAG, "getDefaultCaps: call");
        scanUserAttriputes.setDefaultCaps(defaultCaps);
        startNextActivity();
    }


    /**
     * Executes request for capabilities from ScannerService.
     *
     * @param context {@link Context}
     * @return {@link com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps}
     */
    public ScanAttributesCaps requestCaps(final Context context, Result result) {
        if (result == null) {
            result = new Result();
        }

        // cache capabilities for building ScanUserAttriputes
        mCapabilities = ScannerService.getCapabilities(context, result);

        // get capabilities
        if (result.getCode() == Result.RESULT_OK && mCapabilities != null) {
            Log.d(TAG,
                    "Received Caps as:" +
                            " Destination:" + mCapabilities.getDestinationList() +
                            ",ColorMode:" + mCapabilities.getColorModeList().toString() +
                            ",Duplex:" + mCapabilities.getDuplexList().toString() +
                            ",DocFormat(Me):" + mCapabilities.getDocumentFormatList(ScanAttributes.Destination.ME).toString() +
                            ",Orientation:" + mCapabilities.getOrientationList() +
                            ",Resolution:" + mCapabilities.getResolutionList() +
                            ",ScanPreview:" + mCapabilities.getScanPreviewList() +
                            ",ScanSize:" + mCapabilities.getScanSizeList() +
                            ",CustomLength:" + mCapabilities.getCustomLengthRange() +
                            ",CustomWidth:" + mCapabilities.getCustomWidthRange() +
                            ",BackgroundCleanup:" + mCapabilities.getBackgroundCleanupList() +
                            ",ContrastAdjustment:" + mCapabilities.getContrastAdjustmentList() +
                            ",DarknessAdjustment:" + mCapabilities.getDarknessAdjustmentList() +
                            ",SharpnessAdjustment:" + mCapabilities.getSharpnessAdjustmentList() +
                            ",BlankImageRemovalMode:" + mCapabilities.getBlankImageRemovalModeList() +
                            ",ColorDropoutMode:" + mCapabilities.getColorDropoutModeList() +
                            ",CropMode:" + mCapabilities.getCropModeList() +
                            ",ProgressDialogMode:" + mCapabilities.getProgressDialogModeList() +
                            ",OutputQuality:" + mCapabilities.getOutputQualityList() +
                            ",TransmissionMode:" + mCapabilities.getTransmissionModeList() +
                            ",MediaWeightAdjustment:" + mCapabilities.getMediaWeightAdjustmentList() +
                            ",TextPhotoOptimization:" + mCapabilities.getTextPhotoOptimizationList() +
                            ",JobAssemblyMode:" + mCapabilities.getJobAssemblyModeList() +
                            ",MediaSource:" + mCapabilities.getMediaSourceList() +
                            ",MisfeedDetectionMode:" + mCapabilities.getMisfeedDetectionModeList());

            for (Map.Entry<ScanAttributes.ColorMode, List<ScanAttributes.DocumentFormat>> entry : mCapabilities.getDocumentFormatsByColorMode().entrySet()) {
                Log.d(TAG, "getDocumentFormatsByColorMode: " + entry.getValue() + " [" + entry.getKey() + "]");
            }

            // get file options for defaults
            mFileOptionsAttributesCaps = requestFileOptionsCapabilities(
                    ScanAttributes.ColorMode.DEFAULT, ScanAttributes.DocumentFormat.DEFAULT);

            scanUserAttriputes.setmFileOptionsAttributesCaps(mFileOptionsAttributesCaps);

        } else {
            showResult("ScannerService.getCapabilities()", result);
        }

        return mCapabilities;
    }

    public FileOptionsAttributesCaps requestFileOptionsCapabilities(ScanAttributes.ColorMode colorMode, ScanAttributes.DocumentFormat docFormat) {
        // cache file options capabilities for building FileOptionsAttributes later
        Result result = new Result();
        mFileOptionsAttributesCaps = ScannerService.getFileOptionsCapabilities(this, colorMode, docFormat, result);
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
            showResult("ScannerService.getFileOptionsCapabilities()", result);
        }
        return null;
    }

    public void showResult(final String msg, final Result result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String resultMsg;
                resultMsg = msg;
                if (result == null) {
                    Log.i(TAG, resultMsg);
                } else if (result.getCode() == Result.RESULT_OK) {
                    resultMsg += "\nCode: RESULT_OK";
                    Log.i(TAG, resultMsg);
                } else if (result.getCode() == Result.RESULT_FAIL) {
                    resultMsg += "\nCode: RESULT_FAIL" + "\n"
                            + "ErrorCode: " + result.getErrorCode() + "\n"
                            + "Cause: " + result.getCause();
                    Log.e(TAG, resultMsg);
                }
                Toast.makeText(getApplicationContext(), resultMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }





}
