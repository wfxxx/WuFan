package com.definesys.dsgc.service.svclog.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@Table(value = "DSGC_LOG_AUDIT")
public class DSGCLogAudit {

    @RowID(type = RowIDType.UUID)
    private String adtId;
    private String trackId;
    private String stepCode;   //审计节点
    private String runTimes; //审计批次
    private String curExePath; //审计位置
    private String exeTime;  //审计时间
    private String server;  //审计服务器
    private String stepStatus;//审计状态
    private String stepDesc;  //审计信息
    private String errLob; //错误信息
    private Long sequencesId;

    @Column(type = ColumnType.JAVA)
    private Integer objectVersionNumber;
    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;
    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

    public Integer getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Integer objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getStepCode() {
        return stepCode;
    }

    public void setStepCode(String stepCode) {
        this.stepCode = stepCode;
    }

    public String getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(String runTimes) {
        this.runTimes = runTimes;
    }

    public String getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(String stepStatus) {
        this.stepStatus = stepStatus;
    }

    public String getExeTime() {
        return exeTime;
    }

    public void setExeTime(String exeTime) {
        this.exeTime = exeTime;
    }

    public String getCurExePath() {
        return curExePath;
    }

    public void setCurExePath(String curExePath) {
        this.curExePath = curExePath;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }

    public String getErrLob() {
        return errLob;
    }

    public void setErrLob(String errLob) {
        this.errLob = errLob;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getAdtId() {
        return adtId;
    }

    public void setAdtId(String adtId) {
        this.adtId = adtId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public Long getSequencesId() {
        return sequencesId;
    }

    public void setSequencesId(Long sequencesId) {
        this.sequencesId = sequencesId;
    }


    @Override
    public String toString() {
        StringBuilder arg=new StringBuilder("");
        arg.append("adtId : ").append(this.adtId);
        arg.append("trackId : ").append(this.trackId);
        arg.append("stepCode : ").append(this.stepCode);
        arg.append("runTimes : ").append(this.runTimes);
        arg.append("curExePath : ").append(this.curExePath);
        arg.append("exeTime : ").append(this.exeTime);
        arg.append("server : ").append(this.server);
        arg.append("stepStatus : ").append(this.stepStatus);
        arg.append("stepDesc : ").append(this.stepDesc);
        arg.append("errLob : ").append(this.errLob);
        arg.append("sequencesId : ").append(this.sequencesId);
        arg.append("creationDate : ").append(this.creationDate);
        arg.append("createdBy : ").append(this.createdBy);
        arg.append("lastUpdateDate : ").append(this.lastUpdateDate);
        arg.append("lastUpdatedBy : ").append(this.lastUpdatedBy);
        return new String(arg);
    }
}
