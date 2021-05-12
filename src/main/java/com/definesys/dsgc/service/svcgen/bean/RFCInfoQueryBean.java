package com.definesys.dsgc.service.svcgen.bean;

public class RFCInfoQueryBean {
    private String funcName;
    private String funcGroup;
    private String funcDesc;

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getFuncGroup() {
        return funcGroup;
    }

    public void setFuncGroup(String funcGroup) {
        this.funcGroup = funcGroup;
    }

    public String getFuncDesc() {
        return funcDesc;
    }

    public void setFuncDesc(String funcDesc) {
        this.funcDesc = funcDesc;
    }

    @Override
    public String toString() {
        return "RFCInfoQueryBean{" +
                "funcName='" + funcName + '\'' +
                ", funcGroup='" + funcGroup + '\'' +
                ", funcDesc='" + funcDesc + '\'' +
                '}';
    }
}
