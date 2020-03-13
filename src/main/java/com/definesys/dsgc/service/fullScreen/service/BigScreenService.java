package com.definesys.dsgc.service.fullScreen.service;


import com.definesys.dsgc.service.fullScreen.dao.BigScreenDao;
import com.definesys.dsgc.service.fullScreen.util.MyFactory;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author qirenchao E-mail:3063973019@qq.com
 * @version 创建时间：2020/1/9 15:04
 */
@Service
public class BigScreenService {

    @Autowired
    BigScreenDao bigScreenDao;

//    @Autowired
//    FndPropertiesDao propertiesDao;

    //获取公司信息
//    public JSONObject getCompanyInfo() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("onLineTime", propertiesDao.findFndPropertiesByKey("ESB_ONLINE_DAY").getPropertyValue());
//        jsonObject.put("totalTimes", bigScreenDao.getInvokeTimes().get("TOTAL_TIMES"));
//        return jsonObject;
//    }

    //实时指标
    public JSONObject getRealTime() {
        JSONObject jsonObject = new JSONObject();
        long ong = System.currentTimeMillis();
        Date date = new Date(ong);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Map<String, Object> realTimeObj = bigScreenDao.getRealTime();
        Object value = realTimeObj.get("TOTAL_TIMES");
        jsonObject.put("minCost", realTimeObj.get("MIN_COST")==null?0:realTimeObj.get("MIN_COST"));
        jsonObject.put("maxCost", realTimeObj.get("MAX_COST")==null?0:realTimeObj.get("MAX_COST"));
        jsonObject.put("totalTimes", realTimeObj.get("TOTAL_TIMES")==null?0:realTimeObj.get("TOTAL_TIMES"));
        jsonObject.put("totalTimesS", realTimeObj.get("TOTAL_TIMES_S")==null?0:realTimeObj.get("TOTAL_TIMES_S"));
        jsonObject.put("avgCost", realTimeObj.get("AVG_COST")==null?0:realTimeObj.get("AVG_COST"));
        jsonObject.put("Systemdate",dateFormat.format(date));
        return jsonObject;
    }

    //服务实例
    public JSONObject getServiceInstance() {
        //初始化map
        Map<String, Object> invokeTimes = null;
        JSONObject jsonObject=null;
        try {
            invokeTimes = bigScreenDao.getInvokeTimes();
            jsonObject= new JSONObject();
            jsonObject.put("totalQuantity", invokeTimes.get("TOTAL_TIMES")==null?0:invokeTimes.get("TOTAL_TIMES"));
            jsonObject.put("successQuantity", invokeTimes.get("TOTAL_TIMES_S")==null?0:invokeTimes.get("TOTAL_TIMES_S"));
            jsonObject.put("failureQuantity", invokeTimes.get("TOTAL_TIMES_F")==null?0:invokeTimes.get("TOTAL_TIMES_F"));
            return jsonObject;
        }catch (NullPointerException e){
            jsonObject.put("totalQuantity", 0);
            jsonObject.put("successQuantity",0 );
            jsonObject.put("failureQuantity",0 );
          return jsonObject;
        }
    }

    //服务资产
    public List<Map<String, Object>> getServiceAsset() {
        List<Map<String, Object>>  serviceAssets = bigScreenDao.getServiceAssetsCount();
        return serviceAssets;
    }

    //服务通知
    public JSONObject getWorkNotice() {
        List<Map<String, Object>> serviceAssetsList = bigScreenDao.getWorkNotice();
        JSONObject jsonObject = new JSONObject();
        if (serviceAssetsList != null) {
            for (Map<String, Object> serviceAssetsMap : serviceAssetsList) {
                Object mnType = serviceAssetsMap.get("MN_TYPE");
                Object mnCount = serviceAssetsMap.get("MN_COUNT");
                if ("SERV_ALERT".equals(String.valueOf(mnType))) {
                    mnType = "warningQuantity";
                } else if ("SERV_EXCPT".equals(String.valueOf(mnType))) {
                    mnType = "noticeQuantity";
                } else if ("SERV_RTNF".equals(String.valueOf(mnType))) {
                    mnType = "errorQuantity";
                }
                jsonObject.put(mnType, mnCount);
            }
        }
        return jsonObject;
    }

    //服务分布
    public List<JSONObject> getServiceDistribution() {
        List<Map<String, Object>> serviceDistributionList = bigScreenDao.getServiceDistribution();
        List<JSONObject> rtnList = new ArrayList<>();
        if (serviceDistributionList != null) {
            String[] colorArr = MyFactory.getRandomColorCode(serviceDistributionList.size());
            for (int i = 0; i < serviceDistributionList.size(); i++) {
                Map<String, Object> serviceDistributionMap = serviceDistributionList.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", serviceDistributionMap.get("SYS_CODE"));
                jsonObject.put("value", serviceDistributionMap.get("SERVICE_COUNT"));
                JSONObject obj = new JSONObject();
                obj.put("color", colorArr[i]);
                jsonObject.put("itemStyle", obj);
                rtnList.add(jsonObject);
            }
        }
        return rtnList;
    }

    //服务执行次数小时统计表（当天）
    public List<JSONObject> getCurDayExecuteTimes() {
        List<Map<String, Object>> curDayExecuteTimesList = bigScreenDao.getCurDayExecuteTimes();
        List<JSONObject> rtnList = new ArrayList<>();
        if (curDayExecuteTimesList != null) {
            //24小时每个点都得有数据，没有则传0
            for (int i = 0; i <= 24; i++) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MINUTE,00);
                cal.set(Calendar.SECOND,00);
                cal.add(Calendar.HOUR_OF_DAY, - i);
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String DateTime = sdFormat.format(cal.getTime());
                Object count = 0;
                for (Map<String, Object> curDayExecuteTimesMap : curDayExecuteTimesList) {
                    Object hour = curDayExecuteTimesMap.get("TIME");
                    if ((DateTime).equals(String.valueOf(hour))) {
                        count = curDayExecuteTimesMap.get("COUNT");
                    }
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("date", DateTime);
                jsonObject.put("value", count);
                rtnList.add(jsonObject);
            }
        }
        return rtnList;
    }

    //服务执行次数小时统计表（一周）
    public List<JSONObject> getCurWeekExecuteTimes() {
        List<Map<String, Object>> curWeekExecuteTimesList = bigScreenDao.getCurWeekExecuteTimes();
        List<JSONObject> rtnList = new ArrayList<>();
        if (curWeekExecuteTimesList != null) {
            Calendar calendar = Calendar.getInstance();
            int curDay = calendar.get(Calendar.DAY_OF_YEAR);//当天日志
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
            int lastWeekDay = calendar.get(Calendar.DAY_OF_YEAR);//7天前日期
            String[] dayArr = MyFactory.getIntervalDay(7);
            //7天每个点都得有数据，没有则传0
            for (String day : dayArr) {
                Object count = 0;
                for (Map<String, Object> curWeekExecuteTimesMap : curWeekExecuteTimesList) {
                    Object hour = curWeekExecuteTimesMap.get("DAY");
                    if (day.equals(String.valueOf(hour))) {
                        count = curWeekExecuteTimesMap.get("COUNT");
                    }
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("date", day);
                jsonObject.put("value", count);
                rtnList.add(jsonObject);
            }
        }
        return rtnList;
    }



    //获取服务器状态信息
    public List<JSONObject> getServerStatus() {
        List<Map<String, Object>> serverStatusInfoList = bigScreenDao.getServerStatusInfo();
        List<JSONObject> rtnList = new ArrayList<>();
        if (serverStatusInfoList != null) {
            for (Map<String, Object> serverStatusInfoMap : serverStatusInfoList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", serverStatusInfoMap.get("SERVER_NAME"));
                jsonObject.put("status", "RUNNING".equals(serverStatusInfoMap.get("SERVER_STATUS")) ? "normal" : "except");
                rtnList.add(jsonObject);
            }
        }
        return rtnList;
    }


    //失败top5
    public List<Map<String,Object>> getFailTop5(){
        List<Map<String,Object>> list = new ArrayList<>();
        list = bigScreenDao.getFailureTop5();
        return list;
    }

    //调用成功top5
    public List<Map<String, Object>> getTotalTop5() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = bigScreenDao.getTotalTop5();
        return list;
    }




    }


