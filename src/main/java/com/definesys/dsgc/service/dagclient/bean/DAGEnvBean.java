package com.definesys.dsgc.service.dagclient.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.Table;
import com.definesys.mpaas.query.model.MpaasBasePojo;

@Table(value = "dag_env_info_cfg")
public class DAGEnvBean extends MpaasBasePojo {


    @Column(value = "ENV_CODE", type = ColumnType.DB)
    private String envCode;
    @Column(value = "ADMIN_LOCATION", type = ColumnType.DB)
    private String adminLocation;

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getAdminLocation() {
        return adminLocation;
    }

    public void setAdminLocation(String adminLocation) {
        this.adminLocation = adminLocation;
    }
}
