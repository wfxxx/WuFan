package com.definesys.dsgc.service.svcgen.bean;

public class DBDeployProfileBean {
    private String dpId;
    private String servNo;
    private String dplName;
    private String envCode;
    private String jndiName;
    private String envCodeMeaning;

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getDplName() {
        return dplName;
    }

    public void setDplName(String dplName) {
        this.dplName = dplName;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getEnvCodeMeaning() {
        return envCodeMeaning;
    }

    public void setEnvCodeMeaning(String envCodeMeaning) {
        this.envCodeMeaning = envCodeMeaning;
    }
}
