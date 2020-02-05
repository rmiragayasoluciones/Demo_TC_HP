package com.soluciones.demoKit.Task;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

public class JobCompleteReciever extends BroadcastReceiver {
    public static final String RID_EXTRA = "rid";
    public static final String JOB_ID_EXTRA = "jobid";
    private static final String TAG = "JobCompleteReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null) {
            Log.d(TAG, "Received intent is null");
            return;
        }

        final String action = intent.getAction();
        final ComponentName component = intent.getComponent();
        // Verify that received Job Id is same as expected one
        final String jobId = intent.getStringExtra(JOB_ID_EXTRA);
        final String expectedJobId = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("pref_currentJobId", null);

        if ("com.hp.jetadvantage.link.sample.scansample.ACTION_SCAN_COMPLETED".equals(action) &&
                component != null && context.getPackageName().equals(component.getPackageName()) &&
                jobId.equals(expectedJobId)) {
            Log.d(TAG, "Monitoring Job: received Completed intent");
            Toast.makeText(context, "Monitoring Job: received Completed intent", Toast.LENGTH_SHORT).show();
        }
    }
}
