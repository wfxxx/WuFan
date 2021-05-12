package com.definesys.dsgc.service.apibs.bean;

import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.svcmng.bean.DeployedEnvInfoBean;
import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

import java.util.List;

public class DagBsListDTO {
    private String bsId;
    private String bsCode;
    private String bsDesc;
    private String appName;
    private String appCode;
    List<DeployedEnvInfoBean> envList;

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

    public List<DeployedEnvInfoBean> getEnvList() {
        return envList;
    }

    public void setEnvList(List<DeployedEnvInfoBean> envList) {
        this.envList = envList;
    }
}
