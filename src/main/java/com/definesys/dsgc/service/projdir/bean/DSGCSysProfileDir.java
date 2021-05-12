package com.definesys.dsgc.service.projdir.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: ystar
 * @since: 2020/12/27 上午9:59
 * @history: upd by ystar 2020-12-27
 */
@Table("DSGC_SVCGEN_PROJ_INFO")
@ApiModel(value = "系统项目目录表", description = "此表用于项目划分系统部署")
public class DSGCSysProfileDir extends MpaasBasePojo implements Serializable {
    @RowID(sequence = "DSGC_SVCGEN_PROJ_INFO_S", type = RowIDType.AUTO)
    @ApiModelProperty(value = "系统工程id")
    private String projId;

    @ApiModelProperty(value = "系统工程目录名称", notes = "系统的工程目录名称，如：IAM,HR,OHS等")
    private String projName;

    @ApiModelProperty(value = "项目类型", notes = "项目类型：SOAP、REST、DB、RFC")
    private String projType;

    @ApiModelProperty(value = "系统工程目录描述")
    private String projDesc;

    @ApiModelProperty(value = "系统表主键")
    private String sysCode;

    @ApiModelProperty(value = "备用字段1", notes = "用于存放远程仓库地址")
    private String repoUri;

    @ApiModelProperty(value = "备用字段2", notes = "用于存放远程仓库 分支")
    private String branchName;

    @ApiModelProperty(value = "初始化状态")
    private String initStatus;

    @ApiModelProperty(value = "项目端口")
    private String projPort;

    @ApiModelProperty(value = "当前版本")
    private String textAttribute1;

    @ApiModelProperty(value = "备用字段2")
    private String textAttribute2;

    @ApiModelProperty(value = "备用字段3")
    private String textAttribute3;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
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

    public String getProjPort() {
        return projPort;
    }

    public void setProjPort(String projPort) {
        this.projPort = projPort;
    }

    public String getProjType() {
        return projType;
    }

    public void setProjType(String projType) {
        this.projType = projType;
    }

    public String getRepoUri() {
        return repoUri;
    }

    public void setRepoUri(String repoUri) {
        this.repoUri = repoUri;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getInitStatus() {
        return initStatus;
    }

    public void setInitStatus(String initStatus) {
        this.initStatus = initStatus;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjDesc() {
        return projDesc;
    }

    public void setProjDesc(String projDesc) {
        this.projDesc = projDesc;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getTextAttribute1() {
        return textAttribute1;
    }

    public void setTextAttribute1(String textAttribute1) {
        this.textAttribute1 = textAttribute1;
    }

    public String getTextAttribute2() {
        return textAttribute2;
    }

    public void setTextAttribute2(String textAttribute2) {
        this.textAttribute2 = textAttribute2;
    }

    public String getTextAttribute3() {
        return textAttribute3;
    }

    public void setTextAttribute3(String textAttribute3) {
        this.textAttribute3 = textAttribute3;
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
        return "DSGCSysProfileDir{" +
                "projId='" + projId + '\'' +
                ", projName='" + projName + '\'' +
                ", projType='" + projType + '\'' +
                ", projDesc='" + projDesc + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", repoUri='" + repoUri + '\'' +
                ", branchName='" + branchName + '\'' +
                ", initStatus='" + initStatus + '\'' +
                ", textAttribute1='" + textAttribute1 + '\'' +
                ", textAttribute2='" + textAttribute2 + '\'' +
                ", textAttribute3='" + textAttribute3 + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
