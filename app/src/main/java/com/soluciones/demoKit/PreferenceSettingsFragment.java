package com.soluciones.demoKit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.soluciones.demoKit.UserClass.ScanUserAttriputes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;

import java.util.ArrayList;

public class PreferenceSettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "PreferenceSettingsFragm";
    private ScanUserAttriputes scanUserAttriputes;
    private ScanAttributesCaps mCapabilities;
    private ScanAttributes mCapsDefault;

    public PreferenceSettingsFragment() {
        scanUserAttriputes = ScanUserAttriputes.getInstance();

        this.mCapabilities = scanUserAttriputes.getCaps();
        this.mCapsDefault = scanUserAttriputes.getDefaultCaps();
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferencescreen, rootKey);

        //carga los valores que tiene el equipo
        loadValuesOnListPreferences(mCapabilities);

        //setea los Artibutos por Default
        setDefaultScanAttributes(mCapsDefault);

    }

    /** Populate las opciones de los atributos */
    public void loadValuesOnListPreferences(final ScanAttributesCaps caps) {
        Log.d(TAG, "loadValuesOnListPreferences: CALL");

        ListPreference pref;

        //Load Black Image Removal mode
        pref = findPreference("pref_blankImageRemovalMode");
        ArrayList<CharSequence> blackImageRemovalEntries = new ArrayList<>();
        ArrayList<CharSequence> blackImageRemovalValues = new ArrayList<>();

        for (ScanAttributes.BlankImageRemovalMode blankImageRemovalMode : caps.getBlankImageRemovalModeList()) {
            blackImageRemovalEntries.add(blankImageRemovalMode.name()); //name
            blackImageRemovalValues.add(blankImageRemovalMode.name()); //value
        }
        pref.setEntries(blackImageRemovalEntries.toArray(new CharSequence[blackImageRemovalEntries.size()]));
        pref.setEntryValues(blackImageRemovalValues.toArray(new CharSequence[blackImageRemovalValues.size()]));
        pref.setDefaultValue(ScanAttributes.BlankImageRemovalMode.DEFAULT.name());
        pref.setValueIndex(0);

        // Load Original Size
        pref = findPreference("pref_originalSize");
        ArrayList<CharSequence> osEntries = new ArrayList<>();
        ArrayList<CharSequence> osEntryValues = new ArrayList<>();

        for (ScanAttributes.ScanSize os : caps.getScanSizeList()) {
            osEntries.add(os.name());
            osEntryValues.add(os.name());
        }

        pref.setEntries(osEntries.toArray(new CharSequence[osEntries.size()]));
        pref.setEntryValues(osEntryValues.toArray(new CharSequence[osEntryValues.size()]));
        pref.setValueIndex(0);
        pref.setDefaultValue(ScanAttributes.ScanPreview.DEFAULT);

        // Load Job Assembly Mode
        pref = findPreference("pref_jobAssemblyMode");

        ArrayList<CharSequence> jobAssemblyModeEntries = new ArrayList<>();
        ArrayList<CharSequence> jobAssemblyModeValues = new ArrayList<>();

        for (ScanAttributes.JobAssemblyMode jobAssemblyMode : caps.getJobAssemblyModeList()) {
            jobAssemblyModeEntries.add(jobAssemblyMode.name());
            jobAssemblyModeValues.add(jobAssemblyMode.name());
        }

        if (pref != null) {
            pref.setEntries(jobAssemblyModeEntries.toArray(new CharSequence[jobAssemblyModeEntries.size()]));
            pref.setEntryValues(jobAssemblyModeValues.toArray(new CharSequence[jobAssemblyModeValues.size()]));
            pref.setDefaultValue(ScanAttributes.JobAssemblyMode.DEFAULT.name());
            pref.setValueIndex(0);
        }

        //Load Scan Preview
        pref = findPreference("pref_scanPreview");

        ArrayList<CharSequence> scanPreviewEntries = new ArrayList<>();
        ArrayList<CharSequence> scanPreviewEntryValues = new ArrayList<>();

        for (ScanAttributes.ScanPreview scanPreview : caps.getScanPreviewList()) {
            scanPreviewEntries.add(scanPreview.name());
            scanPreviewEntryValues.add(scanPreview.name());
        }
        if (pref != null) {
            pref.setEntries(scanPreviewEntries.toArray(new CharSequence[scanPreviewEntries.size()]));
            pref.setEntryValues(scanPreviewEntryValues.toArray(new CharSequence[scanPreviewEntryValues.size()]));
            pref.setDefaultValue(ScanAttributes.ScanPreview.DEFAULT);
            pref.setValueIndex(0);
        }

    }

    /** setea por default de la impresora */
    public void setDefaultScanAttributes(ScanAttributes scanAttributes) {
        Log.d(TAG, "setDefaultScanAttributes: CALL");
        if (scanAttributes != null) {
            ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributes);
            setPreferenceEntryValue("pref_blankImageRemovalMode", scanAttributesReader.getBlankImageRemovalMode().name());
            setPreferenceEntryValue("pref_jobAssemblyMode", scanAttributesReader.getJobAssemblyMode().name());
            setPreferenceEntryValue("pref_originalSize", scanAttributesReader.getScanSize().name());
        }
    }

    private void setPreferenceEntryValue(String pref, String attribute) {
        ListPreference listPreference = findPreference(pref);
        int index = 0;
        for (CharSequence value : listPreference.getEntryValues()) {
            if (value != null && value.toString().equals(attribute)) {
                break;
            }
            index++;
        }

        if (index < listPreference.getEntryValues().length) {
            listPreference.setValueIndex(index);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
