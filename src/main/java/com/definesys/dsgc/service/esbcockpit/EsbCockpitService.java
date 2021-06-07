package com.definesys.dsgc.service.esbcockpit;

import com.definesys.dsgc.service.esbcockpit.bean.RadarBean;
import com.definesys.dsgc.service.esbcockpit.bean.eCharts3DBean;
import com.definesys.dsgc.service.esbcockpit.bean.eChartsBean;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    //查询ESB日运行次数
    public Object queryDayRunTimes() {
        return esbCockpitDao.queryTotalDayRunTimes()==null?0:esbCockpitDao.queryTotalDayRunTimes().get("all_count");
    }

    //查询ESB日异常数量
    private Object queryDayExceprionTimes(){
        return esbCockpitDao.queryDayOfFailCount()==null?0:esbCockpitDao.queryDayOfFailCount().get("fail_count");
    }

    //查询当日交易量
    public Object queryDayBusTimes() {
        return esbCockpitDao.queryDayBusTimes()==null?0:esbCockpitDao.queryDayBusTimes().get("all_count");
    }

    //查询总交易量
    public String queryTotalBusTimes() {
        String busTimes = esbCockpitDao.queryTotalBusTimes()==null?"0":((BigDecimal)(esbCockpitDao.queryTotalBusTimes().get("all_count"))).toString();
        DecimalFormat df = new DecimalFormat("######0.00");
        //大于10万 修改单位为 万
        if(busTimes.length()>12){
            Double f = Double.parseDouble(busTimes) / 100000000;
            return df.format(f)+"亿";
        }
        if(busTimes.length()>6) {
            Double f = Double.parseDouble(busTimes) / 10000;
            return df.format(f)+"万";
        }
        return busTimes;
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
            List<Map<String,Object>> trafficAnalysis = new ArrayList<>();
            //添加年月日
            for(int i = 0 ; i < 3 ; i ++){
                String type;
                type = i==0?"year":i==1?"month":i==2?"day":"";
                trafficAnalysis.add(queryTrafficAnalysis(item.getId(),type));
            }
//            Map<String,Object> trafficAnalysis=queryTrafficAnalysis(item.getId());
            itemValue.put("app",item);
            itemValue.put("consumer",consumers);
            itemValue.put("trafficAnalysis",trafficAnalysis);

            result.add(itemValue);
        }
        return result;
    }

    //获取年分月段分析数据
    public  Map<String,Object> queryTrafficAnalysis (String appId,String type){
//        System.out.println(appId);  ESB DMS
        Map<String,Object> result=new HashMap<String,Object>();
        result.put("app",appId);
        result.put("type",type);
        if("year".equals(type)) {
            result.put("flowRunTime", esbCockpitDao.queryMonthRuntimes(appId));
            result.put("flowsucessTime", esbCockpitDao.queryMonthSucess(appId));
            result.put("flowConsumer", esbCockpitDao.queryMonthConsumer(appId));
            return result;
        }
        if("month".equals(type)){
            result.put("flowRunTime", esbCockpitDao.queryDayRuntimes(appId));
            result.put("flowsucessTime", esbCockpitDao.queryDaySucess(appId));
            result.put("flowConsumer", esbCockpitDao.queryMonthConsumer(appId));
            return result;
        }
        if("day".equals(type)){
            result.put("flowRunTime", esbCockpitDao.queryHourRuntimes(appId));
            result.put("flowsucessTime", esbCockpitDao.queryHourSucess(appId));
            result.put("flowConsumer", esbCockpitDao.queryMonthConsumer(appId));
            return result;
        }
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

    private String[] queryFailApi() {
        List<Map<String, Object>> list = esbCockpitDao.queryFailApiServNo();
        String [] failApi = new String[list.size()];
        for(int i = 0 ; i < list.size();i++){
            failApi[i] = (String) list.get(i).get("sys_code");
        }
        return failApi;
    }

    private List<Map<String,Object>> queryAllCountAndFailCount(){
        List<Map<String, Object>> maps = esbCockpitDao.queryAllCountAndFailCount();
        return maps;
    }


    //esb分布占比
    public   List<eChartsBean> queryEsbInfoByTypeALL (){
        return esbCockpitDao.queryEsbInfoByTypeALL();
    }

    public Map<String,Object>   dash(){
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
        result.put("dayRunTimes", queryDayRunTimes());//esb今日调用次数
        result.put("dayExceptionTimes",queryDayExceprionTimes());//esb今日异常总数
        result.put("appConsumer", queryAppInfo());//app消费者
//        result.put("appFlow", queryTrafficAnalysis());//整合进app消费者数据中
        result.put("sourceTotal", querySourceDate());//资源总数
        result.put("appSourceAnalysis", querySourceFlow());//资源周增长
        result.put("apiInfoByApp", queryEsbInfoByAppALL());//esb按应用分类
        result.put("apiInfoByType", queryEsbInfoByTypeALL());//esb按类型分类
        result.put("apiFail",queryFailApi());//一日内接口错误的系统
        result.put("allCountAndFailCount",queryAllCountAndFailCount());//一日内所有系统的接口调用总数，错误数
        result.put("weekReport",select3DEchart());//查询伪3DEchart数据
        result.put("monthReport",select3DEchartMonth());//查询伪3DEchart月数据
        result.put("radar",selectRadar()); //查询雷达图数据
        result.put("balloon",selectBalloon()); //查询驾驶舱右中的气球图数据
        result.put("cone",selectConeData());//查询驾驶舱左上方锥型图数据
        result.put("doughnut",selectDoughnutChart());//查询驾驶舱下方数据
        result.put("dayBusTimes",queryDayBusTimes());//查询当日交易量
        result.put("totalBusTimes",queryTotalBusTimes());//查询交易总量
        result.put("maxDayRunTimes",queryMaxDayRunTimes());//查询今日接口调用TOP3
        return result;
    }

    //查询驾驶舱左上方今日调用接口次数Top3
    public List<Map<String, Object>> queryMaxDayRunTimes(){
        return esbCockpitDao.queryMaxDayRunTimes();
    }




    //查询驾驶舱右上方
    public Map<String,Object> selectRadar(){
        Map<String,Object> map = new HashMap<>();
        List<RadarBean> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        //当前年月
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH)+1;
        //8个月前的年月
        cal.add(Calendar.MONTH,-8);
        int beforeYear = cal.get(Calendar.YEAR);
        int beforeMonth = cal.get(Calendar.MONTH)+1;
        //存在跨年
        if(nowYear!=beforeYear){
            list= esbCockpitDao.queryMonthCountByYearAndMonth(beforeYear, beforeMonth, 12);
            list.addAll(esbCockpitDao.queryMonthCountByYearAndMonth(nowYear, 1, nowMonth));
        }else {
            //同年
            list = esbCockpitDao.queryMonthCountByYearAndMonth(nowYear,beforeMonth,nowMonth);
        }
        //统计最大值
        int max = 0;
        for(RadarBean bean : list){
            max = bean.getCount()>max? bean.getCount():max;
        }
        map.put("max",max);


        if(list.size()==8){
            map.put("data",list);
            return map;
        }
        //处理当月无调用则无记录的问题
        boolean isSave = false;
        for(int i = 0 ; i < 8; i++){
            for(int j = 0 ; j < list.size() ; j++){
                if(list.get(j).getMonth()==beforeMonth){
                    isSave = true;
                    break;
                }
            }
            if(!isSave)
            list.add(new RadarBean(beforeYear,beforeMonth,0));
            //跨年判断
            if(beforeMonth==12){
                beforeMonth = 1;
                beforeYear++;
                isSave = false;
                continue;
            }
            beforeMonth++;
            isSave = false;
        }
        list.sort((o1, o2) -> {
            if(o1.getYear()>o2.getYear()){
                return -1;
            }else if(o1.getYear()<o2.getYear()){
                return 1;
            }else {
                if(o1.getMonth()>o2.getMonth()){
                    return -1;
                }else{
                    return 1;
                }
            }
        });
        map.put("data",list);
        return map;
    }

    //查询驾驶舱右中间
    public Map<String,Object> selectBalloon(){
        Map<String,Object> res = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到前天的日期与今日的日期
        Calendar cal = Calendar.getInstance();
        //拿到今天的日期与几号
        int endDay = cal.get(Calendar.DAY_OF_MONTH);
        String endDate =  sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH,-1);
        //拿到前天的日期与几号
        int startDay = cal.get(Calendar.DAY_OF_MONTH);
        String startDate =  sdf.format(cal.getTime());
        List<Map<String, Object>> maps = esbCockpitDao.queryBeforeYesterday(startDay);
        res.put("beforYesterDay",maps);
        if(maps!=null&&!maps.isEmpty()){
            //今日异常数据
            List<Map<String, Object>> list = new ArrayList<>();
            //添加前天有异常的
            for(int i = 0 ; i < maps.size() ; i++){
                Map<String, Object> obj = esbCockpitDao.queryToDayExcepionByServNo(endDay, (String) maps.get(i).get("serv_no"));
                //如果根据前天异常编号查询今日无报错记录则修改前日失败数添加至集合
                if(obj!=null){
                    list.add(obj);
                }else{
                    Map<String, Object> map = new HashMap<>();
                    map.put("serv_name",maps.get(i).get("serv_name"));
                    map.put("serv_no",maps.get(i).get("serv_no"));
                    map.put("failCount",0);
                    list.add(map);
                }
            }
            //添加前天无异常的
            for (int i = 0 ; i < 3-maps.size() ; i++) {
                list.add(new HashMap<>());
                maps.add(new HashMap<>());
            }
            res.put("todya",list);
        }else{
            res.put("today",new ArrayList<>());
        }
        res.put("beforDate",startDate);
        res.put("todayDate",endDate);
        return res;
    }

    //查询驾驶舱右下方周柱状图
    public List<eCharts3DBean> select3DEchart(){
        Calendar cal = Calendar.getInstance();
        int weekday = cal.get(Calendar.DAY_OF_WEEK)-2;
        //跳转到这个星期的第一天
        cal.add(Calendar.DAY_OF_MONTH,-weekday);
        int startYear = cal.get(Calendar.YEAR);
        int startMonth = cal.get(Calendar.MONTH)+1;
        int startDay =  cal.get(Calendar.DAY_OF_MONTH);
        int overDay = cal.getActualMaximum(Calendar.DATE);
        cal.add(Calendar.DAY_OF_MONTH,6);
        int endYear = cal.get(Calendar.YEAR);
        int endMonth = cal.get(Calendar.MONTH)+1;
        int endDay = cal.get(Calendar.DAY_OF_MONTH);
        List<eCharts3DBean> reslt;
        if(startMonth != endMonth){
            //先查上个月
            reslt = esbCockpitDao.queryWeekCountAndFailCount(startYear, startMonth, startDay, overDay);
            //再查下个月
            List<eCharts3DBean> reslt2 = esbCockpitDao.queryWeekCountAndFailCount(endYear, endMonth, 1, endDay);
            //最后整合
            for(int i = 0 ; i < reslt.size();i++){
                int allCountOffset = reslt2.get(i).getAllCount()==null?0:reslt2.get(i).getAllCount();
                int sucessCountOffset =  reslt2.get(i).getSucessCount()==null?0:reslt2.get(i).getSucessCount();
                reslt.get(i).setAllCount((reslt.get(i).getAllCount()==null?0:reslt.get(i).getAllCount())+allCountOffset);
                reslt.get(i).setSucessCount((reslt.get(i).getSucessCount()==null?0:reslt.get(i).getSucessCount())+sucessCountOffset);
            }
        }else{
            reslt = esbCockpitDao.queryWeekCountAndFailCount(startYear, startMonth, startDay, endDay);
            for(int i = 0 ; i < reslt.size();i++){
                //初始化null
                reslt.get(i).setAllCount(reslt.get(i).getAllCount()==null?0:reslt.get(i).getAllCount());
                reslt.get(i).setSucessCount(reslt.get(i).getSucessCount()==null?0:reslt.get(i).getSucessCount());
            }
        }
        return reslt;
    }

    //查询驾驶舱右下方月柱状图
    public List<eCharts3DBean> select3DEchartMonth(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        return esbCockpitDao.queryMonthCountAndFailCount(year,month);
    }

    //查询驾驶舱左上方数据
    public Map<String,Object> selectConeData(){
        Map<String,Object> rst = new HashMap<>();
        //查询今日报文最大的三个接口
        List<Map<String, Object>> maxMsgSiz = esbCockpitDao.queryMaxMsgSize();
        //格式转换
        if(maxMsgSiz!=null){
            for(int i = 0  ; i < maxMsgSiz.size() ; i++){
                double msgSize = ((BigDecimal) maxMsgSiz.get(i).get("msg_size")).doubleValue();

                String msDesc = "";
                double md = msgSize;
                if (md >= 1024d && md < 1024d * 1024d) {
                    BigDecimal b = new BigDecimal(md / 1024d).setScale(3, RoundingMode.UP);
                    msDesc = b.doubleValue() + "MB";
                } else if (md >= 1024d * 1024d) {
                    BigDecimal b = new BigDecimal(md / (1024d * 1024d)).setScale(3, RoundingMode.UP);
                    msDesc = b.doubleValue() + "GB";
                } else {
                    msDesc = md + "KB";
                }
                maxMsgSiz.get(i).replace("msg_size",msDesc);
            }
        }
        //查询今日调用数量最大的三个接口
        List<Map<String, Object>> maxByDay= esbCockpitDao.queryMaxByDay();
        rst.put("maxMsgSiz",maxMsgSiz);
        rst.put("maxByDay",maxByDay);
        return rst;
    }

    //查询驾驶舱左下方数据(环形图)
    public Map<String,Object> selectDoughnutChart(){
        //查询今日调用的连接异常次数与业务异常的次数
        return esbCockpitDao.queryConnectAndBusinessException();
    }





}
