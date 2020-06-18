package com.definesys.dsgc.service.mynty.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@SQLQuery(value = {
        @SQL(view = "Q_BY_USERID", sql = "select t.see_id,t.rule_id,t.re_name,t.re_expr,t.error_fail,t.biz_fail,t.is_enable,t.object_version_number,t.created_by,t.creation_date,t.last_updated_by,t.last_update_date,(select count(1) from dsgc_mn_services b where b.expr_ref_id = t.see_id) serv_count,(select count(1) from DSGC_MN_SUBCRIBES b where b.mn_rule  = t.see_id) user_count from DSGC_MN_SERVEXCPT_RULEEXPR t, DSGC_MN_SUBCRIBES s where t.rule_id = s.mn_rule and s.scb_user = #userId")
})
@Table(value = "DSGC_MN_SERVEXCPT_RULEEXPR")
public class ServExcptSubRulesBean extends MpaasBasePojo {

    @RowID(type = RowIDType.UUID)
    @Column(value = "see_id", type = ColumnType.DB)
    private String seeId;
    @Column(value = "rule_id", type = ColumnType.DB)
    private String ruleId;
    @Column(value = "re_name", type = ColumnType.DB)
    private String reName;
    @Column(value = "re_expr", type = ColumnType.DB)
    private String reExpr;
    @Column(value = "error_fail", type = ColumnType.DB)
    @JsonIgnore
    private String errorFail;
    @Column(type = ColumnType.JAVA)
    private boolean errorFailBL;
    @JsonIgnore
    @Column(value = "biz_fail", type = ColumnType.DB)
    private String bizFail;
    @Column(type = ColumnType.JAVA)
    private boolean bizFailBL;
    @Column(value = "is_enable", type = ColumnType.DB)
    @JsonIgnore
    private String isEnable;
    @Column(type = ColumnType.JAVA)
    private boolean isEnableBL;

    @Column(value = "APP_CODE", type = ColumnType.DB)
    private String appCode;

    @Column(value = "MN_LEVEL", type = ColumnType.DB)
    private Integer mnLevel;

    @Column(value = "serv_count", type = ColumnType.CALCULATE)
    private Integer servCount;

    @Column(value = "user_count", type = ColumnType.CALCULATE)
    private Integer userCount;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number", type = ColumnType.DB)
    private Integer objectVersionNumber;

    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

    @Column(type = ColumnType.JAVA)
    private int operType = 0;

    public String getSeeId() {
        return seeId;
    }

    public void setSeeId(String seeId) {
        this.seeId = seeId;
    }

    public String getReName() {
        return reName;
    }

    public void setReName(String reName) {
        this.reName = reName;
    }

    public String getReExpr() {
        return reExpr;
    }

    public void setReExpr(String reExpr) {
        this.reExpr = reExpr;
    }

    public String getErrorFail() {
        return errorFail;
    }

    public void setErrorFail(String errorFail) {
        this.errorFail = errorFail;
    }

    public String getBizFail() {
        return bizFail;
    }

    public void setBizFail(String bizFail) {
        this.bizFail = bizFail;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public Integer getServCount() {
        return servCount;
    }

    public void setServCount(Integer servCount) {
        this.servCount = servCount;
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

    public int getOperType() {
        return operType;
    }

    public void setOperType(int operType) {
        this.operType = operType;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public boolean getErrorFailBL() {
        return errorFailBL;
    }

    public void setErrorFailBL(boolean errorFailBL) {
        this.errorFailBL = errorFailBL;
    }

    public boolean getBizFailBL() {
        return bizFailBL;
    }

    public void setBizFailBL(boolean bizFailBL) {
        this.bizFailBL = bizFailBL;
    }

    public boolean getIsEnableBL() {
        return this.isEnableBL;
    }

    public void setIsEnableBL(boolean isEnableBL) {
        this.isEnableBL = isEnableBL;
    }

    public boolean isErrorFailBL() {
        return errorFailBL;
    }

    public boolean isBizFailBL() {
        return bizFailBL;
    }

    public boolean isEnableBL() {
        return isEnableBL;
    }

    public void setEnableBL(boolean enableBL) {
        isEnableBL = enableBL;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Integer getMnLevel() {
        return mnLevel;
    }

    public void setMnLevel(Integer mnLevel) {
        this.mnLevel = mnLevel;
    }
}
