package com.definesys.dsgc.service.envinfo.bean;


import com.definesys.mpaas.query.annotation.RowID;
import com.definesys.mpaas.query.annotation.RowIDType;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class DSGCBusCfgVO {
    @RowID(sequence = "DSGC_ENV_INFO_CFG_S",type= RowIDType.AUTO)
    @ApiModelProperty(value = "Id")
    private String deicId;

    @ApiModelProperty(value = "环境代码")
    private String envCode;

    @ApiModelProperty(value = "环境名称")
    private String envName;

    @ApiModelProperty(value = "环境顺序")
    private Integer envSeq;

    @ApiModelProperty(value = "管理服务器IP")
    private String adminIp;

    @ApiModelProperty(value = "管理服务器端口")
    private String adminPort;

    @ApiModelProperty(value = "管理服务器用户")
    private String adminUser;

    @ApiModelProperty(value = "管理服务器密码")
    private String adminPd;

    @ApiModelProperty(value = "DSGC后端API服务器IP")
    private String dsgcIp;

    @ApiModelProperty(value = "DSGC后端API服务器端口")
    private String dsgcPort;

    @ApiModelProperty(value = "DSGC后端API服务器端口")
    private String esbIp;

    @ApiModelProperty(value = "DSGC后端API服务器端口")
    private String esbPort;

    @ApiModelProperty(value = "DSGC后端API服务器端口")
    private String esbSslPort;

    @ApiModelProperty(value = "环境描述")
    private String envDesc;

//    @ApiModelProperty(value = "服务器名称，关联服务器表服务器名称")
//    @Column(type = ColumnType.JAVA)
//    private String machineName;
//
//    @ApiModelProperty(value = "节点名称，关联节点信息表节点名称")
//    @Column(type = ColumnType.JAVA)
//    private String serverName;

    @ApiModelProperty(value = "服务器个数")
    private Integer serverCount;

    @ApiModelProperty(value = "节点个数")
    private Integer nodeCount;

    private String reqLocation;

    private String adminLocation;

    private List<DSGCEnvMachineCfg> envMachineCfgs;

    private List<DSGCEnvServerCfg> envServerCfgs;

    private List<SvcgenDeployControl> deployControls;

    public String getDeicId() {
        return deicId;
    }

    public void setDeicId(String deicId) {
        this.deicId = deicId;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public Integer getEnvSeq() {
        return envSeq;
    }

    public void setEnvSeq(Integer envSeq) {
        this.envSeq = envSeq;
    }

    public String getAdminIp() {
        return adminIp;
    }

    public void setAdminIp(String adminIp) {
        this.adminIp = adminIp;
    }

    public String getAdminPort() {
        return adminPort;
    }

    public void setAdminPort(String adminPort) {
        this.adminPort = adminPort;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getAdminPd() {
        return adminPd;
    }

    public void setAdminPd(String adminPd) {
        this.adminPd = adminPd;
    }

    public String getDsgcIp() {
        return dsgcIp;
    }

    public void setDsgcIp(String dsgcIp) {
        this.dsgcIp = dsgcIp;
    }

    public String getDsgcPort() {
        return dsgcPort;
    }

    public void setDsgcPort(String dsgcPort) {
        this.dsgcPort = dsgcPort;
    }

    public String getEsbIp() {
        return esbIp;
    }

    public void setEsbIp(String esbIp) {
        this.esbIp = esbIp;
    }

    public String getEsbPort() {
        return esbPort;
    }

    public void setEsbPort(String esbPort) {
        this.esbPort = esbPort;
    }

    public String getEsbSslPort() {
        return esbSslPort;
    }

    public void setEsbSslPort(String esbSslPort) {
        this.esbSslPort = esbSslPort;
    }

    public String getEnvDesc() {
        return envDesc;
    }

    public void setEnvDesc(String envDesc) {
        this.envDesc = envDesc;
    }

    public List<DSGCEnvMachineCfg> getEnvMachineCfgs() {
        return envMachineCfgs;
    }

    public void setEnvMachineCfgs(List<DSGCEnvMachineCfg> envMachineCfgs) {
        this.envMachineCfgs = envMachineCfgs;
    }

    public Integer getServerCount() {
        return serverCount;
    }

    public void setServerCount(Integer serverCount) {
        this.serverCount = serverCount;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    public List<DSGCEnvServerCfg> getEnvServerCfgs() {
        return envServerCfgs;
    }

    public void setEnvServerCfgs(List<DSGCEnvServerCfg> envServerCfgs) {
        this.envServerCfgs = envServerCfgs;
    }

    public List<SvcgenDeployControl> getDeployControls() {
        return deployControls;
    }

    public void setDeployControls(List<SvcgenDeployControl> deployControls) {
        this.deployControls = deployControls;
    }

    public String getReqLocation() {
        return reqLocation;
    }

    public void setReqLocation(String reqLocation) {
        this.reqLocation = reqLocation;
    }

    public String getAdminLocation() {
        return adminLocation;
    }

    public void setAdminLocation(String adminLocation) {
        this.adminLocation = adminLocation;
    }
}
