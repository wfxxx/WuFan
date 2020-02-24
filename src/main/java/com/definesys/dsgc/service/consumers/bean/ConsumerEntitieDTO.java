package com.definesys.dsgc.service.consumers.bean;

import com.definesys.dsgc.service.apps.bean.UserResDTO;

import java.util.List;

public class ConsumerEntitieDTO {
    private String dceId;
    private String csmCode;
    private String csmName;
    private String csmDesc;
    private String owner;
    private List<UserResDTO> userResDTOS;
    private String deployEnv;

    public String getDceId() {
        return dceId;
    }

    public void setDceId(String dceId) {
        this.dceId = dceId;
    }

    public String getCsmCode() {
        return csmCode;
    }

    public void setCsmCode(String csmCode) {
        this.csmCode = csmCode;
    }

    public String getCsmName() {
        return csmName;
    }

    public void setCsmName(String csmName) {
        this.csmName = csmName;
    }

    public String getCsmDesc() {
        return csmDesc;
    }

    public void setCsmDesc(String csmDesc) {
        this.csmDesc = csmDesc;
    }

    public List<UserResDTO> getUserResDTOS() {
        return userResDTOS;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setUserResDTOS(List<UserResDTO> userResDTOS) {
        this.userResDTOS = userResDTOS;
    }

    public String getDeployEnv() {
        return deployEnv;
    }

    public void setDeployEnv(String deployEnv) {
        this.deployEnv = deployEnv;
    }
}
