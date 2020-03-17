package com.definesys.dsgc.service.apilr.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

public class DagLrListDTO {
    private String dlId;
    private String lrName;
    private String lrDesc;
    private String appName;
    private String creationDate;
    private String envCode;
    @Column(type = ColumnType.JAVA)
    private String envName="";
    public String getDlId() {
        return dlId;
    }

    public void setDlId(String dlId) {
        this.dlId = dlId;
    }

    public String getLrName() {
        return lrName;
    }

    public void setLrName(String lrName) {
        this.lrName = lrName;
    }

    public String getLrDesc() {
        return lrDesc;
    }

    public void setLrDesc(String lrDesc) {
        this.lrDesc = lrDesc;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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
