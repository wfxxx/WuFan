package com.definesys.dsgc.service.apilr.bean;


public class LrConfigDTO {
    private String vid;
    private String vName;
    private String envTargets;
    private String creationDate;
    private String envcode;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getEnvTargets() {
        return envTargets;
    }

    public void setEnvTargets(String envTargets) {
        this.envTargets = envTargets;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getEnvcode() {
        return envcode;
    }

    public void setEnvcode(String envcode) {
        this.envcode = envcode;
    }
}
