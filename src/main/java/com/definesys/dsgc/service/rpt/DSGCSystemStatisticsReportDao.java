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
public class DSGCSystemStatisticsReportDao {

    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;
    @Value("${database.type}")
    private String dbType;

    public List<RpSysTotal> getSysRunTotalNum(){
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "select v.*,sys.sys_name from (\n" +
                    "SELECT t.receive,sum(t.total_times) as totalTimes\n" +
                    "FROM RP_SYS_TOTAL t \n" +
                    "group by receive ) v, dsgc_system_entities sys\n" +
                    "where rownum <=10 and v.receive = sys.sys_code ORDER BY totalTimes DESC";
        }
        if ("mysql".equals(dbType)){
            sql = "select v.*,sys.sys_name from (\n" +
                    "SELECT t.receive,sum(t.total_times) as totalTimes\n" +
                    "FROM RP_SYS_TOTAL t \n" +
                    "group by receive ) v, dsgc_system_entities sys\n" +
                    "where  v.receive = sys.sys_code ORDER BY totalTimes DESC limit 10 ";
        }
        return sw
                .buildQuery()
//                .sql("select * from (SELECT * FROM RP_SYS_TOTAL ORDER BY TOTAL_TIMES DESC) where rownum <=10")
                .sql("select v.*,sys.sys_name from (\n" +
                        "SELECT t.receive,sum(t.total_times) as totalTimes\n" +
                        "FROM RP_SYS_TOTAL t \n" +
                        "group by receive ) v, dsgc_system_entities sys\n" +
                        "where rownum <=10 and v.receive = sys.sys_code ORDER BY totalTimes DESC")
                .doQuery(RpSysTotal.class);
    }

    public List<RpSysTotal> getSysAveragetime(){
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT v.*,sys.sys_name FROM ( " +
                    " SELECT t.receive,(sum(t.avg_cost)/count(*)) as avgCost  FROM RP_SYS_TOTAL t " +
                    " group by receive) v,dsgc_system_entities sys  WHERE ROWNUM <=10 and v.receive = sys.sys_code ORDER BY avgCost desc";

        }
        if ("mysql".equals(dbType)){
            sql = "SELECT v.*,sys.sys_name FROM (" +
                    "SELECT t.receive,(sum(t.avg_cost)/count(*)) as avgCost  FROM RP_SYS_TOTAL t " +
                    "group by receive) v,dsgc_system_entities sys  WHERE v.receive = sys.sys_code ORDER BY avgCost desc limit 10";
        }
        return sw
                .buildQuery()
//                .sql("SELECT * FROM (SELECT * FROM RP_SYS_TOTAL  ORDER BY AVG_COST) WHERE ROWNUM <=10")
                .sql(sql)
                .doQuery(RpSysTotal.class);
    }

    public List<RpSysTotal> getSysRate(){
        String sql = null;
        if ("oracle".equals(dbType)){
            sql = "select v.*,sys.sys_name from (" +
                    " select t.receive, sum(t.total_times) as totalTimes,sum(t.total_times_f) as totalTimesF, " +
                    " sum(t.total_times_f)/sum(t.total_times) as fileTemp from RP_SYS_TOTAL t " +
                    " group by t.receive) v, dsgc_system_entities sys " +
                    " where rownum <=10 and v.totalTimesF>0 and v.receive = sys.sys_code " +
                    " order by v.fileTemp desc";
        }
        if ("mysql".equals(dbType)){
            sql = "select v.*,sys.sys_name from ( " +
                    " select t.receive, sum(t.total_times) as totalTimes,sum(t.total_times_f) as totalTimesF, " +
                    " sum(t.total_times_f)/sum(t.total_times) as fileTemp from RP_SYS_TOTAL t " +
                    " group by t.receive) v, dsgc_system_entities sys " +
                    " where  v.totalTimesF>0 and v.receive = sys.sys_code " +
                    " order by v.fileTemp desc limit 10 ";
        }
        return sw
                .buildQuery()
//                .sql("select * from (select * from RP_SYS_TOTAL where total_times_f > 0  order by (total_times_f/total_times) desc) where rownum <=10")
                .sql(sql)
                .doQuery(RpSysTotal.class);
    }
    public List<Map<String, Object>> getSysMinuteRunData(RpSysHour rpSysHour){
        return sw
                .buildQuery()
                .sql("SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM RP_SYS_HOUR hour WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND RECEIVE = #systemName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
                .setVar("systemName",rpSysHour.getReceive())
                .setVar("startTime",rpSysHour.getStartTime())
                .setVar("endTime",rpSysHour.getEndTime())
                .doQuery();
    }



    public List<Map<String, Object>> getSysHoursRunData(RpSysHour rpSysHour){
        if(rpSysHour.getStartTime() == null || rpSysHour.getEndTime() ==null){
            String sql = null;
            if("oracle".equals(dbType)){
                sql = "SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,to_char(to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(avg(hour.DAY),concat(' ',hour.HOUR)))))),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24') as dates FROM RP_SYS_HOUR hour WHERE YEAR = to_char(sysdate,'yyy') AND MONTH = to_char(sysdate,'mm') AND DAY = to_char(sysdate,'dd') AND HOUR >= to_char(sysdate-1,'hh24') AND HOUR <= to_char(sysdate,'hh24')  group by HOUR,RECEIVE having RECEIVE = #systemName order by hour.hour";
            }
            if("mysql".equals(dbType)){
              sql = "SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,date_format(str_to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(avg(hour.DAY),concat(' ',hour.HOUR)))))),'%Y-%m-%d %H:%i:%s'),'%Y-%m-%d %H') as dates FROM RP_SYS_HOUR hour WHERE YEAR = date_format(CURRENT_TIMESTAMP,'%Y') AND MONTH = date_format(CURRENT_TIMESTAMP,'%m') AND DAY = date_format(CURRENT_TIMESTAMP,'$d') AND HOUR >= date_format(CURRENT_TIMESTAMP-1,'%H') AND HOUR <= date_format(CURRENT_TIMESTAMP,'%H')  group by HOUR,RECEIVE having RECEIVE = #systemName order by hour.hour";
            }
            return sw
                    .buildQuery()
                    .sql(sql)
                    //.sql("SELECT * FROM RP_SERV_HOUR WHERE LAST_UPDATE_DATE >= (sysdate-1) AND LAST_UPDATE_DATE <= sysdate AND SERV_NO = #servNo ORDER BY LAST_UPDATE_DATE")
                    .setVar("systemName",rpSysHour.getReceive())
                    .doQuery();
        }
        int year =Integer.parseInt(StringUtils.substringBefore(rpSysHour.getStartTime(),"-"));
        int month = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysHour.getStartTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysHour.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysHour.getEndTime()," "),"-"));
        int startHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpSysHour.getStartTime()," "),":"));
        int endHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpSysHour.getEndTime()," "),":"));
       // System.out.println(year +" | "+ month +" | "+ startDay+" | "+endDay + " | "+ startHour +" | "+endHour);
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,to_char(to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(avg(hour.DAY),concat(' ',hour.HOUR)))))),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24') as dates FROM RP_SYS_HOUR hour WHERE YEAR = #year AND MONTH = #month AND ((DAY = #startDay AND HOUR > #startHour) OR (DAY = #endDay AND HOUR <= #endHour)) group by HOUR,RECEIVE having RECEIVE = #systemName order by hour.hour";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,date_format(str_to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(avg(hour.DAY),concat(' ',hour.HOUR)))))),'%Y-%m-%d %H:%i:%s'),'%Y-%m-%d %H') as dates FROM RP_SYS_HOUR hour WHERE YEAR = #year AND MONTH = #month AND ((DAY = #startDay AND HOUR > #startHour) OR (DAY = #endDay AND HOUR <= #endHour)) group by HOUR,RECEIVE having RECEIVE = #systemName order by hour.hour";
        }
        return sw
                .buildQuery()
                //.sql("SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM RP_SERV_HOUR hour WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERV_NO = #servNo group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
                .sql(sql)
                .setVar("systemName",rpSysHour.getReceive())
                .setVar("year",year)
                .setVar("month",month)
                .setVar("startDay",startDay)
                .setVar("endDay",endDay)
                .setVar("startHour",startHour)
                .setVar("endHour",endHour)
                .doQuery();
    }

    public List<Map<String, Object>> getSysDayRunData(RpSysDay rpSysDay){
        int year =Integer.parseInt(StringUtils.substringBefore(rpSysDay.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysDay.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysDay.getEndTime(),"-"),"-"));
        int statDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysDay.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysDay.getEndTime()," "),"-"));
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT sum(day.total_times) as sum_total,round(avg(day.avg_cost),2) as avg_cost,sum(day.total_times_f) as sum_total_f,avg(day.MONTH) as month,to_char(to_date(concat(avg(day.YEAR),concat('-',concat(avg(day.MONTH),concat('-',day.day)))),'yyyy-mm-dd'),'yyyy-mm-dd') as dates FROM RP_SYS_DAY day WHERE YEAR = #year AND (MONTH = #startMonth AND DAY > #startDay) or (MONTH = #endMonth AND DAY <= #endDay)  group by DAY , receive having RECEIVE = #systemName order by day.DAY";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT sum(day.total_times) as sum_total,round(avg(day.avg_cost),2) as avg_cost,sum(day.total_times_f) as sum_total_f,avg(day.MONTH) as month,date_format(str_to_date(concat(avg(day.YEAR),concat('-',concat(avg(day.MONTH),concat('-',day.day)))),'%Y-%m-%d'),'%Y-%m-%d') as dates FROM RP_SYS_DAY day WHERE YEAR = #year AND (MONTH = #startMonth AND DAY > #startDay) or (MONTH = #endMonth AND DAY <= #endDay)  group by DAY , receive having RECEIVE = #systemName order by day.DAY";
        }
        return sw
                .buildQuery()
               // .sql("SELECT sum(day.total_times) as sum_total,round(avg(day.avg_cost),2) as avg_cost,sum(day.total_times_f) as sum_total_f,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM RP_SERV_DAY day WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERV_NO = #servNo group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
                .sql(sql)
               // .sql("SELECT * FROM RP_SERV_DAY WHERE LAST_UPDATE_DATE BETWEEN to_date(#startTime,'yyyy/mm/dd hh24:mi:ss') AND to_date(#endTime,'yyyy/mm/dd hh24:mi:ss') AND SERV_NO = #servNo ORDER BY LAST_UPDATE_DATE")
                .setVar("systemName",rpSysDay.getReceive())
                .setVar("year",year)
                .setVar("startMonth",startMonth)
                .setVar("endMonth",endMonth)
                .setVar("startDay",statDay)
                .setVar("endDay",endDay)
                .doQuery();
    }

    public List<Map<String, Object>> getSysMonthRunData(RpSysMonth rpSysMonth){
        int year =Integer.parseInt(StringUtils.substringBefore(rpSysMonth.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysMonth.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysMonth.getEndTime(),"-"),"-"));
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT sum(month.total_times) as sum_total,round(avg(month.avg_cost),2) as avg_cost,sum(month.total_times_f) as sum_total_f,to_char(to_date(concat(avg(month.YEAR),concat('-',avg(month.MONTH))),'yyyy-mm'),'yyyy-mm') as dates FROM RP_SYS_MONTH month WHERE YEAR = #year AND MONTH >= #startMonth AND MONTH <= #endMonth group by MONTH,receive having RECEIVE = #systemName  order by month.MONTH";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT sum(month.total_times) as sum_total,round(avg(month.avg_cost),2) as avg_cost,sum(month.total_times_f) as sum_total_f,date_format(str_to_date(concat(avg(month.YEAR),concat('-',avg(month.MONTH))),'%Y-%m'),'%Y-%m') as dates FROM RP_SYS_MONTH month WHERE YEAR = #year AND MONTH >= #startMonth AND MONTH <= #endMonth group by MONTH,receive having RECEIVE = #systemName  order by month.MONTH";
        }
        return sw
                .buildQuery()
                .sql(sql)
               // .sql("SELECT * FROM RP_SERV_MONTH WHERE LAST_UPDATE_DATE BETWEEN to_date(#startTime,'yyyy/mm/dd hh24:mi:ss') AND to_date(#endTime,'yyyy/mm/dd hh24:mi:ss') AND SERV_NO = #servNo ORDER BY LAST_UPDATE_DATE")
                .setVar("systemName",rpSysMonth.getReceive())
                .setVar("year",year)
                .setVar("startMonth",startMonth)
                .setVar("endMonth",endMonth)
                .doQuery();
    }







    public List<Map<String, Object>> getReqSysHoursRunData(RpSysHour rpSysHour){
        if(rpSysHour.getStartTime() == null || rpSysHour.getEndTime() ==null){
            String sql = null;
            if("oracle".equals(dbType)){
                sql = "SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,to_char(to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(hour.DAY,concat(' ',hour.HOUR)))))),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24') as dates FROM RP_SYS_HOUR hour WHERE YEAR = to_char(sysdate,'yyy') AND MONTH = to_char(sysdate,'mm') AND DAY = to_char(sysdate,'dd') AND HOUR >= to_char(sysdate-1,'hh24') AND HOUR <= to_char(sysdate,'hh24') group by HOUR,day,client having CLIENT = #client order by hour.hour";

            }
            if ("mysql".equals(dbType)){
                sql = "SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,date_format(str_to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(hour.DAY,concat(' ',hour.HOUR)))))),'%Y-%m-%d %H:%i:%s'),'%Y-%m-%d %H') as dates FROM RP_SYS_HOUR hour WHERE YEAR = date_format(CURRENT_TIMESTAMP,'%Y') AND MONTH = date_format(CURRENT_TIMESTAMP,'%m') AND DAY = date_format(CURRENT_TIMESTAMP,'%d') AND HOUR >= date_format(CURRENT_TIMESTAMP-1,'%H') AND HOUR <= date_format(CURRENT_TIMESTAMP,'%H') group by HOUR,day,client having CLIENT = #client order by hour.hour";
            }
            return sw
                    .buildQuery()
                    .sql(sql)
                    //.sql("SELECT * FROM RP_SERV_HOUR WHERE LAST_UPDATE_DATE >= (sysdate-1) AND LAST_UPDATE_DATE <= sysdate AND SERV_NO = #servNo ORDER BY LAST_UPDATE_DATE")
                    .setVar("client",rpSysHour.getClient())
                    .doQuery();
        }
        int year =Integer.parseInt(StringUtils.substringBefore(rpSysHour.getStartTime(),"-"));
        int month = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysHour.getStartTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysHour.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysHour.getEndTime()," "),"-"));
        int startHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpSysHour.getStartTime()," "),":"));
        int endHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpSysHour.getEndTime()," "),":"));
        // System.out.println(year +" | "+ month +" | "+ startDay+" | "+endDay + " | "+ startHour +" | "+endHour);
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,to_char(to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(hour.DAY,concat(' ',hour.HOUR)))))),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24') as dates FROM RP_SYS_HOUR hour WHERE YEAR = #year AND MONTH = #month AND ((DAY = #startDay AND HOUR >= #startHour) OR (DAY = #endDay AND HOUR <= #endHour))  group by HOUR,day,client having CLIENT = #client order by hour.hour";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,date_format(str_to_date(concat(avg(hour.YEAR),concat('-',concat(avg(hour.MONTH),concat('-',concat(hour.DAY,concat(' ',hour.HOUR)))))),'%Y-%m-%d %H:%i:%s'),'%Y-%m-%d %H') as dates FROM RP_SYS_HOUR hour WHERE YEAR = #year AND MONTH = #month AND ((DAY = #startDay AND HOUR >= #startHour) OR (DAY = #endDay AND HOUR <= #endHour))  group by HOUR,day,client having CLIENT = #client order by hour.hour";
        }
        return sw
                .buildQuery()
                //.sql("SELECT sum(hour.total_times) as sum_total,round(avg(hour.avg_cost),2) as avg_cost,sum(hour.total_times_f) as sum_total_f,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM RP_SERV_HOUR hour WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERV_NO = #servNo group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
                .sql(sql)
                .setVar("client",rpSysHour.getClient())
                .setVar("year",year)
                .setVar("month",month)
                .setVar("startDay",startDay)
                .setVar("endDay",endDay)
                .setVar("startHour",startHour)
                .setVar("endHour",endHour)
                .doQuery();
    }

    public List<Map<String, Object>> getReqSysDayRunData(RpSysDay rpSysDay){
        int year =Integer.parseInt(StringUtils.substringBefore(rpSysDay.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysDay.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysDay.getEndTime(),"-"),"-"));
        int statDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysDay.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysDay.getEndTime()," "),"-"));
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT sum(day.total_times) as sum_total,round(avg(day.avg_cost),2) as avg_cost,sum(day.total_times_f) as sum_total_f,avg(day.MONTH) as month,to_char(to_date(concat(avg(day.YEAR),concat('-',concat(avg(day.MONTH),concat('-',day.day)))),'yyyy-mm-dd'),'yyyy-mm-dd') as dates FROM RP_SYS_DAY day WHERE YEAR = #year AND ((MONTH = #startMonth AND DAY >= #startDay) or (MONTH = #endMonth AND DAY <= #endDay))  group by DAY ,client having day.CLIENT = #client order by day.DAY";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT sum(day.total_times) as sum_total,round(avg(day.avg_cost),2) as avg_cost,sum(day.total_times_f) as sum_total_f,avg(day.MONTH) as month,date_format(str_to_date(concat(avg(day.YEAR),concat('-',concat(avg(day.MONTH),concat('-',day.day)))),'%Y-%m-%d'),'%Y-%m-%d') as dates FROM RP_SYS_DAY day WHERE YEAR = #year AND ((MONTH = #startMonth AND DAY >= #startDay) or (MONTH = #endMonth AND DAY <= #endDay))  group by DAY ,client having day.CLIENT = #client order by day.DAY";
        }
        return sw
                .buildQuery()
                // .sql("SELECT sum(day.total_times) as sum_total,round(avg(day.avg_cost),2) as avg_cost,sum(day.total_times_f) as sum_total_f,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM RP_SERV_DAY day WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERV_NO = #servNo group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
                .sql(sql)
                // .sql("SELECT * FROM RP_SERV_DAY WHERE LAST_UPDATE_DATE BETWEEN to_date(#startTime,'yyyy/mm/dd hh24:mi:ss') AND to_date(#endTime,'yyyy/mm/dd hh24:mi:ss') AND SERV_NO = #servNo ORDER BY LAST_UPDATE_DATE")
                .setVar("client",rpSysDay.getClient())
                .setVar("year",year)
                .setVar("startMonth",startMonth)
                .setVar("endMonth",endMonth)
                .setVar("startDay",statDay)
                .setVar("endDay",endDay)
                .doQuery();
    }

    public List<Map<String, Object>> getReqSysMonthRunData(RpSysMonth rpSysMonth){
        int year =Integer.parseInt(StringUtils.substringBefore(rpSysMonth.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysMonth.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysMonth.getEndTime(),"-"),"-"));
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT sum(month.total_times) as sum_total,round(avg(month.avg_cost),2) as avg_cost,sum(month.total_times_f) as sum_total_f,to_char(to_date(concat(avg(month.YEAR),concat('-',avg(month.MONTH))),'yyyy-mm'),'yyyy-mm') as dates FROM RP_SYS_MONTH month WHERE YEAR = #year AND MONTH >= #startMonth AND MONTH <= #endMonth  group by MONTH,client having CLIENT = #client order by month.MONTH";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT sum(month.total_times) as sum_total,round(avg(month.avg_cost),2) as avg_cost,sum(month.total_times_f) as sum_total_f,date_format(str_to_date(concat(avg(month.YEAR),concat('-',avg(month.MONTH))),'%Y-%m'),'%Y-%m') as dates FROM RP_SYS_MONTH month WHERE YEAR = #year AND MONTH >= #startMonth AND MONTH <= #endMonth  group by MONTH,client having CLIENT = #client order by month.MONTH";
        }
        return sw
                .buildQuery()
                .sql(sql)
                // .sql("SELECT * FROM RP_SERV_MONTH WHERE LAST_UPDATE_DATE BETWEEN to_date(#startTime,'yyyy/mm/dd hh24:mi:ss') AND to_date(#endTime,'yyyy/mm/dd hh24:mi:ss') AND SERV_NO = #servNo ORDER BY LAST_UPDATE_DATE")
                .setVar("client",rpSysMonth.getClient())
                .setVar("year",year)
                .setVar("startMonth",startMonth)
                .setVar("endMonth",endMonth)
                .doQuery();
    }

    public List<SystemReportDataViewVO> getAllSysDayTimes(RpSysDay rpSysDay){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpSysDay.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpSysDay.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysDay.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysDay.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysDay.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysDay.getEndTime()," "),"-"));
        String view = "";
        if (startMonth != endMonth){
            view = "all_day_or_view";
        } else {
            view = "all_day_and_view";
        }
        PageQueryResult<SystemReportDataViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .orderBy("TOTAL_TIMES", "DESC")
                .doPageQuery(1, 10, SystemReportDataViewVO.class);
        List<SystemReportDataViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<SystemReportDataViewVO> getAllSysMonthTimes(RpSysMonth rpSysMonth){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpSysMonth.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpSysMonth.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysMonth.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysMonth.getEndTime(),"-"),"-"));
        String view = "";
        if (startYear != endYear){
            view = "all_month_or_view";
        } else {
            view = "all_month_and_view";
        }
        PageQueryResult<SystemReportDataViewVO> queryResult = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .orderBy("TOTAL_TIMES", "DESC")
                .doPageQuery(1, 10, SystemReportDataViewVO.class);
        List<SystemReportDataViewVO> result = queryResult.getResult();
        return result;
    }

    public List<SystemReportDataViewVO> getAllSysHourTimes(RpSysHour rpSysHour ){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpSysHour.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpSysHour.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysHour.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysHour.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysHour.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysHour.getEndTime()," "),"-"));
        int startHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpSysHour.getStartTime()," "),":"));
        int endHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpSysHour.getEndTime()," "),":"));
        String view = "";
        if (startDay != endDay){
            view = "all_hour_or_view";
        } else {
            view = "all_hour_and_view";
        }
        PageQueryResult<SystemReportDataViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .setVar("startHour", startHour)
                .setVar("endHour", endHour)
                .orderBy("TOTAL_TIMES", "DESC")
                .doPageQuery(1, 10, SystemReportDataViewVO.class);
        List<SystemReportDataViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<SystemReportDataViewVO> getAllSysDayRate(RpSysDay rpSysDay){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpSysDay.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpSysDay.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysDay.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysDay.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysDay.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysDay.getEndTime()," "),"-"));
        String view = "";
        if (startMonth != endMonth){
            view = "all_day_or_view";
        } else {
            view = "all_day_and_view";
        }
        PageQueryResult<SystemReportDataViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .orderBy("FAIL_RATE", "DESC")
                .doPageQuery(1, 10, SystemReportDataViewVO.class);
        List<SystemReportDataViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<SystemReportDataViewVO> getAllSysMonthRate(RpSysMonth rpSysMonth){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpSysMonth.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpSysMonth.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysMonth.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysMonth.getEndTime(),"-"),"-"));
        String view = "";
        if (startYear != endYear){
            view = "all_month_or_view";
        } else {
            view = "all_month_and_view";
        }
        PageQueryResult<SystemReportDataViewVO> queryResult = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .orderBy("FAIL_RATE", "DESC")
                .doPageQuery(1, 10, SystemReportDataViewVO.class);
        List<SystemReportDataViewVO> result = queryResult.getResult();
        return result;
    }

    public List<SystemReportDataViewVO> getAllSysHourRate(RpSysHour rpSysHour ){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpSysHour.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpSysHour.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysHour.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysHour.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysHour.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysHour.getEndTime()," "),"-"));
        int startHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpSysHour.getStartTime()," "),":"));
        int endHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpSysHour.getEndTime()," "),":"));
        String view = "";
        if (startDay != endDay){
            view = "all_hour_or_view";
        } else {
            view = "all_hour_and_view";
        }
        PageQueryResult<SystemReportDataViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .setVar("startHour", startHour)
                .setVar("endHour", endHour)
                .orderBy("FAIL_RATE", "DESC")
                .doPageQuery(1, 10, SystemReportDataViewVO.class);
        List<SystemReportDataViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<SystemReportDataViewVO> getAllSysDayAvgCost(RpSysDay rpSysDay){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpSysDay.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpSysDay.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysDay.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysDay.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysDay.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysDay.getEndTime()," "),"-"));
        String view = "";
        if (startMonth != endMonth){
            view = "all_day_or_view";
        } else {
            view = "all_day_and_view";
        }
        PageQueryResult<SystemReportDataViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .orderBy("AVG_COST", "DESC")
                .doPageQuery(1, 10, SystemReportDataViewVO.class);
        List<SystemReportDataViewVO> result = pageQuery.getResult();
        return result;
    }

    public List<SystemReportDataViewVO> getAllSysMonthAvgCost(RpSysMonth rpSysMonth){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpSysMonth.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpSysMonth.getStartTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysMonth.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysMonth.getEndTime(),"-"),"-"));
        String view = "";
        if (startYear != endYear){
            view = "all_month_or_view";
        } else {
            view = "all_month_and_view";
        }
        PageQueryResult<SystemReportDataViewVO> queryResult = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .orderBy("AVG_COST", "DESC")
                .doPageQuery(1, 10, SystemReportDataViewVO.class);
        List<SystemReportDataViewVO> result = queryResult.getResult();
        return result;
    }

    public List<SystemReportDataViewVO> getAllSysHourAvgCost(RpSysHour rpSysHour ){
        int startYear =Integer.parseInt(StringUtils.substringBefore(rpSysHour.getStartTime(),"-"));
        int endYear =Integer.parseInt(StringUtils.substringBefore(rpSysHour.getEndTime(),"-"));
        int startMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysHour.getStartTime(),"-"),"-"));
        int endMonth = Integer.parseInt(StringUtils.substringBeforeLast( StringUtils.substringAfter(rpSysHour.getEndTime(),"-"),"-"));
        int startDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysHour.getStartTime()," "),"-"));
        int endDay = Integer.parseInt(StringUtils.substringAfterLast( StringUtils.substringBefore(rpSysHour.getEndTime()," "),"-"));
        int startHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpSysHour.getStartTime()," "),":"));
        int endHour = Integer.parseInt(StringUtils.substringBefore( StringUtils.substringAfterLast(rpSysHour.getEndTime()," "),":"));
        String view = "";
        if (startDay != endDay){
            view = "all_hour_or_view";
        } else {
            view = "all_hour_and_view";
        }
        PageQueryResult<SystemReportDataViewVO> pageQuery = sw.buildViewQuery(view)
                .setVar("startYear", startYear)
                .setVar("endYear", endYear)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .setVar("startHour", startHour)
                .setVar("endHour", endHour)
                .orderBy("AVG_COST", "DESC")
                .doPageQuery(1, 10, SystemReportDataViewVO.class);
        List<SystemReportDataViewVO> result = pageQuery.getResult();
        return result;
    }

}
