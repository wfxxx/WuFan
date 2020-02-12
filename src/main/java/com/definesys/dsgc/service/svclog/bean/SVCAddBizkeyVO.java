package com.definesys.dsgc.service.svclog.bean;

import com.definesys.dsgc.service.svcmng.bean.DSGCServInterfaceNode;

import java.util.List;

public class SVCAddBizkeyVO {
    private String servNo;
    private String servName;
    private List<DSGCServInterfaceNode> keywordList;

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public List<DSGCServInterfaceNode> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<DSGCServInterfaceNode> keywordList) {
        this.keywordList = keywordList;
    }
}
