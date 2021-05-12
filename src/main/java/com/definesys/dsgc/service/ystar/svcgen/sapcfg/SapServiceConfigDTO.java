package com.definesys.dsgc.service.ystar.svcgen.sapcfg;

import javax.validation.constraints.NotBlank;

/**
 * sap快速开发配置
 *
 * @author afan
 * @version 1.0
 * @date 2020/10/6 15:20
 */
public class SapServiceConfigDTO {
    @NotBlank(message = "servNo不能为空")
    private String servNo;
    @NotBlank(message = "servNameCode不能为空")
    private String servName;
    @NotBlank(message = "appCode不能为空")
    private String appCode;
    @NotBlank(message = "toSystem不能为空")
    private String toSystem;
    @NotBlank(message = "rfcName不能为空")
    private String rfcName;
    @NotBlank(message = "sapConn不能为空")
    private String sapConn;
    @NotBlank(message = "resConfig不能为空")
    private String resConfig;
    @NotBlank(message = "appUri不能为空")
    private String appUri;

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServName() {
        return servName;
    }

    public void setServNameCode(String servName) {
        this.servName = servName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getToSystem() {
        return toSystem;
    }

    public void setToSystem(String toSystem) {
        this.toSystem = toSystem;
    }

    public String getRfcName() {
        return rfcName;
    }

    public void setRfcName(String rfcName) {
        this.rfcName = rfcName;
    }

    public String getSapConn() {
        return sapConn;
    }

    public void setSapConn(String sapConn) {
        this.sapConn = sapConn;
    }

    public String getResConfig() {
        return resConfig;
    }

    public void setResConfig(String resConfig) {
        this.resConfig = resConfig;
    }

    public String getAppUri() {
        return appUri;
    }

    public void setAppUri(String appUri) {
        this.appUri = appUri;
    }
}
