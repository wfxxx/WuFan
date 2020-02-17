package com.definesys.dsgc.service.svcmng.bean;

import com.definesys.dsgc.service.svcmng.bean.SVCMngIoParameterDTO;

import java.util.List;

public class SVCMngGenerateMsgVO {
    private String servNo;
    private String msg;
    private String wsdlFunction;
    private String type;
    private String reqOrRes;
    private List<SVCMngIoParameterDTO> parameterList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SVCMngIoParameterDTO> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<SVCMngIoParameterDTO> parameterList) {
        this.parameterList = parameterList;
    }

    public String getWsdlFunction() {
        return wsdlFunction;
    }

    public void setWsdlFunction(String wsdlFunction) {
        this.wsdlFunction = wsdlFunction;
    }

    public String getReqOrRes() {
        return reqOrRes;
    }

    public void setReqOrRes(String reqOrRes) {
        this.reqOrRes = reqOrRes;
    }
}
