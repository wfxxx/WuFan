package com.definesys.dsgc.service.rpt;

import com.definesys.dsgc.service.rpt.bean.*;
import com.definesys.dsgc.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DSGCSystemStatisticsReportService {

    @Autowired
    DSGCSystemStatisticsReportDao systemStatisticsReportDao;

    public List<RpSysTotal> getSysRunTotalNum(){
        return systemStatisticsReportDao.getSysRunTotalNum();
    }

    public List<RpSysTotal> getSysAveragetime(){
        return systemStatisticsReportDao.getSysAveragetime();
    }
    public List<RpSysTotal> getSysRate(){
        return systemStatisticsReportDao.getSysRate();
    }

    public List<Map<String, Object>> getSysMinuteRunData(RpSysHour rpSysHour){

        List<Map<String, Object>> rpList = systemStatisticsReportDao.getSysMinuteRunData(rpSysHour);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = rpSysHour.getStartTime();
        String endTime = rpSysHour.getEndTime();


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

    public static int check(List<Map<String, Object>> list,String d){
        for (int j = 0;j<list.size();j++) {
            String str =String.valueOf(list.get(j).get("DATES")) ;
            if(str.equals(d)){
                return j;
            }
        }
        return -1;
    }

    public List<Map<String, Object>> getSysHoursRunData(RpSysHour rpSysHour){
        List<Map<String, Object>> rpList = systemStatisticsReportDao.getSysHoursRunData(rpSysHour);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = "";
        String endTime = "";
        if( rpSysHour.getStartTime() !=null && rpSysHour.getEndTime() != null){
            startTime = rpSysHour.getStartTime();
            endTime = rpSysHour.getEndTime();
        } else if(rpSysHour.getStartTime() !=null && rpSysHour.getEndTime() == null){
            startTime = rpSysHour.getStartTime();
            Calendar ca = Calendar.getInstance();
            try {
                ca.setTime(format.parse(startTime));
                ca.add(Calendar.HOUR,24);
                endTime = format.format(ca.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if(rpSysHour.getStartTime() ==null && rpSysHour.getEndTime() != null){
            endTime = rpSysHour.getEndTime();
            Calendar ca = Calendar.getInstance();
            try {
                ca.setTime(format.parse(endTime));
                ca.add(Calendar.HOUR,-24);
                startTime = format.format(ca.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
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

    public List<Map<String, Object>> getSysDayRunData(RpSysDay rpSysDay){
        List<Map<String, Object>> rpList =systemStatisticsReportDao.getSysDayRunData(rpSysDay);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = rpSysDay.getStartTime();
        String endTime = rpSysDay.getEndTime();
        try {

            Date sTime = format.parse(startTime);
            Date eTime = format.parse(endTime);
            Calendar ca1 = Calendar.getInstance();
            Calendar ca2 = Calendar.getInstance();
            ca1.setTime(sTime);
            ca1.set(Calendar.HOUR_OF_DAY, 0);
            ca1.set(Calendar.MINUTE, 0);
            ca1.set(Calendar.SECOND, 0);
            Date date1 = ca1.getTime();
            ca2.setTime(eTime);
            ca2.set(Calendar.HOUR_OF_DAY, 0);
            ca2.add(Calendar.DAY_OF_MONTH, 1);
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

    public List<Map<String, Object>> getSysMonthRunData(RpSysMonth rpSysMonth){
        List<Map<String, Object>> rpList = systemStatisticsReportDao.getSysMonthRunData(rpSysMonth);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = rpSysMonth.getStartTime();
        String endTime = rpSysMonth.getEndTime();
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







    public List<Map<String, Object>> getReqSysHoursRunData(RpSysHour rpSysHour){
        List<Map<String, Object>> rpList = systemStatisticsReportDao.getReqSysHoursRunData(rpSysHour);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = "";
        String endTime = "";
        if( rpSysHour.getStartTime() !=null && rpSysHour.getEndTime() != null){
            startTime = rpSysHour.getStartTime();
            endTime = rpSysHour.getEndTime();
        }else if(rpSysHour.getStartTime() !=null && rpSysHour.getEndTime() == null){
            startTime = rpSysHour.getStartTime();
            Calendar ca = Calendar.getInstance();
            try {
                ca.setTime(format.parse(startTime));
                ca.add(Calendar.HOUR,24);
                endTime = format.format(ca.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if(rpSysHour.getStartTime() ==null && rpSysHour.getEndTime() != null){
            endTime = rpSysHour.getEndTime();
            Calendar ca = Calendar.getInstance();
            try {
                ca.setTime(format.parse(endTime));
                ca.add(Calendar.HOUR,-24);
                startTime = format.format(ca.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
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

    public List<Map<String, Object>> getReqSysDayRunData(RpSysDay rpSysDay){
        List<Map<String, Object>> rpList =systemStatisticsReportDao.getReqSysDayRunData(rpSysDay);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = rpSysDay.getStartTime();
        String endTime = rpSysDay.getEndTime();
        try {

            Date sTime = format.parse(startTime);
            Date eTime = format.parse(endTime);
            Calendar ca1 = Calendar.getInstance();
            Calendar ca2 = Calendar.getInstance();
            ca1.setTime(sTime);
            ca1.set(Calendar.HOUR_OF_DAY, 0);
            ca1.set(Calendar.MINUTE, 0);
            ca1.set(Calendar.SECOND, 0);
            Date date1 = ca1.getTime();
            ca2.setTime(eTime);
            ca2.set(Calendar.HOUR_OF_DAY, 0);
            ca2.add(Calendar.DAY_OF_MONTH, 1);
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

    public List<Map<String, Object>> getReqSysMonthRunData(RpSysMonth rpSysMonth){
        List<Map<String, Object>> rpList = systemStatisticsReportDao.getReqSysMonthRunData(rpSysMonth);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = rpSysMonth.getStartTime();
        String endTime = rpSysMonth.getEndTime();
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

    public SystemReportDataListVO getAllSysDayData(RpSysDay rpSysDay){
        List<SystemReportDataViewVO> avgCost = systemStatisticsReportDao.getAllSysDayAvgCost(rpSysDay);
        List<SystemReportDataViewVO> allSysDayRate = systemStatisticsReportDao.getAllSysDayRate(rpSysDay);
        List<SystemReportDataViewVO> allSysDayTimes = systemStatisticsReportDao.getAllSysDayTimes(rpSysDay);
        SystemReportDataListVO serviceStaticListVO = new SystemReportDataListVO();
        serviceStaticListVO.setAvgCostList(avgCost);
        serviceStaticListVO.setFailRateList(allSysDayRate);
        serviceStaticListVO.setTotalTimesList(allSysDayTimes);
        return serviceStaticListVO;
    }

    public SystemReportDataListVO getAllSysMonthData(RpSysMonth rpSysMonth){
        List<SystemReportDataViewVO> avgCost = systemStatisticsReportDao.getAllSysMonthAvgCost(rpSysMonth);
        List<SystemReportDataViewVO> allSysDayRate = systemStatisticsReportDao.getAllSysMonthRate(rpSysMonth);
        List<SystemReportDataViewVO> allSysDayTimes = systemStatisticsReportDao.getAllSysMonthTimes(rpSysMonth);
        SystemReportDataListVO serviceStaticListVO = new SystemReportDataListVO();
        serviceStaticListVO.setAvgCostList(avgCost);
        serviceStaticListVO.setFailRateList(allSysDayRate);
        serviceStaticListVO.setTotalTimesList(allSysDayTimes);
        return serviceStaticListVO;
    }

    public SystemReportDataListVO getAllSysHourData(RpSysHour rpSysHour){
        List<SystemReportDataViewVO> avgCost = systemStatisticsReportDao.getAllSysHourAvgCost(rpSysHour);
        List<SystemReportDataViewVO> allSysDayRate = systemStatisticsReportDao.getAllSysHourRate(rpSysHour);
        List<SystemReportDataViewVO> allSysDayTimes = systemStatisticsReportDao.getAllSysHourTimes(rpSysHour);
        SystemReportDataListVO serviceStaticListVO = new SystemReportDataListVO();
        serviceStaticListVO.setAvgCostList(avgCost);
        serviceStaticListVO.setFailRateList(allSysDayRate);
        serviceStaticListVO.setTotalTimesList(allSysDayTimes);
        return serviceStaticListVO;
    }
}
