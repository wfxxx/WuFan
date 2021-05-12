package com.definesys.dsgc.service.ystar.report.bean;

import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

@SQLQuery(value = {
        @SQL(view = "QUERY_ALL_SYS_VIEW", sql = " select SYS_CODE,SYS_NAME from dsgc_system_entities ")
})
public class SysInfoVO {
    String sysCode;
    String sysName;

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
