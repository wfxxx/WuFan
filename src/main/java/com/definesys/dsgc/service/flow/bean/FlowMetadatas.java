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

@Table(value = "FLOW_METADATAS")
public class FlowMetadatas extends MpaasBasePojo {
    @Column(value = "meta_id")
    @Style(displayName = "主键")
    @RowID(type = RowIDType.UUID)
    private String metaId;

    @Column(value = "road_id")
    @Style(displayName = "外键字段，flow路线图引用的主键")
    private String roadId;

    @Column(value = "sour")
    @Style(displayName = "当前meta生成者，id（引用Node） 或者 user(用户自定义)")
    private String sour;

    @Column(value = "meta_type")
    @Style(displayName = "元数据类型：String Object XML JSON")
    private String metaType;

    @Column(value = "meta_txt")
    @Style(displayName = "元数据结构")
    private String metaTxt;

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

    public String getMetaId() {
        return metaId;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public void setMetaId(String metaId) {
        this.metaId = metaId;
    }

    public String getSour() {
        return sour;
    }

    public void setSour(String sour) {
        this.sour = sour;
    }

    public String getMetaType() {
        return metaType;
    }

    public void setMetaType(String metaType) {
        this.metaType = metaType;
    }

    public String getMetaTxt() {
        return metaTxt;
    }

    public void setMetaTxt(String metaTxt) {
        this.metaTxt = metaTxt;
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
