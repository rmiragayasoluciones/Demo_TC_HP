package com.soluciones.demoKit.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.SsdkUnsupportedException;
import com.hp.jetadvantage.link.api.device.DeviceService;
import com.soluciones.demoKit.ReporteDeErrores;

import java.lang.ref.WeakReference;

public class InitializationDeviceInfoTask extends AsyncTask<Void, Void, InitializationDeviceInfoTask.InitStatus> {
    private static final String TAG = "InitializationDeviceInf";

    private Exception mException = null;

    private final WeakReference<ReporteDeErrores> mContextRef;

    public InitializationDeviceInfoTask(ReporteDeErrores context) {
        this.mContextRef = new WeakReference<>(context);
    }

    @Override
    protected InitStatus doInBackground(Void... voids) {
        InitStatus status = InitStatus.NO_ERROR;

        try {
            // initialize the JetAdvantageLink with app context
            JetAdvantageLink.getInstance().initialize(mContextRef.get());
        } catch (SsdkUnsupportedException sue) {
            Log.e(TAG, "SDK is not supported!", sue);
            mException = sue;
            status = InitStatus.INIT_EXCEPTION;
        } catch (SecurityException se) {
            Log.e(TAG, "Security exception!", se);
            mException = se;
            status = InitStatus.INIT_EXCEPTION;
        }

        // Check if DeviceService is supported
        if (status == InitStatus.NO_ERROR
                && !DeviceService.isSupported(mContextRef.get())) {
            // DeviceService is not supported on this device
            status = InitStatus.NOT_SUPPORTED;
        }

        return status;
    }

    @Override
    protected void onPostExecute(final InitializationDeviceInfoTask.InitStatus status) {
        if (status == InitStatus.NO_ERROR) {
            mContextRef.get().handleComplete();
            return;
        }

        switch (status) {
            case INIT_EXCEPTION:
                mContextRef.get().handleException(mException);
                break;
            case NOT_SUPPORTED:
                mContextRef.get().handleException(new Exception("Equipo no soportado"));
                break;
            default:
                mContextRef.get().handleException(new Exception("Error desconocido"));
        }
    }



    public enum InitStatus {
        INIT_EXCEPTION,
        NOT_SUPPORTED,
        NO_ERROR
    }

}
