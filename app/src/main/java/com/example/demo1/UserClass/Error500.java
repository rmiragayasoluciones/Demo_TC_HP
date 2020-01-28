package com.example.demo1.UserClass;

public class Error500 {
    private String statusCode;
    private String displayError;
    private String error;

    public Error500(String statusCode, String displayError, String error) {
        this.statusCode = statusCode;
        if (displayError == null || displayError.isEmpty()){
            this.displayError = "Ocurrio un error inesperado. Contacte a la mesa de ayuda.";
        } else {
            this.displayError = displayError;
        }
        this.displayError = displayError;
        this.error = error;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDisplayError() {
        return displayError;
    }

    public void setDisplayError(String displayError) {
        this.displayError = displayError;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
