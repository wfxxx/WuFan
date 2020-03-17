package com.definesys.dsgc.service.apibs.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

public class DagBsListDTO {
    private String bsId;
    private String bsCode;
    private String bsDesc;
    private String appName;
    private String appCode;
    private String envCode;
    @Column(type = ColumnType.JAVA)
    private String envName="";
    public String getBsId() {
        return bsId;
    }

    public void setBsId(String bsId) {
        this.bsId = bsId;
    }

    public String getBsCode() {
        return bsCode;
    }

    public void setBsCode(String bsCode) {
        this.bsCode = bsCode;
    }

    public String getBsDesc() {
        return bsDesc;
    }

    public void setBsDesc(String bsDesc) {
        this.bsDesc = bsDesc;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

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
}
