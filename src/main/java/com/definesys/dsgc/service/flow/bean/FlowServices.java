package com.definesys.dsgc.service.flow.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@SQLQuery(value={
        @SQL(view="V_FLOW_SERVICE_INFO",sql="select f.* from (SELECT s.FLOW_ID, s.FLOW_NAME, s.FLOW_DESC, s.PROJECT_CODE, p.SYS_CODE AS APP_CODE , ( SELECT e.SYS_NAME FROM DSGC_SYSTEM_ENTITIES e WHERE e.SYS_CODE = p.SYS_CODE ) AS APP_NAME, s.DELETE_FLAG, s.CREATION_DATE, s.CREATED_BY , ( SELECT u.USER_NAME FROM DSGC_USER u WHERE u.USER_ID = s.CREATED_BY ) AS created_by_name, s.LAST_UPDATE_DATE, s.LAST_UPDATED_BY, s.OBJECT_VERSION_NUMBER FROM flow_services s, DSGC_SVCGEN_PROJ_INFO p WHERE s.PROJECT_CODE = p.PROJ_NAME and s.delete_flag = 'N' ORDER BY s.CREATION_DATE DESC) f")
})
@Table(value = "FLOW_SERVICES")
public class FlowServices {
    @Column(value = "flow_id")
    @Style(displayName = "主键")
    @RowID(type = RowIDType.UUID)
    private String flowId;

    @Column(value = "flow_name")
    @Style(displayName = "flow名称")
    private String flowName;

    @Column(value = "flow_desc")
    @Style(displayName = "flow描述")
    private String flowDesc;

    @Column(value = "project_code")
    @Style(displayName = "所属项目代码")
    private String projectCode;

    @Column(value = "app_code",type = ColumnType.CALCULATE)
    @Style(displayName = "所属应用代码")
    private String appCode;

    @Column(value = "app_name",type = ColumnType.CALCULATE)
    @Style(displayName = "所属应用名称")
    private String appName;

    @JsonIgnore
    @Column(value = "delete_flag",type = ColumnType.CALCULATE)
    @Style(displayName = "是否删除")
    private String deleteFlag;

    @JsonIgnore
    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    @Style(displayName = "创建人")
    private String createdBy;

    @Column(value = "created_by_name",type = ColumnType.CALCULATE)
    @Style(displayName = "所属应用名称")
    private String createdByName;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    @Style(displayName = "创建时间", width = "20")
    private java.util.Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    @Style(displayName = "修改人")
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    @Style(displayName = "修改时间", width = "20")
    private Date lastUpdateDate;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    @Style(displayName = "行版本号，用来处理锁")
    private Long objectVersionNumber;


    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowDesc() {
        return flowDesc;
    }

    public void setFlowDesc(String flowDesc) {
        this.flowDesc = flowDesc;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }
}
