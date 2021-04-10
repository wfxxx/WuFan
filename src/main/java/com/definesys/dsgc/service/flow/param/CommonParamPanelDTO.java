package com.definesys.dsgc.service.flow.param;

import com.definesys.dsgc.service.flow.dto.FlowRoadDTO;

public class CommonParamPanelDTO {

    private String nodeId;
    private String inputType;
    private String inputMeta;
    private String inputValue;
    private String outputType;
    private String outputMeta;
    private String outputValue;

    //流路径图
    private FlowRoadDTO flow;


    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
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

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
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
}
