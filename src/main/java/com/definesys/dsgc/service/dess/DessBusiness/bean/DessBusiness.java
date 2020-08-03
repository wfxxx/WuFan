package com.definesys.dsgc.service.dess.DessBusiness.bean;

import com.definesys.mpaas.query.annotation.*;

/**
 * @ClassName DessBusiness
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-7-28 18:19
 * @Version 1.0
 **/
@Table(value = "DESS_BUSINESS")
public class DessBusiness {
    @Deprecated
    private String jobNo;
    @RowID(sequence = "dess_business_s",type= RowIDType.AUTO)
    private String businessId;
    private String businessName;
    private String invokeUrl;
    private String webServiceType;
    private String service;
    private String portType;
    private String operation;
    private String payload;
    private String headerPayload;
    private String businessDesc;
    private String businessType;


    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
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



    public String getWebServiceType() {
        return webServiceType;
    }

    public void setWebServiceType(String webServiceType) {
        this.webServiceType = webServiceType;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
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

    public String getInvokeUrl() {
        return invokeUrl;
    }

    public void setInvokeUrl(String invokeUrl) {
        this.invokeUrl = invokeUrl;
    }
}
