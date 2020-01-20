package com.example.demo1.UserClass;

public class ScanOptionsSelected {

    public static ScanOptionsSelected mInstance;

    private String jobBuilderSelected;
    private String scanPreviewSelected;
    private String blankPagesSelected;
    private String paperSize;
    private String duplexSelected;

    public ScanOptionsSelected() {

    }

    public static synchronized ScanOptionsSelected getInstance(){
        if (mInstance == null){
            mInstance = new ScanOptionsSelected();
        }
        return mInstance;
    }


    public String getJobBuilderSelected() {
        return jobBuilderSelected;
    }

    public String getScanPreviewSelected() {
        return scanPreviewSelected;
    }

    public String getBlankPagesSelected() {
        return blankPagesSelected;
    }

    public String getPaperSize() {
        return paperSize;
    }

    public String getDuplexSelected() {
        return duplexSelected;
    }

    public void setDuplexSelected(String duplexSelected) {
        this.duplexSelected = duplexSelected;
    }

    public static void setmInstance(ScanOptionsSelected mInstance) {
        ScanOptionsSelected.mInstance = mInstance;
    }

    public void setJobBuilderSelected(String jobBuilderSelected) {
        this.jobBuilderSelected = jobBuilderSelected;
    }

    public void setScanPreviewSelected(String scanPreviewSelected) {
        this.scanPreviewSelected = scanPreviewSelected;
    }

    public void setBlankPagesSelected(String blankPagesSelected) {
        this.blankPagesSelected = blankPagesSelected;
    }

    public void setPaperSize(String paperSize) {
        this.paperSize = paperSize;
    }
}
