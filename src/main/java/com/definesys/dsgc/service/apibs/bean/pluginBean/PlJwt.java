package com.definesys.dsgc.service.apibs.bean.pluginBean;

import com.definesys.mpaas.query.annotation.*;

/**
 * @ClassName PlJwt
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-4-8 18:15
 * @Version 1.0
 **/
@Table(value = "PLUGIN_JWT")
public class PlJwt {

    @RowID(type = RowIDType.UUID)
    private String jwtId;
    @Column(type = ColumnType.JAVA)
    private String consumer;
    private String urlNames;
    private String cookieNames;
    private String dpuId;
    private String keyClaimName;
    private String isBase64;
    private String claimsVerify;
    private String isPreflight;
    private String anonymous;
    private Integer maxAlive;
    private String headerNames;

    public String getJwtId() {
        return jwtId;
    }

    public void setJwtId(String jwtId) {
        this.jwtId = jwtId;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

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

    public String getDpuId() {
        return dpuId;
    }

    public void setDpuId(String dpuId) {
        this.dpuId = dpuId;
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

    public String getIsPreflight() {
        return isPreflight;
    }

    public void setIsPreflight(String isPreflight) {
        this.isPreflight = isPreflight;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public Integer getMaxAlive() {
        return maxAlive;
    }

    public void setMaxAlive(Integer maxAlive) {
        this.maxAlive = maxAlive;
    }

    public String getHeaderNames() {
        return headerNames;
    }

    public void setHeaderNames(String headerNames) {
        this.headerNames = headerNames;
    }
}
