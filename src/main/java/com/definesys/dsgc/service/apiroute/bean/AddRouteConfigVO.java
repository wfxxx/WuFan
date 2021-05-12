package com.definesys.dsgc.service.apiroute.bean;

import java.util.List;

public class AddRouteConfigVO {
    private String configName;
    private List<String> enabledEnv;
    private String courType;
    private String routeCode;

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

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }
}
