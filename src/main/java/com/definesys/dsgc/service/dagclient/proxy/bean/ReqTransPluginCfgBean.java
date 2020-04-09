package com.definesys.dsgc.service.dagclient.proxy.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

public class ReqTransPluginCfgBean {

    @Column(value = "HTTP_METHOD", type = ColumnType.DB)
    private String httpMethod;
    @Column(value = "REMOVE_BODY", type = ColumnType.DB)
    private String removeBody;
    @Column(value = "REMOVE_HEADERS", type = ColumnType.DB)
    private String removeHeaders;
    @Column(value = "REMOVE_QUERYSTRING", type = ColumnType.DB)
    private String removeQuerystring;
    @Column(value = "RENAME_BODY", type = ColumnType.DB)
    private String renameBody;
    @Column(value = "RENAME_HEADERS", type = ColumnType.DB)
    private String renameHeaders;
    @Column(value = "RENAME_QUERYSTRING", type = ColumnType.DB)
    private String renameQuerystring;
    @Column(value = "REPLACE_BODY", type = ColumnType.DB)
    private String replaceBody;
    @Column(value = "REPLACE_HEADERS", type = ColumnType.DB)
    private String replaceHeaders;
    @Column(value = "REPALCE_QUERYSTRING", type = ColumnType.DB)
    private String replaceQuerystring;
    @Column(value = "REPLACE_URL", type = ColumnType.DB)
    private String replaceUrl;
    @Column(value = "ADD_BODY", type = ColumnType.DB)
    private String addBody;
    @Column(value = "ADD_HEADERS", type = ColumnType.DB)
    private String addHeaders;
    @Column(value = "ADD_QUERYSTRING", type = ColumnType.DB)
    private String addQuerystring;
    @Column(value = "APPEND_BODY", type = ColumnType.DB)
    private String appendBody;
    @Column(value = "APPEND_HEADERS", type = ColumnType.DB)
    private String appendHeaders;
    @Column(value = "APPEND_QUERYSTRING", type = ColumnType.DB)
    private String appendQuerystring;

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRemoveBody() {
        return removeBody;
    }

    public void setRemoveBody(String removeBody) {
        this.removeBody = removeBody;
    }

    public String getRemoveHeaders() {
        return removeHeaders;
    }

    public void setRemoveHeaders(String removeHeaders) {
        this.removeHeaders = removeHeaders;
    }

    public String getRemoveQuerystring() {
        return removeQuerystring;
    }

    public void setRemoveQuerystring(String removeQuerystring) {
        this.removeQuerystring = removeQuerystring;
    }

    public String getRenameBody() {
        return renameBody;
    }

    public void setRenameBody(String renameBody) {
        this.renameBody = renameBody;
    }

    public String getRenameHeaders() {
        return renameHeaders;
    }

    public void setRenameHeaders(String renameHeaders) {
        this.renameHeaders = renameHeaders;
    }

    public String getRenameQuerystring() {
        return renameQuerystring;
    }

    public void setRenameQuerystring(String renameQuerystring) {
        this.renameQuerystring = renameQuerystring;
    }

    public String getReplaceBody() {
        return replaceBody;
    }

    public void setReplaceBody(String replaceBody) {
        this.replaceBody = replaceBody;
    }

    public String getReplaceHeaders() {
        return replaceHeaders;
    }

    public void setReplaceHeaders(String replaceHeaders) {
        this.replaceHeaders = replaceHeaders;
    }

    public String getReplaceQuerystring() {
        return replaceQuerystring;
    }

    public void setReplaceQuerystring(String replaceQuerystring) {
        this.replaceQuerystring = replaceQuerystring;
    }

    public String getReplaceUrl() {
        return replaceUrl;
    }

    public void setReplaceUrl(String replaceUrl) {
        this.replaceUrl = replaceUrl;
    }

    public String getAddBody() {
        return addBody;
    }

    public void setAddBody(String addBody) {
        this.addBody = addBody;
    }

    public String getAddHeaders() {
        return addHeaders;
    }

    public void setAddHeaders(String addHeaders) {
        this.addHeaders = addHeaders;
    }

    public String getAddQuerystring() {
        return addQuerystring;
    }

    public void setAddQuerystring(String addQuerystring) {
        this.addQuerystring = addQuerystring;
    }

    public String getAppendBody() {
        return appendBody;
    }

    public void setAppendBody(String appendBody) {
        this.appendBody = appendBody;
    }

    public String getAppendHeaders() {
        return appendHeaders;
    }

    public void setAppendHeaders(String appendHeaders) {
        this.appendHeaders = appendHeaders;
    }

    public String getAppendQuerystring() {
        return appendQuerystring;
    }

    public void setAppendQuerystring(String appendQuerystring) {
        this.appendQuerystring = appendQuerystring;
    }
}
