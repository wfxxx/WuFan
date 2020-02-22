package com.definesys.dsgc.service.apilr.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

@Table(value = "DAG_LR")
public class DagLrbean extends MpaasBasePojo implements Serializable {
    @RowID(type = RowIDType.UUID)
    private String dlId;

    private String lrName;
    private String lrDesc;
    private String appCode;
    @Column(type = ColumnType.JAVA)
    private String appName;
    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    private Date creationDate;
    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;
    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    private Integer objectVersionNumber;

    public String getDlId() {
        return dlId;
    }

    public void setDlId(String dlId) {
        this.dlId = dlId;
    }

    public String getLrName() {
        return lrName;
    }

    public void setLrName(String lrName) {
        this.lrName = lrName;
    }

    public String getLrDesc() {
        return lrDesc;
    }

    public void setLrDesc(String lrDesc) {
        this.lrDesc = lrDesc;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
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

    public Integer getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Integer objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
