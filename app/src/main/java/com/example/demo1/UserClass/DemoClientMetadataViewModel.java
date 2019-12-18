package com.example.demo1.UserClass;

public class DemoClientMetadataViewModel {

    private String clientId;
    private String BusinessName;
    private String mail;
    private String sex;
    private String country;

    public DemoClientMetadataViewModel(String clientId, String businessName, String mail, String sex, String country) {
        this.clientId = clientId;
        BusinessName = businessName;
        this.mail = mail;
        this.sex = sex;
        this.country = country;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
