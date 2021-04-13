package com.definesys.dsgc.service.flow.node.dto;

import com.definesys.dsgc.service.flow.dto.FlowRoadDTO;

public class VariablePreviewReqDTO {
    private String nodeId;
    //流路径图
    private FlowRoadDTO flow;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public FlowRoadDTO getFlow() {
        return flow;
    }

    public void setFlow(FlowRoadDTO flow) {
        this.flow = flow;
    }
}
