package com.definesys.dsgc.service.ystar.report.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

import java.io.Serializable;
import java.util.Date;

@SQLQuery(value = {
        @SQL(view = "RPT_LOG_VIEW", sql = " select o.TOKEN,l.START_TIME REQ_TIME,o.serv_no as SVC_CODE,o.serv_name as SVC_NAME,o.REQ_FROM as SYS_CODE,\n" +
                "(select e.SYS_NAME from dsgc_system_entities e where e.SYS_CODE = o.REQ_FROM) as SYS_NAME,o.SEND_TO as TRG_SYS_CODE,\n" +
                "(select e.SYS_NAME from dsgc_system_entities e where e.SYS_CODE = o.SEND_TO) as TRG_SYS_NAME," +
                "(o.RES_TIME-o.REQ_TIME) as TRG_COST,(l.END_TIME-l.START_TIME) as TOTAL_COST,(l.END_TIME-l.START_TIME) - (o.RES_TIME-o.REQ_TIME) as ESB_COST,\n" +
                "o.STATUS,l.CLIENT_IP as REQ_IP from dsgc_log_outbound o\n" +
                "LEFT JOIN dsgc_log_instance l on o.TRACK_ID = l.TRACK_ID ")
})
public class ReportLogVO implements Serializable {
    @Column(value = "TOKEN")
    private String token;
    @Column(value = "REQ_TIME")
    private Date reqTime;
    @Column(value = "SVC_CODE")
    private String svcCode;
    @Column(value = "SVC_NAME")
    private String svcName;
    @Column(value = "SYS_CODE")
    private String sysCode;
    @Column(value = "TRG_SYS_CODE")
    private String trgSysCode;
    @Column(value = "REQ_IP")
    private String reqIp;
    @Column(value = "STATUS")
    private String status;
    @Column(value = "DATE")
    private Date date;
    @Column(value = "TOTAL_COST")
    private Double totalCost;
    @Column(value = "ESB_COST")
    private Double esbCost;
    @Column(value = "TRG_CODE")
    private Double trgCost;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    public String getSvcCode() {
        return svcCode;
    }

    public void setSvcCode(String svcCode) {
        this.svcCode = svcCode;
    }

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getTrgSysCode() {
        return trgSysCode;
    }

    public void setTrgSysCode(String trgSysCode) {
        this.trgSysCode = trgSysCode;
    }

    public String getReqIp() {
        return reqIp;
    }

    public void setReqIp(String reqIp) {
        this.reqIp = reqIp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getEsbCost() {
        return esbCost;
    }

    public void setEsbCost(Double esbCost) {
        this.esbCost = esbCost;
    }

    public Double getTrgCost() {
        return trgCost;
    }

    public void setTrgCost(Double trgCost) {
        this.trgCost = trgCost;
    }
}