package com.definesys.dsgc.service.apimng.bean;

public class APIInfoListBean {

    private String apiCode;
    private String apiName;
    private String appCode;
    private String infoFull;
    private String authSystemCount;

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
}
