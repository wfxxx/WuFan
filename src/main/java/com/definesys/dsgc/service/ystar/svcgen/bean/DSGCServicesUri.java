package com.definesys.dsgc.service.ystar.svcgen.bean;


import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.SystemColumn;
import com.definesys.mpaas.query.annotation.SystemColumnType;
import com.definesys.mpaas.query.annotation.Table;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.sql.Date;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/7/2 下午4:49
 * @history: 1.2019/7/2 created by biao.luo
 */
@Table(value = "dsgc_services_uri")
public class DSGCServicesUri extends MpaasBasePojo implements Serializable {

    private String servNo;

    private String ibUri;

    private String soapOper;

    private String uriType;

    private String httpMethod;

    private String transportType;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "version_number")
    private Integer objectVersionNumber;

    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getIbUri() {
        return ibUri;
    }

    public void setIbUri(String ibUri) {
        this.ibUri = ibUri;
    }

    public String getSoapOper() {
        return soapOper;
    }

    public void setSoapOper(String soapOper) {
        this.soapOper = soapOper;
    }

    public String getUriType() {
        return uriType;
    }

    public void setUriType(String uriType) {
        this.uriType = uriType;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
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
        return "DSGCServicesUri{" +
                "servNo='" + servNo + '\'' +
                ", ibUri='" + ibUri + '\'' +
                ", soapOper='" + soapOper + '\'' +
                ", uriType='" + uriType + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", transportType='" + transportType + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
