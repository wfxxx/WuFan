package com.definesys.dsgc.service.flow.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class FlowRoadDTO {

    private String flowId;
    private String flowVersion;
    private String title;
    private String startNodeId;
    private List<FlowNodeDTO> nodeList;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(String startNodeId) {
        this.startNodeId = startNodeId;
    }

    public List<FlowNodeDTO> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<FlowNodeDTO> nodeList) {
        this.nodeList = nodeList;
    }
}
