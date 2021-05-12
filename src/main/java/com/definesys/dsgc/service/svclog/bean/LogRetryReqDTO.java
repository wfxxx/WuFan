package com.definesys.dsgc.service.svclog.bean;

import java.util.List;

public class LogRetryReqDTO {

    private String envCode;
    private List<LogRetryDTO> retryList;

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public List<LogRetryDTO> getRetryList() {
        return retryList;
    }

    public void setRetryList(List<LogRetryDTO> retryList) {
        this.retryList = retryList;
    }
}
