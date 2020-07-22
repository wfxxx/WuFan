package com.definesys.dsgc.service.svcgen.utils;


import java.util.List;
import java.util.Map;

public class TreeNode {
    private String key;
    private String title;
    private String parentId;
    private Boolean isRoot = false;
    private Boolean isLeaf = false;
    private List<Map<String,String>> tableFiled;
    private List<TreeNode> children;
    private Boolean expanded;
    private String colType;
    private String isNull;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public List<Map<String, String>> getTableFiled() {
        return tableFiled;
    }

    public void setTableFiled(List<Map<String, String>> tableFiled) {
        this.tableFiled = tableFiled;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Boolean isRoot) {
        this.isRoot = isRoot;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }
}
