package com.definesys.dsgc.service.apibs.bean.pluginBean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * @ClassName PlOauth2
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-24 10:47
 * @Version 1.0
 **/
@Table(value = "plugin_oauth2")
public class PlOauth2 {

    @RowID(type= RowIDType.UUID)
    private String poId;


    private String scopes;
    private String mandatoryScope;
    private String provisionKey;
    private Integer tokenExpiration;
    private String authorizationCode;
    private String implicitGrant;
    private String clientCredentials;
    private String passwordGrant;
    private String redentials;
    private String httpIfTerminated;
    private String anonymous;
    private String globalCredentials;
    private String authHeaderName;
    private Integer refreshTokenTtl;
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

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }







    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getMandatoryScope() {
        return mandatoryScope;
    }

    public void setMandatoryScope(String mandatoryScope) {
        this.mandatoryScope = mandatoryScope;
    }

    public String getProvisionKey() {
        return provisionKey;
    }

    public void setProvisionKey(String provisionKey) {
        this.provisionKey = provisionKey;
    }

    public Integer getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Integer tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getImplicitGrant() {
        return implicitGrant;
    }

    public void setImplicitGrant(String implicitGrant) {
        this.implicitGrant = implicitGrant;
    }

    public String getClientCredentials() {
        return clientCredentials;
    }

    public void setClientCredentials(String clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    public String getPasswordGrant() {
        return passwordGrant;
    }

    public void setPasswordGrant(String passwordGrant) {
        this.passwordGrant = passwordGrant;
    }

    public String getRedentials() {
        return redentials;
    }

    public void setRedentials(String redentials) {
        this.redentials = redentials;
    }

    public String getHttpIfTerminated() {
        return httpIfTerminated;
    }

    public void setHttpIfTerminated(String httpIfTerminated) {
        this.httpIfTerminated = httpIfTerminated;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getGlobalCredentials() {
        return globalCredentials;
    }

    public void setGlobalCredentials(String globalCredentials) {
        this.globalCredentials = globalCredentials;
    }

    public String getAuthHeaderName() {
        return authHeaderName;
    }

    public void setAuthHeaderName(String authHeaderName) {
        this.authHeaderName = authHeaderName;
    }

    public Integer getRefreshTokenTtl() {
        return refreshTokenTtl;
    }

    public void setRefreshTokenTtl(Integer refreshTokenTtl) {
        this.refreshTokenTtl = refreshTokenTtl;
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
