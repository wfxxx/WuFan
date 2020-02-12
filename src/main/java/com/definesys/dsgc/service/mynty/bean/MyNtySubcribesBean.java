package com.definesys.dsgc.service.mynty.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@Table(value = "DSGC_MN_SUBCRIBES")
public class MyNtySubcribesBean {

    @RowID(type = RowIDType.UUID)
    @Column(value = "scb_id", type = ColumnType.DB)
    private String scbId;
    @Column(value = "scb_user", type = ColumnType.DB)
    private String scbUser;
    @Column(value = "MN_RULE", type = ColumnType.DB)
    private String mnRule;
    @Column(value = "IS_ENABLE", type = ColumnType.DB)
    private String isEnable;


    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number", type = ColumnType.DB)
    private Integer objectVersionNumber;

    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;


    public String getScbId() {
        return scbId;
    }

    public void setScbId(String scbId) {
        this.scbId = scbId;
    }

    public String getScbUser() {
        return scbUser;
    }

    public void setScbUser(String scbUser) {
        this.scbUser = scbUser;
    }

    public String getMnRule() {
        return mnRule;
    }

    public void setMnRule(String mnRule) {
        this.mnRule = mnRule;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
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
