package com.definesys.dsgc.service.ystar.mg.svc.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SQLQuery(value = {
        @SQL(view = "V_SVC_INFO", sql = "select SERV_NO as SVC_NO,SERV_NAME as SVC_NAME,SERV_DESC as SVC_DESC,SERV_STATUS as SERVICE_STATUS,\n" +
                "subordinate_system as SYS_CODE,(select SYS_NAME from dsgc_system_entities e where e.SYS_CODE = s.SUBORDINATE_SYSTEM) as SYS_NAME" +
                ",SHARE_TYPE from dsgc_services s"),
        @SQL(view = "V_SYS_INFO", sql = "select a.SYS_CODE,(select SYS_NAME from dsgc_system_entities e where e.SYS_CODE = a.SYS_CODE) as SYS_NAME from dsgc_system_user a\n" +
                "where a.user_id = #userId "),
        @SQL(view = "V_ALL_SYS_INFO", sql = "select a.SYS_CODE,(select SYS_NAME from dsgc_system_entities e where e.SYS_CODE = a.SYS_CODE) as SYS_NAME from dsgc_system_user a ")
})
@ApiModel(value = "系统服务对应信息", description = "系统服务对应信息")
public class SvcInfoView extends MpaasBasePojo implements Serializable {
    private String svcNo;
    private String svcName;
    private String svcDesc;
    private String svcStatus;
    private String sysCode;
    private String sysName;
    private String shareType;
    @Column(type = ColumnType.JAVA)
    private List<Map<String, String>> sysInfoList;
    @Column(type = ColumnType.JAVA)
    private List<Map<String, String>> svcInfoList;

    public String getSvcNo() {
        return svcNo;
    }

    public void setSvcNo(String svcNo) {
        this.svcNo = svcNo;
    }

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

    public String getSvcDesc() {
        return svcDesc;
    }

    public void setSvcDesc(String svcDesc) {
        this.svcDesc = svcDesc;
    }

    public String getSvcStatus() {
        return svcStatus;
    }

    public void setSvcStatus(String svcStatus) {
        this.svcStatus = svcStatus;
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

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public List<Map<String, String>> getSysInfoList() {
        return sysInfoList;
    }

    public void setSysInfoList(List<Map<String, String>> sysInfoList) {
        this.sysInfoList = sysInfoList;
    }

    public List<Map<String, String>> getSvcInfoList() {
        return svcInfoList;
    }

    public void setSvcInfoList(List<Map<String, String>> svcInfoList) {
        this.svcInfoList = svcInfoList;
    }
}
