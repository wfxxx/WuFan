package com.definesys.dsgc.service.envinfo.bean;

import java.util.List;

public class DagEnvInfoDTO {

    private String deicId;
    private String envCode;
    private String envName;
    private Integer envSeq;
    private String reqLocation;
    private String adminLocation;
    private String envType;
    private String adminUser;
    private String adminPd;
    private List<DagEnvInfoNodeBean> nodeList;


    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public Integer getEnvSeq() {
        return envSeq;
    }

    public void setEnvSeq(Integer envSeq) {
        this.envSeq = envSeq;
    }

    public String getReqLocation() {
        return reqLocation;
    }

    public void setReqLocation(String reqLocation) {
        this.reqLocation = reqLocation;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }


    public String getAdminLocation() {
        return adminLocation;
    }

    public void setAdminLocation(String adminLocation) {
        this.adminLocation = adminLocation;
    }

    public String getDeicId() {
        return deicId;
    }

    public void setDeicId(String deicId) {
        this.deicId = deicId;
    }

    public String getEnvType() {
        return envType;
    }

    public void setEnvType(String envType) {
        this.envType = envType;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getAdminPd() {
        return adminPd;
    }

    public void setAdminPd(String adminPd) {
        this.adminPd = adminPd;
    }

    public List<DagEnvInfoNodeBean> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<DagEnvInfoNodeBean> nodeList) {
        this.nodeList = nodeList;
    }
}
