package com.definesys.dsgc.service.consumers.bean;



public class CreateTokenVO {

    private String dceId;

    private String csmCode;

    private String exp;

    private String envCode;

    public String getDceId() {
        return dceId;
    }

    public void setDceId(String dceId) {
        this.dceId = dceId;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getCsmCode() {
        return csmCode;
    }

    public void setCsmCode(String csmCode) {
        this.csmCode = csmCode;
    }
}
