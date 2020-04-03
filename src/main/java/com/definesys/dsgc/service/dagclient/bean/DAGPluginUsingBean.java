package com.definesys.dsgc.service.dagclient.bean;

import com.definesys.mpaas.query.annotation.*;

@Table(value = "DAG_PLUGIN_USING")
public class DAGPluginUsingBean {


    @RowID(type = RowIDType.UUID)
    @Column(value = "DPU_ID", type = ColumnType.DB)
    private String dpuId;
    @Column(value = "VID", type = ColumnType.DB)
    private String vid;
    @Column(value = "PLUGIN_CODE", type = ColumnType.DB)
    private String pluginCode;
    @Column(value = "IS_ENABLE", type = ColumnType.DB)
    private String isEnable;

    @Column(value = "CONSUMER", type = ColumnType.DB)
    private String consumer;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
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

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getDpuId() {
        return dpuId;
    }

    public void setDpuId(String dpuId) {
        this.dpuId = dpuId;
    }
}
