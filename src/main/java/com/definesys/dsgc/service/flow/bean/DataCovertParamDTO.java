package com.definesys.dsgc.service.flow.bean;

import java.util.List;

public class DataCovertParamDTO {
    private List<VarDTO> input;

    private List<NodeMappingDTO> convertList;

    public List<VarDTO> getInput() {
        return input;
    }

    public void setInput(List<VarDTO> input) {
        this.input = input;
    }

    public List<NodeMappingDTO> getConvertList() {
        return convertList;
    }

    public void setConvertList(List<NodeMappingDTO> convertList) {
        this.convertList = convertList;
    }
}
