package com.definesys.dsgc.service.ystar.report.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

import java.io.Serializable;

@SQLQuery(value = {
        @SQL(view = "RPT_YEAR_VIEW", sql = Const.CMN_QUERY_REPORT_SQL )
})
public class ReportYearVO implements Serializable {
    @Column(value = "SYS_CODE")
    private String sysCode;
    @Column(value = "TRG_SYS_CODE")
    private String trgSysCode;
    @Column(value = "SVC_CODE")
    private String svcCode;
    @Column(value = "SVC_NAME")
    private String svcName;
    @Column(value = "YEAR")
    private Integer year;
    @Column(value = "MONTH")
    private Integer month;
    @Column(value = "TOTAL_TIMES")
    private Double totalTimes;
    @Column(value = "AVG_COST")
    private Double avgCost;
    @Column(value = "ESB_AVG_COST")
    private Double esbAvgCost;
    @Column(value = "TRG_AVG_COST")
    private Double trgAvgCost;


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

    public Double getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Double totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Double getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(Double avgCost) {
        this.avgCost = avgCost;
    }

    public Double getEsbAvgCost() {
        return esbAvgCost;
    }

    public void setEsbAvgCost(Double esbAvgCost) {
        this.esbAvgCost = esbAvgCost;
    }

    public Double getTrgAvgCost() {
        return trgAvgCost;
    }

    public void setTrgAvgCost(Double trgAvgCost) {
        this.trgAvgCost = trgAvgCost;
    }
}
