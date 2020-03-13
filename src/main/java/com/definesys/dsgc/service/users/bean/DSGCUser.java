package com.definesys.dsgc.service.users.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@SQLQuery(value = {
        @SQL(view = "query_all_user", sql = " select us.user_id, us.user_name, us.user_dept, us.user_mail, us.user_phone, us.is_admin, us.user_description, us.created_by, us.creation_date, us.last_updated_by, us.last_update_date, us.user_role, us.is_locked, us.work_number, lookup.meaning role_name from dsgc_user us,( select vl.lookup_code, vl.meaning from fnd_lookup_values vl where vl.lookup_id in ( select tp.lookup_id from fnd_lookup_types tp where tp.lookup_type = 'plateFormRole') ) lookup where us.user_role = lookup.lookup_code(+) "),
        @SQL(view = "query_all_sys_user", sql = "select us.user_id, us.user_name, us.user_role, us.is_locked, us.work_number from dsgc_user us where us.user_role in('Administrators','SuperAdministrators','SuperAdministrators') and us.user_id not in ( select sysu.user_id from dsgc_system_user sysu)")
})
@Table(value = "dsgc_user")
@ApiModel(value = "用户信息", description = "存储用户信息")
public class DSGCUser  extends MpaasBasePojo {
    @RowID(type = RowIDType.UUID)
    private String userId;
    @ApiModelProperty(value = "用户名",notes = "用于登陆")
    private String userName;
    private String userPassword;
    private String userDept;
    private String userMail;
    private String userPhone;
    private String isAdmin;
    private String userDescription;
    private String userRole;
    private String isLocked;
    private String workNumber;

    @Column(type = ColumnType.JAVA)
    private String wechat;
    @Column(type = ColumnType.JAVA)
    private List<String> appCode;

    @ApiModelProperty(value = "角色名称",notes = "当前用户对应值列表中的名称")
    @Column(type = ColumnType.JAVA)
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
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

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserDept() {
        return userDept;
    }

    public void setUserDept(String userDept) {
        this.userDept = userDept;
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

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
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

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public List<String> getAppCode() {
        return appCode;
    }

    public void setAppCode(List<String> appCode) {
        this.appCode = appCode;
    }

    @Override
    public String toString() {
        return "DSGCUser{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userDept='" + userDept + '\'' +
                ", userMail='" + userMail + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", userDescription='" + userDescription + '\'' +
                ", userRole='" + userRole + '\'' +
                ", isLocked='" + isLocked + '\'' +
                ", workNumber='" + workNumber + '\'' +
                ", wechat='" + wechat + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }

}
