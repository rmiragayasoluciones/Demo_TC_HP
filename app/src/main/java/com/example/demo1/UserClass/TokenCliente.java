package com.example.demo1.UserClass;

public class TokenCliente {

    String tokenCliente;
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
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
