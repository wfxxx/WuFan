package com.definesys.dsgc.service.consumers.bean;

public class ConsumerTokenVO {
    private String token;
    private String envName;
    private String iatTime;
    private String expTime;
    private String duration;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getIatTime() {
        return iatTime;
    }

    public void setIatTime(String iatTime) {
        this.iatTime = iatTime;
    }

    public String getExpTime() {
        return expTime;
    }

    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
