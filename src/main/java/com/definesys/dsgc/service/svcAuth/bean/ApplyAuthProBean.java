package com.definesys.dsgc.service.svcAuth.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.Table;
import com.definesys.mpaas.query.model.MpaasBasePojo;

/**
 * @ClassName ApplyAuthProBean
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-18 13:26
 * @Version 1.0
 **/
@Table(value = "dsgc_client_auth_apply")
public class ApplyAuthProBean  extends MpaasBasePojo {

    @Column(value = "inst_id")
    private String instanceId;
    @Column(value = "created_by")
    private String applicant;
    @Column(value = "apply_ser_name")
    private String applySerName;

    private String applySerType;//服务类型，服务来源
    @Column(type = ColumnType.JAVA)
    private String processType;
    @Column(value = "consumers")
    private String consumerStr;
    @Column(type = ColumnType.JAVA)
    private String[] consumer;
    @Column(value = "apply_desc")
    private String applyDesc;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
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

    public String getConsumerStr() {
        return consumerStr;
    }

    public void setConsumerStr(String consumerStr) {
        this.consumerStr = consumerStr;
    }

    public String[] getConsumer() {
        return consumer;
    }

    public void setConsumer(String[] consumer) {
        this.consumer = consumer;
    }

    public String getApplyDesc() {
        return applyDesc;
    }

    public void setApplyDesc(String applyDesc) {
        this.applyDesc = applyDesc;
    }
}
