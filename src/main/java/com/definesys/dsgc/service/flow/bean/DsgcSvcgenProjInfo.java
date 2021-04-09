package com.definesys.dsgc.service.flow.bean;

import com.definesys.mpaas.query.annotation.*;

@Table(value = "DSGC_SVCGEN_PROJ_INFO")
public class DsgcSvcgenProjInfo {

    @Column(value = "PROJ_ID")
    @Style(displayName = "主键")
    @RowID(type = RowIDType.UUID)
    private String projId;

    @Column(value = "proj_name")
    @Style(displayName = "项目目录")
    private String projName;

    @Column(value = "sys_code")
    @Style(displayName = "所属应用")
    private String sysCode;

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
}
