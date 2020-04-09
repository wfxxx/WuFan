package com.definesys.dsgc.service.dagclient.proxy.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

public class JWTPlguinCfbBean {
    @Column(value = "URL_NAMES", type = ColumnType.DB)
    private String urlNames;
    @Column(value = "COOKIE_NAMES", type = ColumnType.DB)
    private String cookieNames;
    @Column(value = "KEY_CLAIM_NAME", type = ColumnType.DB)
    private String keyClaimName;
    @Column(value = "IS_BASE64", type = ColumnType.DB)
    private String isBase64;
    @Column(value = "CLAIMS_VERIFY", type = ColumnType.DB)
    private String claimsVerify;
    @Column(value = "ANONYMOUS", type = ColumnType.DB)
    private String anonymous;
    @Column(value = "IS_PREFLIGHT", type = ColumnType.DB)
    private String isPreflight;
    @Column(value = "MAX_ALIVE", type = ColumnType.DB)
    private Long maxAlive = 0L;
    @Column(value = "HEADER_NAMES", type = ColumnType.DB)
    private String headerNames;

    public String getUrlNames() {
        return urlNames;
    }

    public void setUrlNames(String urlNames) {
        this.urlNames = urlNames;
    }

    public String getCookieNames() {
        return cookieNames;
    }

    public void setCookieNames(String cookieNames) {
        this.cookieNames = cookieNames;
    }

    public String getKeyClaimName() {
        return keyClaimName;
    }

    public void setKeyClaimName(String keyClaimName) {
        this.keyClaimName = keyClaimName;
    }

    public String getIsBase64() {
        return isBase64;
    }

    public void setIsBase64(String isBase64) {
        this.isBase64 = isBase64;
    }

    public String getClaimsVerify() {
        return claimsVerify;
    }

    public void setClaimsVerify(String claimsVerify) {
        this.claimsVerify = claimsVerify;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getIsPreflight() {
        return isPreflight;
    }

    public void setIsPreflight(String isPreflight) {
        this.isPreflight = isPreflight;
    }

    public Long getMaxAlive() {
        return maxAlive;
    }

    public void setMaxAlive(Long maxAlive) {
        this.maxAlive = maxAlive;
    }

    public String getHeaderNames() {
        return headerNames;
    }

    public void setHeaderNames(String headerNames) {
        this.headerNames = headerNames;
    }
}
