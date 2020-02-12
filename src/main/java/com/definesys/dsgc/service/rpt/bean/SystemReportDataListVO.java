package com.definesys.dsgc.service.rpt.bean;

import java.util.List;

/**
 * @program: DSGCService
 * @description: 系统报表数据集合VO
 * @author: firename
 * @date: 2019-09-05
 */
public class SystemReportDataListVO {
    private List<SystemReportDataViewVO> failRateList;
    private List<SystemReportDataViewVO> avgCostList;
    private List<SystemReportDataViewVO> totalTimesList;

    public SystemReportDataListVO() {
    }

    public List<SystemReportDataViewVO> getFailRateList() {
        return failRateList;
    }

    public void setFailRateList(List<SystemReportDataViewVO> failRateList) {
        this.failRateList = failRateList;
    }

    public List<SystemReportDataViewVO> getAvgCostList() {
        return avgCostList;
    }

    @Override
    public String toString() {
        return "SystemReportDataListVO{" +
                "failRateList=" + failRateList +
                ", avgCostList=" + avgCostList +
                ", totalTimesList=" + totalTimesList +
                '}';
    }

    public void setAvgCostList(List<SystemReportDataViewVO> avgCostList) {
        this.avgCostList = avgCostList;
    }

    public List<SystemReportDataViewVO> getTotalTimesList() {
        return totalTimesList;
    }

    public void setTotalTimesList(List<SystemReportDataViewVO> totalTimesList) {
        this.totalTimesList = totalTimesList;
    }
}
