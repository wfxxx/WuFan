package com.definesys.dsgc.service.system.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

import java.util.Date;


/**
 * @author zhenglong
 * @Description: 系统和接口对应的表
 * @Date 2019/3/12 9:47
 */
@Table(value = "DSGC_SERV_ROUTING")
@ApiModel(value = "系统接口对应信息", description = "存储系统接口对应信息")
public class DSGCServRouting extends MpaasBasePojo{
    @RowID( type = RowIDType.UUID)
    @Column(value = "routing_id")
    private String routingId;
    @Column(value = "serv_no")
    private String servNo;
    @Column (value = "route_esb_code")
    private  String routeEsbCode;
    @Column(value = "route_system_code")
    private String routeSystemCode;
    @Column (value = "route_func_name")
    private String routeFuncName;
    @Column(value = "rule_str")
    private String ruleStr;
    @Column(value = "rule_exe_path")
    private String ruleExePath ;
    @Column(value = "data_trans_code")
    private String dataTransCode ;

    @Column(value = "data_trans_path")
    private String dataTransPath ;

    @Column(value = "itf_rpl_str")
    private String itfRplStr ;
    @Column(value = "data_encode")
    private String dataEncode ;

    @Column(value = "routing_status")
    private String routingStatus ;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "retry_count")
    private Integer retryCount;
    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "retry_interval")
    private Integer retryInterval;



    @Column(value = "attribue1")
    private String  attribue1;
    @Column(value = "attribue2")
    private String  attribue2;
    @Column(value = "attribue3")
    private String  attribue3;
    @Column(value = "attribue4")
    private String  attribue4;
    @Column(value = "attribue5")
    private String  attribue5;



    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    private Integer objectVersionNumber;
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

    public String getRoutingId() {
        return routingId;
    }

    public void setRoutingId(String routingId) {
        this.routingId = routingId;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getRouteEsbCode() {
        return routeEsbCode;
    }

    public void setRouteEsbCode(String routeEsbCode) {
        this.routeEsbCode = routeEsbCode;
    }

    public String getRouteSystemCode() {
        return routeSystemCode;
    }

    public void setRouteSystemCode(String routeSystemCode) {
        this.routeSystemCode = routeSystemCode;
    }

    public String getRouteFuncName() {
        return routeFuncName;
    }

    public void setRouteFuncName(String routeFuncName) {
        this.routeFuncName = routeFuncName;
    }

    public String getRuleStr() {
        return ruleStr;
    }

    public void setRuleStr(String ruleStr) {
        this.ruleStr = ruleStr;
    }

    public String getRuleExePath() {
        return ruleExePath;
    }

    public void setRuleExePath(String ruleExePath) {
        this.ruleExePath = ruleExePath;
    }

    public String getDataTransCode() {
        return dataTransCode;
    }

    public void setDataTransCode(String dataTransCode) {
        this.dataTransCode = dataTransCode;
    }

    public String getDataTransPath() {
        return dataTransPath;
    }

    public void setDataTransPath(String dataTransPath) {
        this.dataTransPath = dataTransPath;
    }

    public String getItfRplStr() {
        return itfRplStr;
    }

    public void setItfRplStr(String itfRplStr) {
        this.itfRplStr = itfRplStr;
    }

    public String getDataEncode() {
        return dataEncode;
    }

    public void setDataEncode(String dataEncode) {
        this.dataEncode = dataEncode;
    }

    public String getRoutingStatus() {
        return routingStatus;
    }

    public void setRoutingStatus(String routingStatus) {
        this.routingStatus = routingStatus;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
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

    @Override
    public String toString() {
        return "DSGCServRouting{" +
                "routingId='" + routingId + '\'' +
                ", servNo='" + servNo + '\'' +
                ", routeEsbCode='" + routeEsbCode + '\'' +
                ", routeSystemCode='" + routeSystemCode + '\'' +
                ", routeFuncName='" + routeFuncName + '\'' +
                ", ruleStr='" + ruleStr + '\'' +
                ", ruleExePath='" + ruleExePath + '\'' +
                ", dataTransCode='" + dataTransCode + '\'' +
                ", dataTransPath='" + dataTransPath + '\'' +
                ", itfRplStr='" + itfRplStr + '\'' +
                ", dataEncode='" + dataEncode + '\'' +
                ", routingStatus='" + routingStatus + '\'' +
                ", retryCount=" + retryCount +
                ", retryInterval=" + retryInterval +
                ", attribue1='" + attribue1 + '\'' +
                ", attribue2='" + attribue2 + '\'' +
                ", attribue3='" + attribue3 + '\'' +
                ", attribue4='" + attribue4 + '\'' +
                ", attribue5='" + attribue5 + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}

