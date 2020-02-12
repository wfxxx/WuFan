package com.definesys.dsgc.service.rpt;

import com.definesys.dsgc.service.rpt.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ServiceStatisticsService {
    @Autowired
    ServiceStatisticsDao serviceStatisticsDao;

    public List<RpServTotal> getServiceCount(){
        return serviceStatisticsDao.getServiceCount();
    }

    public List<RpSysTotal> getSystemCount(){
        return serviceStatisticsDao.getSystemCount();
    }

    public List<RpServHour> getInterfaceDetailsReportDay(String servNo){
        return serviceStatisticsDao.getInterfaceDetailsReportDay(servNo);
    }

    public List<RpServDay> getInterfaceDetailsReportMonth(String servNo){
        return serviceStatisticsDao.getInterfaceDetailsReportMonth(servNo);
    }

    public List<RpServMonth> getInterfaceDetailsReportYear(String servNo){
        return serviceStatisticsDao.getInterfaceDetailsReportYear(servNo);
    }

    public List<RpServYear> getInterfaceDetailsReport(String servNo){
        return serviceStatisticsDao.getInterfaceDetailsReport(servNo);
    }

    public List<RpSysHour> getSystemDetailsReportDay(String receive){
        return serviceStatisticsDao.getSystemDetailsReportDay(receive);
    }

    public List<RpSysDay> getSystemDetailsReportMonth(String receive){
        return serviceStatisticsDao.getSystemDetailsReportMonth(receive);
    }

    public List<RpSysMonth> getSystemDetailsReportYear(String receive){
        return serviceStatisticsDao.getSystemDetailsReportYear(receive);
    }

    public List<RpSysYear> getSystemDetailsReport(String receive){
        return serviceStatisticsDao.getSystemDetailsReport(receive);
    }

    public List<String> getSystemGroup(){
        List<Map<String,Object>> result=serviceStatisticsDao.getSystemGroup();
        List<String> systemGroup=new ArrayList();
        for(Map<String,Object> item : result){
            systemGroup.add((String)item.get("receive"));
        }
        return systemGroup;
    }
}
