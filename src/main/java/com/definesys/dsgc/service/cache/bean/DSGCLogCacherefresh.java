package com.definesys.dsgc.service.cache.bean;

import com.definesys.mpaas.query.annotation.RowID;
import com.definesys.mpaas.query.annotation.RowIDType;
import com.definesys.mpaas.query.annotation.Table;

@Table(value = "dsgc_log_cacherefesh")
public class DSGCLogCacherefresh {
    @RowID(type = RowIDType.UUID)
    private String crId;
    private String server;
    private String cacheitem;
    private String creationDate;
    private String createdBy;
    private String lastUpdateDate;
    private String lastUpdatedBy;
    private String lastRefreshDate;
    private String cacheStatus;
    private String cacherefreshStatus;

    public String getCrId() {
        return crId;
    }

    public void setCrId(String crId) {
        this.crId = crId;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getCacheitem() {
        return cacheitem;
    }

    public void setCacheitem(String cacheitem) {
        this.cacheitem = cacheitem;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getLastRefreshDate() {
        return lastRefreshDate;
    }

    public void setLastRefreshDate(String lastRefreshDate) {
        this.lastRefreshDate = lastRefreshDate;
    }

    public String getCacheStatus() {
        return cacheStatus;
    }

    public void setCacheStatus(String cacheStatus) {
        this.cacheStatus = cacheStatus;
    }

    public String getCacherefreshStatus() {
        return cacherefreshStatus;
    }

    public void setCacherefreshStatus(String cacherefreshStatus) {
        this.cacherefreshStatus = cacherefreshStatus;
    }

    @Override
    public String toString() {
        StringBuilder arg=new StringBuilder("");
        arg.append("CrId : ").append(this.crId);
        arg.append("Server : ").append(this.server);
        arg.append("Cacheitem : ").append(this.cacheitem);
        arg.append("CreationDate : ").append(this.creationDate);
        arg.append("CreatedBy : ").append(this.createdBy);
        arg.append("LastUpdateDate : ").append(this.lastUpdateDate);
        arg.append("LastUpdatedBy : ").append(this.lastUpdatedBy);
        arg.append("LastRefreshDate : ").append(this.lastRefreshDate);
        arg.append("CacheStatus : ").append(this.cacheStatus);
        arg.append("CacherefreshStatus : ").append(this.cacherefreshStatus);
        return new String(arg);
    }
}
