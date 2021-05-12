package com.definesys.dsgc.service.svcmng.bean;

public class AddSoapServVO {
    private String servNo;
    private String servName;
    private String appCode;
    private String shareType;
    private String servUri;
    private String wsdlFunction;

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getServUri() {
        return servUri;
    }

    public void setServUri(String servUri) {
        this.servUri = servUri;
    }

    public String getWsdlFunction() {
        return wsdlFunction;
    }

    public void setWsdlFunction(String wsdlFunction) {
        this.wsdlFunction = wsdlFunction;
    }
}
