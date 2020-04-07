package com.definesys.dsgc.service.apiauth.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

import java.io.Serializable;

public class APIAuthConsumersBean implements Serializable {

    @Column(value = "CSM_CODE", type = ColumnType.DB)
    private String csmCode;
    @Column(value = "CSM_NAME", type = ColumnType.DB)
    private String csmName;

    public String getCsmCode() {
        return csmCode;
    }

    public void setCsmCode(String csmCode) {
        this.csmCode = csmCode;
    }

    public String getCsmName() {
        return csmName;
    }

    public void setCsmName(String csmName) {
        this.csmName = csmName;
    }
}
