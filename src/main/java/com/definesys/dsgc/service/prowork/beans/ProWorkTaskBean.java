package com.definesys.dsgc.service.prowork.beans;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.model.MpaasBasePojo;

import java.util.Date;

/**
 * @ClassName ProWorkTaskBean
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-18 9:15
 * @Version 1.0
 **/
@Table(value = "dsgc_bpm_task")
public class ProWorkTaskBean extends MpaasBasePojo {

    private String processId;

    private String processType;

    private String processTiTle;

    private String applicantId;

    private String approvalNode;


    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getProcessTiTle() {
        return processTiTle;
    }

    public void setProcessTiTle(String processTiTle) {
        this.processTiTle = processTiTle;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApprovalNode() {
        return approvalNode;
    }

    public void setApprovalNode(String approvalNode) {
        this.approvalNode = approvalNode;
    }

    @Override
    public String toString() {
        return "ProWorkTaskBean{" +
                "processId='" + processId + '\'' +
                ", processType='" + processType + '\'' +
                ", processTiTle='" + processTiTle + '\'' +
                ", applicantId='" + applicantId + '\'' +
                ", approvalNode='" + approvalNode + '\'' +
                '}';
    }
}
