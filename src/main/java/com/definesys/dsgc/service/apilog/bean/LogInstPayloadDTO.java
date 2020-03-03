package com.definesys.dsgc.service.apilog.bean;

public class LogInstPayloadDTO {
    private String url;
    private String reqHeaders;
    private String reqPayload;
    private String resPayload;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(String reqHeaders) {
        this.reqHeaders = reqHeaders;
    }

    public String getReqPayload() {
        return reqPayload;
    }

    public void setReqPayload(String reqPayload) {
        this.reqPayload = reqPayload;
    }

    public String getResPayload() {
        return resPayload;
    }

    public void setResPayload(String resPayload) {
        this.resPayload = resPayload;
    }
}
