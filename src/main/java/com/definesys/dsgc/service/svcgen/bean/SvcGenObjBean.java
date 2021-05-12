package com.definesys.dsgc.service.svcgen.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
@Table(value = "dsgc_svcgen_obj")
public class SvcGenObjBean extends MpaasBasePojo {
    @RowID(type = RowIDType.UUID)
    @Column(value = "OBJ_ID", type = ColumnType.DB)
    private String objId;

    @Column(value = "OBJ_TYPE", type = ColumnType.DB)
    private String objType;

    @Column(value = "OBJ_CODE", type = ColumnType.DB)
    private String objCode;

    @Column(value = "OBJ_NAME", type = ColumnType.DB)
    private String objName;

    @Column(value = "SERV_NO", type = ColumnType.DB)
    private String servNo;

    @Column(value = "IS_ENABLE", type = ColumnType.DB)
    private String isEnable;

    @Column(value = "TEXT_ATTR1", type = ColumnType.DB)
    private String textAttr1;
    @Column(value = "TEXT_ATTR2", type = ColumnType.DB)
    private String textAttr2;
    @Column(value = "TEXT_ATTR3", type = ColumnType.DB)
    private String textAttr3;
    @Column(value = "TEXT_ATTR4", type = ColumnType.DB)
    private String textAttr4;
    @Column(value = "TEXT_ATTR5", type = ColumnType.DB)
    private String textAttr5;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number", type = ColumnType.DB)
    private Integer objectVersionNumber;

    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by" , type = ColumnType.DB)
    private String createdBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date", type = ColumnType.DB)
    private Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by", type = ColumnType.DB)
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date", type = ColumnType.DB)
    private Date lastUpdateDate;

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjCode() {
        return objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getTextAttr1() {
        return textAttr1;
    }

    public void setTextAttr1(String textAttr1) {
        this.textAttr1 = textAttr1;
    }

    public String getTextAttr2() {
        return textAttr2;
    }

    public void setTextAttr2(String textAttr2) {
        this.textAttr2 = textAttr2;
    }

    public String getTextAttr3() {
        return textAttr3;
    }

    public void setTextAttr3(String textAttr3) {
        this.textAttr3 = textAttr3;
    }

    public String getTextAttr4() {
        return textAttr4;
    }

    public void setTextAttr4(String textAttr4) {
        this.textAttr4 = textAttr4;
    }

    public String getTextAttr5() {
        return textAttr5;
    }

    public void setTextAttr5(String textAttr5) {
        this.textAttr5 = textAttr5;
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
