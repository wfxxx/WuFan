package com.definesys.dsgc.service.mynty.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.RowID;
import com.definesys.mpaas.query.annotation.RowIDType;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

public class MyNtyUserSltInfoBean {
    @RowID(type = RowIDType.UUID)
    @Column(value = "user_id", type = ColumnType.DB)
    private String userId;

    @Column(value = "user_name", type = ColumnType.DB)
    private String userName;

    @Column(value = "user_role", type = ColumnType.DB)
    private String userRole;

    @Column(value = "user_mail", type = ColumnType.DB)
    private String userMail;

    @Column(value = "user_phone", type = ColumnType.DB)
    private String userPhone;

    @Column(value = "user_description", type = ColumnType.DB)
    private String userDescription;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @Column(value = "creation_date", type = ColumnType.DB)
    private Date creationDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
