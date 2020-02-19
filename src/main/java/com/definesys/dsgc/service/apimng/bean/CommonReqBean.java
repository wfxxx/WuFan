package com.definesys.dsgc.service.apimng.bean;

import java.util.List;

public class CommonReqBean {
    private String con0;//通用查询条件
    private String isComplete;
    private String queryType;
    private List<String> selectSystemList;

    public String getCon0() {
        return con0;
    }

    public void setCon0(String con0) {
        this.con0 = con0;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
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
}
