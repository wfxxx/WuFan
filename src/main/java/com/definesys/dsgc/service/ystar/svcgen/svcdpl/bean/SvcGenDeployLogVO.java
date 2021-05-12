package com.definesys.dsgc.service.ystar.svcgen.svcdpl.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@SQLQuery(
        value = {
                @SQL(view = "svcgen_deploy_log", sql = "select p.PROJ_ID,p.PROJ_NAME,p.INIT_STATUS,g.ENV_CODE,g.V_ID, g.DPL_DATE,(select u.USER_NAME from dsgc_user u where u.USER_ID = g.USER_CODE) DPL_USER,p.TEXT_ATTRIBUTE1 CUR_VERSION,g.DPL_STATUS,g.DPL_MSG \n" +
                        "from dsgc_svcgen_proj_info p,dsgc_svcgen_deploy_log g \n" +
                        "where p.PROJ_ID = g.PROJ_ID ")
        }
)
public class SvcGenDeployLogVO {
    @Column("PROJ_ID")
    private String projId;
    @Column("PROJ_NAME")
    private String projName;
    @Column("INIT_STATUS")
    private String initStatus;
    @Column("ENV_CODE")
    private String envCode;
    @Column("V_ID")
    private String vId;
    @Column("DPL_DATE")
    private Date dplDate;
    @Column("CUR_VERSION")
    private String curVersion;//当前版本
    @Column("DPL_USER")
    private String dplUser;
    @Column("DPM_STATUS")
    private String dplStatus;
    @Column("DPL_MSG")
    private String dplMsg;

    public SvcGenDeployLogVO() {
    }

    public SvcGenDeployLogVO(String projId, String projName, String initStatus) {
        this.projId = projId;
        this.projName = projName;
        this.initStatus = initStatus;
    }

    public String getCurVersion() {
        return curVersion;
    }

    public void setCurVersion(String curVersion) {
        this.curVersion = curVersion;
    }

    private List<SvcGenDeployLog> deployLogs;//部署版本日志

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getInitStatus() {
        return initStatus;
    }

    public void setInitStatus(String initStatus) {
        this.initStatus = initStatus;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public Date getDplDate() {
        return dplDate;
    }

    public void setDplDate(Date dplDate) {
        this.dplDate = dplDate;
    }

    public String getDplUser() {
        return dplUser;
    }

    public void setDplUser(String dplUser) {
        this.dplUser = dplUser;
    }

    public String getDplStatus() {
        return dplStatus;
    }

    public void setDplStatus(String dplStatus) {
        this.dplStatus = dplStatus;
    }

    public String getDplMsg() {
        return dplMsg;
    }

    public void setDplMsg(String dplMsg) {
        this.dplMsg = dplMsg;
    }

    public List<SvcGenDeployLog> getDeployLogs() {
        return deployLogs;
    }

    public void setDeployLogs(List<SvcGenDeployLog> deployLogs) {
        this.deployLogs = deployLogs;
    }
}
