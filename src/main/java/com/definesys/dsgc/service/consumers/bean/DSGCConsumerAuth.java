package com.definesys.dsgc.service.consumers.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

@Table(value = "DSGC_CONSUMER_AUTH")
public class DSGCConsumerAuth extends MpaasBasePojo implements Serializable {
    @RowID(type = RowIDType.UUID)
    private String dcaId;
    private String csmCode;
    private String caType;
    private String caAttr1;
    private String caAttr2;
    private String caAttr3;
    private String caAttr4;
    private String caAttr5;
    private String envCode;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @Column(value = "ca_date1")
    private Date caDate1;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @Column(value = "ca_date2")
    private Date caDate2;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @Column(value = "ca_date3")
    private Date caDate3;
    private Integer caNum1;
    private Integer caNum2;
    private Integer caNum3;
    private String isEnable;
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

    public String getDcaId() {
        return dcaId;
    }

    public void setDcaId(String dcaId) {
        this.dcaId = dcaId;
    }

    public String getCsmCode() {
        return csmCode;
    }

    public void setCsmCode(String csmCode) {
        this.csmCode = csmCode;
    }

    public String getCaType() {
        return caType;
    }

    public void setCaType(String caType) {
        this.caType = caType;
    }

    public String getCaAttr1() {
        return caAttr1;
    }

    public void setCaAttr1(String caAttr1) {
        this.caAttr1 = caAttr1;
    }

    public String getCaAttr2() {
        return caAttr2;
    }

    public void setCaAttr2(String caAttr2) {
        this.caAttr2 = caAttr2;
    }

    public String getCaAttr3() {
        return caAttr3;
    }

    public void setCaAttr3(String caAttr3) {
        this.caAttr3 = caAttr3;
    }

    public String getCaAttr4() {
        return caAttr4;
    }

    public void setCaAttr4(String caAttr4) {
        this.caAttr4 = caAttr4;
    }

    public String getCaAttr5() {
        return caAttr5;
    }

    public void setCaAttr5(String caAttr5) {
        this.caAttr5 = caAttr5;
    }

    public Date getCaDate1() {
        return caDate1;
    }

    public void setCaDate1(Date caDate1) {
        this.caDate1 = caDate1;
    }

    public Date getCaDate2() {
        return caDate2;
    }

    public void setCaDate2(Date caDate2) {
        this.caDate2 = caDate2;
    }

    public Date getCaDate3() {
        return caDate3;
    }

    public void setCaDate3(Date caDate3) {
        this.caDate3 = caDate3;
    }

    public Integer getCaNum1() {
        return caNum1;
    }

    public void setCaNum1(Integer caNum1) {
        this.caNum1 = caNum1;
    }

    public Integer getCaNum2() {
        return caNum2;
    }

    public void setCaNum2(Integer caNum2) {
        this.caNum2 = caNum2;
    }

    public Integer getCaNum3() {
        return caNum3;
    }

    public void setCaNum3(Integer caNum3) {
        this.caNum3 = caNum3;
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

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }
}
