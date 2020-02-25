package com.definesys.dsgc.service.dagclient.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.Table;

@Table(value = "DAG_LR_TARGETS")
public class DAGUpstreamTargetBean {

    @Column(value = "vid", type = ColumnType.DB)
    private String vid;

    @Column(value = "LR_TARGET", type = ColumnType.DB)
    private String lrTarget;
    @Column(value = "LR_WEIGHT", type = ColumnType.DB)
    private String lrWeight;


    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getLrTarget() {
        return lrTarget;
    }

    public void setLrTarget(String lrTarget) {
        this.lrTarget = lrTarget;
    }

    public String getLrWeight() {
        return lrWeight;
    }

    public void setLrWeight(String lrWeight) {
        this.lrWeight = lrWeight;
    }
}
