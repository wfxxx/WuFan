package com.definesys.dsgc.service.svcgen.bean;

import java.util.List;

public class SoapDeployProfileBean {
    private String servNo;
    private String envCode;
    private String dplName;
    private String saCode;
    private String bsUri;
    private List<OBHeaderBean> obHeaders;

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getDplName() {
        return dplName;
    }

    public void setDplName(String dplName) {
        this.dplName = dplName;
    }

    public String getSaCode() {
        return saCode;
    }

    public void setSaCode(String saCode) {
        this.saCode = saCode;
    }

    public String getBsUri() {
        return bsUri;
    }

    public void setBsUri(String bsUri) {
        this.bsUri = bsUri;
    }

    public List<OBHeaderBean> getObHeaders() {
        return obHeaders;
    }

    public void setObHeaders(List<OBHeaderBean> obHeaders) {
        this.obHeaders = obHeaders;
    }
}
