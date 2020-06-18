package com.definesys.dsgc.service.users.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@Table(value = "dsgc_user_nty_cfg")
public class DSGCUserNtyCfgBean extends MpaasBasePojo {

    @RowID(type = RowIDType.UUID)
    private String uncId;
    private String userId;
    private String mnLevel;
    private String ntySour;
    private String isDdOpen;
    private String isMailOpen;


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
    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    private Integer objectVersionNumber;

    public String getUncId() {
        return uncId;
    }

    public void setUncId(String uncId) {
        this.uncId = uncId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMnLevel() {
        return mnLevel;
    }

    public void setMnLevel(String mnLevel) {
        this.mnLevel = mnLevel;
    }

    public String getNtySour() {
        return ntySour;
    }

    public void setNtySour(String ntySour) {
        this.ntySour = ntySour;
    }

    public String getIsDdOpen() {
        return isDdOpen;
    }

    public void setIsDdOpen(String isDdOpen) {
        this.isDdOpen = isDdOpen;
    }

    public String getIsMailOpen() {
        return isMailOpen;
    }

    public void setIsMailOpen(String isMailOpen) {
        this.isMailOpen = isMailOpen;
    }

    @Override
    public String toString() {
        return "DSGCUserNtyCfgBean{" +
                "uncId='" + uncId + '\'' +
                ", userId='" + userId + '\'' +
                ", mnLevel='" + mnLevel + '\'' +
                ", ntySour='" + ntySour + '\'' +
                ", isDdOpen='" + isDdOpen + '\'' +
                ", isMailOpen='" + isMailOpen + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                ", objectVersionNumber=" + objectVersionNumber +
                '}';
    }
}
