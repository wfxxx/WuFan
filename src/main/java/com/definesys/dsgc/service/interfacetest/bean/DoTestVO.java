package com.definesys.dsgc.service.interfacetest.bean;

import java.util.List;
import java.util.Map;

public class DoTestVO {
    private String envCode;
    private String consumerCode;
    private String url;
    private String methodType;
    private String requestBody;
    private List<Map<String ,String>> paramters;
    private List<Map<String ,String>> headers;

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public List<Map<String, String>> getParamters() {
        return paramters;
    }

    public void setParamters(List<Map<String, String>> paramters) {
        this.paramters = paramters;
    }

    public List<Map<String, String>> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Map<String, String>> headers) {
        this.headers = headers;
    }
}
