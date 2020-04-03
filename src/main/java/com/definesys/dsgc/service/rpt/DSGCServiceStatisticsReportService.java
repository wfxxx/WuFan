package com.definesys.dsgc.service.rpt;

import com.definesys.dsgc.service.rpt.bean.*;
import com.definesys.dsgc.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DSGCServiceStatisticsReportService {

    @Autowired
    DSGCServiceStatisticsReportDao serviceStatisticsReportDao;

    public List<RpServTotal> getRunTotalNum(){
        return serviceStatisticsReportDao.getRunTotalNum();
    }

    public List<RpServTotal> getAveragetime(){
        return serviceStatisticsReportDao.getAveragetime();
    }
    public List<RpServTotal> getRate(){
        return serviceStatisticsReportDao.getRate();
    }

    public List<Map<String, Object>> getServMinuteRunData(RpServHour rpServHour){

        List<Map<String, Object>> rpList = serviceStatisticsReportDao.getServMinuteRunData(rpServHour);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = rpServHour.getStartTime();
        String endTime = rpServHour.getEndTime();


        try {
            Date sTime = format.parse(startTime);
            Date eTime = format.parse(endTime);
            Calendar ca1 = Calendar.getInstance();
            Calendar ca2 = Calendar.getInstance();
            ca1.setTime(sTime);
            ca1.set(Calendar.SECOND, 0);
            Date date1 = ca1.getTime();
            ca2.setTime(eTime);
            ca2.set(Calendar.SECOND, 0);
            Date date2 = ca2.getTime();
            while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),":"));
                ca1.add(Calendar.MINUTE,1);
            }
            list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),":"));
            for (String temp :list) {
                int b = check(rpList,temp);
                if(b == -1){
                    Map<String ,Object> map = new HashMap<String ,Object>();
                    map.put("SUM_TOTAL_F",0);
                    map.put("SUM_TOTAL",0);
                    map.put("DATES",temp);
                    map.put("AVG_COST",0);
                    mapList.add(map);
                } else {
                    mapList.add(rpList.get(b));
                }

            }
        } catch (ParseException e) {

        }

        return mapList;
    }

    public List<Map<String, Object>> getServHoursRunData(RpServHour rpServHour){
        List<Map<String, Object>> rpList = serviceStatisticsReportDao.getServHoursRunData(rpServHour);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = "";
        String endTime = "";
        if( rpServHour.getStartTime() !=null && rpServHour.getEndTime() != null){
           startTime = rpServHour.getStartTime();
           endTime = rpServHour.getEndTime();
        }else {
            Date date = new Date();
            endTime = format.format(date);
            Calendar ca = Calendar.getInstance();
            try {
                ca.setTime(format.parse(endTime));
                ca.add(Calendar.HOUR,-24);
                startTime = format.format(ca.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try {
            Date sTime = format.parse(startTime);
            Date eTime = format.parse(endTime);
            Calendar ca1 = Calendar.getInstance();
            Calendar ca2 = Calendar.getInstance();
            ca1.setTime(sTime);
            ca1.set(Calendar.MINUTE, 0);
            ca1.set(Calendar.SECOND, 0);
            Date date1 = ca1.getTime();
            ca2.setTime(eTime);
            ca2.set(Calendar.MINUTE, 0);
            ca2.set(Calendar.SECOND, 0);
            Date date2 = ca2.getTime();
            while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                list.add(StringUtils.substringBefore(format.format(ca1.getTime()),":"));
                ca1.add(Calendar.HOUR,1);
            }
            list.add(StringUtils.substringBefore(format.format(ca2.getTime()),":"));
            for (String temp :list) {
              //  temp = StringUtils.substringAfterLast(temp," ");
               int b = check(rpList,temp);
               if(b == -1){
                   Map<String ,Object> map = new HashMap<String ,Object>();
                 map.put("SUM_TOTAL_F",0);
                  map.put("SUM_TOTAL",0);
                  map.put("DATES",temp);
                  map.put("AVG_COST",0);
                  mapList.add(map);
               } else {
                   mapList.add(rpList.get(b));
               }

            }
        } catch (ParseException e) {

        }
      return mapList;
    }
    public static int check(List<Map<String, Object>> list,String d){
        for (int j = 0;j<list.size();j++) {
            String str =String.valueOf(list.get(j).get("DATES")) ;
            if(str.equals(d)){
                return j;
            }
        }
        return -1;
    }
    public List<Map<String, Object>> getServDayRunData(RpServDay rpServDay){
        List<Map<String, Object>> rpList =serviceStatisticsReportDao.getServDayRunData(rpServDay);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String startTime = rpServDay.getStartTime();
       String endTime = rpServDay.getEndTime();
        try {

            Date sTime = format.parse(startTime);
            Date eTime = format.parse(endTime);
            Calendar ca1 = Calendar.getInstance();
            Calendar ca2 = Calendar.getInstance();
            ca1.setTime(sTime);
            ca1.set(Calendar.HOUR, 0);
            ca1.set(Calendar.MINUTE, 0);
            ca1.set(Calendar.SECOND, 0);
            Date date1 = ca1.getTime();
            ca2.setTime(eTime);
           // ca2.set(Calendar.HOUR_OF_DAY, 0);
            ca2.set(Calendar.HOUR, 0);
            ca2.set(Calendar.MINUTE, 0);
            ca2.set(Calendar.SECOND, 0);
            Date date2 = ca2.getTime();
            while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                list.add(StringUtils.substringBefore(format.format(ca1.getTime())," "));
                ca1.add(Calendar.HOUR,24);
            }
            list.add(StringUtils.substringBefore(format.format(ca2.getTime())," "));
            for (String temp :list) {
                int b = check(rpList,temp);
                if(b == -1){
                    Map<String ,Object> map = new HashMap<String ,Object>();
                    map.put("SUM_TOTAL_F",0);
                    map.put("SUM_TOTAL",0);
                    map.put("DATES",temp);
                    map.put("AVG_COST",0);
                    mapList.add(map);
                } else {
                    mapList.add(rpList.get(b));
                }
            }
        } catch (ParseException e) {
        }

        return mapList;
    }
    public List<Map<String, Object>> getServMonthRunData(RpServMonth rpServMonth){
        List<Map<String, Object>> rpList = serviceStatisticsReportDao.getServMonthRunData(rpServMonth);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = rpServMonth.getStartTime();
        String endTime = rpServMonth.getEndTime();
        try {

            Date sTime = format.parse(startTime);
            Date eTime = format.parse(endTime);
            Calendar ca1 = Calendar.getInstance();
            Calendar ca2 = Calendar.getInstance();
            ca1.setTime(sTime);;
            ca1.set(Calendar.HOUR,0);
            ca1.set(Calendar.MINUTE, 0);
            ca1.set(Calendar.SECOND, 0);
            Date date1 = ca1.getTime();
            ca2.setTime(eTime);
            ca1.set(Calendar.HOUR,0);
            ca2.set(Calendar.MINUTE, 0);
            ca2.set(Calendar.SECOND, 0);
            Date date2 = ca2.getTime();
            String s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");
            String s2 =StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-");
            while (!s1.equals(s2)){

                list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-"));
                ca1.add(Calendar.MONTH,1);
                s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");

            }
            list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-"));

            for (String temp :list) {
                int b = check(rpList,temp);
                if(b == -1){
                    Map<String ,Object> map = new HashMap<String ,Object>();
                    map.put("SUM_TOTAL_F",0);
                    map.put("SUM_TOTAL",0);
                    map.put("DATES",temp);
                    map.put("AVG_COST",0);
                    mapList.add(map);
                } else {
                    mapList.add(rpList.get(b));
                }
            }
        } catch (ParseException e) {
        }
        return mapList;
    }

    public ServiceStaticListVO getAllServDayData(RpServDay rpServDay){
        List<ServicesStaticViewVO> avgCost = serviceStatisticsReportDao.getAllServDayAvgCost(rpServDay);
        List<ServicesStaticViewVO> allServDayRate = serviceStatisticsReportDao.getAllServDayRate(rpServDay);
        List<ServicesStaticViewVO> allServDayTimes = serviceStatisticsReportDao.getAllServDayTimes(rpServDay);
        ServiceStaticListVO serviceStaticListVO = new ServiceStaticListVO();
        serviceStaticListVO.setAvgCostList(avgCost);
        serviceStaticListVO.setFailRateList(allServDayRate);
        serviceStaticListVO.setTotalTimesList(allServDayTimes);
        return serviceStaticListVO;
    }

    public ServiceStaticListVO getAllServMonthData(RpServMonth rpServMonth){
        List<ServicesStaticViewVO> avgCost = serviceStatisticsReportDao.getAllServMonthAvgCost(rpServMonth);
        List<ServicesStaticViewVO> allServDayRate = serviceStatisticsReportDao.getAllServMonthRate(rpServMonth);
        List<ServicesStaticViewVO> allServDayTimes = serviceStatisticsReportDao.getAllServMonthTimes(rpServMonth);
        ServiceStaticListVO serviceStaticListVO = new ServiceStaticListVO();
        serviceStaticListVO.setAvgCostList(avgCost);
        serviceStaticListVO.setFailRateList(allServDayRate);
        serviceStaticListVO.setTotalTimesList(allServDayTimes);
        return serviceStaticListVO;
    }

    public ServiceStaticListVO getAllServHourData(RpServHour rpServHour){
        List<ServicesStaticViewVO> avgCost = serviceStatisticsReportDao.getAllServHourAvgCost(rpServHour);
        List<ServicesStaticViewVO> allServDayRate = serviceStatisticsReportDao.getAllServHourRate(rpServHour);
        List<ServicesStaticViewVO> allServDayTimes = serviceStatisticsReportDao.getAllServHourTimes(rpServHour);
        ServiceStaticListVO serviceStaticListVO = new ServiceStaticListVO();
        serviceStaticListVO.setAvgCostList(avgCost);
        serviceStaticListVO.setFailRateList(allServDayRate);
        serviceStaticListVO.setTotalTimesList(allServDayTimes);
        return serviceStaticListVO;
    }

    public ServiceStaticListVO getAllServYearData(RpServYear rpServYear){
        List<ServicesStaticViewVO> result = serviceStatisticsReportDao.getAllServYearAvgCost(rpServYear);
        return null;
    }

    /**
     * 查询所有的服务编号
     * @return
     */
    public List<Map<String,Object>> getAllServername(){
        return serviceStatisticsReportDao.getAllServerName();
    }



}
