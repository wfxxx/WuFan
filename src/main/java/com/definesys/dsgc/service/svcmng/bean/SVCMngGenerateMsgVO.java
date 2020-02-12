package com.definesys.dsgc.service.svcmng.bean;

import com.definesys.dsgc.service.svcmng.bean.SVCMngIoParameterDTO;

import java.util.List;

public class SVCMngGenerateMsgVO {
    private String servNo;
    private String msg;
    private String type;
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
}
