package com.definesys.dsgc.service.prowork.beans;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.model.MpaasBasePojo;

import java.util.Date;

/**
 * @ClassName ProWorkNodesBean
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-18 9:27
 * @Version 1.0
 **/
@Table(value = "dsgc_bpm_nodes")
public class ProWorkNodesBean extends MpaasBasePojo {

    private String nodeId;

    private String processId;

    private String nodeName;

    private String nodePos;

    private String nodeDesc;

    private String passNode;

    private String rejectNode;

    private String apprType;

    private String apprCode;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    private Integer objectVersionNumber;

    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;

    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodePos() {
        return nodePos;
    }

    public void setNodePos(String nodePos) {
        this.nodePos = nodePos;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public String getPassNode() {
        return passNode;
    }

    public void setPassNode(String passNode) {
        this.passNode = passNode;
    }

    public String getRejectNode() {
        return rejectNode;
    }

    public void setRejectNode(String rejectNode) {
        this.rejectNode = rejectNode;
    }

    public String getApprType() {
        return apprType;
    }

    public void setApprType(String apprType) {
        this.apprType = apprType;
    }

    public String getApprCode() {
        return apprCode;
    }

    public void setApprCode(String apprCode) {
        this.apprCode = apprCode;
    }

    public Integer getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Integer objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString() {
        return "ProWorkNodesBean{" +
                "nodeId='" + nodeId + '\'' +
                ", processId='" + processId + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", nodePos='" + nodePos + '\'' +
                ", nodeDesc='" + nodeDesc + '\'' +
                ", passNode='" + passNode + '\'' +
                ", rejectNode='" + rejectNode + '\'' +
                ", apprType='" + apprType + '\'' +
                ", apprCode='" + apprCode + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
