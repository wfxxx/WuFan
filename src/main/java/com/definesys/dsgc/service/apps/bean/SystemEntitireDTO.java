package com.definesys.dsgc.service.apps.bean;

import java.util.List;

public class SystemEntitireDTO {
    private String id;
    private String sysName;
    private String sysCode;
    private String sysDesc;
    private List<UserResDTO> userResDTOS;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysDesc() {
        return sysDesc;
    }

    public void setSysDesc(String sysDesc) {
        this.sysDesc = sysDesc;
    }

    public List<UserResDTO> getUserResDTOS() {
        return userResDTOS;
    }

    public void setUserResDTOS(List<UserResDTO> userResDTOS) {
        this.userResDTOS = userResDTOS;
    }
}
