package com.definesys.dsgc.service.rpt;

import com.definesys.dsgc.service.rpt.bean.*;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ServiceStatisticsDao {
    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;


    public List<RpServTotal> getServiceCount(){
      return sw
              .buildQuery()
              .sql("SELECT * FROM RP_SERV_TOTAL WHERE ROWNUM <=20 ORDER BY TOTAL_TIMES DESC")
              .doQuery(RpServTotal.class);
    }

    public List<RpSysTotal> getSystemCount(){
      return sw
              .buildQuery()
              .sql("SELECT * FROM RP_SYS_TOTAL WHERE ROWNUM <=20 ORDER BY TOTAL_TIMES DESC")
              .doQuery(RpSysTotal.class);
    }

    public List<RpServHour> getInterfaceDetailsReportDay(String servNo){
        this.logger.debug(" servNo : "+servNo);
        return sw
                .buildQuery()
                .sql("select * from RP_SERV_HOUR  where SERV_NO=Upper(#servNo) and hour<=to_char(sysdate,'HH24')and day=to_char(sysdate,'DD') and month=to_char(sysdate,'MM')  and year=to_char(sysdate,'YYYY') order by hour")
                .setVar("servNo",servNo)
                .doQuery(RpServHour.class);
    }

    public List<RpServDay> getInterfaceDetailsReportMonth(String servNo){
        this.logger.debug(" servNo : "+servNo);
        return sw
                .buildQuery()
                .sql("select * from RP_SERV_DAY serv_day where serv_day.SERV_NO=Upper(#servNo) and serv_day.year=to_char(sysdate,'YYYY') and serv_day.month=to_char(sysdate,'MM') and serv_day.day<=to_char(sysdate,'DD') order by serv_day.day")
                .setVar("servNo",servNo)
                .doQuery(RpServDay.class);
    }

    public List<RpServMonth> getInterfaceDetailsReportYear(String servNo){
        this.logger.debug(" servNo : "+servNo);
        return sw
                .buildQuery()
                .sql("select * from RP_SERV_MONTH serv_month where serv_month.SERV_NO=Upper(#servNo) and serv_month.year=to_char(sysdate,'YYYY') and serv_month.month<=to_char(sysdate,'MM') order by serv_month.month")
                .setVar("servNo",servNo)
                .doQuery(RpServMonth.class);
    }


    public List<RpServYear> getInterfaceDetailsReport(String servNo){
        this.logger.debug(" servNo : "+servNo);
        return sw
                .buildQuery()
                .sql("select * from RP_SERV_YEAR serv_year where serv_year.SERV_NO=Upper(#servNo) and serv_year.year<=to_char(sysdate,'YYYY') order by serv_year.year")
                .setVar("servNo",servNo)
                .doQuery(RpServYear.class);
    }


    public List<RpSysHour> getSystemDetailsReportDay(String receive){
        this.logger.debug(" receive : "+receive);
        return sw
                .buildQuery()
                .sql("select * from RP_SYS_HOUR sys_hour where receive=Upper(#receive) and  sys_hour.hour<=to_char(sysdate,'HH24') and sys_hour.year=to_char(sysdate,'YYYY') and sys_hour.month=to_char(sysdate,'MM') and sys_hour.day=to_char(sysdate,'DD')   order by hour")
                .setVar("receive",receive)
                .doQuery(RpSysHour.class);
    }

    public List<RpSysDay> getSystemDetailsReportMonth(String receive){
        this.logger.debug(" receive : "+receive);
        return sw
                .buildQuery()
                .sql("select * from RP_SYS_DAY  sys_day where receive=Upper(#receive) and sys_day.year=to_char(sysdate,'YYYY') and sys_day.month=to_char(sysdate,'MM') and sys_day.day<=to_char(sysdate,'DD')  order by day")
                .setVar("receive",receive)
                .doQuery(RpSysDay.class);
    }


    public List<RpSysMonth> getSystemDetailsReportYear(String receive){
        this.logger.debug(" receive : "+receive);
        return sw
                .buildQuery()
                .sql("select * from RP_SYS_Month  sys_month where receive=Upper(#receive) and  sys_month.year=to_char(sysdate,'YYYY') and sys_month.month<=to_char(sysdate,'MM')  order by month")
                .setVar("receive",receive)
                .doQuery(RpSysMonth.class);
    }


    public List<RpSysYear> getSystemDetailsReport(String receive){
        this.logger.debug(" receive : "+receive);
        return sw
                .buildQuery()
                .sql("select * from RP_SYS_YEAR  sys_year where receive=Upper(#receive) and sys_year.year=to_char(sysdate,'YYYY')    order by year ")
                .setVar("receive",receive)
                .doQuery(RpSysYear.class);
    }

    public List<Map<String,Object>> getSystemGroup(){
        return sw
                .buildQuery()
                .sql("SELECT DISTINCT receive FROM RP_SYS_TOTAL")
                .doQuery();
    }
}
