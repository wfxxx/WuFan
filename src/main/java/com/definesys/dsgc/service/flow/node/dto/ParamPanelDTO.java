package com.definesys.dsgc.service.flow.node.dto;

import com.definesys.dsgc.service.flow.dto.FlowRoadDTO;

public class ParamPanelDTO {

    private String flowId;
    private String flowVersion;

    private String nodeId;
    private String inputMeta;
    private String inputValue;
    private String outputMeta;
    private String outputValue;

    private Param params;

    //流路径图
    private FlowRoadDTO flow;

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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getInputMeta() {
        return inputMeta;
    }

    public void setInputMeta(String inputMeta) {
        this.inputMeta = inputMeta;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getOutputMeta() {
        return outputMeta;
    }

    public void setOutputMeta(String outputMeta) {
        this.outputMeta = outputMeta;
    }

    public String getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(String outputValue) {
        this.outputValue = outputValue;
    }

    public FlowRoadDTO getFlow() {
        return flow;
    }

    public void setFlow(FlowRoadDTO flow) {
        this.flow = flow;
    }

    public Param getParams() {
        return params;
    }

    public void setParams(Param params) {
        this.params = params;
    }
}
