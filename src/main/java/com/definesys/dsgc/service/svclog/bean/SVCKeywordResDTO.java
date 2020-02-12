package com.definesys.dsgc.service.svclog.bean;

public class SVCKeywordResDTO {
    private String servNo;
    private String nodeName;
    private String nodeDesc;
    private String nodeType;
    private String busKeyword;

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getBusKeyword() {
        return busKeyword;
    }

    public void setBusKeyword(String busKeyword) {
        this.busKeyword = busKeyword;
    }
}
