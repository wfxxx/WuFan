package com.definesys.dsgc.service.apiHome;

import com.definesys.dsgc.service.apiHome.bean.ApiHomeCard;
import com.definesys.dsgc.service.apiHome.bean.ApiHomeHisto;
import com.definesys.dsgc.service.bpm.bean.BpmInstanceBean;
import com.definesys.dsgc.service.bpm.bean.BpmInstanceDTO;
import com.definesys.dsgc.service.market.bean.MarketEntiy;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName ApiHomeService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-28 10:26
 * @Version 1.0
 **/
@Service
public class ApiHomeService {
    @Autowired
        ApiHomeDao apiHomeDao;

//获取api首页卡片

    public Map<String,Object> queryCardsData(){
//        Map<String,Object> result=new HashMap<String,Object>();
//        //获取上周总数，获取这周总数，获取昨天总数，获取今天总数; API  应用， 用户  api访问量
//        ApiHomeCard apiTotal=new ApiHomeCard();
//        ApiHomeHisto lastWeekA=apiHomeDao.getLastWeekTotalA();
//        ApiHomeHisto nowWeekA=apiHomeDao.getNowWeekTotalA();
//        ApiHomeHisto todayA=apiHomeDao.getTodyTotalA();
//        ApiHomeHisto yestodayA=apiHomeDao.getYestodayTotalA();
//        ApiHomeHisto totalA=apiHomeDao.getTotalA();
//        apiTotal.setDataAdd(todayA.getValue());
//        apiTotal.setDayRate(rate(todayA.getValue(),yestodayA.getValue()));
//        apiTotal.setTotal(totalA.getValue());
//        apiTotal.setWeekRate(rate(nowWeekA.getValue(),lastWeekA.getValue()));
//        result.put("apiTotal",apiTotal);
//        ApiHomeCard sysTotal=new ApiHomeCard();
//        ApiHomeHisto lastWeekE=apiHomeDao.getLastWeekTotalE();
//        ApiHomeHisto nowWeekE=apiHomeDao.getNowWeekTotalE();
//        ApiHomeHisto todayE=apiHomeDao.getTodyTotalE();
//        ApiHomeHisto yestodayE=apiHomeDao.getYestodayTotalE();
//        ApiHomeHisto totalE=apiHomeDao.getTotalE();
//        sysTotal.setDataAdd(todayE.getValue());
//        sysTotal.setDayRate(rate(todayE.getValue(),yestodayE.getValue()));
//        sysTotal.setTotal(totalE.getValue());
//        sysTotal.setWeekRate(rate(nowWeekE.getValue(),lastWeekE.getValue()));
//        result.put("sysTotal",sysTotal);
//        ApiHomeCard userTotal=new ApiHomeCard();
//        ApiHomeHisto lastWeekU=apiHomeDao.getLastWeekTotalU();
//        ApiHomeHisto nowWeekU=apiHomeDao.getNowWeekTotalU();
//        ApiHomeHisto todayU=apiHomeDao.getTodyTotalU();
//        ApiHomeHisto yestodayU=apiHomeDao.getYestodayTotalU();
//        ApiHomeHisto totalU=apiHomeDao.getTotalU();
//        userTotal.setDataAdd(todayU.getValue());
//        userTotal.setDayRate(rate(todayU.getValue(),yestodayU.getValue()));
//        userTotal.setTotal(totalU.getValue());
//        userTotal.setWeekRate(rate(nowWeekU.getValue(),lastWeekU.getValue()));
//        result.put("userTotal",userTotal);
//        //TODO
//        ApiHomeCard apiVisitTotal=new ApiHomeCard();
//        ApiHomeHisto lastWeekV=apiHomeDao.getLastWeekTotalV();
//        lastWeekV=lastWeekV==null?new ApiHomeHisto():lastWeekV;
//        ApiHomeHisto nowWeekV=apiHomeDao.getNowWeekTotalV();
//        nowWeekV=nowWeekV==null?new ApiHomeHisto():nowWeekV;
//        ApiHomeHisto todayV=apiHomeDao.getTodyTotalV();
//        todayV=todayV==null?new ApiHomeHisto():todayV;
//        ApiHomeHisto yestodayV=apiHomeDao.getYestodayTotalV();
//        yestodayV=yestodayV==null?new ApiHomeHisto():yestodayV;
//        ApiHomeHisto totalV=apiHomeDao.getTotalV();
//        totalV=totalV==null?new ApiHomeHisto():totalV;
//        apiVisitTotal.setDataAdd(todayV.getValue());
//        apiVisitTotal.setDayRate(rate(todayV.getValue(),yestodayV.getValue()));
//        apiVisitTotal.setTotal(totalV.getValue());
//        apiVisitTotal.setWeekRate(rate(nowWeekV.getValue(),lastWeekV.getValue()));
//        result.put("apiVisitTotal",apiVisitTotal);
//        return result;


        Map<String,Object> result=new HashMap<String,Object>();
        //获取上周总数，获取这周总数，获取昨天总数，获取今天总数; API  应用， 用户  api访问量
        ApiHomeCard apiTotal=new ApiHomeCard();
        ApiHomeHisto lastWeekA=apiHomeDao.getLastWeekTotalA();
        ApiHomeHisto nowWeekA=apiHomeDao.getNowWeekTotalA();
        ApiHomeHisto todayA=apiHomeDao.getTodyTotalA();
        ApiHomeHisto yestodayA=apiHomeDao.getYestodayTotalA();
        ApiHomeHisto totalA=apiHomeDao.getTotalA();
        apiTotal.setDataAdd(todayA.getValue());
        apiTotal.setDayRate(rate(todayA.getValue(),yestodayA.getValue()));
        apiTotal.setTotal(totalA.getValue());
        apiTotal.setWeekRate(rate(nowWeekA.getValue(),lastWeekA.getValue()));
        result.put("apiTotal",apiTotal);
        ApiHomeCard sysTotal=new ApiHomeCard();
        ApiHomeHisto lastWeekE=apiHomeDao.getLastWeekTotalE();
        ApiHomeHisto nowWeekE=apiHomeDao.getNowWeekTotalE();
        ApiHomeHisto todayE=apiHomeDao.getTodyTotalE();
        ApiHomeHisto yestodayE=apiHomeDao.getYestodayTotalE();
        ApiHomeHisto totalE=apiHomeDao.getTotalE();
        sysTotal.setDataAdd(todayE.getValue());
        sysTotal.setDayRate(rate(todayE.getValue(),yestodayE.getValue()));
        sysTotal.setTotal(totalE.getValue());
        sysTotal.setWeekRate(rate(nowWeekE.getValue(),lastWeekE.getValue()));
        result.put("sysTotal",sysTotal);
        ApiHomeCard userTotal=new ApiHomeCard();
        ApiHomeHisto lastWeekU=apiHomeDao.getLastWeekTotalU();
        ApiHomeHisto nowWeekU=apiHomeDao.getNowWeekTotalU();
        ApiHomeHisto todayU=apiHomeDao.getTodyTotalU();
        ApiHomeHisto yestodayU=apiHomeDao.getYestodayTotalU();
        ApiHomeHisto totalU=apiHomeDao.getTotalU();
        userTotal.setDataAdd(todayU.getValue());
        userTotal.setDayRate(rate(todayU.getValue(),yestodayU.getValue()));
        userTotal.setTotal(totalU.getValue());
        userTotal.setWeekRate(rate(nowWeekU.getValue(),lastWeekU.getValue()));
        result.put("userTotal",userTotal);
        //TODO
        ApiHomeCard apiVisitTotal=new ApiHomeCard();
        Map<String,Object> lastWeekV=apiHomeDao.getLastWeekTotalV();
        Map<String,Object> nowWeekV=apiHomeDao.getNowWeekTotalV();
        if(nowWeekV !=null && nowWeekV.containsKey("VALUE")&& lastWeekV !=null && lastWeekV.containsKey("VALUE")){
            apiVisitTotal.setWeekRate(rate(Integer.parseInt(String.valueOf(nowWeekV.get("VALUE"))),Integer.parseInt(String.valueOf(lastWeekV.get("VALUE")))));
        }else if((nowWeekV ==null || !nowWeekV.containsKey("VALUE"))&& lastWeekV !=null && lastWeekV.containsKey("VALUE")){
            apiVisitTotal.setWeekRate(rate(0,Integer.parseInt(String.valueOf(lastWeekV.get("VALUE")))));
        }else if(nowWeekV !=null && nowWeekV.containsKey("VALUE")&& (lastWeekV ==null || !lastWeekV.containsKey("VALUE"))){
            apiVisitTotal.setWeekRate(rate(Integer.parseInt(String.valueOf(nowWeekV.get("VALUE"))),0));
        }
        else {
            apiVisitTotal.setWeekRate(rate(0,0));
        }

        Map<String,Object> todayV=apiHomeDao.getTodyTotalV();
        Map<String,Object> yestodayV=apiHomeDao.getYestodayTotalV();
        if(todayV!=null && todayV.containsKey("VALUE")){
            apiVisitTotal.setDataAdd(Integer.parseInt(String.valueOf(todayV.get("VALUE"))));
        }else {
            apiVisitTotal.setDataAdd(0);
        }
        if(todayV !=null && todayV.containsKey("VALUE")&& yestodayV !=null && yestodayV.containsKey("VALUE")){
            apiVisitTotal.setDayRate(rate(Integer.parseInt(String.valueOf(todayV.get("VALUE"))),Integer.parseInt(String.valueOf(yestodayV.get("VALUE")))));
        }else if((todayV ==null || !todayV.containsKey("VALUE"))&& yestodayV !=null && yestodayV.containsKey("VALUE")){
            apiVisitTotal.setDayRate(rate(0,Integer.parseInt(String.valueOf(yestodayV.get("VALUE")))));
        }else if(todayV !=null && todayV.containsKey("VALUE")&& (yestodayV ==null || !yestodayV.containsKey("VALUE"))){
            apiVisitTotal.setDayRate(rate(Integer.parseInt(String.valueOf(todayV.get("VALUE"))),0));

        }
        else {
            apiVisitTotal.setDayRate(rate(0,0));
        }
        Map<String,Object> totalV=apiHomeDao.getTotalV();
        if(totalV!=null && totalV.containsKey("VALUE")){
            apiVisitTotal.setTotal(Integer.parseInt(String.valueOf(totalV.get("VALUE"))));
        }else {
            apiVisitTotal.setTotal(0);
        }
        result.put("apiVisitTotal",apiVisitTotal);
        return result;

    }

    //获取Api访问排序柱状图数据
    public List<ApiHomeHisto> querySortVist(String startTime,String endTime,String limitTime){
        return  apiHomeDao.querySortVist(startTime,endTime,limitTime).getResult();
    }

    //获取Api并发排序柱状图数据
    public List<ApiHomeHisto> querySortConcurrent(String startTime,String endTime,String limitTime){
        return  apiHomeDao.querySortConcurrent(startTime,endTime,limitTime).getResult();
    }

    //获取Api等待排序柱状图数据
    public List<ApiHomeHisto> querySortWait(String startTime,String endTime,String limitTime){
        return  apiHomeDao.querySortWait(startTime,endTime,limitTime).getResult();
    }

    //获取本月新增Api详细数据
    public List<MarketEntiy> queryApiIcrease(){
        return apiHomeDao.queryApiIcrease();
    }

    //获取我的待办
    public Map<String,Object> queryMyTask(String userId){
        List<Map<String,Object>> result1=apiHomeDao.queryTaskTotal(userId);
        List<Map<String,Object>> result2=apiHomeDao.queryTaskdayIncrease(userId);
        Integer taskTotal=Integer.parseInt((result1.get(0).get("COUNT(1)").toString()));
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
    public List<ApiHomeHisto> queryApiInfoByAppALL(){
        return apiHomeDao.queryApiInfoByAppALL();
    }

    // 按类型分类获取api信息,获取个人
    public List<ApiHomeHisto> queryApiInfoByType(String userId){
        return apiHomeDao.queryApiInfoByType(userId);
    }

    //按类型分类获取api信息
    public List<ApiHomeHisto> queryApiInfoByTypeALL(){
        return apiHomeDao.queryApiInfoByTypeALL();
    }

    //获取流量分析数据
    public  Map<String,Object> queryTrafficAnalysis (){
        Map<String,Object> result=new HashMap<String,Object>();
        Calendar instance = Calendar.getInstance();
        String hour = instance.get(instance.HOUR_OF_DAY)+"";
        String minute = instance.get(instance.MINUTE)+"";
        ApiHomeHisto errNow=apiHomeDao.queryTrafficErrorNow();
        errNow.setName(hour+":"+minute);
        ApiHomeHisto runTimesNow=apiHomeDao.queryTrafficRuntimesNow();
        runTimesNow.setName(hour+":"+minute);
        ApiHomeHisto trafficCostNow=apiHomeDao.queryTrafficCostNow();
        trafficCostNow.setName(hour+":"+minute);

        List<ApiHomeHisto> runTimesNowList=apiHomeDao.queryTrafficRuntimes();
        runTimesNowList.add(runTimesNow);
        List<ApiHomeHisto> errNowapiList=apiHomeDao.queryTrafficError();
        errNowapiList.add(errNow);
        List<ApiHomeHisto> trafficCostNowList=apiHomeDao.queryTrafficCost();
        trafficCostNowList.add(trafficCostNow);
        result.put("runTimes",runTimesNowList);
        result.put("avgCost",trafficCostNowList);
        result.put("errorTimes",errNowapiList);
        return result;
    }

    public  ApiHomeHisto queryAdminLocaltion(String code){
        return apiHomeDao.queryAdminLocaltion(code);
    }


    //计算比率
    public double rate(Integer num1,Integer num2){
        if(num1==null){
            num1=0;
        }
        if(num2==null){
            num2=0;
        }
        double dayRate=0;
        if(num2!=0){
            dayRate=num1.doubleValue()/num2.doubleValue()*100;
        }else{
            dayRate=num1.doubleValue()*100;
        }
        return Math.ceil(dayRate);
    }

}
