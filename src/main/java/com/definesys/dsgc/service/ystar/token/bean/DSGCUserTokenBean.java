package com.definesys.dsgc.service.ystar.token.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Table("DSGC_USER_TOKEN")
public class DSGCUserTokenBean {

    @RowID(type = RowIDType.UUID)
    private String tokenId;
    private String userId;
    @DateTimeFormat(style = "yyyy-MM-dd HH24:mi:ss")
    private Date offLineDate;
    @SystemColumn(SystemColumnType.CREATE_ON)
    @DateTimeFormat(style = "yyyy-MM-dd HH24:mi:ss")
    @Column(value = "creation_date")
    private Date creationDate;
    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number", type = ColumnType.DB)
    private Integer objectVersionNumber;

    public DSGCUserTokenBean() {
    }

    public DSGCUserTokenBean(String userId) {
        this.userId = userId;
        this.setTokenId(UUID.randomUUID().toString());
        this.setCreationDate(new Date());
        this.setOffLineDate(new Date(new Date().getTime() + 2 * 60 * 60 * 1000));
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getOffLineDate() {
        return offLineDate;
    }

    public void setOffLineDate(Date offLineDate) {
        this.offLineDate = offLineDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public Integer getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Integer objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }
}
