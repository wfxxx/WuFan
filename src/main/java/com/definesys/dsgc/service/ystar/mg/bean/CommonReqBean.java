package com.definesys.dsgc.service.ystar.mg.bean;

public class CommonReqBean {
    private String con0;
    private String userRole;
    private String queryType;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCon0() {
        return con0;
    }

    public void setCon0(String con0) {
        this.con0 = con0;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
