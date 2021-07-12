package com.definesys.dsgc.service.dess.DessBusiness.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import io.swagger.models.auth.In;

/**
 * @ClassName DessBusiness
 * @Description TODO
 * @Author ystar
 * @Date 2020-7-28 18:19
 * @Version 1.0
 **/
@Table(value = "DESS_BUSINESS")
public class DessBusiness {
    @RowID(type = RowIDType.UUID)
    private String businessId;
    private String businessName;
    private String invokeUrl;
    private String webserviceType;
    private String serviceName;
    private String portType;
    private String invokeOperation;
    private String bodyPayload;
    private String headerPayload;
    private String businessDesc;
    private String businessType;
    private Integer stOffSet;
    private Integer edOffSet;
    private String ftpSrcPath;
    private String ftpTrgPath;
    private String connId;
    @Column(type = ColumnType.JAVA)
    private String isDel;

    public Integer getStOffSet() {
        return stOffSet;
    }

    public void setStOffSet(Integer stOffSet) {
        this.stOffSet = stOffSet;
    }

    public Integer getEdOffSet() {
        return edOffSet;
    }

    public void setEdOffSet(Integer edOffSet) {
        this.edOffSet = edOffSet;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getInvokeUrl() {
        return invokeUrl;
    }

    public void setInvokeUrl(String invokeUrl) {
        this.invokeUrl = invokeUrl;
    }

    public String getWebserviceType() {
        return webserviceType;
    }

    public void setWebserviceType(String webserviceType) {
        this.webserviceType = webserviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String getInvokeOperation() {
        return invokeOperation;
    }

    public void setInvokeOperation(String invokeOperation) {
        this.invokeOperation = invokeOperation;
    }

    public String getBodyPayload() {
        return bodyPayload;
    }

    public void setBodyPayload(String bodyPayload) {
        this.bodyPayload = bodyPayload;
    }

    public String getHeaderPayload() {
        return headerPayload;
    }

    public void setHeaderPayload(String headerPayload) {
        this.headerPayload = headerPayload;
    }

    public String getBusinessDesc() {
        return businessDesc;
    }

    public void setBusinessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getFtpSrcPath() {
        return ftpSrcPath;
    }

    public void setFtpSrcPath(String ftpSrcPath) {
        this.ftpSrcPath = ftpSrcPath;
    }

    public String getFtpTrgPath() {
        return ftpTrgPath;
    }

    public void setFtpTrgPath(String ftpTrgPath) {
        this.ftpTrgPath = ftpTrgPath;
    }

    public String getConnId() {
        return connId;
    }

    public void setConnId(String connId) {
        this.connId = connId;
    }
}
