package com.definesys.dsgc.service.apilog.bean;

import java.util.List;
import java.util.Map;

public class QueryApiLogInstVO {

    private String startTime;
    private String endTime;
    private String env;
    private String apiCode;
    private String consumerCode;
    private List<Map<String,String>> keywordForm;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public List<Map<String, String>> getKeywordForm() {
        return keywordForm;
    }

    public void setKeywordForm(List<Map<String, String>> keywordForm) {
        this.keywordForm = keywordForm;
    }
}
