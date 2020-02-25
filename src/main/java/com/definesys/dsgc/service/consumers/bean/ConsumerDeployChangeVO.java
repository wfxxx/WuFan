package com.definesys.dsgc.service.consumers.bean;

public class ConsumerDeployChangeVO {
    private String dceId;
    private Boolean deployment;
    private String envCode;

    public String getDceId() {
        return dceId;
    }

    public void setDceId(String dceId) {
        this.dceId = dceId;
    }

    public Boolean getDeployment() {
        return deployment;
    }

    public void setDeployment(Boolean deployment) {
        this.deployment = deployment;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }
}
