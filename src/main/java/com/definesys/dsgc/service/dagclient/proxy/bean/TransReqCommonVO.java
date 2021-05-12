package com.definesys.dsgc.service.dagclient.proxy.bean;

import java.util.List;

public class TransReqCommonVO {

    private List<String> querystring;
    private List<String> headers;
    private List<String> body;

    public List<String> getQuerystring() {
        return querystring;
    }

    public void setQuerystring(List<String> querystring) {
        this.querystring = querystring;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }
}
