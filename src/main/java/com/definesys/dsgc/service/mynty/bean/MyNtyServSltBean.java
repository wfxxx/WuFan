package com.definesys.dsgc.service.mynty.bean;

import java.util.List;

public class MyNtyServSltBean {
    private String ruleType;
    private String ruleId;
    private String filterServNo;
    private String filterServName;
    private String filterServSystem;
    private String[] oldDel;
    private String[] oldAdd;
    private String curOper;
    private String[] curChgs;
    private List<MyNtyServSltInfoBean>  sltServs;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getFilterServNo() {
        return filterServNo;
    }

    public void setFilterServNo(String filterServNo) {
        this.filterServNo = filterServNo;
    }

    public String getFilterServName() {
        return filterServName;
    }

    public void setFilterServName(String filterServName) {
        this.filterServName = filterServName;
    }

    public String getFilterServSystem() {
        return filterServSystem;
    }

    public void setFilterServSystem(String filterServSystem) {
        this.filterServSystem = filterServSystem;
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

    public List<MyNtyServSltInfoBean> getSltServs() {
        return sltServs;
    }

    public void setSltServs(List<MyNtyServSltInfoBean> sltServs) {
        this.sltServs = sltServs;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }
}
