package com.soluciones.demoKit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.soluciones.demoKit.Model.DeviceInfo;
import com.soluciones.demoKit.Task.DeviceAttrReaderTask;
import com.soluciones.demoKit.Task.InitializationDeviceInfoTask;

import java.util.Map;

public class ReporteDeErrores extends AppCompatActivity {
    private static final String TAG = "ReporteDeErrores";

    private TextView informeErrorTV;
    private Button btnCerrar;
    private String errorReportText;
    private String errores;

    /* Background task for JetAdvantageLink API initialization */
    private InitializationDeviceInfoTask mInitializationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_de_errores);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        informeErrorTV = findViewById(R.id.infoErroresTV);
        btnCerrar = findViewById(R.id.buttonCerrar);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReporteDeErrores.this.finish();
                System.exit(0);
            }
        });

        Intent i = getIntent();
        errores = i.getStringExtra("fillInStackTrace");

        Log.d(TAG, "Error: " + errores);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // call init task
        mInitializationTask = new InitializationDeviceInfoTask(this);
        mInitializationTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mInitializationTask.cancel(true);
        mInitializationTask = null;
    }

    public void handleComplete() {
        // todo: progress dialog activar
        new DeviceAttrReaderTask(ReporteDeErrores.this).execute();
    }

    public void handleUpdate(Map<DeviceInfo, String> result) {
        // Fill device description with received info
        for (DeviceInfo item : DeviceInfo.values()) {
            if (result.containsKey(item)) {
                if (getApplicationContext() != null) {

                    Log.d(TAG, item.name() + ": " + result.get(item));
                    String dato = item.name() + ": " + result.get(item);
                    errorReportText += dato + "\n";
//                    mSummaries.get(item).setText(result.get(item));
                }
            }
        }

        errorReportText += errores;
        Log.d(TAG, "errorReportText: " + errorReportText);
        informeErrorTV.setText(errorReportText);

        //todo: cerrar progress
    }

    private void closeApp() {
        Intent intent = new Intent(getApplicationContext(), AppSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }


    public void handleException(final Exception e) {

    }
}
