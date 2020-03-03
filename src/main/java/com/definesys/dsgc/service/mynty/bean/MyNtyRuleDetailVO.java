package com.definesys.dsgc.service.mynty.bean;

import java.util.Date;

public class MyNtyRuleDetailVO {

    private String ruleId;
    private String ruleType;
    private Integer mnLevel;
    private long runInterval;
    private String ruleExpr;
    private String ruleTitle;
    private Date disableTime;
    private MyNtyServSltBean servSlt;
    private MyNtyUserSltBean userSlt;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
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

    public long getRunInterval() {
        return runInterval;
    }

    public void setRunInterval(long runInterval) {
        this.runInterval = runInterval;
    }

    public String getRuleExpr() {
        return ruleExpr;
    }

    public void setRuleExpr(String ruleExpr) {
        this.ruleExpr = ruleExpr;
    }

    public String getRuleTitle() {
        return ruleTitle;
    }

    public void setRuleTitle(String ruleTitle) {
        this.ruleTitle = ruleTitle;
    }

    public Date getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Date disableTime) {
        this.disableTime = disableTime;
    }

    public MyNtyServSltBean getServSlt() {
        return servSlt;
    }

    public void setServSlt(MyNtyServSltBean servSlt) {
        this.servSlt = servSlt;
    }

    public MyNtyUserSltBean getUserSlt() {
        return userSlt;
    }

    public void setUserSlt(MyNtyUserSltBean userSlt) {
        this.userSlt = userSlt;
    }
}
