package com.definesys.dsgc.service.mynty.bean;


public class MyNtyQueryListBean {


    private boolean isReadonly = false;

    private String subStat;

    private String isEnable;

    private String ruleId;

    private String ruleTitle;

    private String ruleType;

    private String ruleTypeMeaning;

    private String ruleExprDesc;

    private String appCode;
    private String appCodeMeaning;

    private String createdBy;
    private String creator;

    private Integer alertCount = 0;

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public boolean isReadonly() {
        return isReadonly;
    }

    public void setReadonly(boolean readonly) {
        isReadonly = readonly;
    }

    public String getSubStat() {
        return subStat;
    }

    public void setSubStat(String subStat) {
        this.subStat = subStat;
    }

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

    public String getRuleTypeMeaning() {
        return ruleTypeMeaning;
    }

    public void setRuleTypeMeaning(String ruleTypeMeaning) {
        this.ruleTypeMeaning = ruleTypeMeaning;
    }

    public String getRuleExprDesc() {
        return ruleExprDesc;
    }

    public void setRuleExprDesc(String ruleExprDesc) {
        this.ruleExprDesc = ruleExprDesc;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppCodeMeaning() {
        return appCodeMeaning;
    }

    public void setAppCodeMeaning(String appCodeMeaning) {
        this.appCodeMeaning = appCodeMeaning;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(int alertCount) {
        this.alertCount = alertCount;
    }
}
