package com.definesys.dsgc.service.ystar.mg.conn.bean;

public class DBconnVO {
    private String connId;
    private String connName;
    private String dbType;
    private String connUN;
    private String connPD;
    private String port;
    private String dbIp;
    private String sidOrServNameLabel;
    private String sidOrServNameValue;
    private String dbName;

    public String getConnId() {
        return connId;
    }

    public void setConnId(String connId) {
        this.connId = connId;
    }

    public String getConnName() {
        return connName;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbIp() {
        return dbIp;
    }

    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }

    public String getSidOrServNameLabel() {
        return sidOrServNameLabel;
    }

    public void setSidOrServNameLabel(String sidOrServNameLabel) {
        this.sidOrServNameLabel = sidOrServNameLabel;
    }

    public String getSidOrServNameValue() {
        return sidOrServNameValue;
    }

    public void setSidOrServNameValue(String sidOrServNameValue) {
        this.sidOrServNameValue = sidOrServNameValue;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
