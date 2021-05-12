package com.definesys.dsgc.service.ystar.svcgen.bean;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class ServiceConfigDTO {

    @NotBlank(message = "服务编号不能为空")
    public String svcCode;
    @NotBlank(message = "服务名字不能为空")
    public String svcName;
    public String svcPath;
    @NotBlank(message = "所属系统不能为空")
    public String toSystem;
    @NotBlank(message = "项目名称不能为空")
    public String projectName;
    @ApiModelProperty(value = "业务服务http配置IP", required = true)
    @NotBlank(message = "业务服务http配置IP不能为空")
    public String bizReqIp;
    @ApiModelProperty(value = "业务服务http配置端口", required = true)
    @NotBlank(message = "业务服务http配置端口不能为空")
    public String bizReqPort;
    @ApiModelProperty(value = "业务服务访问uri/WSDL地址", required = true)
    @NotBlank(message = "业务服务访问uri不能为空")
    public String bizReqPath;

    public String getSvcCode() {
        return svcCode;
    }

    public String getBizReqPath() {
        return bizReqPath;
    }

    public void setBizReqPath(String bizReqPath) {
        this.bizReqPath = bizReqPath;
    }

    public void setSvcCode(String svcCode) {
        this.svcCode = svcCode;
    }

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

    public String getSvcPath() {
        return svcPath;
    }

    public void setSvcPath(String svcPath) {
        this.svcPath = svcPath;
    }

    public String getToSystem() {
        return toSystem;
    }

    public void setToSystem(String toSystem) {
        this.toSystem = toSystem;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBizReqIp() {
        return bizReqIp;
    }

    public void setBizReqIp(String bizReqIp) {
        this.bizReqIp = bizReqIp;
    }

    public String getBizReqPort() {
        return bizReqPort;
    }

    public void setBizReqPort(String bizReqPort) {
        this.bizReqPort = bizReqPort;
    }

    @Override
    public String toString() {
        return "ServiceConfigDTO{" +
                "svcCode='" + svcCode + '\'' +
                ", svcName='" + svcName + '\'' +
                ", svcPath='" + svcPath + '\'' +
                ", toSystem='" + toSystem + '\'' +
                ", projectName='" + projectName + '\'' +
                ", bizReqIp='" + bizReqIp + '\'' +
                ", bizReqPort='" + bizReqPort + '\'' +
                ", bizReqPath='" + bizReqPath + '\'' +
                '}';
    }
}
