package com.definesys.dsgc.service.flow.dto;

public class FlowReqCommonDTO {

    private String flowId;

    private String projectName;
    private String queryCon0;

    public String getProjectName() {
        return projectName;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getQueryCon0() {
        return queryCon0;
    }

    public void setQueryCon0(String queryCon0) {
        this.queryCon0 = queryCon0;
    }
}
