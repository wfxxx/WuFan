package com.definesys.dsgc.service.dagclient.proxy.bean;

public class JWTAuthVO {
    private String rsa_public_key;
    private IdVO consumer;
    private String id;
    private String key;
    private String secret;
    private String algorithm;

    public String getRsa_public_key() {
        return rsa_public_key;
    }

    public void setRsa_public_key(String rsa_public_key) {
        this.rsa_public_key = rsa_public_key;
    }

    public IdVO getConsumer() {
        return consumer;
    }

    public void setConsumer(IdVO consumer) {
        this.consumer = consumer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
