package com.definesys.dsgc.service.ystar.report.bean;

import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

@SQLQuery(value = {
        @SQL(view = "QUERY_ALL_SVC_VIEW", sql = " SELECT SERV_NO as SVC_CODE,SERV_NAME as SVC_NAME,SUBORDINATE_SYSTEM as SYS_CODE,(select e.SYS_CODE from dsgc_system_entities e where e.SYS_CODE = s.SUBORDINATE_SYSTEM) as SYS_NAME from dsgc_services s ")
})
public class SvcInfoVO extends SysInfoVO {
    String svcCode;
    String svcName;

    public String getSvcCode() {
        return svcCode;
    }

    public void setSvcCode(String svcCode) {
        this.svcCode = svcCode;
    }

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

}
