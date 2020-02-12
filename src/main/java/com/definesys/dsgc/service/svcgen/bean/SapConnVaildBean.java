package com.definesys.dsgc.service.svcgen.bean;

public class SapConnVaildBean {

    private String connName;
    private boolean sapConnVaild;
    private String sapConnVaildMsg;

    public SapConnVaildBean(boolean sapConnVaild){
        this.sapConnVaild = sapConnVaild;
    }

    public String getConnName() {
        return connName;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }

    public boolean isSapConnVaild() {
        return sapConnVaild;
    }

    public void setSapConnVaild(boolean sapConnVaild) {
        this.sapConnVaild = sapConnVaild;
    }

    public String getSapConnVaildMsg() {
        return sapConnVaildMsg;
    }

    public void setSapConnVaildMsg(String sapConnVaildMsg) {
        this.sapConnVaildMsg = sapConnVaildMsg;
    }
}
