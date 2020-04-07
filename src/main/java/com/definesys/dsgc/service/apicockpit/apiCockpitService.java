package com.definesys.dsgc.service.apicockpit;

import com.definesys.dsgc.service.apiHome.bean.ApiHomeHisto;
import com.definesys.dsgc.service.apicockpit.bean.CommonReqBean;
import com.definesys.dsgc.service.apicockpit.bean.eChartsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName apiCockpitService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-30 10:40
 * @Version 1.0
 **/
@Service
public class apiCockpitService {
    @Autowired
    private apiCockpitDao apiCockpitDao;

    //api一段时间内总调用次数
    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public eChartsBean queryTotalRunTimes(Date startDate, Date endDate){

        return apiCockpitDao.queryTotalRunTimes(startDate,endDate);
    }

    //平台接入应用数量
    /**
     * @return result
     */
    public eChartsBean queryTotalapp(){
        return apiCockpitDao.queryTotalapp();
    }

    //一段时间内API调用在各系统分布数量（成功的和失败的）
    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public List<eChartsBean> queryAppDistri(Date startDate, Date endDate){
        //value1为调用总数，value2为调用成功数，失败数为计算数量，rate为成功占比
        List<eChartsBean> result= apiCockpitDao.queryAppDistri(startDate,endDate);
        for(eChartsBean item :result){
            item.setRate(item.getValue2()/Double.valueOf(item.getValue1()));
        }
        return result;
    }

    //一段时间内调用到的API个数（注意不是次数）占总数的比例
    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public eChartsBean queryAppExecute(Date startDate, Date endDate){
        eChartsBean result=apiCockpitDao.queryAppExecute(startDate,endDate);
        eChartsBean appTotal=apiCockpitDao.queryTotalapp();
        result.setRate(result.getValue1()/Double.valueOf(appTotal.getValue1()));
        return result;
    }

    //查询所有应用和每个应用持有的消费者

    public  List<Map<String,Object>> queryAppInfo(){
        List result=new ArrayList<String>();
        List<eChartsBean> appValue=apiCockpitDao.queryApp();
        for(eChartsBean item:appValue){
            Map<String,Object> itemValue=new HashMap<>();
            List<eChartsBean> consumers=apiCockpitDao.queryConsumer(item.getId());
            itemValue.put("app",item);
            itemValue.put("consumer",consumers);
            result.add(itemValue);
        }
        return result;
    }

    //获取年分月段分析数据
    public  Map<String,Object> queryTrafficAnalysis (){
        Map<String,Object> result=new HashMap<String,Object>();
        result.put("flowRunTime",apiCockpitDao.queryMonthRuntimes());
        result.put("flowsucessTime",apiCockpitDao.queryMonthSucess());
        result.put("flowConsumer",apiCockpitDao.queryMonthConsumer());
        return result;
    }

    //获取资源发布总数
    public  Map<String,Object> querySourceDate (){
        Map<String,Object> result=new HashMap<String,Object>();
        Calendar cal=Calendar.getInstance();
        cal.set(1000,1,1);
        Date beginOfDateTotal = cal.getTime();
        cal=Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
        Date endOfDateTotal = cal.getTime();
        //资源总数
        int Total= apiCockpitDao.queryApiTotalDate(beginOfDateTotal,endOfDateTotal).getValue1();
        Total=Total+apiCockpitDao.queryAppTotalDate(beginOfDateTotal,endOfDateTotal).getValue1();
        Total=Total+apiCockpitDao.queryConsumerTotalDate(beginOfDateTotal,endOfDateTotal).getValue1();
        //资源周总数
        cal=Calendar.getInstance();
        int week = cal.get(Calendar.DAY_OF_WEEK)-2;
        cal.add(Calendar.DATE, -week);
        Date beginOfDateWeek = cal.getTime();
        cal=Calendar.getInstance();
        int nextweek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 8-nextweek);
        Date endOfDateWeek = cal.getTime();
        int WeekTotal= apiCockpitDao.queryApiTotalDate(beginOfDateWeek,endOfDateWeek).getValue1();
        WeekTotal=WeekTotal+apiCockpitDao.queryAppTotalDate(beginOfDateWeek,endOfDateWeek).getValue1();
        WeekTotal=WeekTotal+apiCockpitDao.queryConsumerTotalDate(beginOfDateWeek,endOfDateWeek).getValue1();
        //资源月总数
        cal=Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date beginOfDateMonth = cal.getTime();
        cal=Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        Date endOfDateMonth = cal.getTime();
        int MonthTotal= apiCockpitDao.queryApiTotalDate(beginOfDateMonth,endOfDateMonth).getValue1();
        MonthTotal=MonthTotal+apiCockpitDao.queryAppTotalDate(beginOfDateMonth,endOfDateMonth).getValue1();
        MonthTotal=MonthTotal+apiCockpitDao.queryConsumerTotalDate(beginOfDateMonth,endOfDateMonth).getValue1();
        //资源年总数
        cal=Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),1,1);
        Date beginOfDateYear = cal.getTime();
        cal=Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),12,31);
        Date endOfDateYear = cal.getTime();
        int YearTotal= apiCockpitDao.queryApiTotalDate(beginOfDateYear,endOfDateYear).getValue1();
        YearTotal=YearTotal+apiCockpitDao.queryAppTotalDate(beginOfDateYear,endOfDateYear).getValue1();
        YearTotal=YearTotal+apiCockpitDao.queryConsumerTotalDate(beginOfDateYear,endOfDateYear).getValue1();
        result.put("sourceTotal",Total);
        result.put("sourceYearTotal",YearTotal);
        result.put("sourceMonthTotal",MonthTotal);
        result.put("sourceWeekTotal",WeekTotal);
        return result;
    }

    //获取一周内资源发布情况
    public  Map<String,Object> querySourceFlow (){
        Map<String,Object> result=new HashMap<String,Object>();
        result.put("apiSeven",apiCockpitDao.queryApiSeven());
        result.put("consumerSeven",apiCockpitDao.queryConsumerSeven());
        result.put("appSeven",apiCockpitDao.queryAppSeven());
        return result;
    }

    //api类型占比
    public   List<eChartsBean> queryApiInfoByAppALL (){
        return apiCockpitDao.queryApiInfoByAppALL();
    }


    //api分布占比
    public   List<eChartsBean> queryApiInfoByTypeALL (){
        return apiCockpitDao.queryApiInfoByTypeALL();
    }

    public Map<String,Object> dash(){
        Calendar cal = Calendar.getInstance();
        cal.set(1000, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date beginOfDate = cal.getTime();
        cal=Calendar.getInstance();
        cal.set( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date endOfDate = cal.getTime();
        Map<String,Object> result=new HashMap<String,Object>();
        result.put("appTotal", queryTotalapp());//应用总数
        result.put("appInstance", queryAppDistri(beginOfDate,endOfDate));//api日志状况
        result.put("appUseRate", queryAppExecute(beginOfDate,endOfDate));//api使用率
        result.put("totalRunTimes", queryTotalRunTimes(beginOfDate,endOfDate));//api总调用次数
        result.put("appConsumer", queryAppInfo());//app消费者
        result.put("appFlow", queryTrafficAnalysis());//api调用情况
        result.put("sourceTotal", querySourceDate());//资源总数
        result.put("appFlowSeven", querySourceFlow());//资源周增长
        result.put("apiInfoByApp", queryApiInfoByAppALL());//api按应用分类
        result.put("apiInfoByType", queryApiInfoByTypeALL());//api按类型分类
        return result;
    }
}
