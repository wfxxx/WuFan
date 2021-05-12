package com.definesys.dsgc.service.rpt.bean;

import java.util.List;

/**
 * @program: DSGCService
 * @description: 包含服务报表查询所有数据
 * @author: firename
 * @date: 2019-09-04
 */
public class ServiceStaticListVO {
    private List<ServicesStaticViewVO> failRateList;
    private List<ServicesStaticViewVO> avgCostList;
    private List<ServicesStaticViewVO> totalTimesList;

    public ServiceStaticListVO() {
    }

    public List<ServicesStaticViewVO> getFailRateList() {
        return failRateList;
    }

    public void setFailRateList(List<ServicesStaticViewVO> failRateList) {
        this.failRateList = failRateList;
    }

    public List<ServicesStaticViewVO> getAvgCostList() {
        return avgCostList;
    }

    public void setAvgCostList(List<ServicesStaticViewVO> avgCostList) {
        this.avgCostList = avgCostList;
    }

    public List<ServicesStaticViewVO> getTotalTimesList() {
        return totalTimesList;
    }

    public void setTotalTimesList(List<ServicesStaticViewVO> totalTimesList) {
        this.totalTimesList = totalTimesList;
    }

    @Override
    public String toString() {
        return "ServiceStaticListVO{" +
                "failRateList=" + failRateList +
                ", avgCostList=" + avgCostList +
                ", totalTimesList=" + totalTimesList +
                '}';
    }
}
