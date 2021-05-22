package com.definesys.dsgc.service.ystar.svcgen.restcfg;

import com.definesys.dsgc.service.ystar.svcgen.bean.ServiceConfigDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RestServiceConfigDTO
 * @Description: MuleRest接口快速开发配置
 * @Author：afan
 * @Date : 2020/1/6 16:33
 */
@ApiModel("Rest配置属性")
public class RestServiceConfigDTO extends ServiceConfigDTO {

    @NotBlank(message = "服务编号不能为空")
    public String svcCode;
    @NotBlank(message = "服务名字不能为空")
    public String svcName;
    @NotBlank(message = "服务Uri地址不能为空")
    public String svcPath;
    @NotBlank(message = "所属系统不能为空")
    public String toSystem;
    @NotBlank(message = "项目Id不能为空")
    public String projId;
    @NotBlank(message = "项目名称不能为空")
    public String projName;
    @ApiModelProperty(value = "业务服务http配置IP", required = true)
    @NotBlank(message = "业务服务http配置IP不能为空")
    public String bizReqIp;
    @ApiModelProperty(value = "业务服务http配置端口", required = true)
    @NotBlank(message = "业务服务http配置端口不能为空")
    public String bizReqPort;
    @ApiModelProperty(value = "业务服务访问uri/WSDL地址", required = true)
    @NotBlank(message = "业务服务访问uri不能为空")
    public String bizReqPath;
    @ApiModelProperty(value = "请求方式", required = true)
    @NotBlank(message = "业务服务请求方法不能为空")
    private String bizReqMethod;

    @ApiModelProperty(value = "出栈表头设置")
    private List<Map<String, String>> headers;

    @Override
    public String getSvcCode() {
        return svcCode;
    }

    @Override
    public void setSvcCode(String svcCode) {
        this.svcCode = svcCode;
    }

    @Override
    public String getSvcName() {
        return svcName;
    }

    @Override
    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

    @Override
    public String getSvcPath() {
        return svcPath;
    }

    @Override
    public void setSvcPath(String svcPath) {
        this.svcPath = svcPath;
    }

    @Override
    public String getToSystem() {
        return toSystem;
    }

    @Override
    public void setToSystem(String toSystem) {
        this.toSystem = toSystem;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    @Override
    public String getBizReqIp() {
        return bizReqIp;
    }

    @Override
    public void setBizReqIp(String bizReqIp) {
        this.bizReqIp = bizReqIp;
    }

    @Override
    public String getBizReqPort() {
        return bizReqPort;
    }

    @Override
    public void setBizReqPort(String bizReqPort) {
        this.bizReqPort = bizReqPort;
    }

    @Override
    public String getBizReqPath() {
        return bizReqPath;
    }

    @Override
    public void setBizReqPath(String bizReqPath) {
        this.bizReqPath = bizReqPath;
    }

    public String getBizReqMethod() {
        return bizReqMethod;
    }

    public void setBizReqMethod(String bizReqMethod) {
        this.bizReqMethod = bizReqMethod;
    }

    public List<Map<String, String>> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Map<String, String>> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "RestServiceConfigDTO{" +
                "svcCode='" + svcCode + '\'' +
                ", svcName='" + svcName + '\'' +
                ", svcPath='" + svcPath + '\'' +
                ", toSystem='" + toSystem + '\'' +
                ", projId='" + projId + '\'' +
                ", projName='" + projName + '\'' +
                ", bizReqIp='" + bizReqIp + '\'' +
                ", bizReqPort='" + bizReqPort + '\'' +
                ", bizReqPath='" + bizReqPath + '\'' +
                ", bizReqMethod='" + bizReqMethod + '\'' +
                ", headers=" + headers +
                '}';
    }
}

