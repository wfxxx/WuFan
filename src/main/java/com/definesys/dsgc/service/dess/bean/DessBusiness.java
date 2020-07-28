package com.definesys.dsgc.service.dess.bean;

/**
 * @ClassName DessBusiness
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-7-28 18:19
 * @Version 1.0
 **/
public class DessBusiness {

    private String businessId;
    private String wsdlUrl;
    private String webServiceType;
    private String service;
    private String portType;
    private String operation;
    private String payload;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public String getWebServiceType() {
        return webServiceType;
    }

    public void setWebServiceType(String webServiceType) {
        this.webServiceType = webServiceType;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
