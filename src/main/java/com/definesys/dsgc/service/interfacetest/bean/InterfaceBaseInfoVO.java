package com.definesys.dsgc.service.interfacetest.bean;

import com.definesys.dsgc.service.svcmng.bean.ServUriParamterDTO;

import java.util.List;

public class InterfaceBaseInfoVO {
    private String uri;
    private String httpMethod;
    private String soapOper;
    private String reqBody;
    private List<ServUriParamterDTO> uriParam;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
        this.reqBody = reqBody;
    }

    public List<ServUriParamterDTO> getUriParam() {
        return uriParam;
    }

    public void setUriParam(List<ServUriParamterDTO> uriParam) {
        this.uriParam = uriParam;
    }

    public String getSoapOper() {
        return soapOper;
    }

    public void setSoapOper(String soapOper) {
        this.soapOper = soapOper;
    }
}
