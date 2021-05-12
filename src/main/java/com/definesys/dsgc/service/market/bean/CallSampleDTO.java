package com.definesys.dsgc.service.market.bean;

public class CallSampleDTO {
    private StringBuilder requestSample;
    private String requestMessage;
    private String responseMessage;

    public StringBuilder getRequestSample() {
        return requestSample;
    }

    public void setRequestSample(StringBuilder requestSample) {
        this.requestSample = requestSample;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
