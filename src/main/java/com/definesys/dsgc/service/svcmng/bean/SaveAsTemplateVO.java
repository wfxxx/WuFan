package com.definesys.dsgc.service.svcmng.bean;

import java.util.List;

public class SaveAsTemplateVO {
    private String tmplName;
    private List<ServUriParamterDTO> paramterList;


    public String getTmplName() {
        return tmplName;
    }

    public void setTmplName(String tmplName) {
        this.tmplName = tmplName;
    }

    public List<ServUriParamterDTO> getParamterList() {
        return paramterList;
    }

    public void setParamterList(List<ServUriParamterDTO> paramterList) {
        this.paramterList = paramterList;
    }
}
