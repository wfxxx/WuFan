package com.definesys.dsgc.service.svcmng.bean;

import java.util.List;

public class SaveParamDataVO {
    private String servNo;
    private String type;
    private String paramType;
    private String payloadType; //负载类型
    private int offset;//时间偏移量
    private String restSample;
    private String soapSample;
    private List<SVCMngIoParameterDTO> paramList;

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getRestSample() {
        return restSample;
    }

    public void setRestSample(String restSample) {
        this.restSample = restSample;
    }

    public String getSoapSample() {
        return soapSample;
    }

    public void setSoapSample(String soapSample) {
        this.soapSample = soapSample;
    }

    public List<SVCMngIoParameterDTO> getParamList() {
        return paramList;
    }

    public void setParamList(List<SVCMngIoParameterDTO> paramList) {
        this.paramList = paramList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
