package com.definesys.dsgc.service.esbenv.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description: 总线环境信息 - 主配置表
 * @author: biao.luo
 * @since: 2019/7/18 下午6:46
 * @history: 1.2019/7/18 created by biao.luo
 */
@SQLQuery(value = {
        @SQL(view = "bus_base_view", sql = " select cfg.*,sver.server_name from( select inf.*,m.machine_name from DSGC_ENV_INFO_CFG inf, DSGC_ENV_MACHINE_CFG m where inf.env_code = m.env_code(+)) cfg, DSGC_ENV_SERVER_CFG sver where cfg.env_code = sver.env_code(+) "),
        @SQL(view = "bus_detail_view", sql = " select * from ( select cfg.*,sver.server_name from( select inf.*,m.machine_name from DSGC_ENV_INFO_CFG inf, DSGC_ENV_MACHINE_CFG m where inf.env_code = m.env_code(+)) cfg, DSGC_ENV_SERVER_CFG sver where cfg.env_code = sver.env_code(+)  ) c where c.DEIC_ID = #deicId "),
        @SQL(view = "bus_allInfo_view", sql = " select * from (\n" +
                "select cf.*, mc.SERVER_COUNT ,sec.NODE_COUNT from dsgc_env_info_cfg cf left join (select count(mc.demc_id) server_count, mc.env_code from dsgc_env_machine_cfg mc, dsgc_env_info_cfg c where mc.env_code = c.env_code group by mc.env_code) mc on cf.env_code = mc.env_code " +
                " LEFT JOIN (select count(sc.desc_id) node_count, sc.env_code from dsgc_env_server_cfg sc, dsgc_env_info_cfg c where sc.env_code = c.env_code group by sc.env_code) sec on cf.env_code = sec.env_code order by cf.env_seq asc) d ")})

@Table("DSGC_ENV_INFO_CFG")
public class DSGCEnvInfoCfg extends MpaasBasePojo {

    @RowID(type = RowIDType.UUID)
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

    @ApiModelProperty(value = "环境类型")
    private String envType;
    @ApiModelProperty(value = "技术类型")
    private String techType;
    @ApiModelProperty(value = "请求地址")
    private String reqLocation;
    @ApiModelProperty(value = "管理地址")
    private String adminLocation;

    @ApiModelProperty(value = "服务器个数")
    @Column(type = ColumnType.JAVA)
    private Integer serverCount;

    @ApiModelProperty(value = "节点个数")
    @Column(type = ColumnType.JAVA)
    private Integer nodeCount;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    private Integer objectVersionNumber;

    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

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

    public String getEnvDesc() {
        return envDesc;
    }

    public void setEnvDesc(String envDesc) {
        this.envDesc = envDesc;
    }

    public Integer getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Integer objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
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

    @Override
    public String toString() {
        return "DSGCEnvInfoCfg{" +
                "deicId='" + deicId + '\'' +
                ", envCode='" + envCode + '\'' +
                ", envName='" + envName + '\'' +
                ", envSeq=" + envSeq +
                ", adminIp='" + adminIp + '\'' +
                ", adminPort='" + adminPort + '\'' +
                ", adminUser='" + adminUser + '\'' +
                ", adminPd='" + adminPd + '\'' +
                ", dsgcIp='" + dsgcIp + '\'' +
                ", esbIp='" + esbIp + '\'' +
                ", esbPort='" + esbPort + '\'' +
                ", dsgcPort='" + dsgcPort + '\'' +
                ", esbSslPort='" + esbSslPort + '\'' +
                ", envDesc='" + envDesc + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
