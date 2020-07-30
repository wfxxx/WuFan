package com.definesys.dsgc.service.apilog.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
@SQLQuery(value = {
        @SQL(view = "have_bizKey_log_view", sql = "select logs.* from (select track_id from dsgc_log_bizkey biz where biz.bus_serv_no = #apiCode and (biz.Col1 = #COL1 OR biz.Col2 = #COL2 OR biz.Col3 = #COL3 OR biz.Col4 = #COL4 OR biz.Col5 = #COL5 OR biz.Col6 = #COL6 OR biz.Col7 = #COL7 OR biz.Col8 = #COL8 OR biz.Col9 = #COL9 OR biz.Col10 = #COLTEN)) t_id, dag_log_instance logs where logs.track_id = t_id.track_id"),
})
@Table(value = "DAG_LOG_INSTANCE")
public class DagLogInstanceBean extends MpaasBasePojo implements Serializable {
    @RowID(type = RowIDType.UUID)
    private String trackId;
    private String apiCode;
    private String csmCode;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    private Date startTime;
    private String clientIp;
    private String httpMethod;
    private String resCode;
    private Integer costTime;
    private Integer bsCost;
    private String bsCode;
    private String routeCode;
    private String reqMsgSize;
    private String resMsgSize;
    private String ibUri;
    private String reqContentType;
    private String resContentType;
    private String reqHeaders;
    private String resHeaders;
    private String extraAttr1;
    private String extraAttr2;
    private String extraAttr3;
    private String extraAttr4;
    private String extraAttr5;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    private Date creationDate;


    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public String getCsmCode() {
        return csmCode;
    }

    public void setCsmCode(String csmCode) {
        this.csmCode = csmCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public Integer getCostTime() {
        return costTime;
    }

    public void setCostTime(Integer costTime) {
        this.costTime = costTime;
    }

    public Integer getBsCost() {
        return bsCost;
    }

    public void setBsCost(Integer bsCost) {
        this.bsCost = bsCost;
    }

    public String getBsCode() {
        return bsCode;
    }

    public void setBsCode(String bsCode) {
        this.bsCode = bsCode;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getReqMsgSize() {
        return reqMsgSize;
    }

    public void setReqMsgSize(String reqMsgSize) {
        this.reqMsgSize = reqMsgSize;
    }

    public String getResMsgSize() {
        return resMsgSize;
    }

    public void setResMsgSize(String resMsgSize) {
        this.resMsgSize = resMsgSize;
    }


    public String getIbUri() {
        return ibUri;
    }

    public void setIbUri(String ibUri) {
        this.ibUri = ibUri;
    }

    public String getReqContentType() {
        return reqContentType;
    }

    public void setReqContentType(String reqContentType) {
        this.reqContentType = reqContentType;
    }

    public String getResContentType() {
        return resContentType;
    }

    public void setResContentType(String resContentType) {
        this.resContentType = resContentType;
    }

    public String getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(String reqHeaders) {
        this.reqHeaders = reqHeaders;
    }

    public String getResHeaders() {
        return resHeaders;
    }

    public void setResHeaders(String resHeaders) {
        this.resHeaders = resHeaders;
    }

    public String getExtraAttr1() {
        return extraAttr1;
    }

    public void setExtraAttr1(String extraAttr1) {
        this.extraAttr1 = extraAttr1;
    }

    public String getExtraAttr2() {
        return extraAttr2;
    }

    public void setExtraAttr2(String extraAttr2) {
        this.extraAttr2 = extraAttr2;
    }

    public String getExtraAttr3() {
        return extraAttr3;
    }

    public void setExtraAttr3(String extraAttr3) {
        this.extraAttr3 = extraAttr3;
    }

    public String getExtraAttr4() {
        return extraAttr4;
    }

    public void setExtraAttr4(String extraAttr4) {
        this.extraAttr4 = extraAttr4;
    }

    public String getExtraAttr5() {
        return extraAttr5;
    }

    public void setExtraAttr5(String extraAttr5) {
        this.extraAttr5 = extraAttr5;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString() {
        return "DagLogInstanceBean{" +
                "trackId='" + trackId + '\'' +
                ", apiCode='" + apiCode + '\'' +
                ", csmCode='" + csmCode + '\'' +
                ", startTime='" + startTime + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", resCode='" + resCode + '\'' +
                ", costTime=" + costTime +
                ", bsCost=" + bsCost +
                ", bsCode='" + bsCode + '\'' +
                ", routeCode='" + routeCode + '\'' +
                ", reqMsgSize='" + reqMsgSize + '\'' +
                ", resMsgSize='" + resMsgSize + '\'' +
                ", ibUri='" + ibUri + '\'' +
                ", reqContentType='" + reqContentType + '\'' +
                ", resContentType='" + resContentType + '\'' +
                ", reqHeaders='" + reqHeaders + '\'' +
                ", resHeaders='" + resHeaders + '\'' +
                ", extraAttr1='" + extraAttr1 + '\'' +
                ", extraAttr2='" + extraAttr2 + '\'' +
                ", extraAttr3='" + extraAttr3 + '\'' +
                ", extraAttr4='" + extraAttr4 + '\'' +
                ", extraAttr5='" + extraAttr5 + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
