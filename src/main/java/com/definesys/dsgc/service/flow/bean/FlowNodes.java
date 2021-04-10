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

@Table(value = "FLOW_NODES")
public class FlowNodes extends MpaasBasePojo {
    @Column(value = "fn_id")
    @Style(displayName = "主键")
    @RowID(type = RowIDType.UUID)
    private String fnId;

    @Column(value = "node_id")
    @Style(displayName = "flow路线图中引用的节点id")
    private String nodeId;

    @Column(value = "node_name")
    @Style(displayName = "节点名称")
    private String nodeName;

    @Column(value = "cnpt_code")
    @Style(displayName = "节点所使用组件唯一编码")
    private String cnptCode;

    @Column(value = "road_id")
    @Style(displayName = "外键字段，flow路线图引用的主键")
    private String roadId;

    @Column(value = "input_type")
    @Style(displayName = "input数据类型：none text json xml")
    private String inputType;

    @Column(value = "input_meta")
    @Style(displayName = "input对象的数据结构")
    private String inputMeta;

    @Column(value = "input_value")
    @Style(displayName = "input对象")
    private String inputValue;

    @Column(value = "ouput_type")
    @Style(displayName = "输出数据类型：none text json xml")
    private String outputType;

    @Column(value = "output_meta")
    @Style(displayName = "output对象的数据结构")
    private String outputMeta;

    @Column(value = "output_value")
    @Style(displayName = "output对象")
    private String outputValue;

    @Column(value = "params")
    @Style(displayName = "其它参数")
    private String params;

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


    public String getFnId() {
        return fnId;
    }

    public void setFnId(String fnId) {
        this.fnId = fnId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getCnptCode() {
        return cnptCode;
    }

    public void setCnptCode(String cnptCode) {
        this.cnptCode = cnptCode;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getInputMeta() {
        return inputMeta;
    }

    public void setInputMeta(String inputMeta) {
        this.inputMeta = inputMeta;
    }

    public String getOutputMeta() {
        return outputMeta;
    }

    public void setOutputMeta(String outputMeta) {
        this.outputMeta = outputMeta;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(String outputValue) {
        this.outputValue = outputValue;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
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
