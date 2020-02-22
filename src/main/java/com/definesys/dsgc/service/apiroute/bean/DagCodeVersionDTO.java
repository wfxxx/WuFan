package com.definesys.dsgc.service.apiroute.bean;

import java.util.List;

public class DagCodeVersionDTO {
    private String vid;
    private String sourCode;
    private String sourType;
    private List<String> envList;


    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getSourCode() {
        return sourCode;
    }

    public void setSourCode(String sourCode) {
        this.sourCode = sourCode;
    }

    public String getSourType() {
        return sourType;
    }

    public void setSourType(String sourType) {
        this.sourType = sourType;
    }

    public List<String> getEnvList() {
        return envList;
    }

    public void setEnvList(List<String> envList) {
        this.envList = envList;
    }
}
