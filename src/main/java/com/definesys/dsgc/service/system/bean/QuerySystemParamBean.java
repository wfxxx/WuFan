package com.definesys.dsgc.service.system.bean;

public class QuerySystemParamBean {
    private String sysCode;
    private String sysName;
    private String userId;
    private String role;
    private String status;//状态：1已注册并已启用服务的系统，0未注册或未启用服务的系统，ALL全部

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
