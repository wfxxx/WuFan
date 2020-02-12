package com.definesys.dsgc.service.consumers.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
@Table(value = "DSGC_CONSUMER_ENTITIES")
public class DSGCConsumerEntities  extends MpaasBasePojo implements Serializable {
    @RowID(type = RowIDType.UUID)
    private String dceId;
    private String csmCode;
    private String csmName;
    private String csmDesc;
    private String isEnable;

    @Column(type = ColumnType.JAVA)
    private String owner;

    @Column(type = ColumnType.JAVA)
    private String attribue1;

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

    public String getDceId() {
        return dceId;
    }

    public void setDceId(String dceId) {
        this.dceId = dceId;
    }

    public String getCsmCode() {
        return csmCode;
    }

    public void setCsmCode(String csmCode) {
        this.csmCode = csmCode;
    }

    public String getCsmName() {
        return csmName;
    }

    public void setCsmName(String csmName) {
        this.csmName = csmName;
    }

    public String getCsmDesc() {
        return csmDesc;
    }

    public void setCsmDesc(String csmDesc) {
        this.csmDesc = csmDesc;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAttribue1() {
        return attribue1;
    }

    public void setAttribue1(String attribue1) {
        this.attribue1 = attribue1;
    }
}
