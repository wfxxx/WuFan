package com.definesys.dsgc.service.market.bean;

import java.util.List;

public class SrevDetailInfoDTO {
    public List<ServLocation> locationList;
    public List<ServUriParamterDTO> paramterList;
    public List<PayloadParamDTO> reqPayloadParamList;
    public List<PayloadParamDTO> resPayloadParamList;

    public CallSampleDTO restCallSample;
    public CallSampleDTO soapCallSample;

    public List<ServLocation> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<ServLocation> locationList) {
        this.locationList = locationList;
    }

    public List<ServUriParamterDTO> getParamterList() {
        return paramterList;
    }

    public void setParamterList(List<ServUriParamterDTO> paramterList) {
        this.paramterList = paramterList;
    }

    public List<PayloadParamDTO> getReqPayloadParamList() {
        return reqPayloadParamList;
    }

    public void setReqPayloadParamList(List<PayloadParamDTO> reqPayloadParamList) {
        this.reqPayloadParamList = reqPayloadParamList;
    }

    public List<PayloadParamDTO> getResPayloadParamList() {
        return resPayloadParamList;
    }

    public void setResPayloadParamList(List<PayloadParamDTO> resPayloadParamList) {
        this.resPayloadParamList = resPayloadParamList;
    }

    public CallSampleDTO getRestCallSample() {
        return restCallSample;
    }

    public void setRestCallSample(CallSampleDTO restCallSample) {
        this.restCallSample = restCallSample;
    }

    public CallSampleDTO getSoapCallSample() {
        return soapCallSample;
    }

    public void setSoapCallSample(CallSampleDTO soapCallSample) {
        this.soapCallSample = soapCallSample;
    }
}
