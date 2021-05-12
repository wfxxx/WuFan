package com.definesys.dsgc.service.svcgen.bean;

import java.util.ArrayList;
import java.util.List;

public class SvcGenReqJsonBean {
    private String servNo;
    private String resolveDenpendencies;
    private List<String> toResolveFileList = new ArrayList<>();

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getResolveDenpendencies() {
        return resolveDenpendencies;
    }

    public void setResolveDenpendencies(String resolveDenpendencies) {
        this.resolveDenpendencies = resolveDenpendencies;
    }

    public List<String> getToResolveFileList() {
        return toResolveFileList;
    }

    public void setToResolveFileList(List<String> toResolveFileList) {
        this.toResolveFileList = toResolveFileList;
    }
}
