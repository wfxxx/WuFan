package com.definesys.dsgc.service.dess.DessLog.bean;

import com.definesys.mpaas.query.annotation.*;

import java.util.Date;

/**
 * @ClassName DessLog
 * @Description TODO
 * @Author ystar
 * @Date 2020-7-28 18:21
 * @Version 1.0
 **/
@Table(value = "DESS_LOG")
public class DessLog {
    @RowID(sequence = "DESS_LOG_S", type = RowIDType.AUTO)
    private String logId;
    private String jobNo;
    private String groupName;
    private String logStatus;
    //计划执行时间
    private Date doTime;
    //实际执行时间
    private Date creationDate;
    private Integer retryTimes;
    private Integer runTime;
    @Column(type = ColumnType.JAVA)
    private String jobName;
    @Column(type = ColumnType.JAVA)
    private String businessType;
    @Column(type = ColumnType.JAVA)
    private DessLogPayload reqPayload;
    @Column(type = ColumnType.JAVA)
    private DessLogPayload resPayload;
    @Column(type = ColumnType.JAVA)
    private String reqUrl;

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public Date getDoTime() {
        return doTime;
    }

    public void setDoTime(Date doTime) {
        this.doTime = doTime;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
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

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
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
