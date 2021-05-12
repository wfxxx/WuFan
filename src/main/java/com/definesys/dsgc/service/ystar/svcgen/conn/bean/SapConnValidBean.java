package com.definesys.dsgc.service.ystar.svcgen.conn.bean;

public class SapConnValidBean {

    private String connName;
    private boolean sapConnValid;
    private String sapConnValidMsg;

    public SapConnValidBean(boolean sapConnValid){
        this.sapConnValid = sapConnValid;
    }

    public String getConnName() {
        return connName;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }

    public boolean isSapConnValid() {
        return sapConnValid;
    }

    public void setSapConnValid(boolean sapConnValid) {
        this.sapConnValid = sapConnValid;
    }

    public String getSapConnValidMsg() {
        return sapConnValidMsg;
    }

    public void setSapConnValidMsg(String sapConnValidMsg) {
        this.sapConnValidMsg = sapConnValidMsg;
    }
}
