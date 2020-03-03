package com.definesys.dsgc.service.apilog.bean;

public class ApiLogInstListDTO {
    private String trackId;
    private String apiName;
    private String consumerName;
    private String startTime;
    private String httpMethod;
    private String reqMsgSize;
    private Integer costTime;
    private String resCode;
    private String resMsgSize;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getReqMsgSize() {
        return reqMsgSize;
    }

    public void setReqMsgSize(String reqMsgSize) {
        this.reqMsgSize = reqMsgSize;
    }

    public Integer getCostTime() {
        return costTime;
    }

    public void setCostTime(Integer costTime) {
        this.costTime = costTime;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsgSize() {
        return resMsgSize;
    }

    public void setResMsgSize(String resMsgSize) {
        this.resMsgSize = resMsgSize;
    }
}
