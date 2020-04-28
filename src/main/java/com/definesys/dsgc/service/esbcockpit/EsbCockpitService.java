package com.definesys.dsgc.service.esbcockpit;

import com.definesys.dsgc.service.esbcockpit.bean.eChartsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName esbCockpitService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-4-7 10:40
 * @Version 1.0
 **/
@Service
public class EsbCockpitService {
    @Autowired
    private EsbCockpitDao esbCockpitDao;

    //esb一段时间内总调用次数
    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public eChartsBean queryTotalRunTimes(Date startDate, Date endDate){

        return esbCockpitDao.queryTotalRunTimes(startDate,endDate);
    }

    //平台接入应用数量
    /**
     * @return result
     */
    public eChartsBean queryTotalapp(){
        return esbCockpitDao.queryTotalapp();
    }

    //一段时间内esb调用在各系统分布数量（成功的和失败的）
    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public List<eChartsBean> queryAppDistri(Date startDate, Date endDate){
        //value1为调用总数，value2为调用成功数，失败数为计算数量，rate为成功占比
        List<eChartsBean> result= esbCockpitDao.queryAppDistri(startDate,endDate);
        for(eChartsBean item :result){
            item.setRate(item.getValue2()/Double.valueOf(item.getValue1()));
        }
        return result;
    }

    //一段时间内调用到的esb个数（注意不是次数）占总数的比例
    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public eChartsBean queryAppExecute(Date startDate, Date endDate){
        eChartsBean result=esbCockpitDao.queryAppExecute(startDate,endDate);
        Calendar cal = Calendar.getInstance();
        cal.set(1000, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date beginOfDate = cal.getTime();
        eChartsBean appTotal=esbCockpitDao.queryEsbTotalDate(beginOfDate,endDate);
        result.setRate(result.getValue1()/Double.valueOf(appTotal.getValue1()));
        return result;
    }

    //查询所有应用和每个应用持有的消费者
    public  List<Map<String,Object>> queryAppInfo(){
        List result=new ArrayList<String>();
        List<eChartsBean> appValue=esbCockpitDao.queryApp();
        for(eChartsBean item:appValue){
            Map<String,Object> itemValue=new HashMap<>();
            List<eChartsBean> consumers=esbCockpitDao.queryConsumer(item.getId());
            Map<String,Object> trafficAnalysis=queryTrafficAnalysis(item.getId());
            itemValue.put("app",item);
            itemValue.put("consumer",consumers);
            itemValue.put("trafficAnalysis",trafficAnalysis);
            result.add(itemValue);
        }
        return result;
    }

    //获取年分月段分析数据
    public  Map<String,Object> queryTrafficAnalysis (String appId){
        Map<String,Object> result=new HashMap<String,Object>();
        result.put("flowRunTime",esbCockpitDao.queryMonthRuntimes(appId));
        result.put("flowsucessTime",esbCockpitDao.queryMonthSucess(appId));
        result.put("flowConsumer",esbCockpitDao.queryMonthConsumer(appId));
        return result;
    }

    //获取资源发布总数
    public  Map<String,Object> querySourceDate (){
        //添加数据顺序和querySourceFlow()中一致
        Map<String,Object> result=new HashMap<String,Object>();
        Calendar cal=Calendar.getInstance();
        cal.set(1000,1,1);
        Date beginOfDateTotal = cal.getTime();
        cal=Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
        Date endOfDateTotal = cal.getTime();
        //资源总数
        int Total= esbCockpitDao.queryEsbTotalDate(beginOfDateTotal,endOfDateTotal).getValue1();
        Total=Total+esbCockpitDao.queryAppTotalDate(beginOfDateTotal,endOfDateTotal).getValue1();
        Total=Total+esbCockpitDao.queryConsumerTotalDate(beginOfDateTotal,endOfDateTotal).getValue1();
        //资源周总数
        cal=Calendar.getInstance();
        int week = cal.get(Calendar.DAY_OF_WEEK)-2;
        cal.add(Calendar.DATE, -week);
        Date beginOfDateWeek = cal.getTime();
        cal=Calendar.getInstance();
        int nextweek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 8-nextweek);
        Date endOfDateWeek = cal.getTime();
        int WeekTotal= esbCockpitDao.queryEsbTotalDate(beginOfDateWeek,endOfDateWeek).getValue1();
        WeekTotal=WeekTotal+esbCockpitDao.queryAppTotalDate(beginOfDateWeek,endOfDateWeek).getValue1();
        WeekTotal=WeekTotal+esbCockpitDao.queryConsumerTotalDate(beginOfDateWeek,endOfDateWeek).getValue1();
        //资源月总数
        cal=Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date beginOfDateMonth = cal.getTime();
        cal=Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        Date endOfDateMonth = cal.getTime();
        int MonthTotal= esbCockpitDao.queryEsbTotalDate(beginOfDateMonth,endOfDateMonth).getValue1();
        MonthTotal=MonthTotal+esbCockpitDao.queryAppTotalDate(beginOfDateMonth,endOfDateMonth).getValue1();
        MonthTotal=MonthTotal+esbCockpitDao.queryConsumerTotalDate(beginOfDateMonth,endOfDateMonth).getValue1();
        //资源年总数
        cal=Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),1,1);
        Date beginOfDateYear = cal.getTime();
        cal=Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),12,31);
        Date endOfDateYear = cal.getTime();
        int YearTotal= esbCockpitDao.queryEsbTotalDate(beginOfDateYear,endOfDateYear).getValue1();
        YearTotal=YearTotal+esbCockpitDao.queryAppTotalDate(beginOfDateYear,endOfDateYear).getValue1();
        YearTotal=YearTotal+esbCockpitDao.queryConsumerTotalDate(beginOfDateYear,endOfDateYear).getValue1();
        result.put("sourceTotal",Total);
        result.put("sourceYearTotal",YearTotal);
        result.put("sourceMonthTotal",MonthTotal);
        result.put("sourceWeekTotal",WeekTotal);
        return result;
    }

    //获取近来资源发布情况
    public  Map<String,Object> querySourceFlow (){
        //添加数据顺序和querySourceDate()中一致
        Map<String,Object> result=new HashMap<String,Object>();
        Map<String,Object> weekData=new HashMap<String,Object>();
        Map<String,Object> monthData=new HashMap<String,Object>();
        Map<String,Object> yearData=new HashMap<String,Object>();
        weekData.put("ESB",esbCockpitDao.queryEsbSeven());
        weekData.put("消费者",esbCockpitDao.queryConsumerSeven());
        weekData.put("应用",esbCockpitDao.queryAppSeven());

        monthData.put("ESB",esbCockpitDao.queryEsbMonth());
        monthData.put("消费者",esbCockpitDao.queryConsumerMonth());
        monthData.put("应用",esbCockpitDao.queryAppMonth());

        yearData.put("ESB",esbCockpitDao.queryEsbyear());
        yearData.put("消费者",esbCockpitDao.queryConsumerYear());
        yearData.put("应用",esbCockpitDao.queryAppYear());

        result.put("sourceWeek",weekData);
        result.put("sourceMonth",monthData);
        result.put("sourceYear",yearData);
        return result;
    }

    //esb类型占比
    public   List<eChartsBean> queryEsbInfoByAppALL (){
        return esbCockpitDao.queryEsbInfoByAppALL();
    }


    //esb分布占比
    public   List<eChartsBean> queryEsbInfoByTypeALL (){
        return esbCockpitDao.queryEsbInfoByTypeALL();
    }

    public Map<String,Object> dash(){
        Calendar cal = Calendar.getInstance();
        cal.set(1000, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date beginOfDate = cal.getTime();
        cal=Calendar.getInstance();
        cal.set( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date endOfDate = cal.getTime();
        //当天 0:0:0
        cal=Calendar.getInstance();
        cal.set( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)-1, 23, 59, 59);
        Date todayOfDate = cal.getTime();
        Map<String,Object> result=new HashMap<String,Object>();
        result.put("appTotal", queryTotalapp());//应用总数
        result.put("appInstance", queryAppDistri(todayOfDate,endOfDate));//esb日志状况
        result.put("appUseRate", queryAppExecute(todayOfDate,endOfDate));//aesb使用率
        result.put("totalRunTimes", queryTotalRunTimes(beginOfDate,endOfDate));//esb总调用次数
        result.put("appConsumer", queryAppInfo());//app消费者
//        result.put("appFlow", queryTrafficAnalysis());//整合进app消费者数据中
        result.put("sourceTotal", querySourceDate());//资源总数
        result.put("appSourceAnalysis", querySourceFlow());//资源周增长
        result.put("apiInfoByApp", queryEsbInfoByAppALL());//esb按应用分类
        result.put("apiInfoByType", queryEsbInfoByTypeALL());//esb按类型分类
        return result;
    }
}
