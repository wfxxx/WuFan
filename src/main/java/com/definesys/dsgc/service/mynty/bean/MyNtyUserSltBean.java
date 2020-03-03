package com.definesys.dsgc.service.mynty.bean;

import java.util.List;

public class MyNtyUserSltBean {

    private String ruleType;
    private String ruleId;
    private String filterUserName;
    private String filterUserDesc;
    private String[] oldDel;
    private String[] oldAdd;
    private String curOper;
    private String[] curChgs;
    private List<MyNtyUserSltInfoBean> sltUsers;

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getFilterUserName() {
        return filterUserName;
    }

    public void setFilterUserName(String filterUserName) {
        this.filterUserName = filterUserName;
    }


    public String getFilterUserDesc() {
        return filterUserDesc;
    }

    public void setFilterUserDesc(String filterUserDesc) {
        this.filterUserDesc = filterUserDesc;
    }

    public String[] getOldDel() {
        return oldDel;
    }

    public void setOldDel(String[] oldDel) {
        this.oldDel = oldDel;
    }

    public String[] getOldAdd() {
        return oldAdd;
    }

    public void setOldAdd(String[] oldAdd) {
        this.oldAdd = oldAdd;
    }

    public String getCurOper() {
        return curOper;
    }

    public void setCurOper(String curOper) {
        this.curOper = curOper;
    }

    public String[] getCurChgs() {
        return curChgs;
    }

    public void setCurChgs(String[] curChgs) {
        this.curChgs = curChgs;
    }

    public List<MyNtyUserSltInfoBean> getSltUsers() {
        return sltUsers;
    }

    public void setSltUsers(List<MyNtyUserSltInfoBean> sltUsers) {
        this.sltUsers = sltUsers;
    }
}
