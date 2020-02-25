package com.definesys.dsgc.service.dagclient.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.model.MpaasBasePojo;

import java.util.Date;

@Table(value = "DAG_DEPLOY_LOG")
public class DAGDeployStatBean extends MpaasBasePojo {

    @RowID(type = RowIDType.UUID)
    @Column(value = "DDS_ID", type = ColumnType.DB)
    private String ddsId;

    @Column(value = "VID", type = ColumnType.DB)
    private String vid;

    @Column(value = "ENV_CODE", type = ColumnType.DB)
    private String envCode;

    @Column(value = "DEPLOY_TIME", type = ColumnType.DB)
    private Date deployTime;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number", type = ColumnType.DB)
    private Integer objectVersionNumber;

    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;

    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

    public String getDdsId() {
        return ddsId;
    }

    public void setDdsId(String ddsId) {
        this.ddsId = ddsId;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public Date getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }

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
}
