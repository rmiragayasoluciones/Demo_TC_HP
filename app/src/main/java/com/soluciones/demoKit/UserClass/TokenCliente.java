package com.soluciones.demoKit.UserClass;

import com.google.gson.annotations.SerializedName;

public class TokenCliente {

    @SerializedName("Token")
    String tokenCliente;
    @SerializedName("Url")
    String url;

    public TokenCliente(String tokenCliente, String url) {
        this.tokenCliente = tokenCliente;
        if (url==null){
            url = "";
        }
        this.url = url;
    }

    public String getTokenCliente() {
        return tokenCliente;
    }

    public void setTokenCliente(String tokenCliente) {
        this.tokenCliente = tokenCliente;
    }

    public String getUrl() {
        if (url == null){
            url = "";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
