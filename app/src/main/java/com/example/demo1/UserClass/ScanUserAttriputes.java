package com.example.demo1.UserClass;

import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;

public class ScanUserAttriputes {
    private static final String TAG = "ScanUserAttriputes";

    private static ScanUserAttriputes INSTANCE = new ScanUserAttriputes();

    private ScanAttributesCaps caps;
    private ScanAttributes defaultCaps;
    private FileOptionsAttributesCaps mFileOptionsAttributesCaps = null;


    private ScanUserAttriputes(){
    }

    public static synchronized ScanUserAttriputes getInstance(){
        if (INSTANCE == null){
            INSTANCE = new ScanUserAttriputes();
        }
        return INSTANCE;
    }

    public ScanAttributesCaps getCaps() {
        return caps;
    }

    public void setCaps(ScanAttributesCaps caps) {
        this.caps = caps;
    }

    public ScanAttributes getDefaultCaps() {
        return defaultCaps;
    }

    public void setDefaultCaps(ScanAttributes defaultCaps) {
        this.defaultCaps = defaultCaps;
    }

    public FileOptionsAttributesCaps getmFileOptionsAttributesCaps() {
        return mFileOptionsAttributesCaps;
    }

    public void setmFileOptionsAttributesCaps(FileOptionsAttributesCaps mFileOptionsAttributesCaps) {
        this.mFileOptionsAttributesCaps = mFileOptionsAttributesCaps;
    }
}
