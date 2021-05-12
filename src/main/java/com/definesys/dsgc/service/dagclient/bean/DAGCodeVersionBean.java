package com.definesys.dsgc.service.dagclient.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.Table;

@Table(value = "DAG_CODE_VERSION")
public class DAGCodeVersionBean {

    @Column(value = "VID", type = ColumnType.DB)
    public String vid;

    @Column(value = "SOUR_CODE", type = ColumnType.DB)
    public String sourCode;

    @Column(value = "SOUR_TYPE", type = ColumnType.DB)
    public String sourType;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getSourCode() {
        return sourCode;
    }

    public void setSourCode(String sourCode) {
        this.sourCode = sourCode;
    }

    public String getSourType() {
        return sourType;
    }

    public void setSourType(String sourType) {
        this.sourType = sourType;
    }
}
