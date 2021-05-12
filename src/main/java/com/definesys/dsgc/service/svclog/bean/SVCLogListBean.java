package com.definesys.dsgc.service.svclog.bean;

public class SVCLogListBean {
    private String servId;
    private String servNo;
    private String servName;
    private String subSystem;
    private String subSystemName;
    private String bodyStoreType;
    private String modifiable;

    public String getServId() {
        return servId;
    }

    public void setServId(String servId) {
        this.servId = servId;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getSubSystemName() {
        return subSystemName;
    }

    public void setSubSystemName(String subSystemName) {
        this.subSystemName = subSystemName;
    }

    public String getBodyStoreType() {
        return bodyStoreType;
    }

    public void setBodyStoreType(String bodyStoreType) {
        this.bodyStoreType = bodyStoreType;
    }

    public String getModifiable() {
        return modifiable;
    }

    public void setModifiable(String modifiable) {
        this.modifiable = modifiable;
    }
}
