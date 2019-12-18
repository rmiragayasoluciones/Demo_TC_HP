package com.example.demo1.UserClass;

public class DemoViewModel {

    private int id;
    private int tcId;
    private String name;
    private String description;
    private String logo;
    private String token;
    private String expirationTime;
    private boolean isExpired;
    private boolean isActive;
    private String creationTime;
    private String updateTime;
    private String url;
    private String workPathConfig;
    private String oXPDConfig;

    public DemoViewModel(int id, int tcId, String name, String description, String logo, String token, String expirationTime, boolean isExpired, boolean isActive, String creationTime, String updateTime, String url, String workPathConfig, String oXPDConfig) {
        this.id = id;
        this.tcId = tcId;
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.token = token;
        this.expirationTime = expirationTime;
        this.isExpired = isExpired;
        this.isActive = isActive;
        this.creationTime = creationTime;
        this.updateTime = updateTime;
        this.url = url;
        this.workPathConfig = workPathConfig;
        this.oXPDConfig = oXPDConfig;
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

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
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

    public String getoXPDConfig() {
        return oXPDConfig;
    }

    public void setoXPDConfig(String oXPDConfig) {
        this.oXPDConfig = oXPDConfig;
    }

    @Override
    public String toString() {
        return "DemoViewModel{" +
                "id=" + id +
                ", tcId=" + tcId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                ", token='" + token + '\'' +
                ", expirationTime='" + expirationTime + '\'' +
                ", isExpired=" + isExpired +
                ", isActive=" + isActive +
                ", creationTime='" + creationTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", url='" + url + '\'' +
                ", workPathConfig='" + workPathConfig + '\'' +
                ", oXPDConfig='" + oXPDConfig + '\'' +
                '}';
    }
}
