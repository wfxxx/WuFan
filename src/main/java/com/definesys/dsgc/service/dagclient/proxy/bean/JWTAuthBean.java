package com.definesys.dsgc.service.dagclient.proxy.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

public class JWTAuthBean {

    @Column(value = "CSM_CODE", type = ColumnType.DB)
    private String csmCode;
    @Column(value = "ISS_KEY", type = ColumnType.DB)
    private String issKey;
    @Column(value = "SECRET_KEY", type = ColumnType.DB)
    private String secretKey;
    @Column(value = "ALGORITHM", type = ColumnType.DB)
    private String algorithm;
    @Column(value = "RSA_PUBLIC_KEY", type = ColumnType.DB)
    private String rsaPublicKey;
    @Column(value = "ENV_CODE", type = ColumnType.DB)
    private String envCode;

    public String getCsmCode() {
        return csmCode;
    }

    public void setCsmCode(String csmCode) {
        this.csmCode = csmCode;
    }

    public String getIssKey() {
        return issKey;
    }

    public void setIssKey(String issKey) {
        this.issKey = issKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }
}
