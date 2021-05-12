package com.definesys.dsgc.service.apiauth.bean;

import java.util.List;

public class CommonReqBean {
    private String con0;
    private String apiCode;
    private String sysCode;
    private String queryType;
    private List<String> selectSystemList;
    private String vid;

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public String getCon0() {
        return con0;
    }

    public void setCon0(String con0) {
        this.con0 = con0;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public List<String> getSelectSystemList() {
        return selectSystemList;
    }

    public void setSelectSystemList(List<String> selectSystemList) {
        this.selectSystemList = selectSystemList;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
