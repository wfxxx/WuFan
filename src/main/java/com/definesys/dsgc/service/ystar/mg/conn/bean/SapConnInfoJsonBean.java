package com.definesys.dsgc.service.ystar.mg.conn.bean;

public class SapConnInfoJsonBean {
    private String connId;
    private String sapIp;
    private String sn;
    private String sapCient;
    private String connUN;
    private String connPD;
    private String lang;
    private String connName;

    public String getConnId() {
        return connId;
    }

    public void setConnId(String connId) {
        this.connId = connId;
    }

    public String getSapIp() {
        return sapIp;
    }

    public void setSapIp(String sapIp) {
        this.sapIp = sapIp;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSapCient() {
        return sapCient;
    }

    public void setSapCient(String sapCient) {
        this.sapCient = sapCient;
    }

    public String getConnUN() {
        return connUN;
    }

    public void setConnUN(String connUN) {
        this.connUN = connUN;
    }

    public String getConnPD() {
        return connPD;
    }

    public void setConnPD(String connPD) {
        this.connPD = connPD;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getConnName() {
        return connName;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }
}
