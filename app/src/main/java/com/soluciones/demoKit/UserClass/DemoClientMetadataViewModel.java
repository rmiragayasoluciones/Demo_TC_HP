package com.soluciones.demoKit.UserClass;

public class DemoClientMetadataViewModel {

    private String clientId;
    private String businessName;
    private String email;
    private String sex;
    private String country;

    public DemoClientMetadataViewModel(String clientId, String businessName, String mail, String sex, String country) {
        this.clientId = clientId;
        this.businessName = businessName;
        this.email = mail;
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
        return businessName;
    }

    public void setBusinessName(String businessName) {
        businessName = businessName;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
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
