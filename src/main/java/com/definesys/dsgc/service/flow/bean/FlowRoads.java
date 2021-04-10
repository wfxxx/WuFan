package com.definesys.dsgc.service.flow.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@Table(value = "FLOW_ROADS")
public class FlowRoads extends MpaasBasePojo {

    @Column(value = "road_id")
    @Style(displayName = "主键")
    @RowID(type = RowIDType.UUID)
    private String roadId;

    @Column(value = "flow_id")
    @Style(displayName = "flow_services表的外键id")
    private String flowId;

    @Column(value = "flow_version")
    @Style(displayName = "flow版本号")
    private String flowVersion;

    @Column(value = "start_node_id")
    @Style(displayName = "flow开始的节点id")
    private String startNodeId;

    @Column(value = "flow_stat")
    @Style(displayName = "flow的状态，editing编辑中，saved已保存,old被更新后的旧记录")
    private String flowStat;

    @Column(value = "road_graph")
    @Style(displayName = "flow的编排图，用Json的结构保存")
    private String roadGraph;

    @JsonIgnore
    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    @Style(displayName = "创建人")
    private String createdBy;


    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    @Style(displayName = "创建时间", width = "20")
    private java.util.Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    @Style(displayName = "修改人")
    private String lastUpdatedBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    @Style(displayName = "修改时间", width = "20")
    private Date lastUpdateDate;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    @Style(displayName = "行版本号，用来处理锁")
    private Long objectVersionNumber;


    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(String flowVersion) {
        this.flowVersion = flowVersion;
    }

    public String getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(String startNodeId) {
        this.startNodeId = startNodeId;
    }

    public String getFlowStat() {
        return flowStat;
    }

    public void setFlowStat(String flowStat) {
        this.flowStat = flowStat;
    }

    public String getRoadGraph() {
        return roadGraph;
    }

    public void setRoadGraph(String roadGraph) {
        this.roadGraph = roadGraph;
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

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }
}
