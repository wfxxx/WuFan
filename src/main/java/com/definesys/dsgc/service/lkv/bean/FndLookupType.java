package com.definesys.dsgc.service.lkv.bean;


import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

@Table(value = "fnd_lookup_types")
public class FndLookupType extends MpaasBasePojo {

    @RowID( type = RowIDType.UUID)
    public String uuid;

    public String lookupId;

    public String lookupType;

    public String lookupName;

    public String lookupDescription;

    public String moduleId;

    @Column(type = ColumnType.JAVA)
    public FndModules fndModules;

    public String allowUserDefined;

    @Column(type = ColumnType.JAVA)
    public List<FndLookupValue> values;

    public FndLookupType() {
    }

    public FndModules getFndModules() {
        return fndModules;
    }

    public void setFndModules(FndModules fndModules) {
        this.fndModules = fndModules;
    }

    public FndLookupType(String lookupType) {
        this.lookupType = lookupType;
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

    public List<FndLookupValue> getValues() {
        return values;
    }

    public void setValues(List<FndLookupValue> values) {
        this.values = values;
    }

    public String getLookupId() {
        return lookupId;
    }

    public void setLookupId(String lookupId) {
        this.lookupId = lookupId;
    }

    public String getLookupType() {
        if(lookupType == null){
            return "";
        }
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }

    public String getLookupName() {
        if(lookupName == null){
            return "";
        }
        return lookupName;
    }

    public void setLookupName(String lookupName) {
        this.lookupName = lookupName;
    }

    public String getLookupDescription() {
        if(lookupDescription == null){
            return "";
        }
        return lookupDescription;
    }

    public void setLookupDescription(String lookupDescription) {
        this.lookupDescription = lookupDescription;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getAllowUserDefined() {
        return allowUserDefined;
    }

    public void setAllowUserDefined(String allowUserDefined) {
        this.allowUserDefined = allowUserDefined;
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
        return "LookupType{" +
                "lookupId=" + lookupId +
                ", lookupType='" + lookupType + '\'' +
                ", lookupName='" + lookupName + '\'' +
                ", lookupDescription='" + lookupDescription + '\'' +
                ", moduleId=" + moduleId +
                ", allowUserDefined='" + allowUserDefined + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
