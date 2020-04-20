package com.definesys.dsgc.service.esbHome;

import com.definesys.dsgc.service.esbHome.bean.EsbHomeHisto;
import com.definesys.dsgc.service.esbHome.bean.EsbHomeCard;
import com.definesys.dsgc.service.esbHome.bean.EsbHomeHisto;
import com.definesys.dsgc.service.lkv.FndLookupTypeDao;
import com.definesys.dsgc.service.lkv.bean.FndLookupType;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.market.bean.MarketEntiy;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName esbHomeService
 * @Description TODO
 * @Author xiezhongyuan
 * @Date 2020-3-2
 * @Version 1.0
 **/
@Service
public class EsbHomeService {
    @Autowired
    EsbHomeDao esbHomeDao;
    @Autowired
    private SVCLogDao sldao;

    @Autowired
    private FndLookupTypeDao fndLookupTypeDao;

//获取api首页卡片

    public Map<String,Object> queryCardsData(){
        Map<String,Object> result=new HashMap<String,Object>();
        //获取上周总数，获取这周总数，获取昨天总数，获取今天总数; API  应用， 用户  api访问量
        EsbHomeCard esbTotal=new EsbHomeCard();
        EsbHomeHisto lastWeekA= esbHomeDao.getLastWeekTotalA();
        EsbHomeHisto nowWeekA= esbHomeDao.getNowWeekTotalA();
        EsbHomeHisto todayA= esbHomeDao.getTodyTotalA();
        EsbHomeHisto yestodayA= esbHomeDao.getYestodayTotalA();
        EsbHomeHisto totalA= esbHomeDao.getTotalA();
        esbTotal.setDataAdd(todayA.getValue());
        esbTotal.setDayRate(rate(todayA.getValue(),yestodayA.getValue()));
        esbTotal.setTotal(totalA.getValue());
        esbTotal.setWeekRate(rate(nowWeekA.getValue(),lastWeekA.getValue()));
        result.put("esbTotal",esbTotal);
        EsbHomeCard sysTotal=new EsbHomeCard();
        EsbHomeHisto lastWeekE= esbHomeDao.getLastWeekTotalE();
        EsbHomeHisto nowWeekE= esbHomeDao.getNowWeekTotalE();
        EsbHomeHisto todayE= esbHomeDao.getTodyTotalE();
        EsbHomeHisto yestodayE= esbHomeDao.getYestodayTotalE();
        EsbHomeHisto totalE= esbHomeDao.getTotalE();
        sysTotal.setDataAdd(todayE.getValue());
        sysTotal.setDayRate(rate(todayE.getValue(),yestodayE.getValue()));
        sysTotal.setTotal(totalE.getValue());
        sysTotal.setWeekRate(rate(nowWeekE.getValue(),lastWeekE.getValue()));
        result.put("sysTotal",sysTotal);

//        EsbHomeCard failTotal=new EsbHomeCard();
//        EsbHomeHisto lastWeekF= esbHomeDao.getLastWeekTotalF();
//        lastWeekF=lastWeekF==null?new EsbHomeHisto():lastWeekF;
//        EsbHomeHisto nowWeekF= esbHomeDao.getNowWeekTotalF();
//        nowWeekF=nowWeekF==null?new EsbHomeHisto():nowWeekF;
//        EsbHomeHisto todayF= esbHomeDao.getTodyTotalF();
//        todayF=todayF==null?new EsbHomeHisto():todayF;
//        EsbHomeHisto yestodayF= esbHomeDao.getYestodayTotalF();
//        yestodayF=yestodayF==null?new EsbHomeHisto():yestodayF;
//        EsbHomeHisto totalF= esbHomeDao.getTotalF();
//        totalF=totalF==null?new EsbHomeHisto():totalF;
//        failTotal.setDataAdd(todayF.getValue());
//        failTotal.setDayRate(rate(todayF.getValue(),yestodayF.getValue()));
//        failTotal.setTotal(totalF.getValue());
//        failTotal.setWeekRate(rate(nowWeekF.getValue(),lastWeekF.getValue()));
//        result.put("failTotal",failTotal);
//
//        EsbHomeCard esbVisitTotal=new EsbHomeCard();
//        EsbHomeHisto lastWeekV= esbHomeDao.getLastWeekTotalV();
//        lastWeekV=lastWeekV==null?new EsbHomeHisto():lastWeekV;
//        EsbHomeHisto nowWeekV= esbHomeDao.getNowWeekTotalV();
//        nowWeekV=nowWeekV==null?new EsbHomeHisto():nowWeekV;
//        EsbHomeHisto todayV= esbHomeDao.getTodyTotalV();
//        todayV=todayV==null?new EsbHomeHisto():todayV;
//        EsbHomeHisto yestodayV= esbHomeDao.getYestodayTotalV();
//        yestodayV=yestodayV==null?new EsbHomeHisto():yestodayV;
//        EsbHomeHisto totalV= esbHomeDao.getTotalV();
//        totalV=totalV==null?new EsbHomeHisto():totalV;
//        esbVisitTotal.setDataAdd(todayV.getValue());
//        esbVisitTotal.setDayRate(rate(todayV.getValue(),yestodayV.getValue()));
//        esbVisitTotal.setTotal(totalV.getValue());
//        esbVisitTotal.setWeekRate(rate(nowWeekV.getValue(),lastWeekV.getValue()));
//        result.put("esbVisitTotal",esbVisitTotal);




        EsbHomeCard failTotal=new EsbHomeCard();

        Map<String,Object> lastWeekF = esbHomeDao.getLastWeekTotalF();
        Map<String,Object> nowWeekF= esbHomeDao.getNowWeekTotalF();
        if(lastWeekF !=null && lastWeekF.containsKey("VALUE")&& nowWeekF !=null && nowWeekF.containsKey("VALUE")){
            failTotal.setWeekRate(rate(Integer.parseInt(String.valueOf(nowWeekF.get("VALUE"))),Integer.parseInt(String.valueOf(lastWeekF.get("VALUE")))));
        }else if((lastWeekF ==null || !lastWeekF.containsKey("VALUE"))&& nowWeekF !=null && nowWeekF.containsKey("VALUE")){
            failTotal.setWeekRate(rate(Integer.parseInt(String.valueOf(nowWeekF.get("VALUE"))),0));
        }else if(lastWeekF !=null && lastWeekF.containsKey("VALUE")&& (nowWeekF ==null || !nowWeekF.containsKey("VALUE"))){
            failTotal.setWeekRate(rate(0,Integer.parseInt(String.valueOf(lastWeekF.get("VALUE")))));
        }
        else {
            failTotal.setWeekRate(rate(0,0));
        }
        Map<String,Object> todayF= esbHomeDao.getTodyTotalF();
        Map<String,Object> yestodayF= esbHomeDao.getYestodayTotalF();
        if(todayF!=null && todayF.containsKey("VALUE")){
            failTotal.setDataAdd(Integer.parseInt(String.valueOf(todayF.get("VALUE"))));
        }else {
            failTotal.setDataAdd(0);
        }
        if(todayF !=null && todayF.containsKey("VALUE")&& yestodayF !=null && yestodayF.containsKey("VALUE")){
            failTotal.setDayRate(rate(Integer.parseInt(String.valueOf(todayF.get("VALUE"))),Integer.parseInt(String.valueOf(yestodayF.get("VALUE")))));
        }else if((todayF ==null || !todayF.containsKey("VALUE"))&& yestodayF !=null && yestodayF.containsKey("VALUE")){
            failTotal.setDayRate(rate(0,Integer.parseInt(String.valueOf(yestodayF.get("VALUE")))));
        }else if(todayF !=null && todayF.containsKey("VALUE")&& (yestodayF ==null || !yestodayF.containsKey("VALUE"))){
            failTotal.setDayRate(rate(Integer.parseInt(String.valueOf(todayF.get("VALUE"))),0));
        }
        else {
            failTotal.setDayRate(rate(0,0));
        }

        Map<String, Object> totalF= esbHomeDao.getTotalF();
        if(totalF != null && totalF.containsKey("VALUE")){
            failTotal.setTotal(Integer.parseInt(String.valueOf(totalF.get("VALUE"))));
        }else {
            failTotal.setTotal(0);
        }
        result.put("failTotal",failTotal);
        //TODO
        EsbHomeCard esbVisitTotal=new EsbHomeCard();
        Map<String, Object> lastWeekV= esbHomeDao.getLastWeekTotalV();
        Map<String, Object> nowWeekV= esbHomeDao.getNowWeekTotalV();
        if(nowWeekV !=null && nowWeekV.containsKey("VALUE")&& lastWeekV !=null && lastWeekV.containsKey("VALUE")){
            esbVisitTotal.setWeekRate(rate(Integer.parseInt(String.valueOf(nowWeekV.get("VALUE"))),Integer.parseInt(String.valueOf(lastWeekV.get("value")))));
        }else if((nowWeekV ==null || !nowWeekV.containsKey("VALUE"))&& lastWeekV !=null && lastWeekV.containsKey("VALUE")){
            esbVisitTotal.setWeekRate(rate(0,Integer.parseInt(String.valueOf(lastWeekV.get("VALUE")))));
        }else if(nowWeekV !=null && nowWeekV.containsKey("VALUE")&& (lastWeekV ==null || !lastWeekV.containsKey("VALUE"))){
            esbVisitTotal.setWeekRate(rate(Integer.parseInt(String.valueOf(nowWeekV.get("VALUE"))),0));
        }
        else {
            esbVisitTotal.setWeekRate(rate(0,0));
        }

        Map<String, Object> todayV= esbHomeDao.getTodyTotalV();
        Map<String, Object> yestodayV= esbHomeDao.getYestodayTotalV();
        Map<String, Object> totalV= esbHomeDao.getTotalV();

        if(todayV != null && todayV.containsKey("VALUE")){
            esbVisitTotal.setDataAdd(Integer.parseInt(String.valueOf(todayV.get("VALUE"))));
        }else {
            esbVisitTotal.setDataAdd(0);
        }
       // esbVisitTotal.setDataAdd(todayV.getValue());
        if(todayV !=null && todayV.containsKey("VALUE")&& yestodayV !=null && yestodayV.containsKey("VALUE")){
            esbVisitTotal.setDayRate(rate(Integer.parseInt(String.valueOf(todayV.get("VALUE"))),Integer.parseInt(String.valueOf(yestodayV.get("value")))));
        }else if((todayV ==null || !todayV.containsKey("VALUE"))&& yestodayV !=null && yestodayV.containsKey("VALUE")){
            esbVisitTotal.setDayRate(rate(0,Integer.parseInt(String.valueOf(yestodayV.get("VALUE")))));
        }else if(todayV !=null && todayV.containsKey("VALUE")&& (yestodayV ==null || !yestodayV.containsKey("VALUE"))){
            esbVisitTotal.setDayRate(rate(Integer.parseInt(String.valueOf(todayV.get("VALUE"))),0));
        }
        else {
            esbVisitTotal.setDayRate(rate(0,0));
        }
        if(totalV != null && totalV.containsKey("VALUE")){
            esbVisitTotal.setTotal(Integer.parseInt(String.valueOf(totalV.get("VALUE"))));
        }else {
            esbVisitTotal.setTotal(0);
        }
        result.put("esbVisitTotal",esbVisitTotal);
        return result;
    }


    //获取Api访问排序柱状图数据
    public List<EsbHomeHisto> querySortVist( String limitTime){
        List<Map<String,Object>> list = esbHomeDao.querySortVist(limitTime);
        return totalResultHandle(limitTime,list);
    }

    public List<EsbHomeHisto> totalResultHandle(String limitTime, List<Map<String,Object>> list ){
        List<EsbHomeHisto> result = new ArrayList<>();
        if("week".equals(limitTime)){
            String day;
            Calendar curStartCal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd");
            Calendar cal = (Calendar) curStartCal.clone();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            for (int i = 0; i < 7; i++) {
                day = df.format(cal.getTime());
                cal.add(Calendar.DATE, 1);
                EsbHomeHisto esbHomeHisto = new EsbHomeHisto();
                Boolean temp = false;
                for (int j = 0; j <list.size() ; j++) {
                    if(day.equals(list.get(j).get("DAY").toString())){
                        esbHomeHisto.setName(String.valueOf(list.get(j).get("DAY")));
                        esbHomeHisto.setValue(Integer.parseInt(String.valueOf(list.get(j).get("TOTAL"))));
                        temp = true;
                        break;
                    }
                }
                if(!temp){
                    esbHomeHisto.setName(day);
                    esbHomeHisto.setValue(0);
                }
                result.add(esbHomeHisto);
            }
        }
        if("day".equals(limitTime)){
            for (int i = 0; i < 24; i++) {
                EsbHomeHisto esbHomeHisto = new EsbHomeHisto();
                Boolean temp = false;
                for (int j = 0; j <list.size() ; j++) {
                    if(i ==Integer.parseInt(String.valueOf(list.get(j).get("HOUR")))){
                        esbHomeHisto.setName(String.valueOf(i));
                        esbHomeHisto.setValue(Integer.parseInt(String.valueOf(list.get(j).get("TOTAL"))));
                        temp = true;
                        break;
                    }
                }
                if(!temp){
                    esbHomeHisto.setName(String.valueOf(i));
                    esbHomeHisto.setValue(0);
                }
                result.add(esbHomeHisto);
            }

        }
        if("month".equals(limitTime)){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = 1; i <= count; i++) {
                EsbHomeHisto esbHomeHisto = new EsbHomeHisto();
                Boolean temp = false;
                for (int j = 0; j <list.size() ; j++) {
                    if(i ==Integer.parseInt(String.valueOf(list.get(j).get("DAY")))){
                        esbHomeHisto.setName(String.valueOf(i));
                        esbHomeHisto.setValue(Integer.parseInt(String.valueOf(list.get(j).get("TOTAL"))));
                        temp = true;
                        break;
                    }
                }
                if(!temp){
                    esbHomeHisto.setName(String.valueOf(i));
                    esbHomeHisto.setValue(0);
                }
                result.add(esbHomeHisto);
            }
        }
        if("year".equals(limitTime)){
            for (int i = 1; i <=12; i++) {
                EsbHomeHisto esbHomeHisto = new EsbHomeHisto();
                Boolean temp = false;
                for (int j = 0; j <list.size() ; j++) {
                    if(i ==Integer.parseInt(String.valueOf(list.get(j).get("MONTH")))){
                        esbHomeHisto.setName(String.valueOf(i));
                        esbHomeHisto.setValue(Integer.parseInt(String.valueOf(list.get(j).get("TOTAL"))));
                        temp = true;
                        break;
                    }
                }
                if(!temp){
                    esbHomeHisto.setName(String.valueOf(i));
                    esbHomeHisto.setValue(0);
                }
                result.add(esbHomeHisto);
            }

        }
        return result;
    }
    //获取ESB失败排序柱状图数据
    public List<EsbHomeHisto> queryFailTotal(String startTime, String endTime, String limitTime){
        List<Map<String,Object>> list = esbHomeDao.queryFailTotal(startTime,endTime,limitTime);
        return totalResultHandle(limitTime,list);
    }

    //获取Api等待排序柱状图数据
    public List<EsbHomeHisto> querySortWait(String startTime, String endTime, String limitTime){
        return  esbHomeDao.querySortWait(startTime,endTime,limitTime).getResult();
    }

    //获取本月新增ESB详细数据
    public List<MarketEntiy> queryEsbIcrease(){
        return esbHomeDao.queryEsbIcrease();
    }

    //获取我的待办
    public Map<String,Object> queryMyTask(String userId){
        List<Map<String,Object>> result1= esbHomeDao.queryTaskTotal(userId);
        List<Map<String,Object>> result2= esbHomeDao.queryTaskdayIncrease(userId);
      //  BigDecimal bigDecimal1= (BigDecimal) result1.get(0).get("COUNT(1)");
       // BigDecimal bigDecimal1=  new BigDecimal((String)result1.get(0).get("COUNT(1)"));
        Integer taskTotal=Integer.parseInt((result1.get(0).get("COUNT(1)").toString()));
      //  BigDecimal bigDecimal2= (BigDecimal) result2.get(0).get("COUNT(1)");
       // BigDecimal bigDecimal2=  new BigDecimal((String)result2.get(0).get("COUNT(1)"));
       // bigDecimal2.toString()
        Integer dayIncrease=Integer.parseInt(result2.get(0).get("COUNT(1)").toString());
        double dayRate=0.0;
        if(taskTotal!=0){
            dayRate=dayIncrease/taskTotal*100;
        }else{
            dayRate=dayIncrease*100;
        }
        Map<String,Object> result=new HashMap<String,Object>();
        result.put("taskTotal",taskTotal);
        result.put("dayIncrease",dayIncrease);
        result.put("dayRate",dayRate);
        return result;

    }





    //按应用分类获取api信息,获取全部
    public List<EsbHomeHisto> queryEsbInfoByAppALL(){
        return esbHomeDao.queryEsbInfoByAppALL();
    }

    // 按类型分类获取api信息,获取个人
    public List<EsbHomeHisto> queryEsbInfoByType(String userId,String userRole){
        List<String> sysCodeList = new ArrayList<>();
        if("SystemLeader".equals(userRole) || "Tourist".equals(userRole)){
            List<DSGCSystemUser> dsgcSystemUsers =   sldao.findUserSystemByUserId(userId);
            Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
            while (iter.hasNext()) {
                DSGCSystemUser s = iter.next();
                sysCodeList.add(s.getSysCode());
            }
        }
        return esbHomeDao.queryEsbInfoByType(userRole,sysCodeList);
    }

    //按类型分类获取ESB信息
    public List<EsbHomeHisto> queryEsbInfoByTypeALL(){
        return esbHomeDao.queryEsbInfoByTypeALL();
    }

    public List<Map<String,String>> queryEsbServerNodeStatus(){
        List<Map<String,String>> result = new ArrayList<>();
        FndLookupType fndLookupType = fndLookupTypeDao.getFndLookupTypeByType("SERVER_NODE_INFO");
        List<Map<String, Object>> list = esbHomeDao.queryEsbServerNodeStatus();
        List<FndLookupValue> fndLookupValues = fndLookupType.getValues();
        Iterator<FndLookupValue> iterator = fndLookupValues.iterator();
        while (iterator.hasNext()){
            Map<String,String> map = new HashMap<>();
            FndLookupValue fndLookupValue = iterator.next();
            map.put("desc",fndLookupValue.getMeaning());
            Boolean temp = false;
            for (int i = 0; i < list.size(); i++) {
                if (fndLookupValue.getLookupCode().equals(list.get(i).get("SERVER"))){
                    temp = true;
                    map.put("server",String.valueOf(list.get(i).get("SERVER").toString()));
                    map.put("status",String.valueOf(list.get(i).get("STATUS")));
                    break;
                }
            }
            if(!temp){
                map.put("desc",fndLookupValue.getMeaning());
                map.put("server",fndLookupValue.getMeaning());
                map.put("status","SHOWDOWN");
            }
            result.add(map);
        }
        return result;
    }

    //获取流量分析数据
    public  Map<String,Object> queryTrafficAnalysis (){
        Map<String,Object> result=new HashMap<String,Object>();
        Calendar instance = Calendar.getInstance();
        String hour = instance.get(instance.HOUR_OF_DAY)+"";
        String minute = instance.get(instance.MINUTE)+"";
        EsbHomeHisto errNow= esbHomeDao.queryTrafficErrorNow();
        errNow.setName(hour+":"+minute);
        EsbHomeHisto runTimesNow= esbHomeDao.queryTrafficRuntimesNow();
        runTimesNow.setName(hour+":"+minute);
        List<EsbHomeHisto> runTimesNowList= esbHomeDao.queryTrafficRuntimes();
        runTimesNowList.add(runTimesNow);
        List<EsbHomeHisto> errNowapiList= esbHomeDao.queryTrafficError();
        errNowapiList.add(errNow);
        EsbHomeHisto trafficCostNow=new EsbHomeHisto();
        trafficCostNow.setName(hour+":"+minute);
        trafficCostNow.setValue(0);
        List<EsbHomeHisto> trafficCostNowList= esbHomeDao.queryTrafficCost();
        trafficCostNowList.add(trafficCostNow);
        result.put("runTimes",runTimesNowList);
        result.put("avgCost",trafficCostNowList);
        result.put("errorTimes",errNowapiList);
        return result;
    }

    public EsbHomeHisto queryAdminLocaltion(String code){
        return esbHomeDao.queryAdminLocaltion(code);
    }


    //计算比率
    public Integer rate(Integer num1,Integer num2){
        if(num1==null){
            num1=0;
        }
        if(num2==null){
            num2=0;
        }

        Integer dayRate=0;
        if(num2!=0){
            dayRate=num1/num2*100;
        }else{
            dayRate=num1*100;
        }
        return dayRate;
    }

}
