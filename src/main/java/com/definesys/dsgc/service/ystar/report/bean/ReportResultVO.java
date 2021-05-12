package com.definesys.dsgc.service.ystar.report.bean;

import java.util.List;

public class ReportResultVO {
    String svcCode;
    String type;
    double totalTimes;
    double totalCost;
    double totalEsbCost;
    double totalTrgCost;
    double avgCost;
    double avgEsbCost;
    double avgTrgCost;
    double maxDate; //最大月份
    List<ReportDayVO> reportDayList;
    List<ReportMonthVO> reportMonthList;
    List<ReportYearVO> reportYearList;

    public double getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(double maxDate) {
        this.maxDate = maxDate;
    }

    public String getSvcCode() {
        return svcCode;
    }

    public void setSvcCode(String svcCode) {
        this.svcCode = svcCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(double totalTimes) {
        this.totalTimes = totalTimes;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalEsbCost() {
        return totalEsbCost;
    }

    public void setTotalEsbCost(double totalEsbCost) {
        this.totalEsbCost = totalEsbCost;
    }

    public double getTotalTrgCost() {
        return totalTrgCost;
    }

    public void setTotalTrgCost(double totalTrgCost) {
        this.totalTrgCost = totalTrgCost;
    }

    public double getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(double avgCost) {
        this.avgCost = avgCost;
    }

    public double getAvgEsbCost() {
        return avgEsbCost;
    }

    public void setAvgEsbCost(double avgEsbCost) {
        this.avgEsbCost = avgEsbCost;
    }

    public double getAvgTrgCost() {
        return avgTrgCost;
    }

    public void setAvgTrgCost(double avgTrgCost) {
        this.avgTrgCost = avgTrgCost;
    }

    public List<ReportDayVO> getReportDayList() {
        return reportDayList;
    }

    public void setReportDayList(List<ReportDayVO> reportDayList) {
        this.reportDayList = reportDayList;
    }

    public List<ReportMonthVO> getReportMonthList() {
        return reportMonthList;
    }

    public void setReportMonthList(List<ReportMonthVO> reportMonthList) {
        this.reportMonthList = reportMonthList;
    }

    public List<ReportYearVO> getReportYearList() {
        return reportYearList;
    }

    public void setReportYearList(List<ReportYearVO> reportYearList) {
        this.reportYearList = reportYearList;
    }
}
