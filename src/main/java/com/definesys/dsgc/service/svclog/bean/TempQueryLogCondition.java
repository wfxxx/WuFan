package com.definesys.dsgc.service.svclog.bean;

import java.util.List;

public class TempQueryLogCondition {
    private DSGCLogInstance logInstance;
    private List<Object> keywordForm;

    //http请求url，此url配置在值列表，多个url对应不同的环境
    private String httpReqUrl;
    private String env;
    private String userName;

    public DSGCLogInstance getLogInstance() {
        return logInstance;
    }

    public void setLogInstance(DSGCLogInstance logInstance) {
        this.logInstance = logInstance;
    }

    public List<Object> getKeywordForm() {
        return keywordForm;
    }

    public void setKeywordForm(List<Object> keywordForm) {
        this.keywordForm = keywordForm;
    }

    public String getHttpReqUrl() {
        return httpReqUrl;
    }

    public void setHttpReqUrl(String httpReqUrl) {
        this.httpReqUrl = httpReqUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return "TempQueryLogCondition{" +
                "logInstance=" + logInstance +
                ", keywordForm=" + keywordForm +
                '}';
    }
}
