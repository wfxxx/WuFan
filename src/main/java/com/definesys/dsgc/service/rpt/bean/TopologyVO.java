package com.definesys.dsgc.service.rpt.bean;

import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

/**
 *  拓扑图视图
 */


@SQLQuery(value = {
        @SQL(view = "DSGC_TOPOLOGY_V",
                    sql = "select sys.serv_no,ser.serv_name,ser.subordinate_system,sys.sys_code ,ser.serv_desc\n" +
                            "from dsgc_system_access sys,dsgc_services ser \n" +
                            "where ser.serv_no (+) = sys.serv_no"
        )
})
public class TopologyVO {

    private String servNo;

    private  String servName;

    private String subordinateSystem;

    private String sysCode;

    private String servDesc;

    private String isInterface; //控制查看接口


    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getSubordinateSystem() {
        return subordinateSystem;
    }

    public void setSubordinateSystem(String subordinateSystem) {
        this.subordinateSystem = subordinateSystem;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getServDesc() {
        return servDesc;
    }

    public void setServDesc(String servDesc) {
        this.servDesc = servDesc;
    }

    public String getIsInterface() {
        return isInterface;
    }

    public void setIsInterface(String isInterface) {
        this.isInterface = isInterface;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }
}
