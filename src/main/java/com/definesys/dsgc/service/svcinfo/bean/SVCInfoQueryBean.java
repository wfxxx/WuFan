package com.definesys.dsgc.service.svcinfo.bean;

import java.util.List;

public class SVCInfoQueryBean {
    private String con0;
    private String shareType;

    private String svcCode;
    private String svcName;
    private List<String> svcCodeList;
    private String sysCode;
    private List<String>  sysCodeList;
    private String compCode;
    private List<String>  compCodeList;
    public String getCon0() {
        return con0;
    }

    public void setCon0(String con0) {
        this.con0 = con0;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getSvcCode() {
        return svcCode;
    }

    public void setSvcCode(String svcCode) {
        this.svcCode = svcCode;
    }

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

    public List<String> getSvcCodeList() {
        return svcCodeList;
    }

    public void setSvcCodeList(List<String> svcCodeList) {
        this.svcCodeList = svcCodeList;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public List<String> getSysCodeList() {
        return sysCodeList;
    }

    public void setSysCodeList(List<String> sysCodeList) {
        this.sysCodeList = sysCodeList;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public List<String> getCompCodeList() {
        return compCodeList;
    }

    public void setCompCodeList(List<String> compCodeList) {
        this.compCodeList = compCodeList;
    }
}
