package com.definesys.dsgc.service.svcmng.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ystar
 * @Description:
 * @Date 2021/4/21 11:58
 */
@Table(value = "DSGC_TRG_SVC_INFO")
@ApiModel(value = "系统服务对应信息", description = "系统服务对应信息")
public class DsgcTrgSvcInfo extends MpaasBasePojo implements Serializable {
    @RowID(type = RowIDType.UUID)
    private String tsId;
    @Column(value = "SVC_NO")
    private String svcNo;
    @Column(value = "SVC_TRG_TYPE")
    private String svcTrgType;
    @Column(value = "SVC_TRG_VALUE")
    private String svcTrgValue;
    @Column(value = "CONN_ID")
    private String connId;
    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "OBJECT_VERSION_NUMBER")
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

    public String getTsId() {
        return tsId;
    }

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public String getSvcNo() {
        return svcNo;
    }

    public void setSvcNo(String svcNo) {
        this.svcNo = svcNo;
    }

    public String getSvcTrgType() {
        return svcTrgType;
    }

    public void setSvcTrgType(String svcTrgType) {
        this.svcTrgType = svcTrgType;
    }

    public String getSvcTrgValue() {
        return svcTrgValue;
    }

    public void setSvcTrgValue(String svcTrgValue) {
        this.svcTrgValue = svcTrgValue;
    }

    public String getConnId() {
        return connId;
    }

    public void setConnId(String connId) {
        this.connId = connId;
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
        return "DsgcTrgSvcInfo{" +
                "tsId='" + tsId + '\'' +
                ", svcNo='" + svcNo + '\'' +
                ", svcTrgType='" + svcTrgType + '\'' +
                ", svcTrgValue='" + svcTrgValue + '\'' +
                ", connId='" + connId + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
