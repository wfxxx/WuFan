package com.definesys.dsgc.service.apicert.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

public class DagCertRefIDbean {

    @Column(value = "REF_ID", type = ColumnType.DB)
    private String refId;
    @Column(value = "ENV_CODE", type = ColumnType.DB)
    private String envCode;

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }
}
