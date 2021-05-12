package com.definesys.dsgc.service.dagclient.proxy.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

public class ResTransPluginCfgBean {

    @Column(value = "REMOVE_JSON", type = ColumnType.DB)
    private String removeJson;
    @Column(value = "REMOVE_HEADERS", type = ColumnType.DB)
    private String removeHeaders;
    @Column(value = "RENAME_HEADERS", type = ColumnType.DB)
    private String renameHeaders;
    @Column(value = "REPLACE_JSON", type = ColumnType.DB)
    private String replaceJson;
    @Column(value = "REPLACE_JSON_TYPE", type = ColumnType.DB)
    private String replaceJsonType;
    @Column(value = "REPLACE_HEADERS", type = ColumnType.DB)
    private String replaceHeaders;
    @Column(value = "ADD_JSON", type = ColumnType.DB)
    private String addJson;
    @Column(value = "ADD_JSON_TYPES", type = ColumnType.DB)
    private String addJsonTypes;
    @Column(value = "ADD_HEADERS", type = ColumnType.DB)
    private String addHeaders;
    @Column(value = "APPEND_JSON", type = ColumnType.DB)
    private String appendJson;
    @Column(value = "APPEND_JSON_TYPE", type = ColumnType.DB)
    private String appendJsonType;
    @Column(value = "APPEND_HEADERS", type = ColumnType.DB)
    private String appendHeaders;

    public String getRemoveJson() {
        return removeJson;
    }

    public void setRemoveJson(String removeJson) {
        this.removeJson = removeJson;
    }

    public String getRemoveHeaders() {
        return removeHeaders;
    }

    public void setRemoveHeaders(String removeHeaders) {
        this.removeHeaders = removeHeaders;
    }

    public String getRenameHeaders() {
        return renameHeaders;
    }

    public void setRenameHeaders(String renameHeaders) {
        this.renameHeaders = renameHeaders;
    }

    public String getReplaceJson() {
        return replaceJson;
    }

    public void setReplaceJson(String replaceJson) {
        this.replaceJson = replaceJson;
    }

    public String getReplaceHeaders() {
        return replaceHeaders;
    }

    public void setReplaceHeaders(String replaceHeaders) {
        this.replaceHeaders = replaceHeaders;
    }

    public String getAddJson() {
        return addJson;
    }

    public void setAddJson(String addJson) {
        this.addJson = addJson;
    }

    public String getAddHeaders() {
        return addHeaders;
    }

    public void setAddHeaders(String addHeaders) {
        this.addHeaders = addHeaders;
    }

    public String getAppendJson() {
        return appendJson;
    }

    public void setAppendJson(String appendJson) {
        this.appendJson = appendJson;
    }

    public String getAppendHeaders() {
        return appendHeaders;
    }

    public void setAppendHeaders(String appendHeaders) {
        this.appendHeaders = appendHeaders;
    }

    public String getReplaceJsonType() {
        return replaceJsonType;
    }

    public void setReplaceJsonType(String replaceJsonType) {
        this.replaceJsonType = replaceJsonType;
    }

    public String getAddJsonTypes() {
        return addJsonTypes;
    }

    public void setAddJsonTypes(String addJsonTypes) {
        this.addJsonTypes = addJsonTypes;
    }

    public String getAppendJsonType() {
        return appendJsonType;
    }

    public void setAppendJsonType(String appendJsonType) {
        this.appendJsonType = appendJsonType;
    }
}
