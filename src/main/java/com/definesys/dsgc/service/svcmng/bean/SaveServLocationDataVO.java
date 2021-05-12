package com.definesys.dsgc.service.svcmng.bean;

import java.util.List;

public class SaveServLocationDataVO {
    private String servNo;
    private String type;
    private List<ServUriDTO> uriList;
    private List<ServUriParamterDTO> paramterList;

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public List<ServUriDTO> getUriList() {
        return uriList;
    }

    public void setUriList(List<ServUriDTO> uriList) {
        this.uriList = uriList;
    }

    public List<ServUriParamterDTO> getParamterList() {
        return paramterList;
    }

    public void setParamterList(List<ServUriParamterDTO> paramterList) {
        this.paramterList = paramterList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
