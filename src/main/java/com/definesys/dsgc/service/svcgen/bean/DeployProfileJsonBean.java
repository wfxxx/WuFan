package com.definesys.dsgc.service.svcgen.bean;

import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

public class DeployProfileJsonBean {

    private String dpId;
    private String dpName;
    private String envCode;
    private String envName;
    private String deveId;
    private String dlpCust;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    private Date lastDplDate;
    private String lastDplUser;
    private String isEnable;
    private String isEnableMeaning;
    private String lastUpdatedBy;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    private Date lastUpdateDate;
    private String textAttribute1;
    private String textAttribute2;
    private String textAttribute3;
    private boolean readonly = false;

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getDpName() {
        return dpName;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
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

    public String getDeveId() {
        return deveId;
    }

    public void setDeveId(String deveId) {
        this.deveId = deveId;
    }

    public String getDlpCust() {
        return dlpCust;
    }

    public void setDlpCust(String dlpCust) {
        this.dlpCust = dlpCust;
    }

    public Date getLastDplDate() {
        return lastDplDate;
    }

    public void setLastDplDate(Date lastDplDate) {
        this.lastDplDate = lastDplDate;
    }

    public String getLastDplUser() {
        return lastDplUser;
    }

    public void setLastDplUser(String lastDplUser) {
        this.lastDplUser = lastDplUser;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
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

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getIsEnableMeaning() {
        return isEnableMeaning;
    }

    public void setIsEnableMeaning(String isEnableMeaning) {
        this.isEnableMeaning = isEnableMeaning;
    }
}
