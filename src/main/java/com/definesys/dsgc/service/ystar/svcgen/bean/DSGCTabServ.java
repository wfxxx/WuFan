package com.definesys.dsgc.service.ystar.svcgen.bean;


import com.definesys.mpaas.query.annotation.RowID;
import com.definesys.mpaas.query.annotation.RowIDType;
import com.definesys.mpaas.query.annotation.Table;
import com.definesys.mpaas.query.model.MpaasBasePojo;

import java.io.Serializable;

@Table(value = "dsgc_tab_serv")
public class DSGCTabServ extends MpaasBasePojo implements Serializable {
    @RowID(type = RowIDType.UUID)
    String id;
    String servNo;
    String tabId;

    public DSGCTabServ(){}

    public DSGCTabServ(String servNo) {
        this.servNo = servNo;
    }

    public DSGCTabServ(String tabId, String svcNo) {
        this.tabId = tabId;
        this.servNo = svcNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    @Override
    public String toString() {
        return "DSGCTabServ{" +
                "id='" + id + '\'' +
                ", servNo='" + servNo + '\'' +
                ", tabId='" + tabId + '\'' +
                '}';
    }
}

