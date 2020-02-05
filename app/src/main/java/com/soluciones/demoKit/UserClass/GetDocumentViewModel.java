package com.soluciones.demoKit.UserClass;

public class GetDocumentViewModel {

    private String id;
    private String serieName;
    private String demoId;
    private String filePath;
    private String client;
    private MetadataCliente metadataViewModel;


    public GetDocumentViewModel(String id, String serieName, String demoId, String filePath, String client, MetadataCliente metadataViewModel) {
        this.id = id;
        this.serieName = serieName;
        this.demoId = demoId;
        this.filePath = filePath;
        this.client = client;
        this.metadataViewModel = metadataViewModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public String getDemoId() {
        return demoId;
    }

    public void setDemoId(String demoId) {
        this.demoId = demoId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
