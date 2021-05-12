package com.definesys.dsgc.service.rpt;

import com.definesys.dsgc.service.rpt.bean.*;
import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DSGCServiceStatisticsReportDao {

    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;

    @Value("${database.type}")
    private String dbType;

    public List<RpServTotal> getRunTotalNum(){
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "select * from (SELECT * FROM RP_SERV_TOTAL WHERE SERV_NO != 'N/A' ORDER BY TOTAL_TIMES DESC)  where rownum <=10";

        }
        if("mysql".equals(dbType)){
            sql = "select * from (SELECT * FROM RP_SERV_TOTAL WHERE SERV_NO != 'N/A' ORDER BY TOTAL_TIMES DESC) t limit 10 ";
        }
        return sw
                .buildQuery()
                .sql(sql)
                .doQuery(RpServTotal.class);
    }

    public List<RpServTotal> getAveragetime(){
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT * FROM (SELECT * FROM RP_SERV_TOTAL WHERE SERV_NO != 'N/A' ORDER BY AVG_COST desc) WHERE ROWNUM <=10";

        }
        if("mysql".equals(dbType)){
            sql = "SELECT * FROM (SELECT * FROM RP_SERV_TOTAL WHERE SERV_NO != 'N/A' ORDER BY AVG_COST desc) t limit 10 ";
        }
        return sw
                .buildQuery()
                .sql(sql)
                .doQuery(RpServTotal.class);
    }

    public List<RpServTotal> getRate(){
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "select * from (select * from RP_SERV_TOTAL where serv_no != 'N/A' and total_times_f > 0 order by (total_times_f/total_times) desc) where rownum <=10";

        }
        if("mysql".equals(dbType)){
            sql = "select * from (select * from RP_SERV_TOTAL where serv_no != 'N/A' and total_times_f > 0 order by (total_times_f/total_times) desc) t limit 10 ";
        }
        return sw
                .buildQuery()
                .sql(sql)
                .doQuery(RpServTotal.class);
    }
    public List<Map<String, Object>> getServMinuteRunData(RpServHour rpServHour){
        return sw
                .buildQuery()
                .sql("SELECT sum(hour.total_times) as SUM_TOTAL,round(avg(hour.avg_cost),2) as AVG_COST,sum(hour.total_times_f) as SUM_TOTAL_F,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as DATES FROM RP_SERV_HOUR hour WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi'),serv_no having serv_no = #servNo order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
                .setVar("servNo",rpServHour.getServNo())
                .setVar("startTime",rpServHour.getStartTime())
                .setVar("endTime",rpServHour.getEndTime())
                .doQuery();
    }
    public List<Map<String, Object>> getServHoursRunData(RpServHour rpServHour){
        if(rpServHour.getStartTime() == null || rpServHour.getEndTime() ==null){
            String sql1 = null;
            if ("oracle".equals(dbType)){
                sql1 = "SELECT sum(hour.total_times) as SUM_TOTAL,round(avg(hour.avg_cost),2) as AVG_COST,sum(hour.total_times_f) as SUM_TOTAL_F,to_char(to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(avg(hour.DAY),concat(' ',hour.HOUR)))))),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24') as DATES FROM RP_SERV_HOUR hour WHERE YEAR = to_char(sysdate,'yyy') AND MONTH = to_char(sysdate,'mm') AND DAY = to_char(sysdate,'dd') AND HOUR >= to_char(sysdate-1,'hh24') AND HOUR <= to_char(sysdate,'hh24') group by  HOUR,serv_no having SERV_NO = #servNo order by hour.hour";
            }
            if ("mysql".equals(dbType)){
                sql1 = "SELECT sum(hour.total_times) as SUM_TOTAL,round(avg(hour.avg_cost),2) as AVG_COST,sum(hour.total_times_f) as SUM_TOTAL_F,date_format(str_to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(avg(hour.DAY),concat(' ',hour.HOUR)))))),'yyyy-mm-dd hh24:mi:ss'),'%Y-%m-%d %H') as DATES FROM RP_SERV_HOUR hour WHERE YEAR = date_format(CURRENT_TIMESTAMP,'%Y') AND MONTH = date_format(CURRENT_TIMESTAMP,'%m') AND DAY = date_format(CURRENT_TIMESTAMP,'%d') AND HOUR >= date_format(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'%H') AND HOUR <= date_format(CURRENT_TIMESTAMP,'%H') group by  HOUR,serv_no having SERV_NO = '' order by hour.hour";
            }
            return sw
                    .buildQuery()
                    .sql(sql1)
                    //.sql("SELECT * FROM RP_SERV_HOUR WHERE LAST_UPDATE_DATE >= (sysdate-1) AND LAST_UPDATE_DATE <= sysdate AND SERV_NO = #servNo ORDER BY LAST_UPDATE_DATE")
                    .setVar("servNo",rpServHour.getServNo())
                    .doQuery();
        }
        int year =Integer.parseInt(StringUtils.substringBefore(rpServHour.getStartTime(),"-"));
        int month = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServHour.getStartTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServHour.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServHour.getEndTime()," "),"-"));
        int startHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpServHour.getStartTime()," "),":"));
        int endHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpServHour.getEndTime()," "),":"));
       // System.out.println(year +" | "+ month +" | "+ startDay+" | "+endDay + " | "+ startHour +" | "+endHour);
        String sql2 = null;
        if ("oracle".equals(dbType)){
            sql2 = "SELECT sum(hour.total_times) as SUM_TOTAL,round(avg(hour.avg_cost),2) as AVG_COST,sum(hour.total_times_f) as SUM_TOTAL_F," +
                    " to_char(to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(avg(hour.DAY),concat(' ',hour.HOUR)))))),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24') as DATES " +
                    " FROM RP_SERV_HOUR hour WHERE YEAR = #year AND MONTH = #month AND ((DAY = #startDay AND HOUR >= #startHour) OR (DAY = #endDay AND HOUR <= #endHour))  group by DAY, HOUR,serv_no having SERV_NO = #servNo order by hour.hour";
        }
        if ("mysql".equals(dbType)){
            sql2 = "SELECT sum(hour.total_times) as SUM_TOTAL,round(avg(hour.avg_cost),2) as AVG_COST,sum(hour.total_times_f) as SUM_TOTAL_F," +
                    "date_format(str_to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(avg(hour.DAY),concat(' ',hour.HOUR)))))),'%Y-%m-%d %H:%i:%s'),'%Y-%m-%d %H') as DATES " +
                    "    FROM RP_SERV_HOUR hour WHERE YEAR = #year AND MONTH = #month AND ((DAY = #startDay AND HOUR >= #startHour) OR (DAY = #endDay AND HOUR <= #endHour))  group by DAY, HOUR,serv_no having SERV_NO = #servNo order by hour.hour";
        }
        return sw
                .buildQuery()
                //.sql("SELECT sum(hour.total_times) as SUM_TOTAL,round(avg(hour.avg_cost),2) as AVG_COST,sum(hour.total_times_f) as SUM_TOTAL_F,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as DATES FROM RP_SERV_HOUR hour WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERV_NO = #servNo group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
                .sql(sql2)
                .setVar("servNo",rpServHour.getServNo())
                .setVar("year",year)
                .setVar("month",month)
                .setVar("startDay",startDay)
                .setVar("endDay",endDay)
                .setVar("startHour",startHour)
                .setVar("endHour",endHour)
                .doQuery();
    }

    public List<Map<String, Object>> getServDayRunData(RpServDay rpServDay){
        int year =Integer.parseInt(StringUtils.substringBefore(rpServDay.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServDay.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServDay.getEndTime(),"-"),"-"));
        int statDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServDay.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServDay.getEndTime()," "),"-"));
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT sum(day.total_times) as SUM_TOTAL,round(avg(day.avg_cost),2) as AVG_COST,sum(day.total_times_f) as SUM_TOTAL_F," +
                    " avg(day.MONTH) as month,to_char(to_date(concat(avg(day.YEAR),concat('-',concat(avg(day.MONTH),concat('-',day.day)))),'yyyy-mm-dd')," +
                    " 'yyyy-mm-dd') as DATES FROM RP_SERV_DAY day WHERE YEAR = #year AND (MONTH = #startMonth AND DAY >= #startDay) or (MONTH = #endMonth " +
                    " AND DAY <= #endDay)  group by MONTH, DAY,serv_no having SERV_NO = #servNo order by day.DAY";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT sum(day.total_times) as SUM_TOTAL,round(avg(day.avg_cost),2) as AVG_COST,sum(day.total_times_f) as SUM_TOTAL_F," +
                    " avg(day.MONTH) as month,date_format(str_to_date(concat(avg(day.YEAR),concat('-',concat(avg(day.MONTH),concat('-',day.day)))),'%Y-%m-%d'),'%Y-%m-%d') as DATES FROM RP_SERV_DAY day WHERE YEAR = #year AND (MONTH = #startMonth AND DAY >= #startDay) or (MONTH = #endMonth AND DAY <= #endDay)  group by MONTH, DAY,serv_no having SERV_NO = #servNo order by day.DAY";
        }
        return sw
                .buildQuery()
               // .sql("SELECT sum(day.total_times) as SUM_TOTAL,round(avg(day.avg_cost),2) as AVG_COST,sum(day.total_times_f) as SUM_TOTAL_F,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as DATES FROM RP_SERV_DAY day WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERV_NO = #servNo group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
                .sql(sql)
               // .sql("SELECT * FROM RP_SERV_DAY WHERE LAST_UPDATE_DATE BETWEEN to_date(#startTime,'yyyy/mm/dd hh24:mi:ss') AND to_date(#endTime,'yyyy/mm/dd hh24:mi:ss') AND SERV_NO = #servNo ORDER BY LAST_UPDATE_DATE")
                .setVar("servNo",rpServDay.getServNo())
                .setVar("year",year)
                .setVar("startMonth",startMonth)
                .setVar("endMonth",endMonth)
                .setVar("startDay",statDay)
                .setVar("endDay",endDay)
                .doQuery();
    }

    public List<Map<String, Object>> getServYearRunData(RpServYear rpServYear){
      return null;
    }

    public List<Map<String, Object>> getServMonthRunData(RpServMonth rpServMonth){
        int year =Integer.parseInt(StringUtils.substringBefore(rpServMonth.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServMonth.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServMonth.getEndTime(),"-"),"-"));
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT sum(month.total_times) as SUM_TOTAL,round(avg(month.avg_cost),2) as AVG_COST,sum(month.total_times_f) as SUM_TOTAL_F," +
                    " to_char(to_date(concat(avg(month.YEAR),concat('-',avg(month.MONTH))),'yyyy-mm'),'yyyy-mm') as DATES FROM RP_SERV_MONTH month " +
                    " WHERE YEAR = #year AND MONTH >= #startMonth AND MONTH <= #endMonth AND SERV_NO = #servNo group by YEAR,MONTH order by month.MONTH";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT sum(month.total_times) as SUM_TOTAL,round(avg(month.avg_cost),2) as AVG_COST,sum(month.total_times_f) as SUM_TOTAL_F, concat(MONTH.YEAR,'-',LPAD(MONTH.MONTH,2,'0'))  as DATES FROM RP_SERV_MONTH month " +
                    "  WHERE YEAR = #year AND MONTH >= #startMonth AND MONTH <= #endMonth AND SERV_NO = #servNo group by YEAR,MONTH order by month.MONTH";
        }
        return sw
                .buildQuery()
                .sql(sql)
               // .sql("SELECT * FROM RP_SERV_MONTH WHERE LAST_UPDATE_DATE BETWEEN to_date(#startTime,'yyyy/mm/dd hh24:mi:ss') AND to_date(#endTime,'yyyy/mm/dd hh24:mi:ss') AND SERV_NO = #servNo ORDER BY LAST_UPDATE_DATE")
                .setVar("servNo",rpServMonth.getServNo())
                .setVar("year",year)
                .setVar("startMonth",startMonth)
                .setVar("endMonth",endMonth)
                .doQuery();
    }

    public List<ServicesStaticViewVO> getAllServDayTimes(RpServDay rpServDay){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpServDay.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpServDay.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServDay.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServDay.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServDay.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServDay.getEndTime()," "),"-"));
        String view = "";
        if (startMonth != endMonth){
            view = "all_day_or_view";
        } else {
            view = "all_day_and_view";
        }
        PageQueryResult<ServicesStaticViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .orderBy("TOTAL_TIMES", "DESC")
                .doPageQuery(1, 10, ServicesStaticViewVO.class);
        List<ServicesStaticViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<ServicesStaticViewVO> getAllServMonthTimes(RpServMonth rpServMonth){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpServMonth.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpServMonth.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServMonth.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServMonth.getEndTime(),"-"),"-"));
        String view = "";
        if (startMonth != endMonth){
            view = "all_month_or_view";
        } else {
            view = "all_month_and_view";
        }
        PageQueryResult<ServicesStaticViewVO> queryResult = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .orderBy("TOTAL_TIMES", "DESC")
                .doPageQuery(1, 10, ServicesStaticViewVO.class);
        List<ServicesStaticViewVO> result = queryResult.getResult();
        return result;
    }

    public List<ServicesStaticViewVO> getAllServHourTimes(RpServHour rpServHour ){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpServHour.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpServHour.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServHour.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServHour.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServHour.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServHour.getEndTime()," "),"-"));
        int startHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpServHour.getStartTime()," "),":"));
        int endHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpServHour.getEndTime()," "),":"));
        String view = "";
        if (startDay != endDay){
            view = "all_hour_or_view";
        } else {
            view = "all_hour_and_view";
        }
        PageQueryResult<ServicesStaticViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .setVar("startHour", startHour)
                .setVar("endHour", endHour)
                .orderBy("TOTAL_TIMES", "DESC")
                .doPageQuery(1, 10, ServicesStaticViewVO.class);
        List<ServicesStaticViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<ServicesStaticViewVO> getAllServDayRate(RpServDay rpServDay){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpServDay.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpServDay.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServDay.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServDay.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServDay.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServDay.getEndTime()," "),"-"));
        String view = "";
        if (startMonth != endMonth){
            view = "all_day_or_view";
        } else {
            view = "all_day_and_view";
        }
        PageQueryResult<ServicesStaticViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .orderBy("FAIL_RATE", "DESC")
                .doPageQuery(1, 10, ServicesStaticViewVO.class);
        List<ServicesStaticViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<ServicesStaticViewVO> getAllServMonthRate(RpServMonth rpServMonth){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpServMonth.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpServMonth.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServMonth.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServMonth.getEndTime(),"-"),"-"));
        String view = "";
        if (startMonth != endMonth){
            view = "all_month_or_view";
        } else {
            view = "all_month_and_view";
        }

        PageQueryResult<ServicesStaticViewVO> queryResult = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .orderBy("FAIL_RATE", "DESC")
                .doPageQuery(1, 10, ServicesStaticViewVO.class);
        List<ServicesStaticViewVO> result = queryResult.getResult();
        return result;
    }

    public List<ServicesStaticViewVO> getAllServHourRate(RpServHour rpServHour ){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpServHour.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpServHour.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServHour.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServHour.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServHour.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServHour.getEndTime()," "),"-"));
        int startHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpServHour.getStartTime()," "),":"));
        int endHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpServHour.getEndTime()," "),":"));
        String view = "";
        if (startDay != endDay){
            view = "all_hour_or_view";
        } else {
            view = "all_hour_and_view";
        }
        PageQueryResult<ServicesStaticViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .setVar("startHour", startHour)
                .setVar("endHour", endHour)
                .orderBy("FAIL_RATE", "DESC")
                .doPageQuery(1, 10, ServicesStaticViewVO.class);
        List<ServicesStaticViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<ServicesStaticViewVO> getAllServDayAvgCost(RpServDay rpServDay){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpServDay.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpServDay.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServDay.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServDay.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServDay.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServDay.getEndTime()," "),"-"));
        String view = "";
        if (startMonth != endMonth){
            view = "all_day_or_view";
        } else {
            view = "all_day_and_view";
        }

        PageQueryResult<ServicesStaticViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .orderBy("AVG_COST", "DESC")
                .doPageQuery(1, 10, ServicesStaticViewVO.class);
        List<ServicesStaticViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<ServicesStaticViewVO> getAllServMonthAvgCost(RpServMonth rpServMonth){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpServMonth.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpServMonth.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServMonth.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServMonth.getEndTime(),"-"),"-"));
        String view = "";
        if (startMonth != endMonth){
            view = "all_month_or_view";
        } else {
            view = "all_month_and_view";
        }
        PageQueryResult<ServicesStaticViewVO> queryResult = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .orderBy("AVG_COST", "DESC")
                .doPageQuery(1, 10, ServicesStaticViewVO.class);
        List<ServicesStaticViewVO> result = queryResult.getResult();
        return result;
    }

    public List<ServicesStaticViewVO> getAllServHourAvgCost(RpServHour rpServHour ){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpServHour.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpServHour.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServHour.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpServHour.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServHour.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpServHour.getEndTime()," "),"-"));
        int startHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpServHour.getStartTime()," "),":"));
        int endHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpServHour.getEndTime()," "),":"));
        String view = "";
        if (startDay != endDay){
            view = "all_hour_or_view";
        } else {
            view = "all_hour_and_view";
        }
        PageQueryResult<ServicesStaticViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .setVar("startHour", startHour)
                .setVar("endHour", endHour)
                .orderBy("AVG_COST", "DESC")
                .doPageQuery(1, 10, ServicesStaticViewVO.class);
        List<ServicesStaticViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<ServicesStaticViewVO> getAllServYearAvgCost(RpServYear rpServYear){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpServYear.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpServYear.getEndTime(),"-"));
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "select * from (select SERV_NO, sum(TOTAL_TIMES*AVG_COST) as sum_cost_time, sum(TOTAL_TIMES) as SUM_TOTAL_times , " +
                    " sum(TOTAL_TIMES*AVG_COST)/sum(TOTAL_TIMES) as AVG_COST_time from RP_SERV_YEAR where " +
                    " YEAR >= #startYear AND YEAR <= #endYear AND TOTAL_TIMES <> '0' " +
                    " group by SERV_NO) where ROWNUM <=10 order by avg_cost_time DESC";
        }
        if ("mysql".equals(dbType)){
            sql = "select * from (select SERV_NO, sum(TOTAL_TIMES*AVG_COST) as sum_cost_time, sum(TOTAL_TIMES) as SUM_TOTAL_times , " +
                    " sum(TOTAL_TIMES*AVG_COST)/sum(TOTAL_TIMES) as AVG_COST_time from RP_SERV_YEAR where " +
                    " YEAR >= #startYear AND YEAR <= #endYear AND TOTAL_TIMES <> '0' " +
                    " group by SERV_NO) t  order by avg_cost_time DESC limit 10";
        }
        List<Map<String, Object>> mapList = sw.buildQuery()
                .sql(sql)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .doQuery();
        return null;
    }

    /**
     * 查询所有的服务编号
     * @return
     */
    public List<Map<String,Object>> getAllServerName(){
        return sw.buildQuery()
                .sql("SELECT SERV_NO, SERV_NAME FROM dsgc_services")
                .doQuery();
    }
}
