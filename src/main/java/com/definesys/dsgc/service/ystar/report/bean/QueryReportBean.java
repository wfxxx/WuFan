package com.definesys.dsgc.service.ystar.report.bean;

import java.util.Date;

public class QueryReportBean {

    private String svcCode;
    private Date date;
    private String sysCode;
    private String trgSysCode;
    private String status;
    private String token;
    private Date startTime;
    private Date endTime;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrgSysCode() {
        return trgSysCode;
    }

    public void setTrgSysCode(String trgSysCode) {
        this.trgSysCode = trgSysCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSvcCode() {
        return svcCode;
    }

    public void setSvcCode(String svcCode) {
        this.svcCode = svcCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
}
