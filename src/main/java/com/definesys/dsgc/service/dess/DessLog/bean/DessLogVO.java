package com.definesys.dsgc.service.dess.DessLog.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

import java.util.Date;

public class DessLogVO {

    private String logId;
    private String jobNo;
    private String logStatus;
    private String doTime;
    private String creationDate;
    private Integer retryTimes;
    private String jobName;
    private String businessType;
    private DessLogPayload reqPayload;
    private DessLogPayload resPayload;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public String getDoTime() {
        return doTime;
    }

    public void setDoTime(String doTime) {
        this.doTime = doTime;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public DessLogPayload getReqPayload() {
        return reqPayload;
    }

    public void setReqPayload(DessLogPayload reqPayload) {
        this.reqPayload = reqPayload;
    }

    public DessLogPayload getResPayload() {
        return resPayload;
    }

    public void setResPayload(DessLogPayload resPayload) {
        this.resPayload = resPayload;
    }
}
