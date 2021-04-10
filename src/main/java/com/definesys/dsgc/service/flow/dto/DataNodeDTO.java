package com.definesys.dsgc.service.flow.dto;

import java.util.List;

public class DataNodeDTO {
    private String title;
    private String key;
    private boolean expanded;
    private List<DataNodeDTO> children;
    private boolean isLeaf;

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
}
