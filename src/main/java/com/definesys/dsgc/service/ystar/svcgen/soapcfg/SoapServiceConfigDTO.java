package com.definesys.dsgc.service.ystar.svcgen.soapcfg;

import com.definesys.dsgc.service.ystar.svcgen.bean.ServiceConfigDTO;
import org.ow2.easywsdl.wsdl.api.Service;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * webservice快速开发DTO
 *
 * @author afan
 * @version 1.0
 * @date 2020/9/1 11:08
 */
public class SoapServiceConfigDTO extends ServiceConfigDTO {
    @NotBlank(message = "wsdl地址不能为空")
    private String wsdlUri;
    private String wsdlName;
    @NotBlank(message = "绑定服务不能为空")
    private String sltPort;
    private String nameSpace;
    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getWsdlName() {
        return wsdlName;
    }

    public void setWsdlName(String wsdlName) {
        this.wsdlName = wsdlName;
    }

    private List<String> soapOpers;

    private List<Map<String, String>> operInfos;

    public List<Map<String, String>> getOperInfos() {
        return operInfos;
    }

    public void setOperInfos(List<Map<String, String>> operInfos) {
        this.operInfos = operInfos;
    }

    public String getSvcCode() {
        return super.getSvcCode();
    }

    public void setSvcCode(String svcCode) {
        super.setSvcCode(svcCode);
    }

    public String getWsdlUri() {
        return wsdlUri;
    }

    public void setWsdlUri(String wsdlUri) {
        this.wsdlUri = wsdlUri;
    }

    public String getSltPort() {
        return sltPort;
    }

    public void setSltPort(String sltPort) {
        this.sltPort = sltPort;
    }

    public List<String> getSoapOpers() {
        return soapOpers;
    }

    public void setSoapOpers(List<String> soapOpers) {
        this.soapOpers = soapOpers;
    }

    @Override
    public String toString() {
        super.toString();
        return "SoapServiceConfigDTO{" +
                ", wsdlUri='" + wsdlUri + '\'' +
                ", sltPort='" + sltPort + '\'' +
                ", soapOpers=" + soapOpers +
                ", operInfos=" + operInfos +
                '}';
    }
}
