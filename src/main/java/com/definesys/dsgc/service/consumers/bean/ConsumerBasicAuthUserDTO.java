package com.definesys.dsgc.service.consumers.bean;

public class ConsumerBasicAuthUserDTO {
    private String csmCode;

    private String pwd;


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCsmCode() {
        return csmCode;
    }

    public void setCsmCode(String csmCode) {
        this.csmCode = csmCode;
    }
}
