package com.definesys.dsgc.service.dagclient.proxy.bean;

import java.util.List;

public class TransReqReplaceVO {
    private List<String> querystring;
    private List<String> headers;
    private List<String> body;
    private String uri;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
