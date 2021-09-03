package com.definesys.dsgc.service.dess.DessLog.bean;

import com.definesys.mpaas.query.annotation.*;

/**
 * @ClassName DessLogPayload
 * @Description TODO
 * @Author ystar
 * @Date 2020-8-3 14:30
 * @Version 1.0
 **/
@Table("dess_log_payload")
public class DessLogPayload {

    private String logId  ;
    private String headerPayload ;
    private String bodyPayload ;
    private String errPayload ;
    private String creationDate ;
    @Column(type = ColumnType.JAVA)
    private String invokeUrl;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getHeaderPayload() {
        return headerPayload;
    }

    public void setHeaderPayload(String headerPayload) {
        this.headerPayload = headerPayload;
    }

    public String getBodyPayload() {
        return bodyPayload;
    }

    public void setBodyPayload(String bodyPayload) {
        this.bodyPayload = bodyPayload;
    }

    public String getErrPayload() {
        return errPayload;
    }

    public void setErrPayload(String errPayload) {
        this.errPayload = errPayload;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getInvokeUrl() {
        return invokeUrl;
    }

    public void setInvokeUrl(String invokeUrl) {
        this.invokeUrl = invokeUrl;
    }
}
