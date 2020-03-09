package com.definesys.dsgc.service.role.bean;
import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author xiezhongyuan
 * @Description: 角色控制表
 * @Date 2019/6/11 9:30
 */
@Table(value = "dsgc_role_control")
@ApiModel(value = "角色控制", description = "存储角色控制")
public class DSGCRoleControl extends MpaasBasePojo {
    @ApiModelProperty(value = "唯一标识",notes = "数据库是NUMBER类型")
    @RowID(sequence = "DSGC_ROLE_CONTROL_S",type = RowIDType.AUTO)
    private String RoleId;
    @Column(value = "role_code")
    private String roleCode;
    @Column(value = "menu_bar")
    private String menuBar;
    @Column(value = "is_see")
    private String isSee;
    @Column(value = "is_edit")
    private String isEdit;
    @Column(value = "role_control_description")
    private String roleControlDescription;
    @Column(value = "attribue1")
    private String attribue1;
    @Column(value = "attribue2")
    private String attribue2;
    @Column(value = "attribue3")
    private String attribue3;
    @Column(value = "attribue4")
    private String attribue4;
    @Column(value = "attribue5")
    private String attribue5;

    @Column(type = ColumnType.JAVA)
//    @Lookup(from = "meaning", table = "fnd_lookup_values", where = "lookup_code=#roleCode")
    private String roleName;

    @Column(type = ColumnType.JAVA)
//    @Lookup(from = "meaning", table = "fnd_lookup_values", where = "lookup_code=#menuBar")
    private String menuBarName;


    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    private Integer objectVersionNumber;

    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;

    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;;

    public String getMenuBarName() {
        return menuBarName;
    }

    public void setMenuBarName(String menuBarName) {
        this.menuBarName = menuBarName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(String menuBar) {
        this.menuBar = menuBar;
    }

    public String getIsSee() {
        return isSee;
    }

    public void setIsSee(String isSee) {
        this.isSee = isSee;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
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

    public String getAttribue2() {
        return attribue2;
    }

    public void setAttribue2(String attribue2) {
        this.attribue2 = attribue2;
    }

    public String getAttribue1() {
        return attribue1;
    }

    public void setAttribue1(String attribue1) {
        this.attribue1 = attribue1;
    }

    public String getAttribue3() {
        return attribue3;
    }

    public void setAttribue3(String attribue3) {
        this.attribue3 = attribue3;
    }

    public String getAttribue4() {
        return attribue4;
    }

    public void setAttribue4(String attribue4) {
        this.attribue4 = attribue4;
    }

    public String getAttribue5() {
        return attribue5;
    }

    public void setAttribue5(String attribue5) {
        this.attribue5 = attribue5;
    }

    public String getRoleControlDescription() {
        return roleControlDescription;
    }

    public void setRoleControlDescription(String roleControlDescription) {
        this.roleControlDescription = roleControlDescription;
    }

    @Override
    public String toString() {
        return "DSGCRoleControl{" +
                "RoleId='" + RoleId + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", menuBar='" + menuBar + '\'' +
                ", isSee='" + isSee + '\'' +
                ", isEdit='" + isEdit + '\'' +
                ", attribue2='" + attribue2 + '\'' +
                ", attribue1='" + attribue1 + '\'' +
                ", attribue3='" + attribue3 + '\'' +
                ", attribue4='" + attribue4 + '\'' +
                ", attribue5='" + attribue5 + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
