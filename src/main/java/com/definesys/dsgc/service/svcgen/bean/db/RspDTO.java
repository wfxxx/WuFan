package com.definesys.dsgc.service.svcgen.bean.db;

public class RspDTO {
    private String rspName;
    private String masterTbl;
    private String masterCol;
    private String childTbl;
    private String childCol;
    private String rspType;


    public String getRspName() {
        return rspName;
    }

    public void setRspName(String rspName) {
        this.rspName = rspName;
    }

    public String getMasterTbl() {
        return masterTbl;
    }

    public void setMasterTbl(String masterTbl) {
        this.masterTbl = masterTbl;
    }

    public String getMasterCol() {
        return masterCol;
    }

    public void setMasterCol(String masterCol) {
        this.masterCol = masterCol;
    }

    public String getChildTbl() {
        return childTbl;
    }

    public void setChildTbl(String childTbl) {
        this.childTbl = childTbl;
    }

    public String getChildCol() {
        return childCol;
    }

    public void setChildCol(String childCol) {
        this.childCol = childCol;
    }

    public String getRspType() {
        return rspType;
    }

    public void setRspType(String rspType) {
        this.rspType = rspType;
    }
}
