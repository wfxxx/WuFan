package com.definesys.dsgc.service.dess.bean;

import java.util.Date;

/**
 * @ClassName DessLog
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-7-28 18:21
 * @Version 1.0
 **/
public class DessLog {

    private String logId;
    private String jobNo;
    private String groupName;
    private String status;
    //计划执行时间
    private Date doTime;
    //实际执行时间
    private Date creationDate;
    private Integer retryTimes;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
