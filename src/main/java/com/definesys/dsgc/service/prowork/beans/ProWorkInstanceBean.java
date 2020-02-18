package com.definesys.dsgc.service.prowork.beans;

import com.definesys.mpaas.query.annotation.RowID;
import com.definesys.mpaas.query.annotation.RowIDType;
import com.definesys.mpaas.query.annotation.Table;
import com.definesys.mpaas.query.model.MpaasBasePojo;

import java.util.Date;

/**
 * @ClassName ProWorkInstanceBean
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-18 9:17
 * @Version 1.0
 **/
@Table(value = "dsgc_bpm_instances")
public class ProWorkInstanceBean extends MpaasBasePojo {

    @RowID(sequence = "DSGC_BPM_INSTANCES_S",type = RowIDType.AUTO)
    private String instanseId;

    private String processId;

    private ProWorkProcessBean proWorkProcessBean;

    private String instanceTiTle;

    private String curNode;

    private ProWorkNodesBean proWorkNodesBean;

    private String instanceState;

    private String createdBy;

    private Date creationDate;

    private String lastUpdatedBy;

    private Date lastUpdateDate;;


    public String getInstanseId() {
        return instanseId;
    }

    public void setInstanseId(String instanseId) {
        this.instanseId = instanseId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public ProWorkProcessBean getProWorkProcessBean() {
        return proWorkProcessBean;
    }

    public void setProWorkProcessBean(ProWorkProcessBean proWorkProcessBean) {
        this.proWorkProcessBean = proWorkProcessBean;
    }

    public String getInstanceTiTle() {
        return instanceTiTle;
    }

    public void setInstanceTiTle(String instanceTiTle) {
        this.instanceTiTle = instanceTiTle;
    }

    public String getCurNode() {
        return curNode;
    }

    public void setCurNode(String curNode) {
        this.curNode = curNode;
    }

    public ProWorkNodesBean getProWorkNodesBean() {
        return proWorkNodesBean;
    }

    public void setProWorkNodesBean(ProWorkNodesBean proWorkNodesBean) {
        this.proWorkNodesBean = proWorkNodesBean;
    }

    public String getInstanceState() {
        return instanceState;
    }

    public void setInstanceState(String instanceState) {
        this.instanceState = instanceState;
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
        return "ProWorkInstanceBean{" +
                "instanseId='" + instanseId + '\'' +
                ", processId='" + processId + '\'' +
                ", proWorkProcessBean=" + proWorkProcessBean +
                ", instanceTiTle='" + instanceTiTle + '\'' +
                ", curNode='" + curNode + '\'' +
                ", proWorkNodesBean=" + proWorkNodesBean +
                ", instanceState='" + instanceState + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
