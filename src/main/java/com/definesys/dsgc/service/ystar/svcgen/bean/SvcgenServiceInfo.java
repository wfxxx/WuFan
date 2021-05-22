package com.definesys.dsgc.service.ystar.svcgen.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName: SvcgenServiceInfo
 * @Description: Mule接口快速配置
 * @Author：afan
 * @Date : 2020/1/10 10:30
 */
@SQLQuery(value = {
        @SQL(view = "v_dsgc_svcgen_service_info", sql = "select a.svc_code,a.svc_name,a.proj_id,a.dpl_status,a.svc_info,b.proj_name,b.cur_version,a.sys_code,\n" +
                "b.repo_grp, b.repo_name,b.branch_name,(select concat(c.ATTR1,'://',c.ATTR2,':',c.ATTR3) from dsgc_svcgen_conn c where c.CONN_ID = b.CONN_ID) as repo_uri,a.svc_type,a.text_attribute1,a.text_attribute2,a.text_attribute3,a.created_by,a.creation_date,(select b.sys_name from\n" +
                "   dsgc_system_entities  b where a.sys_code = b.sys_code) sys_name from dsgc_svcgen_service_info a left join dsgc_svcgen_proj_info b on a.proj_id = b.proj_id ")
})
@Component
@Table("dsgc_svcgen_service_info")
@ApiModel(value = "服务快速配置表", description = "服务配置信息")
public class SvcgenServiceInfo extends MpaasBasePojo implements Serializable {
    @ApiModelProperty(value = "服务编号")
    @RowID(sequence = "DSGC_SVCGEN_SERVICE_INFO_S", type = RowIDType.AUTO_INCREMENT)
    @Column(value = "SVG_ID")
    private String svgId;
    @ApiModelProperty(value = "服务编号")
    private String svcCode;
    @ApiModelProperty(value = "服务名称")
    private String svcName;
    @ApiModelProperty(value = "项目目录名称")
    private String projId;
    @Column(type = ColumnType.JAVA)
    private String projName;
    @ApiModelProperty(value = "所属系统")
    private String sysCode;
    @Column(type = ColumnType.JAVA)
    private String sysName;
    @ApiModelProperty(value = "接口类型")
    private String svcType;
    @ApiModelProperty(value = "接口部署状态")
    private String dplStatus;
    @Column(type = ColumnType.JAVA)
    private String curVersion;//当前部署版本
    @ApiModelProperty(value = "快速配置信息")
    private String svcInfo;
    @Column(type = ColumnType.JAVA)
    private String repoGrp;//当前部署版本
    @Column(type = ColumnType.JAVA)
    private String repoName;//当前部署版本
    @Column(type = ColumnType.JAVA)
    private String repoUri;//当前部署版本
    @Column(type = ColumnType.JAVA)
    private String branchName;//代码分支
    @ApiModelProperty(value = "备用字段1", notes = "")
    private String textAttribute1;
    @ApiModelProperty(value = "备用字段2", notes = "")
    private String textAttribute2;
    @ApiModelProperty(value = "备用字段3", notes = "")
    private String textAttribute3;
    @Column(value = "OBJECT_VERSION_NUMBER")
    private String objectVersionNumber;
    @ApiModelProperty(value = "创建者")
    @Column(value = "CREATED_BY")
    private String createdBy;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "CREATION_DATE")
    private Date creationDate;
    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "LAST_UPDATED_BY")
    private String lastUpdatedBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "LAST_UPDATE_DATE")
    private Date lastUpdatedDate;

    public SvcgenServiceInfo() {
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    public String getRepoUri() {
        return repoUri + "/" + repoGrp + "/" + repoName + ".git";
    }

    public void setRepoUri(String repoUri) {
        this.repoUri = repoUri;
    }

    public String getCurVersion() {
        return curVersion;
    }

    public void setCurVersion(String curVersion) {
        this.curVersion = curVersion;
    }

    public String getSvgId() {
        return svgId;
    }

    public void setSvgId(String svgId) {
        this.svgId = svgId;
    }

    public String getSvcCode() {
        return svcCode;
    }

    public void setSvcCode(String svcCode) {
        this.svcCode = svcCode;
    }

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
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

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSvcType() {
        return svcType;
    }

    public void setSvcType(String svcType) {
        this.svcType = svcType;
    }

    public String getDplStatus() {
        return dplStatus;
    }

    public void setDplStatus(String dplStatus) {
        this.dplStatus = dplStatus;
    }

    public String getSvcInfo() {
        return svcInfo;
    }

    public void setSvcInfo(String svcInfo) {
        this.svcInfo = svcInfo;
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

    public String getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(String objectVersionNumber) {
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

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Override
    public String toString() {
        return "SvcgenServiceInfo{" +
                "svgId='" + svgId + '\'' +
                ", svcCode='" + svcCode + '\'' +
                ", svcName='" + svcName + '\'' +
                ", projId='" + projId + '\'' +
                ", projName='" + projName + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", sysName='" + sysName + '\'' +
                ", svcType='" + svcType + '\'' +
                ", dplStatus='" + dplStatus + '\'' +
                ", curVersion='" + curVersion + '\'' +
                ", svcInfo='" + svcInfo + '\'' +
                ", textAttribute1='" + textAttribute1 + '\'' +
                ", textAttribute2='" + textAttribute2 + '\'' +
                ", textAttribute3='" + textAttribute3 + '\'' +
                ", objectVersionNumber='" + objectVersionNumber + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdatedDate=" + lastUpdatedDate +
                '}';
    }
}
