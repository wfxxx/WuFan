package com.definesys.dsgc.service.apideploylog.bean;

public class QueryDeployLogDTO {
    private String envName;
    private String logCnt;
    private String deployTime;
    private String deployor;

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getLogCnt() {
        return logCnt;
    }

    public void setLogCnt(String logCnt) {
        this.logCnt = logCnt;
    }

    public String getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(String deployTime) {
        this.deployTime = deployTime;
    }

    public String getDeployor() {
        return deployor;
    }

    public void setDeployor(String deployor) {
        this.deployor = deployor;
    }
}
