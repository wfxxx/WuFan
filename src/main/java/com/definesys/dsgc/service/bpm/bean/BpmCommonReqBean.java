package com.definesys.dsgc.service.bpm.bean;

public class BpmCommonReqBean {
    private String con0;//通用查询条件
    private String queryType;

    public String getCon0() {
        return con0;
    }

    public void setCon0(String con0) {
        this.con0 = con0;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
