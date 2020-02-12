package com.definesys.dsgc.service.rpt;



import com.definesys.dsgc.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DSGCMachineReportService {

    @Autowired
    @Lazy
    private DSGCMachineReportDao machineReportDao;
    @Autowired
    private DSGCServiceStatisticsReportService serviceStatisticsReportService;

    public List<Map<String,Object>> getMachineMemMsg(Map map){
        List<Map<String,Object>> mapList = machineReportDao.getMachineMemMsg(map);
        List<Map<String,Object>>  returnList = new ArrayList<Map<String,Object>>();
        List<String> attribute = new ArrayList<String>();
        attribute.add("MEM_USE");
        attribute.add("MEM_FREE");
        attribute.add("MEM_TOTAL");
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca1 = Calendar.getInstance();
        Calendar ca2 = Calendar.getInstance();
        String temp = timeCheck((String )map.get("startTime"),(String )map.get("endTime"));
        try {
            Date sTime = format.parse((String )map.get("startTime"));
            Date eTime = format.parse((String )map.get("endTime"));
            ca1.setTime(sTime);
            ca2.setTime(eTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

       switch (temp){
           case "minute":
               ca1.set(Calendar.SECOND, 0);
               ca2.set(Calendar.SECOND, 0);
               while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                   list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),":"));
                   ca1.add(Calendar.MINUTE,1);
               }
               list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),":"));
               doFilter(list,mapList,returnList,attribute);
               break;
           case "hour":
               ca1.set(Calendar.SECOND, 0);
               ca1.set(Calendar.MINUTE, 0);
               ca2.set(Calendar.MINUTE, 0);
               ca2.set(Calendar.SECOND, 0);
               System.out.println(format.format(ca1.getTime()) +" | "+format.format(ca2.getTime()));
               while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                   list.add(StringUtils.substringBefore(format.format(ca1.getTime()),":"));
                   ca1.add(Calendar.HOUR,1);
               }
               list.add(StringUtils.substringBefore(format.format(ca2.getTime()),":"));
               doFilter(list,mapList,returnList,attribute);
               break;
           case "day":
               ca1.set(Calendar.HOUR, 0);
               ca1.set(Calendar.SECOND, 0);
               ca1.set(Calendar.MINUTE, 0);
               ca2.set(Calendar.HOUR, 0);
               ca2.set(Calendar.SECOND, 0);
               ca2.set(Calendar.MINUTE, 0);
               String str1 =StringUtils.substringBefore(format.format(ca1.getTime())," ");
               String str2 =StringUtils.substringBefore(format.format(ca2.getTime())," ");
               while (!str1.equals(str2)){
                   list.add(StringUtils.substringBefore(format.format(ca1.getTime())," "));
                   ca1.add(Calendar.HOUR,24);
                   str1 =StringUtils.substringBeforeLast(format.format(ca1.getTime())," ");
               }
               list.add(StringUtils.substringBefore(format.format(ca2.getTime())," "));

               doFilter(list,mapList,returnList,attribute);
               break;
           case "month":
               ca1.set(Calendar.SECOND, 0);
               ca1.set(Calendar.MINUTE, 0);
               ca1.set(Calendar.HOUR, 0);
               ca2.set(Calendar.SECOND, 0);
               ca2.set(Calendar.SECOND, 0);
               ca2.set(Calendar.HOUR, 0);
               String s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");
               String s2 =StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-");
               while (!s1.equals(s2)){

                   list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-"));
                   ca1.add(Calendar.MONTH,1);
                   s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");

               }
               list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-"));
               doFilter(list,mapList,returnList,attribute);
               break;
       }
       return returnList;
    }




    public List<Map<String,Object>> getMachineCpuMsg(Map map){
        List<Map<String,Object>> mapList = machineReportDao.getMachineCpuMsg(map);
        List<Map<String,Object>>  returnList = new ArrayList<Map<String,Object>>();
        List<String> list = new ArrayList<String>();
        List<String> attribute = new ArrayList<String>();
        attribute.add("CPU_USAGE");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca1 = Calendar.getInstance();
        Calendar ca2 = Calendar.getInstance();
        String temp = timeCheck((String )map.get("startTime"),(String )map.get("endTime"));
        if(temp == null){
            return returnList;
        }
        try {
            Date sTime = format.parse((String )map.get("startTime"));
            Date eTime = format.parse((String )map.get("endTime"));
            System.out.println(sTime +" | "+ eTime);
            ca1.setTime(sTime);
            ca2.setTime(eTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (temp){
            case "minute":
                ca1.set(Calendar.SECOND, 0);
                ca2.set(Calendar.SECOND, 0);
                while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                    list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),":"));
                    ca1.add(Calendar.MINUTE,1);
                }
                list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),":"));
                doFilter(list,mapList,returnList,attribute);
                break;
            case "hour":
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.SECOND, 0);
                while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                    list.add(StringUtils.substringBefore(format.format(ca1.getTime()),":"));
                    ca1.add(Calendar.HOUR,1);
                }
                list.add(StringUtils.substringBefore(format.format(ca2.getTime()),":"));
                doFilter(list,mapList,returnList,attribute);
                break;
            case "day":
                ca1.set(Calendar.HOUR, 0);
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.HOUR, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.MINUTE, 0);
                String str1 =StringUtils.substringBefore(format.format(ca1.getTime())," ");
                String str2 =StringUtils.substringBefore(format.format(ca2.getTime())," ");
                while (!str1.equals(str2)){
                    list.add(StringUtils.substringBefore(format.format(ca1.getTime())," "));
                    ca1.add(Calendar.HOUR,24);
                    str1 =StringUtils.substringBeforeLast(format.format(ca1.getTime())," ");
                }
                list.add(StringUtils.substringBefore(format.format(ca2.getTime())," "));

                doFilter(list,mapList,returnList,attribute);
                break;
            case "month":
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca1.set(Calendar.HOUR, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.HOUR, 0);
                String s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");
                String s2 =StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-");
                while (!s1.equals(s2)){

                    list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-"));
                    ca1.add(Calendar.MONTH,1);
                    s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");

                }
                list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-"));
                doFilter(list,mapList,returnList,attribute);
                break;
        }
        return returnList;
    }



    public List<Map<String,Object>> getMachineNetMsg(Map map){
        List<Map<String,Object>> mapList = machineReportDao.getMachineNetMsg(map);
        List<Map<String,Object>>  returnList = new ArrayList<Map<String,Object>>();
        List<String> list = new ArrayList<String>();
        List<String> attribute = new ArrayList<String>();
        attribute.add("NET_CUR_SPEC");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca1 = Calendar.getInstance();
        Calendar ca2 = Calendar.getInstance();
        String temp = timeCheck((String )map.get("startTime"),(String )map.get("endTime"));
        if(temp == null){
            return returnList;
        }
        try {
            Date sTime = format.parse((String )map.get("startTime"));
            Date eTime = format.parse((String )map.get("endTime"));
            System.out.println(sTime +" | "+ eTime);
            ca1.setTime(sTime);
            ca2.setTime(eTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (temp){
            case "minute":
                ca1.set(Calendar.SECOND, 0);
                ca2.set(Calendar.SECOND, 0);
                while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                    list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),":"));
                    ca1.add(Calendar.MINUTE,1);
                }
                list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),":"));
                doFilter(list,mapList,returnList,attribute);
                break;
            case "hour":
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.SECOND, 0);
                while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                    list.add(StringUtils.substringBefore(format.format(ca1.getTime()),":"));
                    ca1.add(Calendar.HOUR,1);
                }
                list.add(StringUtils.substringBefore(format.format(ca2.getTime()),":"));
                doFilter(list,mapList,returnList,attribute);
                break;
            case "day":
                ca1.set(Calendar.HOUR, 0);
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.HOUR, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.MINUTE, 0);
                String str1 =StringUtils.substringBefore(format.format(ca1.getTime())," ");
                String str2 =StringUtils.substringBefore(format.format(ca2.getTime())," ");
                while (!str1.equals(str2)){
                    list.add(StringUtils.substringBefore(format.format(ca1.getTime())," "));
                    ca1.add(Calendar.HOUR,24);
                    str1 =StringUtils.substringBeforeLast(format.format(ca1.getTime())," ");
                }
                list.add(StringUtils.substringBefore(format.format(ca2.getTime())," "));

                doFilter(list,mapList,returnList,attribute);
                break;
            case "month":
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca1.set(Calendar.HOUR, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.HOUR, 0);
                String s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");
                String s2 =StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-");
                while (!s1.equals(s2)){

                    list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-"));
                    ca1.add(Calendar.MONTH,1);
                    s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");

                }
                list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-"));
                doFilter(list,mapList,returnList,attribute);
                break;
        }
        return returnList;
    }



    public List<Map<String,Object>> getMachineIoMsg(Map map){
        List<Map<String,Object>> mapList = machineReportDao.getMachineIoMsg(map);
        List<Map<String,Object>>  returnList = new ArrayList<Map<String,Object>>();
        List<String> list = new ArrayList<String>();
        List<String> attribute = new ArrayList<String>();
        attribute.add("IO_USAGE");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca1 = Calendar.getInstance();
        Calendar ca2 = Calendar.getInstance();
        String temp = timeCheck((String )map.get("startTime"),(String )map.get("endTime"));
        if(temp == null){
            return returnList;
        }
        try {
            Date sTime = format.parse((String )map.get("startTime"));
            Date eTime = format.parse((String )map.get("endTime"));
            System.out.println(sTime +" | "+ eTime);
            ca1.setTime(sTime);
            ca2.setTime(eTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (temp){
            case "minute":
                ca1.set(Calendar.SECOND, 0);
                ca2.set(Calendar.SECOND, 0);
                while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                    list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),":"));
                    ca1.add(Calendar.MINUTE,1);
                }
                list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),":"));
                doFilter(list,mapList,returnList,attribute);
                break;
            case "hour":
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.SECOND, 0);
                while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                    list.add(StringUtils.substringBefore(format.format(ca1.getTime()),":"));
                    ca1.add(Calendar.HOUR,1);
                }
                list.add(StringUtils.substringBefore(format.format(ca2.getTime()),":"));
                doFilter(list,mapList,returnList,attribute);
                break;
            case "day":
                ca1.set(Calendar.HOUR, 0);
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.HOUR, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.MINUTE, 0);
                String str1 =StringUtils.substringBefore(format.format(ca1.getTime())," ");
                String str2 =StringUtils.substringBefore(format.format(ca2.getTime())," ");
                while (!str1.equals(str2)){
                    list.add(StringUtils.substringBefore(format.format(ca1.getTime())," "));
                    ca1.add(Calendar.HOUR,24);
                    str1 =StringUtils.substringBeforeLast(format.format(ca1.getTime())," ");
                }
                list.add(StringUtils.substringBefore(format.format(ca2.getTime())," "));

                doFilter(list,mapList,returnList,attribute);
                break;
            case "month":
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca1.set(Calendar.HOUR, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.HOUR, 0);
                String s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");
                String s2 =StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-");
                while (!s1.equals(s2)){

                    list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-"));
                    ca1.add(Calendar.MONTH,1);
                    s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");

                }
                list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-"));
                doFilter(list,mapList,returnList,attribute);
                break;
        }
        return returnList;
    }



    public List<Map<String,Object>> getMachineDiskMsg(Map map){
        List<Map<String,Object>> mapList = machineReportDao.getMachineDiskMsg(map);
        List<Map<String,Object>>  returnList = new ArrayList<Map<String,Object>>();
        List<String> list = new ArrayList<String>();
        List<String> attribute = new ArrayList<String>();
        attribute.add("DISK_USAGE");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca1 = Calendar.getInstance();
        Calendar ca2 = Calendar.getInstance();
        String temp = timeCheck((String )map.get("startTime"),(String )map.get("endTime"));
        if(temp == null){
            return returnList;
        }
        try {
            Date sTime = format.parse((String )map.get("startTime"));
            Date eTime = format.parse((String )map.get("endTime"));
            System.out.println(sTime +" | "+ eTime);
            ca1.setTime(sTime);
            ca2.setTime(eTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (temp){
            case "minute":
                ca1.set(Calendar.SECOND, 0);
                ca2.set(Calendar.SECOND, 0);
                while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                    list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),":"));
                    ca1.add(Calendar.MINUTE,1);
                }
                list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),":"));
                doFilter(list,mapList,returnList,attribute);
                break;
            case "hour":
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.SECOND, 0);
                while (!format.format(ca1.getTime()).equals(format.format(ca2.getTime()))){
                    list.add(StringUtils.substringBefore(format.format(ca1.getTime()),":"));
                    ca1.add(Calendar.HOUR,1);
                }
                list.add(StringUtils.substringBefore(format.format(ca2.getTime()),":"));
                doFilter(list,mapList,returnList,attribute);
                break;
            case "day":
                ca1.set(Calendar.HOUR, 0);
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca2.set(Calendar.HOUR, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.MINUTE, 0);
                String str1 =StringUtils.substringBefore(format.format(ca1.getTime())," ");
                String str2 =StringUtils.substringBefore(format.format(ca2.getTime())," ");
                while (!str1.equals(str2)){
                    list.add(StringUtils.substringBefore(format.format(ca1.getTime())," "));
                    ca1.add(Calendar.HOUR,24);
                    str1 =StringUtils.substringBeforeLast(format.format(ca1.getTime())," ");
                }
                list.add(StringUtils.substringBefore(format.format(ca2.getTime())," "));

                doFilter(list,mapList,returnList,attribute);
                break;
            case "month":
                ca1.set(Calendar.SECOND, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca1.set(Calendar.HOUR, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.SECOND, 0);
                ca2.set(Calendar.HOUR, 0);
                String s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");
                String s2 =StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-");
                while (!s1.equals(s2)){

                    list.add(StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-"));
                    ca1.add(Calendar.MONTH,1);
                    s1 =StringUtils.substringBeforeLast(format.format(ca1.getTime()),"-");

                }
                list.add(StringUtils.substringBeforeLast(format.format(ca2.getTime()),"-"));
                doFilter(list,mapList,returnList,attribute);
                break;
        }
        return returnList;
    }


    public void doFilter(List<String> list,List<Map<String,Object>> mapList,List<Map<String,Object>>  returnList,List<String> attribute){
        for (String s :list) {
            int b =serviceStatisticsReportService.check(mapList,s);
            if(b == -1){
                Map<String ,Object> m = new HashMap<String ,Object>();
                for (String temp: attribute) {
                    m.put(temp,0);
                }
//                m.put("MEM_USE",0);
//                m.put("MEM_FREE",0);
//                m.put("MEM_TOTAL",0);
                m.put("DATES",s);
                returnList.add(m);
            } else {
                returnList.add(mapList.get(b));
            }
        }
    }
    public String timeCheck(String startTime,String endTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sTime;
        Date eTime;
        try {
           sTime = format.parse(startTime);
           eTime = format.parse(endTime);
          long temp = (eTime.getTime() - sTime.getTime())/(1000*60*60);
          System.out.println(temp);
          if(temp < 0){
              return null;
          }
          if(temp <=1){
            return "minute";
          }
          if(temp>1 && temp <=24){
                return "hour";
          }
          if(temp>24 && temp<=720){
              return "day";
          }
            if(temp>720){
                return "month";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
