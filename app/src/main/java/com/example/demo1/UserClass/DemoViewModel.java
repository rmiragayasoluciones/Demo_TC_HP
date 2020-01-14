package com.example.demo1.UserClass;

import android.util.Base64;

import java.util.Date;

public class DemoViewModel {

    private int id;
    private int tcId;
    private String name;
    private String client;
    private String description;
    private String logo;
    private String token;
    private String tokenExpirationTime;
    private boolean isExpired;
    private boolean isActive;
    private String creationTime;
    private String updateTime;
    private String url;
    private String workPathConfig;
    private String oxpdConfig;

    public DemoViewModel(int id, int tcId, String name, String client, String description, Base64 logo, String token, Date tokenExpirationTime, boolean isExpired, boolean isActive, Date creationTime, Date updateTime, String url, String workPathConfig, String oxpdConfig) {
        this.id = id;
        this.tcId = tcId;
        this.name = name;
        this.client = client;
        this.description = description;
        this.logo = logo.toString();
        this.token = token;
        this.tokenExpirationTime = tokenExpirationTime.toString();
        this.isExpired = isExpired;
        this.isActive = isActive;
        this.creationTime = creationTime.toString();
        this.updateTime = updateTime.toString();
        this.url = url;
        this.workPathConfig = workPathConfig;
        this.oxpdConfig = oxpdConfig;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTcId() {
        return tcId;
    }

    public void setTcId(int tcId) {
        this.tcId = tcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(String tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWorkPathConfig() {
        return workPathConfig;
    }

    public void setWorkPathConfig(String workPathConfig) {
        this.workPathConfig = workPathConfig;
    }

    public String getOxpdConfig() {
        return oxpdConfig;
    }

    public void setOxpdConfig(String oxpdConfig) {
        this.oxpdConfig = oxpdConfig;
    }

    @Override
    public String toString() {
        return "DemoViewModel{" +
                "id=" + id +
                ", tcId=" + tcId +
                ", name='" + name + '\'' +
                ", client='" + client + '\'' +
                ", description='" + description + '\'' +
//                ", logo='" + logo + '\'' +
                ", token='" + token + '\'' +
                ", tokenExpirationTime='" + tokenExpirationTime + '\'' +
                ", isExpired=" + isExpired +
                ", isActive=" + isActive +
                ", creationTime='" + creationTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", url='" + url + '\'' +
                ", workPathConfig='" + workPathConfig + '\'' +
                ", oxpdConfig='" + oxpdConfig + '\'' +
                '}';
    }
}
