package com.definesys.dsgc.service.flow.bean;

public class NodeMappingDTO {
    private String startVar;
    private String startNodeKey;
    private String endNodeKey;

    public String getStartVar() {
        return startVar;
    }

    public void setStartVar(String startVar) {
        this.startVar = startVar;
    }

    public String getStartNodeKey() {
        return startNodeKey;
    }

    public void setStartNodeKey(String startNodeKey) {
        this.startNodeKey = startNodeKey;
    }

    public String getEndNodeKey() {
        return endNodeKey;
    }

    public void setEndNodeKey(String endNodeKey) {
        this.endNodeKey = endNodeKey;
    }
}
