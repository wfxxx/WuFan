package com.definesys.dsgc.service.system.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(value = "DSGC_SYSTEM_ENTITIES")
public class DSGCSystemEntities extends MpaasBasePojo {
    @RowID(type = RowIDType.UUID)
    private String id;
    private String sysCode;
    private String sysName;
    private String pwd;
    private String owner;
    private String sysDesc;
    private String attribue1;
    private String attribue2;
    private String attribue3;
    private String attribue4;
    private String attribue5;

    @Column(type = ColumnType.JAVA)
    List<DSGCSystemUser> dsgcSystemUserList = new ArrayList<>();

    @Column(type = ColumnType.JAVA)
    private Boolean isEdit;

    public List<DSGCSystemUser> getDsgcSystemUserList() {
        return dsgcSystemUserList;
    }

    public void setDsgcSystemUserList(List<DSGCSystemUser> dsgcSystemUserList) {
        this.dsgcSystemUserList = dsgcSystemUserList;
    }

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
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

    public String getSysDesc() {
        return sysDesc;
    }

    public void setSysDesc(String sysDesc) {
        this.sysDesc = sysDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

    public String getAttribue2() {
        return attribue2;
    }

    public void setAttribue2(String attribue2) {
        this.attribue2 = attribue2;
    }

    public String getAttribue3() {
        return attribue3;
    }

    public void setAttribue3(String attribue3) {
        this.attribue3 = attribue3;
    }

    public String getAttribue4() {
        return attribue4;
    }

    public void setAttribue4(String attribue4) {
        this.attribue4 = attribue4;
    }

    public String getAttribue5() {
        return attribue5;
    }

    public void setAttribue5(String attribue5) {
        this.attribue5 = attribue5;
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

    public Boolean getEdit() {
        return isEdit;
    }

    public void setEdit(Boolean edit) {
        isEdit = edit;
    }

    @Override
    public String toString() {
        return "DSGCSystemEntities{" +
                "id='" + id + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", sysName='" + sysName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", owner='" + owner + '\'' +
                ", attribue1='" + attribue1 + '\'' +
                ", attribue2='" + attribue2 + '\'' +
                ", attribue3='" + attribue3 + '\'' +
                ", attribue4='" + attribue4 + '\'' +
                ", attribue5='" + attribue5 + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
