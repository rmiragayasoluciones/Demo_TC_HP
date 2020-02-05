package com.soluciones.demoKit.UserClass;

public class CreateDocumentViewModel {
    private String serieName;
    private int demoId;
    private String client;
    private MetadataCliente metadataViewModel;

    public CreateDocumentViewModel(String serieName, int demoId, String client, MetadataCliente metadataViewModel) {
        this.serieName = serieName;
        this.demoId = demoId;
        this.client = client;
        this.metadataViewModel = metadataViewModel;
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public int getDemoId() {
        return demoId;
    }

    public void setDemoId(int demoId) {
        this.demoId = demoId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public MetadataCliente getMetadataViewModel() {
        return metadataViewModel;
    }

    public void setMetadataViewModel(MetadataCliente metadataViewModel) {
        this.metadataViewModel = metadataViewModel;
    }
}
