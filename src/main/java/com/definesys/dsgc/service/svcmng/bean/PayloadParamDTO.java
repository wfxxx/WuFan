package com.definesys.dsgc.service.svcmng.bean;

public class PayloadParamDTO {
    private String paramCode;
    private String paramType;
    private String paramNeed;
    private String paramSample;
    private String paramDesc;

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamNeed() {
        return paramNeed;
    }

    public void setParamNeed(String paramNeed) {
        this.paramNeed = paramNeed;
    }

    public String getParamSample() {
        return paramSample;
    }

    public void setParamSample(String paramSample) {
        this.paramSample = paramSample;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }
}
