package com.definesys.dsgc.service.svcmng.bean;

import java.util.List;
import java.util.Map;

public class ReqParamBaseDataDTO {
    private List<SVCMngIoParameterDTO> paramList;
    private Map<String,String> sampleMap;

    public List<SVCMngIoParameterDTO> getParamList() {
        return paramList;
    }

    public void setParamList(List<SVCMngIoParameterDTO> paramList) {
        this.paramList = paramList;
    }

    public Map<String, String> getSampleMap() {
        return sampleMap;
    }

    public void setSampleMap(Map<String, String> sampleMap) {
        this.sampleMap = sampleMap;
    }
}
