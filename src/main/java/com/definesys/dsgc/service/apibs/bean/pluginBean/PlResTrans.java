package com.definesys.dsgc.service.apibs.bean.pluginBean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * @ClassName PlResTrans
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-24 10:54
 * @Version 1.0
 **/
@Table(value = "plugin_res_trans")
public class PlResTrans {
    @RowID(type= RowIDType.UUID)
    private String prtId;

    private String removeJson;
    private String removeHeaders;
    private String renameHeaders;
    private String replaceJson;
    private String  replaceJsonType;
    private String  replaceHeaders;
    private String addJson;
    private String addJsonTypes;
    private String addHeaders;
    private String appendJson;
    private String appendJsonType;
    private String appendHeaders;
    private String dpuId;

    public String getDpuId() {
        return dpuId;
    }

    public void setDpuId(String dpuId) {
        this.dpuId = dpuId;
    }
    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    private Date creationDate;
    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;
    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    private Integer objectVersionNumber;

    public String getPrtId() {
        return prtId;
    }

    public void setPrtId(String prtId) {
        this.prtId = prtId;
    }






    public String getRemoveJson() {
        return removeJson;
    }

    public void setRemoveJson(String removeJson) {
        this.removeJson = removeJson;
    }

    public String getRemoveHeaders() {
        return removeHeaders;
    }

    public void setRemoveHeaders(String removeHeaders) {
        this.removeHeaders = removeHeaders;
    }

    public String getRenameHeaders() {
        return renameHeaders;
    }

    public void setRenameHeaders(String renameHeaders) {
        this.renameHeaders = renameHeaders;
    }

    public String getReplaceJson() {
        return replaceJson;
    }

    public void setReplaceJson(String replaceJson) {
        this.replaceJson = replaceJson;
    }

    public String getReplaceJsonType() {
        return replaceJsonType;
    }

    public void setReplaceJsonType(String replaceJsonType) {
        this.replaceJsonType = replaceJsonType;
    }

    public String getReplaceHeaders() {
        return replaceHeaders;
    }

    public void setReplaceHeaders(String replaceHeaders) {
        this.replaceHeaders = replaceHeaders;
    }

    public String getAddJson() {
        return addJson;
    }

    public void setAddJson(String addJson) {
        this.addJson = addJson;
    }

    public String getAddJsonTypes() {
        return addJsonTypes;
    }

    public void setAddJsonTypes(String addJsonTypes) {
        this.addJsonTypes = addJsonTypes;
    }

    public String getAddHeaders() {
        return addHeaders;
    }

    public void setAddHeaders(String addHeaders) {
        this.addHeaders = addHeaders;
    }

    public String getAppendJson() {
        return appendJson;
    }

    public void setAppendJson(String appendJson) {
        this.appendJson = appendJson;
    }

    public String getAppendJsonType() {
        return appendJsonType;
    }

    public void setAppendJsonType(String appendJsonType) {
        this.appendJsonType = appendJsonType;
    }

    public String getAppendHeaders() {
        return appendHeaders;
    }

    public void setAppendHeaders(String appendHeaders) {
        this.appendHeaders = appendHeaders;
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

    public Integer getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Integer objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }
}
