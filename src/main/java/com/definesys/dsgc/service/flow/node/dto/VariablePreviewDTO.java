package com.definesys.dsgc.service.flow.node.dto;

import java.util.List;

public class VariablePreviewDTO {
    private String payloadType;
    private List<VariableDTO> varList;

    public String getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public List<VariableDTO> getVarList() {
        return varList;
    }

    public void setVarList(List<VariableDTO> varList) {
        this.varList = varList;
    }
}
