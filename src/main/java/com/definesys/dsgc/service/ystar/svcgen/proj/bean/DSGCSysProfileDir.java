package com.definesys.dsgc.service.ystar.svcgen.proj.bean;

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
@SQLQuery(value = {
        @SQL(view = "V_DSGC_SVCGEN_PROJ_INFO", sql = "select proj_id,proj_name,proj_desc,proj_type,proj_port,branch_name,init_status,cur_version,s.sys_code,(select e.SYS_NAME from dsgc_system_entities e where e.SYS_CODE = s.SYS_CODE) sys_name,\n" +
                "s.conn_id,(select c.CONN_NAME from dsgc_svcgen_conn c where s.CONN_ID = c.CONN_ID) conn_name,repo_grp,repo_name,\n" +
                "(select concat(c.ATTR2,':',c.ATTR3) from dsgc_svcgen_conn c where s.CONN_ID = c.CONN_ID) repo_uri from dsgc_svcgen_proj_info s")
})
@Table("DSGC_SVCGEN_PROJ_INFO")
@ApiModel(value = "系统项目目录表", description = "此表用于项目划分系统部署")
public class DSGCSysProfileDir extends MpaasBasePojo implements Serializable {
    @ApiModelProperty(value = "系统工程id")
    @RowID(sequence = "DSGC_SVCGEN_PROJ_INFO_S", type = RowIDType.AUTO)
    private String projId;
    @ApiModelProperty(value = "系统工程目录名称", notes = "系统的工程目录名称，如：IAM,HR,OHS等")
    private String projName;
    @ApiModelProperty(value = "项目类型", notes = "项目类型：SOAP、REST、DB、RFC")
    private String projType;
    @ApiModelProperty(value = "系统工程目录描述")
    private String projDesc;
    @ApiModelProperty(value = "项目端口")
    private String projPort;
    @ApiModelProperty(value = "连接信息Id")
    private String connId;
    @ApiModelProperty(value = "系统表主键")
    private String sysCode;
    @ApiModelProperty(value = "远程仓库分组")
    private String repoGrp;
    @ApiModelProperty(value = "远程仓库名称")
    private String repoName;
    @ApiModelProperty(value = "远程仓库 分支", notes = "用于存放远程仓库 分支")
    private String branchName;
    @ApiModelProperty(value = "初始化状态")
    private String initStatus;
    @ApiModelProperty(value = "当前版本")
    private String curVersion;

    @ApiModelProperty(value = "备用字段1", notes = "")
    private String textAttribute1;

    @ApiModelProperty(value = "备用字段2", notes = "")
    private String textAttribute2;

    @ApiModelProperty(value = "备用字段3", notes = "")
    private String textAttribute3;

    @Column(type = ColumnType.JAVA)
    private String sysName; //系统名称
    @Column(type = ColumnType.JAVA)
    private String connName; //连接用户名
    @Column(type = ColumnType.JAVA)
    private String repoUri; //远程仓库地址
    @Column(type = ColumnType.JAVA)
    private String userName; //用户名
    @Column(type = ColumnType.JAVA)
    private String password; //密码

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

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjType() {
        return projType;
    }

    public void setProjType(String projType) {
        this.projType = projType;
    }

    public String getProjDesc() {
        return projDesc;
    }

    public void setProjDesc(String projDesc) {
        this.projDesc = projDesc;
    }

    public String getProjPort() {
        return projPort;
    }

    public void setProjPort(String projPort) {
        this.projPort = projPort;
    }

    public String getConnId() {
        return connId;
    }

    public void setConnId(String connId) {
        this.connId = connId;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
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

    public String getCurVersion() {
        return curVersion;
    }

    public void setCurVersion(String curVersion) {
        this.curVersion = curVersion;
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

    public String getConnName() {
        return connName;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }

    public String getRepoUri() {
        return "http://" + repoUri + "/" + repoGrp + "/" + repoName + ".git";
    }

    public void setRepoUri(String repoUri) {
        this.repoUri = repoUri;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getRepoGrp() {
        return repoGrp;
    }

    public void setRepoGrp(String repoGrp) {
        this.repoGrp = repoGrp;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    @Override
    public String toString() {
        return "DSGCSysProfileDir{" +
                "projId='" + projId + '\'' +
                ", projName='" + projName + '\'' +
                ", projType='" + projType + '\'' +
                ", projDesc='" + projDesc + '\'' +
                ", projPort='" + projPort + '\'' +
                ", connId='" + connId + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", repoGrp='" + repoGrp + '\'' +
                ", repoName='" + repoName + '\'' +
                ", branchName='" + branchName + '\'' +
                ", initStatus='" + initStatus + '\'' +
                ", curVersion='" + curVersion + '\'' +
                ", textAttribute1='" + textAttribute1 + '\'' +
                ", textAttribute2='" + textAttribute2 + '\'' +
                ", textAttribute3='" + textAttribute3 + '\'' +
                ", sysName='" + sysName + '\'' +
                ", connName='" + connName + '\'' +
                ", repoUri='" + repoUri + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
