package com.definesys.dsgc.service.dagclient.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.Table;
import com.definesys.mpaas.query.model.MpaasBasePojo;

@Table(value = "dsgc_env_info_cfg")
public class DAGEnvBean extends MpaasBasePojo {


    @Column(value = "ENV_CODE", type = ColumnType.DB)
    private String envCode;

    @Column(value = "ENV_TYPE", type = ColumnType.DB)
    private String envType;

    @Column(value = "TECH_TYPE", type = ColumnType.DB)
    private String techType;

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

    public String getEnvType() {
        return envType;
    }

    public void setEnvType(String envType) {
        this.envType = envType;
    }

    public String getTechType() {
        return techType;
    }

    public void setTechType(String techType) {
        this.techType = techType;
    }
}
