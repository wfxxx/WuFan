package com.definesys.dsgc.service.apilr.bean;

import java.util.List;

public class AddLrConfigVO {
    private String configName;
    private List<String> enabledEnv;
    private String courType;
    private String dlId;
    private String vid;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public List<String> getEnabledEnv() {
        return enabledEnv;
    }

    public void setEnabledEnv(List<String> enabledEnv) {
        this.enabledEnv = enabledEnv;
    }

    public String getCourType() {
        return courType;
    }

    public void setCourType(String courType) {
        this.courType = courType;
    }

    public String getDlId() {
        return dlId;
    }

    public void setDlId(String dlId) {
        this.dlId = dlId;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
