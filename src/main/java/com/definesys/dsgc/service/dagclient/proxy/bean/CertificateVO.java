package com.definesys.dsgc.service.dagclient.proxy.bean;

import java.util.List;

public class CertificateVO {
    private String id;
    private String cert;
    private String key;
    private List<String> snis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getSnis() {
        return snis;
    }

    public void setSnis(List<String> snis) {
        this.snis = snis;
    }
}
