package com.definesys.dsgc.service.apilog.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.model.MpaasBasePojo;

@Table(value = "FND_PROPERTIES")
public class FndProperties extends MpaasBasePojo {
    @RowID(type = RowIDType.UUID)
    private String propertyId;
    private String propertyKey;
    private String propertyValue;
    private String propertyDescription;

    @Column(type = ColumnType.JAVA)
    private String allowUserDefined;
    @Column(type = ColumnType.JAVA)
    private String user;
    @Column(type = ColumnType.JAVA)
    private String serverName;
    @Column(type = ColumnType.JAVA)
    private String attribute1;
    @Column(type = ColumnType.JAVA)
    private String attribute2;
    @Column(type = ColumnType.JAVA)
    private String attribute3;
    @Column(type = ColumnType.JAVA)
    private String attribute4;
    @Column(type = ColumnType.JAVA)
    private String attribute5;

//    @SystemColumn(SystemColumnType.OBJECT_VERSION)
//    @Column(value = "object_version_number")
//    private Integer objectVersionNumber;
//    @SystemColumn(SystemColumnType.CREATE_BY)
//    @Column(value = "created_by")
//    private String createdBy;
//    @JsonSerialize(using = MpaasDateSerializer.class)
//    @JsonDeserialize(using = MpaasDateDeserializer.class)
//    @SystemColumn(SystemColumnType.CREATE_ON)
//    @Column(value = "creation_date")
//    private Date creationDate;
//    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
//    @Column(value = "last_updated_by")
//    private String lastUpdatedBy;
//    @JsonSerialize(using = MpaasDateSerializer.class)
//    @JsonDeserialize(using = MpaasDateDeserializer.class)
//    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
//    @Column(value = "last_update_date")
//    private Date lastUpdateDate;


    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public String getAllowUserDefined() {
        return allowUserDefined;
    }

    public void setAllowUserDefined(String allowUserDefined) {
        this.allowUserDefined = allowUserDefined;
    }
//
//    public Date getCreationDate() {
//        return creationDate;
//    }
//
//    public void setCreationDate(Date creationDate) {
//        this.creationDate = creationDate;
//    }
//
//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public Date getLastUpdateDate() {
//        return lastUpdateDate;
//    }
//
//    public void setLastUpdateDate(Date lastUpdateDate) {
//        this.lastUpdateDate = lastUpdateDate;
//    }
//
//    public String getLastUpdatedBy() {
//        return lastUpdatedBy;
//    }
//
//    public void setLastUpdatedBy(String lastUpdatedBy) {
//        this.lastUpdatedBy = lastUpdatedBy;
//    }
//
//    public int getVersionNumber() {
//        return objectVersionNumber;
//    }
//
//    public void setVersionNumber(int versionNumber) {
//        this.objectVersionNumber = versionNumber;
//    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

//    @Override
//    public String toString() {
//        StringBuilder arg = new StringBuilder("");
//        arg.append("CrId : ").append(this.propertyId);
//        arg.append("Server : ").append(this.propertyKey);
//        arg.append("Cacheitem : ").append(this.propertyValue);
//        arg.append("CreationDate : ").append(this.propertyDescription);
//        arg.append("CreatedBy : ").append(this.allowUserDefined);
//        arg.append("LastUpdateDate : ").append(this.creationDate);
//        arg.append("LastUpdatedBy : ").append(this.createdBy);
//        arg.append("LastRefreshDate : ").append(this.lastUpdateDate);
//        arg.append("CacheStatus : ").append(this.lastUpdatedBy);
//        arg.append("CacherefreshStatus : ").append(this.objectVersionNumber);
//        arg.append("Attribute1 : ").append(this.attribute1);
//        arg.append("Attribute2 : ").append(this.attribute2);
//        arg.append("Attribute3 : ").append(this.attribute3);
//        arg.append("Attribute4 : ").append(this.attribute4);
//        arg.append("Attribute5 : ").append(this.attribute5);
//        return new String(arg);
//
//    }
}
