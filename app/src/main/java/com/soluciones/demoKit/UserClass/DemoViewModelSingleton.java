package com.soluciones.demoKit.UserClass;

public class DemoViewModelSingleton {
    private static DemoViewModelSingleton mInstance;
    private DemoViewModel demoViewModel;
    private MetadataCliente metadataCliente = new MetadataCliente();

    private DemoViewModelSingleton(DemoViewModel demoViewModel){
        this.demoViewModel = demoViewModel;
    }

    public static synchronized DemoViewModelSingleton getInstance(DemoViewModel demoViewModel){
        if (mInstance == null){
            mInstance = new DemoViewModelSingleton(demoViewModel);
        }
        return mInstance;
    }

 //LLAMAR SI NO SE USA PRIMERO
    public static synchronized DemoViewModelSingleton getInstance(){
        return mInstance;
    }

    public DemoViewModel getDemoViewModelGuardado() {
        return demoViewModel;
    }

    public void setMetadataCliente(MetadataCliente metadataCliente) {
        this.metadataCliente = metadataCliente;
    }


    public MetadataCliente getMetadataCliente() {
        return metadataCliente;
    }

    public void borrarMetadataCliente(){
        this.metadataCliente = new MetadataCliente();
    }
}
