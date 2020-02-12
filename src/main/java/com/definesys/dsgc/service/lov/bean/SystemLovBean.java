package com.definesys.dsgc.service.lov.bean;

public class SystemLovBean {

    public static final String SQL_SYSTEM_LOV_ALL = "select s.sys_code,s.sys_name from dsgc_system_entities s";

    public static final String SQL_SYSTEM_LOV_AUTH = "select s.sys_code,s.sys_name from dsgc_system_entities s,dsgc_system_user u where s.sys_code = u.sys_code and u.user_id = #uid";

    private String sysCode;
    private String sysName;

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
}
