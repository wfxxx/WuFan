package com.definesys.dsgc.service.ystar.report;

import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

/**
 * @ClassName: ReportAnalyzeDao
 * @Description: TODO
 * @Author：ystar
 * @Date : 2020/12/16 12:00
 */
@Repository("ReportAnalyzeDao")
public class ReportAnalyzeDao {

    @Value("${database.type}")
    private String dbType;

    @Autowired
    private MpaasQueryFactory sw;

    public List<Map<String, Object>> getWeekRpData(String startTime, String endTime) {
        //SQLLoader.sql(SqlUtils.SQL_RP_SVC_TRG_SYS_DATA);
        String sql = "select rp.trg_sys_code,sum(rp.TOTAL_TIMES) run_times from ( " +
                " select (select s.SUBORDINATE_SYSTEM from dsgc_services s where s.SERV_NO = r.SERV_NO) trg_sys_code ,";

        if ("oracle".equals(dbType)) {
            sql += " to_date((r.year|| '-'|| r.MONTH||'-'||r.DAY), 'yyyy-MM-dd') rp_date,r.TOTAL_TIMES " +
                    "   from rp_serv_day r where r.CLIENT <> 'N/A' and r.SERV_NO <> 'N/A') rp " +
                    "    where rp.rp_date between to_date(#startTime,'yyyy-MM-dd HH24:mi:ss')  and to_date(#endTime,'yyyy-MM-dd HH24:mi:ss') group by rp.trg_sys_code  ";
        } else if ("mysql".equals(dbType)) {
            sql += " str_to_date(CONCAT_WS('-',r.year,r.MONTH,r.DAY), '%Y-%m-%d') rp_date,r.TOTAL_TIMES " +
                    " from rp_serv_day r where r.CLIENT <> 'N/A' and r.SERV_NO <> 'N/A') rp " +
                    "            where rp.rp_date between str_to_date(#startTime,'%Y-%m-%d') " +
                    " and str_to_date(#endTime,'%Y-%m-%d') group by rp.trg_sys_code ";
        }
        return sw.buildQuery().sql(sql)
                .setVar("startTime", startTime)
                .setVar("endTime", endTime)
                .doQuery();
    }


    /**
     * 获取总耗时实例数
     */
    public List<Map<String, Object>> getSvcAllCostData(String startTime, String endTime, String techType) {
//        String sql = "select (select s.SUBORDINATE_SYSTEM from dsgc_services s where s.serv_no =  i.SERV_NO ) sys_code,";
//        if ("oracle".equals(dbType)) {
//            sql += " EXTRACT(SECOND FROM(i.end_time - i.start_time)) * 1000 as inst_cost from dsgc_log_instance i where i.SERV_NO <> 'N/A' and i.REQ_FROM <> 'N/A' " +
//                    " and  i.CREATION_DATE between TO_DATE(#startTime,'yyyy-MM-dd HH24:mi:ss') and TO_DATE(#endTime,'yyyy-MM-dd HH24:mi:ss') ";
//        } else if ("mysql".equals(dbType)) {
//            sql += " (UNIX_TIMESTAMP(i.END_TIME) - UNIX_TIMESTAMP(i.START_TIME))*1000 as inst_cost from dsgc_log_instance i where i.SERV_NO <> 'N/A' and i.REQ_FROM <> 'N/A' " +
//                    " and  i.CREATION_DATE  between STR_TO_DATE(#startTime,'%Y-%m-%d') and STR_TO_DATE(#endTime,'%Y-%m-%d') ";
//        }
        String sql = " select (select s.SUBORDINATE_SYSTEM from dsgc_services s where s.serv_no =  svc.svc_code ) as sys_code,svc.inst_cost  from (select r.SERV_NO svc_code ,";
        if ("oracle".equals(dbType)) {
            sql += " to_date(r.year||'-'||r.MONTH||'-'||r.DAY||' '||r.HOUR, 'yyyy-MM-dd HH24') as inst_date,r.AVG_COST as inst_cost " +
                    " from RP_SERV_HOUR r where r.CLIENT <> 'N/A' and r.SERV_NO <> 'N/A' #type# ) svc  " +
                    " where svc.INST_DATE between to_date(#startTime,'yyyy-MM-dd HH24:mi:ss')  and to_date(#endTime,'yyyy-MM-dd HH24:mi:ss')";
        } else if ("mysql".equals(dbType)) {
            sql += " str_to_date(CONCAT(CONCAT_WS('-',r.year,r.MONTH,r.DAY),' ',r.HOUR), '%Y-%m-%d %H') as inst_date,r.AVG_COST as inst_cost " +
                    " from RP_SERV_HOUR r where r.CLIENT <> 'N/A' and r.SERV_NO <> 'N/A' #type# ) svc  " +
                    " where svc.INST_DATE between STR_TO_DATE(#startTime,'%Y-%m-%d')  and STR_TO_DATE(#endTime,'%Y-%m-%d') ";
        }
        MpaasQuery mq = sw.buildQuery();
        if ("A".equals(techType) || "B".equals(techType)) {
            sql = sql.replace("#type#", " and r.SERV_NO in (select se.serv_no from dsgc_services se where se.TECH_TYPE ='" + techType + "') ");
        } else {
            sql = sql.replace("#type#", "");
        }
        sql += " order by sys_code desc, inst_cost asc ";
        mq = mq.sql(sql);
        System.out.println("sql->" + sql);
        return mq.setVar("startTime", startTime)
                .setVar("endTime", endTime)
                .doQuery();
    }

    /**
     * 获取ESB内耗 实例数据
     */
    public List<Map<String, Object>> getSvcEsbCostData(String startTime, String endTime) {
//        String sql = "select (select s.SUBORDINATE_SYSTEM from dsgc_services s where s.serv_no =  i.SERV_NO ) sys_code,";
//        if ("oracle".equals(dbType)) {
//            sql += " EXTRACT(SECOND FROM(i.END_TIME - i.START_TIME))*1000 as inst_cost, " +
//                    " (select EXTRACT(SECOND FROM(o.RES_TIME - o.REQ_TIME))*1000 as ob_cost from dsgc_log_outbound o where o.TRACK_ID = i.TRACK_ID) ob_cost from dsgc_log_instance i where i.SERV_NO <> 'N/A' and i.REQ_FROM <> 'N/A'\n" +
//                    " and  i.CREATION_DATE between to_date(#startTime,'yyyy-MM-dd HH24:mi:ss') and to_date(#endTime,'yyyy-MM-dd HH24:mi:ss') order by sys_code desc, inst_cost asc";
//        } else if ("mysql".equals(dbType)) {
//            sql += " (UNIX_TIMESTAMP(i.END_TIME) - UNIX_TIMESTAMP(i.START_TIME)- (select UNIX_TIMESTAMP(o.RES_TIME) - UNIX_TIMESTAMP(o.REQ_TIME) from dsgc_log_outbound o where o.TRACK_ID = i.TRACK_ID))*1000 as inst_cost" +
//                    " from dsgc_log_instance i where i.SERV_NO <> 'N/A' and i.REQ_FROM <> 'N/A' and  i.CREATION_DATE between STR_TO_DATE(#startTime,'%Y-%m-%d') and STR_TO_DATE(#endTime,'%Y-%m-%d') order by sys_code desc, inst_cost asc ";
//        }
        String sql = "select (select s.SUBORDINATE_SYSTEM from dsgc_services s where s.serv_no =  sys.svc_code ) as sys_code,(svc.inst_cost-sys.ob_cost) inst_cost  from (select r.SERV_NO svc_code ,";
        if ("oracle".equals(dbType)) {
            sql += "to_date(r.year||'-'||r.MONTH||'-'||r.DAY||' '||r.HOUR, 'yyyy-MM-dd HH24') as inst_date,r.AVG_COST as inst_cost " +
                    " from RP_SERV_HOUR r where r.CLIENT <> 'N/A' and r.SERV_NO <> 'N/A' ) svc, " +
                    " (select r.SERV_NO svc_code, to_date(r.year||'-'||r.MONTH||'-'||r.DAY||' '||r.HOUR, 'yyyy-MM-dd HH24') as ob_date,r.AVG_COST ob_cost from rp_sys_hour r where r.CLIENT <> 'N/A' and r.SERV_NO <> 'N/A' ) sys " +
                    " where svc.svc_code = sys.svc_code and svc.inst_date = sys.ob_date " +
                    " and (sys.ob_date  between to_date(#startTime,'yyyy-MM-dd HH24:mi:ss')  and to_date(#endTime,'yyyy-MM-dd HH24:mi:ss')) " +
                    " order by sys_code,inst_cost asc ";
        } else if ("mysql".equals(dbType)) {
            sql += " str_to_date(CONCAT(CONCAT_WS('-',r.year,r.MONTH,r.DAY),' ',r.HOUR), '%Y-%m-%d %H') as inst_date,r.AVG_COST as inst_cost \n" +
                    " from RP_SERV_HOUR r where r.CLIENT <> 'N/A' and r.SERV_NO <> 'N/A' ) svc,\n" +
                    " (select r.SERV_NO svc_code, str_to_date(CONCAT(CONCAT_WS('-',r.year,r.MONTH,r.DAY),' ',r.HOUR), '%Y-%m-%d %H') as ob_date,r.AVG_COST ob_cost from rp_sys_hour r where r.CLIENT <> 'N/A' and r.SERV_NO <> 'N/A' ) sys \n" +
                    " where svc.svc_code = sys.svc_code and svc.inst_date = sys.ob_date " +
                    " and (sys.ob_date  between str_to_date(#startTime,'%Y-%m-%d')  and str_to_date(#endTime,'%Y-%m-%d')) " +
                    " order by sys_code,inst_cost asc ";
        }
        System.out.println("sql->" + sql);
        return sw.buildQuery()
                .sql(sql)
                .setVar("startTime", startTime)
                .setVar("endTime", endTime)
                .doQuery();
    }
}
