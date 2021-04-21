package com.definesys.dsgc.service.flow.meta.dto;

import java.util.List;

public class DataNodeDTO {
    private String id;
    private String title;
    private String key;
    private boolean expanded;
    private List<DataNodeDTO> children;
    private boolean isLeaf;
    private String valueType;
    private String minOccur;
    private String maxOccur;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<DataNodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DataNodeDTO> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getMinOccur() {
        return minOccur;
    }

    public void setMinOccur(String minOccur) {
        this.minOccur = minOccur;
    }

    public String getMaxOccur() {
        return maxOccur;
    }

    public void setMaxOccur(String maxOccur) {
        this.maxOccur = maxOccur;
    }
}
