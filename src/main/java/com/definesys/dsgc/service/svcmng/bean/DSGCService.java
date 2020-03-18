package com.definesys.dsgc.service.svcmng.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.io.Serializable;
import java.util.Date;

/**
 * @author zhenglong
 * @Description:
 * @Date 2019/3/13 11:58
 */
@Table(value = "dsgc_services")
@ApiModel(value = "系统服务对应信息", description = "系统服务对应信息")
public class DSGCService extends MpaasBasePojo implements Serializable {
    //    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="demo_emp_seq")
//    @SequenceGenerator(name="demo_emp_seq", sequenceName="DEMO_SEQ")
    @RowID(type = RowIDType.UUID)
    private String servId;
    @Column(value = "serv_no")
    private String servNo;
    @Column(value = "serv_name")
    private String servName;
    @Column(value = "serv_desc")
    private String servDesc;
    @Column(value = "serv_desc_en")
    private String servDescEn;
    @Column(value = "data_queue_code")
    private String dataQueueCode;
    @Column(value = "data_decode")
    private String dataDecode;
    @Column(value = "sync_exe_path")
    private String syncExePath;
    @Column(value = "asyn_exe_path")
    private String asynExePath;

    @Column(value = "cdc_exe_path")
    private String cdcExePath;
    @Column(value = "cdc_queue_code")
    private String cdcQueueCode;
    @Column(value = "cdc_to_system")
    private String cdcToSystem;
    @Column(value = "ctu_with_failed")
    private String ctuWithFailed;

    @Column(value = "serv_url")
    private String servUrl;
    @Column(value = "serv_status")
    private String servStatus;
    @Column(value = "data_type_code")
    private String dataTypeCode;
    @Column(value = "comp_resolve")
    private String compResolve;
    @Column(value = "body_store_type")
    private String bodyStoreType;
    @Column(value = "log_compress")
    private String logCompRess;
    @Column(value = "serv_template")
    private String servTemplate;
    @Column(value = "nty_policy")
    private String ntyPolicy;
    @Column(value = "biz_level")
    private String bizLevel;
    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "serv_version")
    private Integer servVersion;

    @Column(value = "attribue1")
    private String attribue1;
    @Column(value = "attribue2")
    private String attribue2;
    @Column(value = "attribue3")
    private String attribue3;
    @Column(value = "attribue4")
    private String attribue4;
    @Column(value = "attribue5")
    private String attribue5;


    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "version_number")
    private Integer versionNumber;
    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;
    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

    @Column(value = "tech_type")
    private String techType;
    @Column(value = "trans_type")
    private String transType;
    @Column(value = "category_level1")
    private String categoryLevel1;
    @Column(value = "category_level2")
    private String categoryLevel2;
    @Column(value = "category_level3")
    private String categoryLevel3;
    @Column(value = "category_level4")
    private String categoryLevel4;

    @Column(value = "biz_desc")
    private String bizDesc;
    @Column(value = "tech_desc")
    private String techDesc;
    @Column(value = "log_detail")
    private String logDetail;
    @Column(value = "log_purge")
    private String logPurge;
    @Column(value = "file_name")
    private String fileName;
    @Column(value = "file_path")
    private String filePath;

    @Column(value = "spy_oper")
    @ApiModelProperty(value = "服务方法")
    private String spyOper;

    @Column(value = "subordinate_system")
    @ApiModelProperty(value = "所属系统")
    private String subordinateSystem;

    @Column(value = "deploy_flag")
    private String deployFlag;

    private String networkAccess;

    private String shareType;

    private Integer infoFull;

    private String isProd;

    private  String instanceId;

    private String deployedNode;

    private String bizResolve;

    @Column(type = ColumnType.JAVA)
    private String ibUri;

    @Column(type = ColumnType.JAVA)
    private String httpMethod;

    public String getDeployedNode() {
        return deployedNode;
    }

    public void setDeployedNode(String deployedNode) {
        this.deployedNode = deployedNode;
    }

    public String getServId() {
        return servId;
    }

    public void setServId(String servId) {
        this.servId = servId;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getServDesc() {
        return servDesc;
    }

    public void setServDesc(String servDesc) {
        this.servDesc = servDesc;
    }

    public String getServDescEn() {
        return servDescEn;
    }

    public void setServDescEn(String servDescEn) {
        this.servDescEn = servDescEn;
    }

    public String getDataQueueCode() {
        return dataQueueCode;
    }

    public void setDataQueueCode(String dataQueueCode) {
        this.dataQueueCode = dataQueueCode;
    }

    public String getNtyPolicy() {
        return ntyPolicy;
    }

    public void setNtyPolicy(String ntyPolicy) {
        this.ntyPolicy = ntyPolicy;
    }

    public String getDataDecode() {
        return dataDecode;
    }

    public void setDataDecode(String dataDecode) {
        this.dataDecode = dataDecode;
    }

    public String getSyncExePath() {
        return syncExePath;
    }

    public void setSyncExePath(String syncExePath) {
        this.syncExePath = syncExePath;
    }

    public String getAsynExePath() {
        return asynExePath;
    }

    public void setAsynExePath(String asynExePath) {
        this.asynExePath = asynExePath;
    }

    public String getCdcExePath() {
        return cdcExePath;
    }

    public void setCdcExePath(String cdcExePath) {
        this.cdcExePath = cdcExePath;
    }

    public String getCdcQueueCode() {
        return cdcQueueCode;
    }

    public void setCdcQueueCode(String cdcQueueCode) {
        this.cdcQueueCode = cdcQueueCode;
    }

    public String getCdcToSystem() {
        return cdcToSystem;
    }

    public void setCdcToSystem(String cdcToSystem) {
        this.cdcToSystem = cdcToSystem;
    }

    public String getCtuWithFailed() {
        return ctuWithFailed;
    }

    public void setCtuWithFailed(String ctuWithFailed) {
        this.ctuWithFailed = ctuWithFailed;
    }

    public String getServUrl() {
        return servUrl;
    }

    public void setServUrl(String servUrl) {
        this.servUrl = servUrl;
    }

    public String getServStatus() {
        return servStatus;
    }

    public void setServStatus(String servStatus) {
        this.servStatus = servStatus;
    }

    public String getDataTypeCode() {
        return dataTypeCode;
    }

    public void setDataTypeCode(String dataTypeCode) {
        this.dataTypeCode = dataTypeCode;
    }

    public String getCompResolve() {
        return compResolve;
    }

    public void setCompResolve(String compResolve) {
        this.compResolve = compResolve;
    }

    public String getBodyStoreType() {
        return bodyStoreType;
    }

    public void setBodyStoreType(String bodyStoreType) {
        this.bodyStoreType = bodyStoreType;
    }

    public String getLogCompRess() {
        return logCompRess;
    }

    public void setLogCompRess(String logCompRess) {
        this.logCompRess = logCompRess;
    }

    public String getServTempLate() {
        return servTemplate;
    }

    public void setServTempLate(String servTempLate) {
        this.servTemplate = servTempLate;
    }

    public String getNty_policy() {
        return ntyPolicy;
    }

    public void setNty_policy(String nty_policy) {
        this.ntyPolicy = nty_policy;
    }

    public String getBizLevel() {
        return bizLevel;
    }

    public void setBizLevel(String bizLevel) {
        this.bizLevel = bizLevel;
    }

    public Integer getServVersion() {
        return servVersion;
    }

    public void setServVersion(Integer servVersion) {
        this.servVersion = servVersion;
    }

    public String getAttribue1() {
        return attribue1;
    }

    public void setAttribue1(String attribue1) {
        this.attribue1 = attribue1;
    }

    public String getAttribue2() {
        return attribue2;
    }

    public void setAttribue2(String attribue2) {
        this.attribue2 = attribue2;
    }

    public String getAttribue3() {
        return attribue3;
    }

    public void setAttribue3(String attribue3) {
        this.attribue3 = attribue3;
    }

    public String getAttribue4() {
        return attribue4;
    }

    public void setAttribue4(String attribue4) {
        this.attribue4 = attribue4;
    }

    public String getAttribue5() {
        return attribue5;
    }

    public void setAttribue5(String attribue5) {
        this.attribue5 = attribue5;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
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

    public String getTechType() {
        return techType;
    }

    public void setTechType(String techType) {
        this.techType = techType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getCategoryLevel1() {
        return categoryLevel1;
    }

    public void setCategoryLevel1(String categoryLevel1) {
        this.categoryLevel1 = categoryLevel1;
    }

    public String getCategoryLevel2() {
        return categoryLevel2;
    }

    public void setCategoryLevel2(String categoryLevel2) {
        this.categoryLevel2 = categoryLevel2;
    }

    public String getCategoryLevel3() {
        return categoryLevel3;
    }

    public void setCategoryLevel3(String categoryLevel3) {
        this.categoryLevel3 = categoryLevel3;
    }

    public String getCategoryLevel4() {
        return categoryLevel4;
    }

    public void setCategoryLevel4(String categoryLevel4) {
        this.categoryLevel4 = categoryLevel4;
    }

    public String getBizDesc() {
        return bizDesc;
    }

    public void setBizDesc(String bizDesc) {
        this.bizDesc = bizDesc;
    }

    public String getTechDesc() {
        return techDesc;
    }

    public void setTechDesc(String techDesc) {
        this.techDesc = techDesc;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    public String getLogPurge() {
        return logPurge;
    }

    public void setLogPurge(String logPurge) {
        this.logPurge = logPurge;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getServTemplate() {
        return servTemplate;
    }

    public void setServTemplate(String servTemplate) {
        this.servTemplate = servTemplate;
    }

    public String getDeployFlag() {
        return deployFlag;
    }

    public void setDeployFlag(String deployFlag) {
        this.deployFlag = deployFlag;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSpyOper() {
        return spyOper;
    }

    public void setSpyOper(String spyOper) {
        this.spyOper = spyOper;
    }

    public String getSubordinateSystem() {
        return subordinateSystem;
    }

    public void setSubordinateSystem(String subordinateSystem) {
        this.subordinateSystem = subordinateSystem;
    }

    public String getNetworkAccess() {
        return networkAccess;
    }

    public void setNetworkAccess(String networkAccess) {
        this.networkAccess = networkAccess;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public Integer getInfoFull() {
        return infoFull;
    }

    public void setInfoFull(Integer infoFull) {
        this.infoFull = infoFull;
    }

    public String getIsProd() {
        return isProd;
    }

    public void setIsProd(String isProd) {
        this.isProd = isProd;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getBizResolve() {
        return bizResolve;
    }

    public void setBizResolve(String bizResolve) {
        this.bizResolve = bizResolve;
    }

    public String getIbUri() {
        return ibUri;
    }

    public void setIbUri(String ibUri) {
        this.ibUri = ibUri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public String toString() {
        return "DSGCService{" +
                "servId='" + servId + '\'' +
                ", servNo='" + servNo + '\'' +
                ", servName='" + servName + '\'' +
                ", servDesc='" + servDesc + '\'' +
                ", servDescEn='" + servDescEn + '\'' +
                ", dataQueueCode='" + dataQueueCode + '\'' +
                ", dataDecode='" + dataDecode + '\'' +
                ", syncExePath='" + syncExePath + '\'' +
                ", asynExePath='" + asynExePath + '\'' +
                ", cdcExePath='" + cdcExePath + '\'' +
                ", cdcQueueCode='" + cdcQueueCode + '\'' +
                ", cdcToSystem='" + cdcToSystem + '\'' +
                ", ctuWithFailed='" + ctuWithFailed + '\'' +
                ", servUrl='" + servUrl + '\'' +
                ", servStatus='" + servStatus + '\'' +
                ", dataTypeCode='" + dataTypeCode + '\'' +
                ", compResolve='" + compResolve + '\'' +
                ", bodyStoreType='" + bodyStoreType + '\'' +
                ", logCompRess='" + logCompRess + '\'' +
                ", servTemplate='" + servTemplate + '\'' +
                ", ntyPolicy='" + ntyPolicy + '\'' +
                ", bizLevel='" + bizLevel + '\'' +
                ", servVersion=" + servVersion +
                ", attribue1='" + attribue1 + '\'' +
                ", attribue2='" + attribue2 + '\'' +
                ", attribue3='" + attribue3 + '\'' +
                ", attribue4='" + attribue4 + '\'' +
                ", attribue5='" + attribue5 + '\'' +
                ", versionNumber=" + versionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                ", techType='" + techType + '\'' +
                ", transType='" + transType + '\'' +
                ", categoryLevel1='" + categoryLevel1 + '\'' +
                ", categoryLevel2='" + categoryLevel2 + '\'' +
                ", categoryLevel3='" + categoryLevel3 + '\'' +
                ", categoryLevel4='" + categoryLevel4 + '\'' +
                ", bizDesc='" + bizDesc + '\'' +
                ", techDesc='" + techDesc + '\'' +
                ", logDetail='" + logDetail + '\'' +
                ", logPurge='" + logPurge + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", spyOper='" + spyOper + '\'' +
                ", subordinate_system='" + subordinateSystem + '\'' +
                ", deployFlag='" + deployFlag + '\'' +
                '}';
    }
}
