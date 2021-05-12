package com.definesys.dsgc.service.ystar.svcgen.svcdpl.bean;

import com.definesys.mpaas.query.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName: SvcgenDeployLog
 * @Description: 发布日志pojo
 * @Author：ystar
 * @Date : 2020/12/28 20:25
 */
@Component
@Table("dsgc_svcgen_deploy_log")
@ApiModel(value = "部署备份日志", description = "部署备份日志")
public class SvcGenDeployLog {

    @ApiModelProperty(value = "日志Id")
    @Column(value = "LOG_ID")
    @RowID(type = RowIDType.UUID)
    private String logId;

    @ApiModelProperty(value = "环境编码")
    @Column(value = "ENV_CODE")
    private String envCode;

    @ApiModelProperty(value = "服务编号")
    @Column(value = "PROJ_ID")
    private String projId;

    @ApiModelProperty(value = "版本ID")
    @Column(value = "V_ID")
    private String vId;

    @ApiModelProperty(value = "操作人")
    @Column(value = "USER_CODE")
    private String userCode;

    @ApiModelProperty(value = "状态")
    @Column(value = "DPL_STATUS")
    private String dplStatus;

    @ApiModelProperty(value = "更新说明")
    @Column(value = "DPL_MSG")
    private String dplMsg;

    @ApiModelProperty(value = "部署时间")
    @Column(value = "DPL_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dplDate;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
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

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
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

    public Date getDplDate() {
        return dplDate;
    }

    public void setDplDate(Date dplDate) {
        this.dplDate = dplDate;
    }
}

