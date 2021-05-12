package com.definesys.dsgc.service.apiplugin.bean;

import com.definesys.dsgc.service.svcmng.bean.DeployedEnvInfoBean;
import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.SystemColumn;
import com.definesys.mpaas.query.annotation.SystemColumnType;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * @ClassName DAGPluginListVO
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-16 13:21
 * @Version 1.0
 **/
public class DAGPluginListVO {

    //插件使用id
    private String pluginUsingId;
    //插件类型
    private String pluginCode;
    //插件是否启用
    private String isEnable;
    //插件名称
    private String pluginName;
    //插件所属配置的id
    private String vid;
    //插件所属配置的名称
    private String configName;
    //插件所属服务的code
    private String sourCode;
    //插件所属服务的类型
    private String sourType;
    //插件所属应用的code
    private String appCode;
    //插件所属应用的名称
    private String appName;
    //插件部署环境名称
    private String devName="";
    //插件部署环境code
    private String envCode;

    List<DeployedEnvInfoBean> envList;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    private Date creationDate;


    public String getPluginUsingId() {
        return pluginUsingId;
    }

    public void setPluginUsingId(String pluginUsingId) {
        this.pluginUsingId = pluginUsingId;
    }

    public String getPluginCode() {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getSourType() {
        return sourType;
    }

    public void setSourType(String sourType) {
        this.sourType = sourType;
    }



    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getSourCode() {
        return sourCode;
    }

    public void setSourCode(String sourCode) {
        this.sourCode = sourCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public List<DeployedEnvInfoBean> getEnvList() {
        return envList;
    }

    public void setEnvList(List<DeployedEnvInfoBean> envList) {
        this.envList = envList;
    }
}
