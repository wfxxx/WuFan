package com.definesys.dsgc.service.dagclient.proxy.bean;

public class ReqSizePluginCfgVO {
    private Long allowed_payload_size = new Long(128L);
    private String size_unit = "bytes";

    public Long getAllowed_payload_size() {
        return allowed_payload_size;
    }

    public void setAllowed_payload_size(Long allowed_payload_size) {
        this.allowed_payload_size = allowed_payload_size;
    }

    public String getSize_unit() {
        return size_unit;
    }

    public void setSize_unit(String size_unit) {
        this.size_unit = size_unit;
    }
}
