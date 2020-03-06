package com.definesys.dsgc.service.svcmng.bean;

public class ServUriDTO {
    private String ibUri;
    private String uriType;
    private String soapOper;
    private String transportType;
    private String httpMethod;

    public String getIbUri() {
        return ibUri;
    }

    public void setIbUri(String ibUri) {
        this.ibUri = ibUri;
    }

    public String getUriType() {
        return uriType;
    }

    public void setUriType(String uriType) {
        this.uriType = uriType;
    }

    public String getSoapOper() {
        return soapOper;
    }

    public void setSoapOper(String soapOper) {
        this.soapOper = soapOper;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}
