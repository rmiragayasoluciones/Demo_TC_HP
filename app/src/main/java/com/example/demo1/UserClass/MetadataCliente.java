package com.example.demo1.UserClass;

public class MetadataCliente {
    private String razonSocial;
    private String mail;
    private String sexo;
    private String pais;
    private String fecha;

    public MetadataCliente(String razonSocial, String mail, String sexo, String pais, String fecha) {
        this.razonSocial = razonSocial;
        this.mail = mail;
        this.sexo = sexo;
        this.pais = pais;
        this.fecha = fecha;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
