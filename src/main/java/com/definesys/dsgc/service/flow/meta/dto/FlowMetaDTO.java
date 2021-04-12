package com.definesys.dsgc.service.flow.meta.dto;

public class FlowMetaDTO {
    private String flowId;
    private String flowVersion;
    private String metaId;
    private String metaName;
    private String sour;
    private String metaType;
    private DataNodeDTO metaTree;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(String flowVersion) {
        this.flowVersion = flowVersion;
    }

    public String getMetaId() {
        return metaId;
    }

    public String getMetaName() {
        return metaName;
    }

    public void setMetaName(String metaName) {
        this.metaName = metaName;
    }

    public void setMetaId(String metaId) {
        this.metaId = metaId;
    }

    public String getSour() {
        return sour;
    }

    public void setSour(String sour) {
        this.sour = sour;
    }

    public String getMetaType() {
        return metaType;
    }

    public void setMetaType(String metaType) {
        this.metaType = metaType;
    }

    public DataNodeDTO getMetaTree() {
        return metaTree;
    }

    public void setMetaTree(DataNodeDTO metaTree) {
        this.metaTree = metaTree;
    }
}
