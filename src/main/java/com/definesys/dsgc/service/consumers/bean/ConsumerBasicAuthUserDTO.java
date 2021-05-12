package com.definesys.dsgc.service.consumers.bean;

import java.util.List;
import java.util.Map;

public class ConsumerBasicAuthUserDTO {
    private String csmCode;

    private List<Map<String,String>> pwdList;


    public List<Map<String, String>> getPwdList() {
        return pwdList;
    }

    public void setPwdList(List<Map<String, String>> pwdList) {
        this.pwdList = pwdList;
    }

    public String getCsmCode() {
        return csmCode;
    }

    public void setCsmCode(String csmCode) {
        this.csmCode = csmCode;
    }
}
