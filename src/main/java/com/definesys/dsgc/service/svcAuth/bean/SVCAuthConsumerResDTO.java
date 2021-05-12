package com.definesys.dsgc.service.svcAuth.bean;

public class SVCAuthConsumerResDTO {
    private String saId;
    private String csmCode;
    private String csmName;
    private String lastUpdateDate;
    private String lastUpdatedBy;

    public String getSaId() {
        return saId;
    }

    public void setSaId(String saId) {
        this.saId = saId;
    }

    public String getCsmCode() {
        return csmCode;
    }

    public void setCsmCode(String csmCode) {
        this.csmCode = csmCode;
    }

    public String getCsmName() {
        return csmName;
    }

    public void setCsmName(String csmName) {
        this.csmName = csmName;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
