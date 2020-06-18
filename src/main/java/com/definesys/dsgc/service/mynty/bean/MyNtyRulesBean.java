package com.definesys.dsgc.service.mynty.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@SQLQuery(value={
        @SQL(view="V_GET_MN_RULES",sql="select r.*,(select count(1) from dsgc_mn_services b where b.rule_id = r.rule_id) serv_count,(select count(1) from dsgc_mn_user b where b.rule_id = r.rule_id) user_count from dsgc_mn_rules r,dsgc_mn_subcribes s where s.mn_rule = r.rule_id and s.scb_user = #userId and r.rule_type = #ruleType")
})
@Table(value = "DSGC_MN_RULES")
public class MyNtyRulesBean extends MpaasBasePojo {

    @RowID(type = RowIDType.UUID)
    @Column(value = "RULE_ID", type = ColumnType.DB)
    private String ruleId;

    @Column(value = "RULE_TITLE", type = ColumnType.DB)
    private String ruleTitle;

    @Column(value = "RULE_TYPE", type = ColumnType.DB)
    private String ruleType;

    @Column(value = "MN_LEVEL", type = ColumnType.DB)
    private Integer mnLevel;

    @Column(value = "DAY_BD", type = ColumnType.CALCULATE)
    @JsonIgnore
    private String dayBD;

    @Column(value = "DAY_ED", type = ColumnType.CALCULATE)
    @JsonIgnore
    private String dayED;

    @Column(value = "run_interval", type = ColumnType.DB)
    private Long runInterval;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @Column(value = "DISABLE_TIME", type = ColumnType.DB)
    private Date disableTime;

    @Column(value = "RULE_EXPR", type = ColumnType.DB)
    private String ruleExpr;

    @Column(value = "RULE_EXPR_DESC", type = ColumnType.DB)
    @JsonIgnore
    private String ruleExprDesc;

    @Column(value = "EXTRA_ATTR1", type = ColumnType.DB)
    private String extraAttr1;

    @Column(value = "EXTRA_ATTR2", type = ColumnType.DB)
    private String extraAttr2;

    @Column(value = "EXTRA_ATTR3", type = ColumnType.DB)
    private String extraAttr3;

    @Column(value = "APP_CODE", type = ColumnType.DB)
    private String appCode;

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

    @Column(value = "is_enable",type=ColumnType.CALCULATE)
    @JsonIgnore
    private String isEnable;

    @Column(type = ColumnType.JAVA)
    private int operType = 0;

    @Column(type = ColumnType.JAVA)
    private boolean isEnableBL = true;

    @Column(value = "serv_count", type = ColumnType.CALCULATE)
    private Integer servCount;

    @Column(value = "user_count", type = ColumnType.CALCULATE)
    private Integer userCount;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleTitle() {
        return ruleTitle;
    }

    public void setRuleTitle(String ruleTitle) {
        this.ruleTitle = ruleTitle;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getMnLevel() {
        return mnLevel;
    }

    public void setMnLevel(Integer mnLevel) {
        this.mnLevel = mnLevel;
    }

    public String getDayBD() {
        return dayBD;
    }

    public void setDayBD(String dayBD) {
        this.dayBD = dayBD;
    }

    public String getDayED() {
        return dayED;
    }

    public void setDayED(String dayED) {
        this.dayED = dayED;
    }

    public Long getRunInterval() {
        return runInterval;
    }

    public void setRunInterval(Long runInterval) {
        this.runInterval = runInterval;
    }

    public Date getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Date disableTime) {
        this.disableTime = disableTime;
    }

    public String getRuleExpr() {
        return ruleExpr;
    }

    public void setRuleExpr(String ruleExpr) {
        this.ruleExpr = ruleExpr;
    }

    public String getExtraAttr1() {
        return extraAttr1;
    }

    public void setExtraAttr1(String extraAttr1) {
        this.extraAttr1 = extraAttr1;
    }

    public String getExtraAttr2() {
        return extraAttr2;
    }

    public void setExtraAttr2(String extraAttr2) {
        this.extraAttr2 = extraAttr2;
    }

    public String getExtraAttr3() {
        return extraAttr3;
    }

    public void setExtraAttr3(String extraAttr3) {
        this.extraAttr3 = extraAttr3;
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

    public boolean getIsEnableBL() {
        return this.isEnableBL;
    }

    public void setIsEnableBL(boolean isEnableBL) {
        this.isEnableBL = isEnableBL;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getRuleExprDesc() {
        return ruleExprDesc;
    }

    public void setRuleExprDesc(String ruleExprDesc) {
        this.ruleExprDesc = ruleExprDesc;
    }

    public Integer getServCount() {
        return servCount;
    }

    public void setServCount(Integer servCount) {
        this.servCount = servCount;
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
}
