package com.definesys.dsgc.service.flow.dto;

public class FlowDeployDTO {
    private String flowId;
    private String flowVersion;
    private String deployDesc;

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

    public String getDeployDesc() {
        return deployDesc;
    }

    public void setDeployDesc(String deployDesc) {
        this.deployDesc = deployDesc;
    }
}
