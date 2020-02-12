package com.definesys.dsgc.service.svcgen.bean;

import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

public class BasicInfoJsonBean {
    private String servNo;
    private String servName;
    private String servSystemCode;
    private String servSystemName;
    private String tmplCode;
    private String tmplCodeMeaning;
    private String vcStat;
    private String vcStatMeaning;
    private String lastUpdateBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    private Date lastUpdatedDate;

    private boolean readonly = true;

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

    public String getServSystemCode() {
        return servSystemCode;
    }

    public void setServSystemCode(String servSystemCode) {
        this.servSystemCode = servSystemCode;
    }

    public String getServSystemName() {
        return servSystemName;
    }

    public void setServSystemName(String servSystemName) {
        this.servSystemName = servSystemName;
    }

    public String getTmplCode() {
        return tmplCode;
    }

    public void setTmplCode(String tmplCode) {
        this.tmplCode = tmplCode;
    }

    public String getTmplCodeMeaning() {
        return tmplCodeMeaning;
    }

    public void setTmplCodeMeaning(String tmplCodeMeaning) {
        this.tmplCodeMeaning = tmplCodeMeaning;
    }

    public String getVcStat() {
        return vcStat;
    }

    public void setVcStat(String vcStat) {
        this.vcStat = vcStat;
    }

    public String getVcStatMeaning() {
        return vcStatMeaning;
    }

    public void setVcStatMeaning(String vcStatMeaning) {
        this.vcStatMeaning = vcStatMeaning;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }
}
