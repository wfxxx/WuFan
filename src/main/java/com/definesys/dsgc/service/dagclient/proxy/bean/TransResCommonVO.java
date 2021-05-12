package com.definesys.dsgc.service.dagclient.proxy.bean;

import java.util.List;

public class TransResCommonVO {
    private List<String> headers;
    private List<String> json;
    private List<String> json_types;

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getJson() {
        return json;
    }

    public void setJson(List<String> json) {
        this.json = json;
    }

    public List<String> getJson_types() {
        return json_types;
    }

    public void setJson_types(List<String> json_types) {
        this.json_types = json_types;
    }
}
