package com.definesys.dsgc.service.svcinfo.bean;

import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

@SQLQuery(value = {
        @SQL(view = "SVC_INFO_V", sql = "select s.serv_no svc_code,s.serv_name svc_name, " +
                "s.subordinate_system sys_code,(SELECT e.sys_name FROM dsgc_system_entities e WHERE e.sys_code = s.subordinate_system) sys_name," +
                "s.subordinate_company comp_code,(SELECT v.meaning FROM fnd_lookup_types t,fnd_lookup_values v WHERE t.lookup_id = v.lookup_id AND t.lookup_type = 'SUBORDINATE_COMPANY' AND v.lookup_code = s.subordinate_company)  comp_name," +
                "s.serv_status svc_status,s.basic_auth_status ip_check_status,s.normal_return,s.norm_res_time from dsgc_services s ")
})
/**
 * 服务资产查询结果视图
 */
public class SVCInfoVO {
    private String svcCode;
    private String svcName;
    private String sysCode;
    private String sysName;
    private String compCode;
    private String compName;
    private String svcStatus;
    private String ipCheckStatus;
    private String normalReturn;
    private String normResTime;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;

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

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getSvcStatus() {
        return svcStatus;
    }

    public void setSvcStatus(String svcStatus) {
        this.svcStatus = svcStatus;
    }

    public String getIpCheckStatus() {
        return ipCheckStatus;
    }

    public void setIpCheckStatus(String ipCheckStatus) {
        this.ipCheckStatus = ipCheckStatus;
    }

    public String getNormalReturn() {
        return normalReturn;
    }

    public void setNormalReturn(String normalReturn) {
        this.normalReturn = normalReturn;
    }

    public String getNormResTime() {
        return normResTime;
    }

    public void setNormResTime(String normResTime) {
        this.normResTime = normResTime;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }
}
