package com.definesys.dsgc.service.apimng.bean;

public class APIInfoListBean {

    private String apiCode;
    private String apiName;
    private String apiDesc;
    private String appCode;
    private String infoFull;
    private String authSystemCount;
    private String provider;
    private String httpMethod;
    private String ibUri;

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }


    public String getInfoFull() {
        return infoFull;
    }

    public void setInfoFull(String infoFull) {
        this.infoFull = infoFull;
    }

    public String getAuthSystemCount() {
        return authSystemCount;
    }

    public void setAuthSystemCount(String authSystemCount) {
        this.authSystemCount = authSystemCount;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getIbUri() {
        return ibUri;
    }

    public void setIbUri(String ibUri) {
        this.ibUri = ibUri;
    }
}
