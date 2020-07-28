package com.definesys.dsgc.service.apilr.bean;

import com.definesys.dsgc.service.svcmng.bean.DeployedEnvInfoBean;
import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

import java.util.List;

public class DagLrListDTO {
    private String dlId;
    private String lrName;
    private String lrDesc;
    private String appName;
    private String creationDate;
    List<DeployedEnvInfoBean> envList;

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

    public List<DeployedEnvInfoBean> getEnvList() {
        return envList;
    }

    public void setEnvList(List<DeployedEnvInfoBean> envList) {
        this.envList = envList;
    }
}
