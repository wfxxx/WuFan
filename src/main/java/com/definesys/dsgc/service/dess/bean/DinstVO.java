package com.definesys.dsgc.service.dess.bean;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @ClassName DinstVO
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-7-28 18:20
 * @Version 1.0
 **/
public class DinstVO {
    //DinstBean
    private String jobNo;
    private String businessId;
    private String jobName;
    private String jobType;
    private String status;
    private JSONObject frequency; //json字符串，接收corn表达式
    private Date nextDoTime;
    private Date aliveStart;
    private Date aliveEnd;
    private String group;
    private Integer sucessTimes;
    private Integer failTimes;
    private Integer avgRunTime;
    private String description;
    private String version;
    //DessBusiness
    private String wsdlUrl;
    private String webServiceType;
    private String service;
    private String portType;
    private String operation;
    private String payload;


    public DinstVO(DinstBean dinstBean,DessBusiness dessBusiness){
        //DinstBean
        if(dinstBean != null){
            jobNo=dinstBean.getJobNo();
            businessId=dinstBean.getBusinessId();
            jobName=dinstBean.getJobName();
            jobType=dinstBean.getJobType();
            //等待使用corn工具类转换
            //frequency=dinstBean.getFrequency();
            nextDoTime=dinstBean.getNextDoTime();
            aliveStart=dinstBean.getAliveStart();
            aliveEnd=dinstBean.getAliveEnd();
            group=dinstBean.getGroup();
            sucessTimes=dinstBean.getSucessTimes();
            failTimes=dinstBean.getFailTimes();
            avgRunTime=dinstBean.getAvgRunTime();
            description=dinstBean.getDescription();
            version=dinstBean.getVersion();
        }
        if(dessBusiness != null){
            //DessBusiness
            wsdlUrl=dessBusiness.getWsdlUrl();
            webServiceType=dessBusiness.getWebServiceType();
            service=dessBusiness.getWebServiceType();
            portType=dessBusiness.getPortType();
            operation=dessBusiness.getOperation();
            payload=dessBusiness.getPayload();
        }
    }

    public  DinstBean getDinstBean(){
        DinstBean dinstBean =new DinstBean();
        dinstBean.setAliveEnd(aliveEnd);
        dinstBean.setAliveStart(aliveStart);
        dinstBean.setAvgRunTime(avgRunTime);
        dinstBean.setBusinessId(businessId);
        dinstBean.setDescription(description);
        dinstBean.setFailTimes(failTimes);
        //等待corn工具转换
        //dinstBean.setFrequency(frequency);
        dinstBean.setGroup(group);
        dinstBean.setJobName(jobName);
        dinstBean.setJobType(jobType);
        dinstBean.setJobNo(jobNo);
        dinstBean.setVersion(version);
        dinstBean.setNextDoTime(nextDoTime);
        return dinstBean;
    }

    public  DessBusiness getDessBusiness(){
        DessBusiness dessBusiness = new DessBusiness();
        dessBusiness.setBusinessId(businessId);
        dessBusiness.setOperation(operation);
        dessBusiness.setPayload(payload);
        dessBusiness.setPortType(portType);
        dessBusiness.setService(service);
        dessBusiness.setWebServiceType(webServiceType);
        dessBusiness.setWsdlUrl(wsdlUrl);
        return dessBusiness;
    }



    public String getJobNo() { return jobNo; }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JSONObject getFrequency() {
        return frequency;
    }

    public void setFrequency(JSONObject frequency) {
        this.frequency = frequency;
    }

    public Date getNextDoTime() {
        return nextDoTime;
    }

    public void setNextDoTime(Date nextDoTime) {
        this.nextDoTime = nextDoTime;
    }

    public Date getAliveStart() {
        return aliveStart;
    }

    public void setAliveStart(Date aliveStart) {
        this.aliveStart = aliveStart;
    }

    public Date getAliveEnd() {
        return aliveEnd;
    }

    public void setAliveEnd(Date aliveEnd) {
        this.aliveEnd = aliveEnd;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getSucessTimes() {
        return sucessTimes;
    }

    public void setSucessTimes(Integer sucessTimes) {
        this.sucessTimes = sucessTimes;
    }

    public Integer getFailTimes() {
        return failTimes;
    }

    public void setFailTimes(Integer failTimes) {
        this.failTimes = failTimes;
    }

    public Integer getAvgRunTime() {
        return avgRunTime;
    }

    public void setAvgRunTime(Integer avgRunTime) {
        this.avgRunTime = avgRunTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public String getWebServiceType() {
        return webServiceType;
    }

    public void setWebServiceType(String webServiceType) {
        this.webServiceType = webServiceType;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }



}
