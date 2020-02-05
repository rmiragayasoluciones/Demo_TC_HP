package com.soluciones.demoKit.UserClass;

import android.graphics.Bitmap;

public class BrandInfo {

    private static BrandInfo INSTANCE = new BrandInfo();
    private Bitmap logoMarca;
    private String nomberMarca;

    public BrandInfo() {
    }

    public static synchronized BrandInfo getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new BrandInfo();
            return INSTANCE;
        }
        return INSTANCE;
    }

    public Bitmap getLogoMarca() {
        return logoMarca;
    }

    public void setLogoMarca(Bitmap logoMarca) {
        this.logoMarca = logoMarca;
    }

    public String getNomberMarca() {
        return nomberMarca;
    }

    public void setNomberMarca(String nomberMarca) {
        this.nomberMarca = nomberMarca;
    }
}
