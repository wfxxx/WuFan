package com.definesys.dsgc.service.svcmng.bean;

import java.util.ArrayList;
import java.util.List;

public class SVCMngInfoListBean {
    private String servNo;
    private String servName;
    private String subSystem;
    private String shareTypeDesc;
    private String authSystemCount;
    private Integer infoFull;
    private String isProd;
    private String deployedNode;
    private String deployedNodeName;
    private String curBpmNode;
    private String ibUri;
    private String httpMethod;
    private List<DeployedEnvInfoBean> deplEnv = new ArrayList<DeployedEnvInfoBean>();

    public String getIbUri() {
        return ibUri;
    }

    public void setIbUri(String ibUri) {
        this.ibUri = ibUri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getShareTypeDesc() {
        return shareTypeDesc;
    }

    public void setShareTypeDesc(String shareTypeDesc) {
        this.shareTypeDesc = shareTypeDesc;
    }

    public String getAuthSystemCount() {
        return authSystemCount;
    }

    public void setAuthSystemCount(String authSystemCount) {
        this.authSystemCount = authSystemCount;
    }

    public String getIsProd() {
        return isProd;
    }

    public void setIsProd(String isProd) {
        this.isProd = isProd;
    }

    public Integer getInfoFull() {
        return infoFull;
    }

    public void setInfoFull(Integer infoFull) {
        this.infoFull = infoFull;
    }

    public String getDeployedNode() {
        return deployedNode;
    }

    public void setDeployedNode(String deployedNode) {
        this.deployedNode = deployedNode;
    }

    public String getDeployedNodeName() {
        return deployedNodeName;
    }

    public void setDeployedNodeName(String deployedNodeName) {
        this.deployedNodeName = deployedNodeName;
    }

    public String getCurBpmNode() {
        return curBpmNode;
    }

    public void setCurBpmNode(String curBpmNode) {
        this.curBpmNode = curBpmNode;
    }

    public List<DeployedEnvInfoBean> getDeplEnv() {
        return deplEnv;
    }

    public void setDeplEnv(List<DeployedEnvInfoBean> deplEnv) {
        this.deplEnv = deplEnv;
    }
}
