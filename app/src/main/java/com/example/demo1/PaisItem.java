package com.example.demo1;

public class PaisItem {

    private String mPaisNombre;
    private int mPaisBandera;

    public PaisItem(String mPaisNombre, int mPaisBandera) {
        this.mPaisNombre = mPaisNombre;
        this.mPaisBandera = mPaisBandera;
    }

    public String getmPaisNombre() {
        return mPaisNombre;
    }

    public void setmPaisNombre(String mPaisNombre) {
        this.mPaisNombre = mPaisNombre;
    }

    public int getmPaisBandera() {
        return mPaisBandera;
    }

    public void setmPaisBandera(int mPaisBandera) {
        this.mPaisBandera = mPaisBandera;
    }
}
