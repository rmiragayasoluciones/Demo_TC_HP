package com.soluciones.demoKit.UserClass;

import android.os.Parcel;
import android.os.Parcelable;

public class Documents implements Parcelable {

    private String id;
    private String serieName;
    private String demoId;
    private String filePath;
    private String client;

    public Documents(String id, String serieName, String demoId, String filePath, String client) {
        this.id = id;
        this.serieName = serieName;
        this.demoId = demoId;
        this.filePath = filePath;
        this.client = client;
    }

    protected Documents(Parcel in) {
        id = in.readString();
        serieName = in.readString();
        demoId = in.readString();
        filePath = in.readString();
        client = in.readString();
    }

    public static final Creator<Documents> CREATOR = new Creator<Documents>() {
        @Override
        public Documents createFromParcel(Parcel in) {
            return new Documents(in);
        }

        @Override
        public Documents[] newArray(int size) {
            return new Documents[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getSerieName() {
        return serieName;
    }

    public String getDemoId() {
        return demoId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getClient() {
        return client;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(serieName);
        dest.writeString(demoId);
        dest.writeString(filePath);
        dest.writeString(client);
    }
}
