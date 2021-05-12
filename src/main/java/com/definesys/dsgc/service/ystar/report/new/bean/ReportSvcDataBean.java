package com.definesys.dsgc.service.ystar.report.bean;

import java.util.List;
import java.util.Map;

/**
 * 返回数据
 */
public class ReportSvcDataBean {
    String startTime; //开始时间
    String endTime; //结束时间
    String serviceType;//接口类型
    String costType ;//耗时值类型：ALL:全部，ESB:ESB内耗
    int standard;//（90分位标准线值）
    List<Map<String, Object>> curList; //本周接口统计
    List<Map<String, Object>> lastList; //上周接口统计
    public ReportSvcDataBean(){}

    public ReportSvcDataBean(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public int getStandard() {
        return standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<Map<String, Object>> getCurList() {
        return curList;
    }

    public void setCurList(List<Map<String, Object>> curList) {
        this.curList = curList;
    }

    public List<Map<String, Object>> getLastList() {
        return lastList;
    }

    public void setLastList(List<Map<String, Object>> lastList) {
        this.lastList = lastList;
    }
}
