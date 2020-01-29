package com.example.demo1.UserClass;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetadataCliente {
    private static final String TAG = "MetadataCliente";
    private String businessName;
    private String email;
    private String sex;
    private String country;
    private String documentName;
    private String code;
    private String reason;
    private Date fecha;

    public MetadataCliente(String businessName, String mail, String sex, String country,String documentName, String codigoDe, String razonDe ,String fecha) {
        this.businessName = businessName;
        this.email = mail;
        this.sex = sex;
        this.country = country;
        this.documentName = documentName;
        this.code = codigoDe;
        this.reason = razonDe;
        this.fecha = convertStringToDate(fecha);
    }

    public MetadataCliente() {
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date convertStringToDate(String fechaString){
        Log.d(TAG, "convertStringToDate: convertir fecha " + fechaString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(fechaString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertedDate;

    }
}


