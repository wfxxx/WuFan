package com.definesys.dsgc.service.flow.dto;

import java.util.List;

public class FlowNodeDTO {
    //节点id
    private String nodeId;
    //节点所使用的组件代码
    private String cnptCode;
    //节点标题
    private String title;
    //节点描述
    private String desc;
    //节点类型 INPUT OPT OUTPUT
    private String type;
    //前置节点id
    private String beforeNodeId;
    //后置节点id
    private String afterNodeId;
    //子节点列表
    private List<FlowNodeDTO> children;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getCnptCode() {
        return cnptCode;
    }

    public void setCnptCode(String cnptCode) {
        this.cnptCode = cnptCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBeforeNodeId() {
        return beforeNodeId;
    }

    public void setBeforeNodeId(String beforeNodeId) {
        this.beforeNodeId = beforeNodeId;
    }

    public String getAfterNodeId() {
        return afterNodeId;
    }

    public void setAfterNodeId(String afterNodeId) {
        this.afterNodeId = afterNodeId;
    }

    public List<FlowNodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<FlowNodeDTO> children) {
        this.children = children;
    }
}
