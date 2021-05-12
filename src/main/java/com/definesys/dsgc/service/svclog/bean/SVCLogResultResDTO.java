package com.definesys.dsgc.service.svclog.bean;

import java.util.Date;

public class SVCLogResultResDTO {
    private String servNo;
    private String servName;
    private String subSystem;
    private String reqSystem;
    private String reqSystemCode;
    private String validType;
    private String validColumn;
    private String validValue;
    private String warningValue;
    private String msgValue;
    private String createdBy;
    private Date creationDate;

    public String getMsgValue() {
        return msgValue;
    }

    public void setMsgValue(String msgValue) {
        this.msgValue = msgValue;
    }

    public String getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(String warningValue) {
        this.warningValue = warningValue;
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

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getReqSystem() {
        return reqSystem;
    }

    public void setReqSystem(String reqSystem) {
        this.reqSystem = reqSystem;
    }

    public String getValidType() {
        return validType;
    }

    public void setValidType(String validType) {
        this.validType = validType;
    }

    public String getValidColumn() {
        return validColumn;
    }

    public void setValidColumn(String validColumn) {
        this.validColumn = validColumn;
    }

    public String getValidValue() {
        return validValue;
    }

    public void setValidValue(String validValue) {
        this.validValue = validValue;
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

    public String getReqSystemCode() {
        return reqSystemCode;
    }

    public void setReqSystemCode(String reqSystemCode) {
        this.reqSystemCode = reqSystemCode;
    }
}
