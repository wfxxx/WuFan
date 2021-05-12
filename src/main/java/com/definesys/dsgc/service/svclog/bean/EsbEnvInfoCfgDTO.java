package com.definesys.dsgc.service.svclog.bean;

public class EsbEnvInfoCfgDTO {
    private String envCode;
    private String envName;
    private String dsgcUri;

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getDsgcUri() {
        return dsgcUri;
    }

    public void setDsgcUri(String dsgcUri) {
        this.dsgcUri = dsgcUri;
    }
}
