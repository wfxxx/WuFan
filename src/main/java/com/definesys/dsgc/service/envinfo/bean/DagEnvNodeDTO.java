package com.definesys.dsgc.service.envinfo.bean;

public class DagEnvNodeDTO {

    private String einId;
    private String location;
    private Integer mngPort;
    private Integer accessPort;

    public String getEinId() {
        return einId;
    }

    public void setEinId(String einId) {
        this.einId = einId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMngPort() {
        return mngPort;
    }

    public void setMngPort(Integer mngPort) {
        this.mngPort = mngPort;
    }

    public Integer getAccessPort() {
        return accessPort;
    }

    public void setAccessPort(Integer accessPort) {
        this.accessPort = accessPort;
    }
}
