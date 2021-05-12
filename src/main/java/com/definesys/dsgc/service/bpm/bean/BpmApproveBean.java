package com.definesys.dsgc.service.bpm.bean;

public class BpmApproveBean {

    private String instId;  //流程实例ID

    private String passOrReject;  //审批通过或者驳回标识

    private String approveOpinion;   //审批意见

    private String approver;         //指定的审批人

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getPassOrReject() {
        return passOrReject;
    }

    public void setPassOrReject(String passOrReject) {
        this.passOrReject = passOrReject;
    }

    public String getApproveOpinion() {
        return approveOpinion;
    }

    public void setApproveOpinion(String approveOpinion) {
        this.approveOpinion = approveOpinion;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }
}
