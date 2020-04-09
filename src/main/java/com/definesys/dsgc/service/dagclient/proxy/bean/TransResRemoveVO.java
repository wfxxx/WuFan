package com.definesys.dsgc.service.dagclient.proxy.bean;

import java.util.List;

public class TransResRemoveVO {
    private List<String> headers;
    private List<String> json;

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
}
