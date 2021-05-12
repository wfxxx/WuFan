package com.definesys.dsgc.service.ystar.svcgen.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName: SvcgenServiceInfo
 * @Description: Mule接口快速配置
 * @Author：afan
 * @Date : 2020/1/10 10:30
 */
@SQLQuery(value = {
        @SQL(view = "svggen_service_v", sql = "select a.serv_no,a.serv_name,a.res_name,a" +
                ".sys_code,a.servicetype,a.text_attribute1,a.text_attribute2,a.text_attribute3,a.created_by,a.creation_date,b.sys_name from dsgc_svcgen_service_info a ," +
                "dsgc_system_entities b where a.sys_code = b.sys_code")
})
@Component
@Table("dsgc_svcgen_service_info")
@ApiModel(value = "服务快速配置表", description = "服务配置信息")
public class SvcgenServiceInfo extends MpaasBasePojo {

    @ApiModelProperty(value = "服务编号")
    @Column(value = "SERV_NO")
    private String servNo;

    @ApiModelProperty(value = "服务名称")
    @Column(value = "SERV_NAME")
    private String servName;

    @ApiModelProperty(value = "项目目录名称")
    @Column(value = "RES_NAME")
    private String resName;

    @ApiModelProperty(value = "所属系统")
    @Column(value = "SYS_CODE")
    private String sysCode;

    @ApiModelProperty(value = "接口类型")
    @Column(value = "SERVICETYPE")
    private String serviceType;

    @Column(value = "TEXT_ATTRIBUTE1")
    private String textAttribute1;

    @ApiModelProperty(value = "是否部署")
    @Column(value = "TEXT_ATTRIBUTE2")
    private String textAttribute2;

    @Column(value = "TEXT_ATTRIBUTE3")
    private String textAttribute3;
    @Column(value = "OBJECT_VERSION_NUMBER")
    private String objectVersionNumber;

    @ApiModelProperty(value = "创建者")
    @Column(value = "CREATED_BY")
    private String createdBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "CREATION_DATE")
    private Date creationDate;


    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "LAST_UPDATED_BY")
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "LAST_UPDATE_DATE")
    private Date lastUpdatedDate;


    @Column(type = ColumnType.JAVA)
    private String sysName;


    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
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

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTextAttribute1() {
        return textAttribute1;
    }

    public void setTextAttribute1(String textAttribute1) {
        this.textAttribute1 = textAttribute1;
    }

    public String getTextAttribute2() {
        return textAttribute2;
    }

    public void setTextAttribute2(String textAttribute2) {
        this.textAttribute2 = textAttribute2;
    }

    public String getTextAttribute3() {
        return textAttribute3;
    }

    public void setTextAttribute3(String textAttribute3) {
        this.textAttribute3 = textAttribute3;
    }

    public String getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(String objectVersionNumber) {
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

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Override
    public String toString() {
        return "SvcgenServiceInfo{" +
                "servNo='" + servNo + '\'' +
                ", servName='" + servName + '\'' +
                ", resName='" + resName + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", textAttribute1='" + textAttribute1 + '\'' +
                ", textAttribute2='" + textAttribute2 + '\'' +
                ", textAttribute3='" + textAttribute3 + '\'' +
                ", objectVersionNumber='" + objectVersionNumber + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdatedDate=" + lastUpdatedDate +
                ", sysName='" + sysName + '\'' +
                '}';
    }
}
