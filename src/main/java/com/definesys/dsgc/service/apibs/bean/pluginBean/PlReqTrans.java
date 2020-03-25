package com.definesys.dsgc.service.apibs.bean.pluginBean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * @ClassName PlReqTrans
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-24 10:50
 * @Version 1.0
 **/
@Table(value = "plugin_req_trans")
public class PlReqTrans {
    @RowID(type= RowIDType.UUID)
    private String prtId;
    private String httpMethod;
    private String  removeBody;
    private String removeHeaders;
    private String removeQuerystring;
    private String renameBody;
    private String renameHeaders;
    private String renameQuerystring;
    private String replaceBody;
    private String replaceHeaders;
    private String repalceQuerystring;
    private String replaceUrl;
    private String addBody;
    private String addHeaders;
    private String addQuerystring;
    private String appendBody;
    private String appendHeaders;
    private String appendQuerystring;
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







    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRemoveBody() {
        return removeBody;
    }

    public void setRemoveBody(String removeBody) {
        this.removeBody = removeBody;
    }

    public String getRemoveHeaders() {
        return removeHeaders;
    }

    public void setRemoveHeaders(String removeHeaders) {
        this.removeHeaders = removeHeaders;
    }

    public String getRemoveQuerystring() {
        return removeQuerystring;
    }

    public void setRemoveQuerystring(String removeQuerystring) {
        this.removeQuerystring = removeQuerystring;
    }

    public String getRenameBody() {
        return renameBody;
    }

    public void setRenameBody(String renameBody) {
        this.renameBody = renameBody;
    }

    public String getRenameHeaders() {
        return renameHeaders;
    }

    public void setRenameHeaders(String renameHeaders) {
        this.renameHeaders = renameHeaders;
    }

    public String getRenameQuerystring() {
        return renameQuerystring;
    }

    public void setRenameQuerystring(String renameQuerystring) {
        this.renameQuerystring = renameQuerystring;
    }

    public String getReplaceBody() {
        return replaceBody;
    }

    public void setReplaceBody(String replaceBody) {
        this.replaceBody = replaceBody;
    }

    public String getReplaceHeaders() {
        return replaceHeaders;
    }

    public void setReplaceHeaders(String replaceHeaders) {
        this.replaceHeaders = replaceHeaders;
    }

    public String getRepalceQuerystring() {
        return repalceQuerystring;
    }

    public void setRepalceQuerystring(String repalceQuerystring) {
        this.repalceQuerystring = repalceQuerystring;
    }

    public String getReplaceUrl() {
        return replaceUrl;
    }

    public void setReplaceUrl(String replaceUrl) {
        this.replaceUrl = replaceUrl;
    }

    public String getAddBody() {
        return addBody;
    }

    public void setAddBody(String addBody) {
        this.addBody = addBody;
    }

    public String getAddHeaders() {
        return addHeaders;
    }

    public void setAddHeaders(String addHeaders) {
        this.addHeaders = addHeaders;
    }

    public String getAddQuerystring() {
        return addQuerystring;
    }

    public void setAddQuerystring(String addQuerystring) {
        this.addQuerystring = addQuerystring;
    }

    public String getAppendBody() {
        return appendBody;
    }

    public void setAppendBody(String appendBody) {
        this.appendBody = appendBody;
    }

    public String getAppendHeaders() {
        return appendHeaders;
    }

    public void setAppendHeaders(String appendHeaders) {
        this.appendHeaders = appendHeaders;
    }

    public String getAppendQuerystring() {
        return appendQuerystring;
    }

    public void setAppendQuerystring(String appendQuerystring) {
        this.appendQuerystring = appendQuerystring;
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
