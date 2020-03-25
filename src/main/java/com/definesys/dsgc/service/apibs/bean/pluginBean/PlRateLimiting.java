package com.definesys.dsgc.service.apibs.bean.pluginBean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * @ClassName PlRateLimiting
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-24 10:39
 * @Version 1.0
 **/
@Table(value = "plugin_rate_limiting")
public class PlRateLimiting {

    @RowID(type= RowIDType.UUID)
    private String prlId;


    private Integer pSecond ;
    private Integer  pMinute;
    private Integer pHour;
    private Integer pDay;
    private Integer  pMonth;
    private Integer  pYear;
    private String limitBy;
    private String policy;
    private String faultTolerant;
    private String redisHost;
    private Integer redisPort;
    private String redisPassword;
    private Integer redisTimeout;
    private Integer redisDatabase;
    private String hideClientHeaders;
    private String dpuId;

    public String getDpuId() {
        return dpuId;
    }

    public void setDpuId(String dpuId) {
        this.dpuId = dpuId;
    }
    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    private Date creationDate;
    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;
    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    private Integer objectVersionNumber;

    public String getPrlId() {
        return prlId;
    }

    public void setPrlId(String prlId) {
        this.prlId = prlId;
    }







    public Integer getpSecond() {
        return pSecond;
    }

    public void setpSecond(Integer pSecond) {
        this.pSecond = pSecond;
    }

    public Integer getpMinute() {
        return pMinute;
    }

    public void setpMinute(Integer pMinute) {
        this.pMinute = pMinute;
    }

    public Integer getpHour() {
        return pHour;
    }

    public void setpHour(Integer pHour) {
        this.pHour = pHour;
    }

    public Integer getpDay() {
        return pDay;
    }

    public void setpDay(Integer pDay) {
        this.pDay = pDay;
    }

    public Integer getpMonth() {
        return pMonth;
    }

    public void setpMonth(Integer pMonth) {
        this.pMonth = pMonth;
    }

    public Integer getpYear() {
        return pYear;
    }

    public void setpYear(Integer pYear) {
        this.pYear = pYear;
    }

    public String getLimitBy() {
        return limitBy;
    }

    public void setLimitBy(String limitBy) {
        this.limitBy = limitBy;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getFaultTolerant() {
        return faultTolerant;
    }

    public void setFaultTolerant(String faultTolerant) {
        this.faultTolerant = faultTolerant;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public Integer getRedisTimeout() {
        return redisTimeout;
    }

    public void setRedisTimeout(Integer redisTimeout) {
        this.redisTimeout = redisTimeout;
    }

    public Integer getRedisDatabase() {
        return redisDatabase;
    }

    public void setRedisDatabase(Integer redisDatabase) {
        this.redisDatabase = redisDatabase;
    }

    public String getHideClientHeaders() {
        return hideClientHeaders;
    }

    public void setHideClientHeaders(String hideClientHeaders) {
        this.hideClientHeaders = hideClientHeaders;
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

    public Integer getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Integer objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }
}
