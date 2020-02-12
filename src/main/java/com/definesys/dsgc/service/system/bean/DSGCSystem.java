package com.definesys.dsgc.service.system.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

import java.util.Date;


/**
 * @author zhenglong
 * @Description: 系统对应的表
 * @Date 2019/3/12 9:47
 */
@Table(value = "dsgc_system")
@ApiModel(value = "系统信息", description = "存储系统列表的信息")
public class DSGCSystem extends MpaasBasePojo {
    @RowID( type = RowIDType.AUTO)
    @Column(value = "system_id")
    private String systemId;
    @Column(value = "system_code")
    private String systemCode;
    @Column(value = "system_biz_code")
    private String systemBizCode;
    @Column(value = "esb_code")
    private String esbCode;
    @Column(value = "system_name")
    private String systemName;
    @Column(value = "system_desc")
    private String SystemName;
    @Column(value = "system_url")
    private String systemUrl;
    @Column(value = "key_str")
    private String keyStr;

    private String servNo;

    @Column(value = "attribue1")
    private String attribue1;
    @Column(value = "attribue2")
    private String attribue2;
    @Column(value = "attribue3")
    private String attribue3;
    @Column(value = "attribue4")
    private String attribue4;
    @Column(value = "attribue5")
    private String attribue5;


    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
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


    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemBizCode() {
        return systemBizCode;
    }

    public void setSystemBizCode(String systemBizCode) {
        this.systemBizCode = systemBizCode;
    }

    public String getEsbCode() {
        return esbCode;
    }

    public void setEsbCode(String esbCode) {
        this.esbCode = esbCode;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemUrl() {
        return systemUrl;
    }

    public void setSystemUrl(String systemUrl) {
        this.systemUrl = systemUrl;
    }

    public String getKeyStr() {
        return keyStr;
    }

    public void setKeyStr(String keyStr) {
        this.keyStr = keyStr;
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

    @Override
    public String toString() {
        return "DSGCSystem{" +
                "systemId='" + systemId + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", systemBizCode='" + systemBizCode + '\'' +
                ", esbCode='" + esbCode + '\'' +
                ", systemName='" + systemName + '\'' +
                ", SystemName='" + SystemName + '\'' +
                ", systemUrl='" + systemUrl + '\'' +
                ", keyStr='" + keyStr + '\'' +
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
