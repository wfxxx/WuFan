package com.definesys.dsgc.service.dess.DessInstance.bean;

public class DInstSltBean {

    private String startTime;
    private String endTime;
    private String limitTime;

    public DInstSltBean(){}

    public DInstSltBean(String startTime, String endTime, String limitTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.limitTime = limitTime;
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

    public String getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

    @Override
    public String toString() {
        return "DInstSltBean{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", limitTime='" + limitTime + '\'' +
                '}';
    }
}
