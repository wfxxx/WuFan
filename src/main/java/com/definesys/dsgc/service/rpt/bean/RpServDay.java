package com.definesys.dsgc.service.rpt.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "接口执行消耗表--日表",description = "存储接口执行消耗,最细粒度到日")
@SQLQuery(
    value = {
    }
)
@Table(value = "RP_SERV_DAY")
public class RpServDay extends MpaasBasePojo {

    @ApiModelProperty(value = "唯一标识",notes = "不能超过30个字符")
    @RowID(type=RowIDType.UUID)
    private String id;

    @ApiModelProperty(value = "发送系统地址",notes = "不能超过20个字符")
    @Column(value = "CLIENT")
    private String client;
    private String startTime;  //请求时间
    private String endTime;   //结束时间

    @ApiModelProperty(value = "接口编号",notes = "不能超过10个字符")
    @Column(value="SERV_NO")
    private String servNo;

    @ApiModelProperty(value = "年份",notes = "NUMBER类型")
    @Column(value = "YEAR")
    private Integer year;

    @ApiModelProperty(value = "月份",notes = "NUMBER类型")
    @Column(value = "MONTH")
    private Integer month;

    @ApiModelProperty(value = "日期",notes = "NUMBER类型")
    @Column(value = "DAY")
    private Integer day;

    @ApiModelProperty(value = "调用总次数",notes = "NUMBER类型")
    @Column(value="TOTAL_TIMES")
    private Double totalTimes;

    @ApiModelProperty(value = "调用成功次数",notes = "NUMBER类型")
    @Column(value = "TOTAL_TIMES_S")
    private Double totalTimesS;

    @ApiModelProperty(value = "调用失败次数",notes = "NUMBER类型")
    @Column(value = "TOTAL_TIMES_F")
    private Double totalTimesF;

    @ApiModelProperty(value = "最大消耗",notes = "NUMBER类型")
    @Column(value = "MAX_COST")
    private Double maxCost;

    @ApiModelProperty(value = "最小消耗",notes = "NUMBER类型")
    @Column(value="MIN_COST")
    private Double minCost;

    @ApiModelProperty(value = "平均消耗",notes = "NUMBER类型")
    @Column(value = "AVG_COST")
    private Double avgCost;

    @ApiModelProperty(value = "最大消息数量",notes = "NUMBER类型")
    @Column(value="MAX_MSG_NUM")
    private Double maxMsgNum;

    @ApiModelProperty(value = "最小消息数量",notes = "NUMBER类型")
    @Column(value = "MIN_MSG_NUM")
    private Double minMsgNum;

    @ApiModelProperty(value = "平均消息数量",notes = "NUMBER类型")
    @Column(value="AVG_MSG_NUM")
    private Double avgMsgNum;

    @ApiModelProperty(value = "最大消息大小",notes = "NUMBER类型")
    @Column(value = "MAX_MSG_SIZE")
    private Double maxMsgSize;

    @ApiModelProperty(value = "最小消息大小",notes = "NUMBER类型")
    @Column(value = "MIN_MSG_SIZE")
    private Double minMsgSize;

    @ApiModelProperty(value = "平均消息大小",notes = "NUMBER类型")
    @Column(value="AVG_MSG_SIZE")
    private Double avgMsgSize;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using=MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "CREATION_DATE")
    private Date creationDate;

    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "CREATED_BY")
    private String createdBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "LAST_UPDATED_BY")
    private String lastUpdatedBy;

    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "OBJECT_VERSION_NUMBER")
    private Integer objectVersionNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Double getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Double totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Double getTotalTimesS() {
        return totalTimesS;
    }

    public void setTotalTimesS(Double totalTimesS) {
        this.totalTimesS = totalTimesS;
    }

    public Double getTotalTimesF() {
        return totalTimesF;
    }

    public void setTotalTimesF(Double totalTimesF) {
        this.totalTimesF = totalTimesF;
    }

    public Double getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Double maxCost) {
        this.maxCost = maxCost;
    }

    public Double getMinCost() {
        return minCost;
    }

    public void setMinCost(Double minCost) {
        this.minCost = minCost;
    }

    public Double getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(Double avgCost) {
        this.avgCost = avgCost;
    }

    public Double getMaxMsgNum() {
        return maxMsgNum;
    }

    public void setMaxMsgNum(Double maxMsgNum) {
        this.maxMsgNum = maxMsgNum;
    }

    public Double getMinMsgNum() {
        return minMsgNum;
    }

    public void setMinMsgNum(Double minMsgNum) {
        this.minMsgNum = minMsgNum;
    }

    public Double getAvgMsgNum() {
        return avgMsgNum;
    }

    public void setAvgMsgNum(Double avgMsgNum) {
        this.avgMsgNum = avgMsgNum;
    }

    public Double getMaxMsgSize() {
        return maxMsgSize;
    }

    public void setMaxMsgSize(Double maxMsgSize) {
        this.maxMsgSize = maxMsgSize;
    }

    public Double getMinMsgSize() {
        return minMsgSize;
    }

    public void setMinMsgSize(Double minMsgSize) {
        this.minMsgSize = minMsgSize;
    }

    public Double getAvgMsgSize() {
        return avgMsgSize;
    }

    public void setAvgMsgSize(Double avgMsgSize) {
        this.avgMsgSize = avgMsgSize;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Integer getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Integer objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
