package com.definesys.dsgc.service.mynty.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: jason.zhong
 * @since: 2019/9/17 13:23
 * @history 2019/9/17 created by jason.zhong
 */
@Table("DSGC_MN_NOTICES")
public class DSGCMnNotices extends MpaasBasePojo {
    @RowID(sequence = "DSGC_MN_NOTICES_S",type = RowIDType.AUTO)
    private Integer mnId;
    private String ntyUser;
    private String mnTitle;
    private String mnCnt;
    private String mnType;
    private String refType;
    private String refValue;
    private String readStat;
    private String mnLevel;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
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
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

    public DSGCMnNotices() {
    }



    public DSGCMnNotices(Integer mnId,String ntyUser,String mnTitle,String mnCnt,String mnType,
                         String refType,String refValue,String readStat,Integer objectVersionNumber,
                         String createdBy,Date creationDate,String lastUpdatedBy,Date lastUpdateDate) {
        this.mnId = mnId;
        this.ntyUser = ntyUser;
        this.mnTitle = mnTitle;
        this.mnCnt = mnCnt;
        this.mnType = mnType;
        this.refType = refType;
        this.refValue = refValue;
        this.readStat = readStat;
        this.objectVersionNumber = objectVersionNumber;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Integer getMnId() {
        return mnId;
    }

    public void setMnId(Integer mnId) {
        this.mnId = mnId;
    }

    public String getNtyUser() {
        return ntyUser;
    }

    public void setNtyUser(String ntyUser) {
        this.ntyUser = ntyUser;
    }

    public String getMnTitle() {
        return mnTitle;
    }

    public void setMnTitle(String mnTitle) {
        this.mnTitle = mnTitle;
    }

    public String getMnCnt() {
        return mnCnt;
    }

    public void setMnCnt(String mnCnt) {
        this.mnCnt = mnCnt;
    }

    public String getMnType() {
        return mnType;
    }

    public void setMnType(String mnType) {
        this.mnType = mnType;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getRefValue() {
        return refValue;
    }

    public void setRefValue(String refValue) {
        this.refValue = refValue;
    }

    public String getReadStat() {
        return readStat;
    }

    public void setReadStat(String readStat) {
        this.readStat = readStat;
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

    public String getMnLevel() {
        return mnLevel;
    }

    public void setMnLevel(String mnLevel) {
        this.mnLevel = mnLevel;
    }
}
