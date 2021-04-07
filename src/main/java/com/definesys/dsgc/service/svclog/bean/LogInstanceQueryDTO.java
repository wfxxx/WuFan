package com.definesys.dsgc.service.svclog.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogInstanceQueryDTO {

    private String trackId;
    private String servNo;
    private String servName;
    private String logPartition;
    private String token;
    private String startTime;
    private String endTime;
    private String instStatus;
    private String bizStatus;
    private String reqFrom;
    private String bizKey1;
    private String history;
    private String bizStatusDtl;
    private String payloadMatch;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getLogPartition() {
        return logPartition;
    }

    public void setLogPartition(String logPartition) {
        this.logPartition = logPartition;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInstStatus() {
        return instStatus;
    }

    public void setInstStatus(String instStatus) {
        this.instStatus = instStatus;
    }

    public String getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getReqFrom() {
        return reqFrom;
    }

    public void setReqFrom(String reqFrom) {
        this.reqFrom = reqFrom;
    }

    public String getBizKey1() {
        return bizKey1;
    }

    public void setBizKey1(String bizKey1) {
        this.bizKey1 = bizKey1;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getBizStatusDtl() {
        return bizStatusDtl;
    }

    public void setBizStatusDtl(String bizStatusDtl) {
        this.bizStatusDtl = bizStatusDtl;
    }

    public Date getStartTimeDate() throws ParseException {
        if(startTime == null){
            return null;
        }

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(startTime);


    }

    public Date getEndTimeDate() throws Exception{
        if(endTime == null){
            return null;
        }


        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(endTime);
    }

    public String getPayloadMatch() {
        return payloadMatch;
    }

    public void setPayloadMatch(String payloadMatch) {
        this.payloadMatch = payloadMatch;
    }
}
