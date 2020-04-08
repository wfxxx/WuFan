package com.definesys.dsgc.service.dagclient.proxy.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

public class RateLimitPluginCfgVO {
    @Column(value = "P_SECOND", type = ColumnType.DB)
    private Integer second;
    @Column(value = "P_MINUTE", type = ColumnType.DB)
    private Integer minute;
    @Column(value = "P_HOUR", type = ColumnType.DB)
    private Integer hour;
    @Column(value = "P_DAY", type = ColumnType.DB)
    private Integer day;
    @Column(value = "P_MONTH", type = ColumnType.DB)
    private Integer month;
    @Column(value = "P_YEAR", type = ColumnType.DB)
    private Integer year;
    private String limit_by = "consumer";
    private String policy = "cluster";
    private boolean fault_tolerant = true;

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getLimit_by() {
        return limit_by;
    }

    public void setLimit_by(String limit_by) {
        this.limit_by = limit_by;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public boolean isFault_tolerant() {
        return fault_tolerant;
    }

    public void setFault_tolerant(boolean fault_tolerant) {
        this.fault_tolerant = fault_tolerant;
    }
}
