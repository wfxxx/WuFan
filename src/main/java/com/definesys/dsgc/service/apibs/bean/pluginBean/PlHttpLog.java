package com.definesys.dsgc.service.apibs.bean.pluginBean;

import com.definesys.mpaas.query.annotation.RowID;
import com.definesys.mpaas.query.annotation.RowIDType;
import com.definesys.mpaas.query.annotation.Table;

/**
 * @ClassName PlHttpLog
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-24 10:37
 * @Version 1.0
 **/
@Table(value = "plugin_http_log")
public class PlHttpLog {
    @RowID(type= RowIDType.UUID)
    private String hlId;
    private String pluginCode;
    private String vid;
    private String consumer;
    private String httpEndpoint;
    private String method;
    private String contentType;
    private Integer timeout;
    private Integer keepalive;
    private Integer retryCount;
    private Integer queueSize;
    private Integer flushTimeout;

    public String getHlId() {
        return hlId;
    }

    public void setHlId(String hlId) {
        this.hlId = hlId;
    }

    public String getPluginCode() {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getHttpEndpoint() {
        return httpEndpoint;
    }

    public void setHttpEndpoint(String httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(Integer keepalive) {
        this.keepalive = keepalive;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getFlushTimeout() {
        return flushTimeout;
    }

    public void setFlushTimeout(Integer flushTimeout) {
        this.flushTimeout = flushTimeout;
    }
}
