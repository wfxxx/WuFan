package com.definesys.dsgc.service.svclog.bean;

import com.definesys.mpaas.query.annotation.Table;
import com.definesys.mpaas.query.model.MpaasBasePojo;

@Table(value = "DSGC_SERV_RETRY_JOB")
public class RetryJobDTO extends MpaasBasePojo {

    private String jobId;
    private String trackId;
    private String retrySystem;
    private String errorMsg;
    private String reqContent;
    private String resContent;
    private String createdBy;
    private String creationDate;
    private String status;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getRetrySystem() {
        return retrySystem;
    }

    public void setRetrySystem(String retrySystem) {
        this.retrySystem = retrySystem;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getReqContent() {
        return reqContent;
    }

    public void setReqContent(String reqContent) {
        this.reqContent = reqContent;
    }

    public String getResContent() {
        return resContent;
    }

    public void setResContent(String resContent) {
        this.resContent = resContent;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
