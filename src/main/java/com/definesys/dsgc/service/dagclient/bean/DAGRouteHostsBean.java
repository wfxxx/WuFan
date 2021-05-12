package com.definesys.dsgc.service.dagclient.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.Table;

@Table(value = "DAG_ROUTES_HOSTNAME")
public class DAGRouteHostsBean {
    @Column(value = "vid", type = ColumnType.DB)
    private String vid;

    @Column(value = "HOST_NAME", type = ColumnType.DB)
    private String hostname;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
