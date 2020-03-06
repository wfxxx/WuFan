package com.definesys.dsgc.service.svcAuth.bean;

import com.definesys.dsgc.service.consumers.bean.ConsumerEntitieDTO;
import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.dsgc.service.market.bean.MarketApiBean;
import com.definesys.dsgc.service.market.bean.MarketEntiy;
import com.definesys.dsgc.service.users.UsersDao;
import com.definesys.dsgc.service.users.bean.UserInfoBean;
import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;

import java.util.List;

/**
 * @ClassName ApplyAuthProBeanDTO
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-19 15:56
 * @Version 1.0
 **/
public class ApplyAuthProBeanDTO {


    private String instanceId;


    private String applicantName;


    private String applicantDesc;

    private String applicantPhone;

    private String applicantEmail;

    private String applySerName;

    private String applySerType;//服务类型，服务来源

    private String processType;

    private MarketEntiy appliSer;

    private List<DSGCConsumerEntities> consumerList;

    private String applyDesc;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getApplySerName() {
        return applySerName;
    }

    public void setApplySerName(String applySerName) {
        this.applySerName = applySerName;
    }

    public String getApplySerType() {
        return applySerType;
    }

    public void setApplySerType(String applySerType) {
        this.applySerType = applySerType;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public List<DSGCConsumerEntities> getConsumerList() {
        return consumerList;
    }

    public void setConsumerList(List<DSGCConsumerEntities> consumerList) {
        this.consumerList = consumerList;
    }

    public String getApplyDesc() {
        return applyDesc;
    }

    public void setApplyDesc(String applyDesc) {
        this.applyDesc = applyDesc;
    }

    public MarketEntiy getAppliSer() {
        return appliSer;
    }

    public void setAppliSer(MarketEntiy appliSer) {
        this.appliSer = appliSer;
    }

    public String getApplicantDesc() {
        return applicantDesc;
    }

    public void setApplicantDesc(String applicantDesc) {
        this.applicantDesc = applicantDesc;
    }
}
