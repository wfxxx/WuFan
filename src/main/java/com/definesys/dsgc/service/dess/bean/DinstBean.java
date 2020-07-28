package com.definesys.dsgc.service.dess.bean;

import java.util.Date;

/**
 * @ClassName DinstBean
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-7-28 18:18
 * @Version 1.0
 **/
public class DinstBean {


    private String jobNo;
    private String businessId;
    private String jobName;
    private String jobType;
    private String status;
    private String frequency; //json字符串，接收corn表达式
    private Date nextDoTime;
    private Date aliveStart;
    private Date aliveEnd;
    private String group="default";
    private Integer sucessTimes;
    private Integer failTimes;
    private Integer avgRunTime;
    private String description;
    private String version;

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Date getNextDoTime() {
        return nextDoTime;
    }

    public void setNextDoTime(Date nextDoTime) {
        this.nextDoTime = nextDoTime;
    }

    public Date getAliveStart() {
        return aliveStart;
    }

    public void setAliveStart(Date aliveStart) {
        this.aliveStart = aliveStart;
    }

    public Date getAliveEnd() {
        return aliveEnd;
    }

    public void setAliveEnd(Date aliveEnd) {
        this.aliveEnd = aliveEnd;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getSucessTimes() {
        return sucessTimes;
    }

    public void setSucessTimes(Integer sucessTimes) {
        this.sucessTimes = sucessTimes;
    }

    public Integer getFailTimes() {
        return failTimes;
    }

    public void setFailTimes(Integer failTimes) {
        this.failTimes = failTimes;
    }

    public Integer getAvgRunTime() {
        return avgRunTime;
    }

    public void setAvgRunTime(Integer avgRunTime) {
        this.avgRunTime = avgRunTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
