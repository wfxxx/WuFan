package com.definesys.dsgc.service.dashboard;

import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Repository
public class IndexDao {
    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;

    //获取服务实例
    public Map<String, Object> getServeExam() {
// 查询24小时的       select count(1) TOTAL_TIMES, SUM(CASE WHEN INST_STATUS = 1 AND BIZ_STATUS = 1 THEN 1 ELSE 0 END) AS TOTAL_TIMES_S, SUM(CASE WHEN INST_STATUS != 1 OR BIZ_STATUS != 1 THEN 1 ELSE 0 END) AS TOTAL_TIMES_F from dsgc_log_instance t where t.start_time < sysdate and t.start_time >= sysdate - 1
     // 查询今天的 SELECT COUNT(1) AS TOTAL_TIMES, SUM(CASE  WHEN INST_STATUS = 1 AND BIZ_STATUS = 1 THEN 1 ELSE 0 END) AS TOTAL_TIMES_S , SUM(CASE  WHEN INST_STATUS != 1 OR BIZ_STATUS != 1 THEN 1 ELSE 0 END) AS TOTAL_TIMES_F FROM dsgc_log_instance t WHERE TO_CHAR(t.start_time, 'YYYY-MM-DD') = TO_CHAR(SYSDATE, 'YYYY-MM-DD')
      Map<String, Object> result = sw.buildQuery()
                .sql(" select count(1) TOTAL_TIMES, SUM(CASE WHEN INST_STATUS = 1 AND BIZ_STATUS = 1 THEN 1 ELSE 0 END) AS TOTAL_TIMES_S, SUM(CASE WHEN INST_STATUS != 1 OR BIZ_STATUS != 1 THEN 1 ELSE 0 END) AS TOTAL_TIMES_F from dsgc_log_instance t where t.start_time < sysdate and t.start_time >= sysdate - 1")
                .doQueryFirst();
        return result;
    }

    /**
     * 获取接口数量
     * @return
     */
    public Object getServices() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("select count(*) from dsgc_SERVICES")
                .doQuery();
        return result.get(0).get("COUNT(*)");
    }

    /**
     * 获取系统数量
     * @return
     */
    public Object getSystems() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("select count(*) from dsgc_sysTEM_ENTITIES")
                .doQuery();
        return result.get(0).get("COUNT(*)");
    }

    //调用统计top5数据
    public List<Map<String, Object>> getTotalTop5() {
        List<Map<String, Object>> result = sw.buildQuery()
               // .sql("select SERV_NO, sum(TOTAL_TIMES) TOTAL_TIMES, sum(TOTAL_TIMES_S) TOTAL_TIMES_S, SUM(TOTAL_TIMES_F) TOTAL_TIMES_F from RP_SERV_TOTAL t where rownum < 6 and t.total_times_f != 0 and t.serv_No not in('N/A') GROUP BY SERV_NO order by TOTAL_TIMES_F desc ")
               .sql("select * from ( select day.serv_no,sum(day.total_times),sum(day.total_times_s),sum(day.total_times_f) TOTAL_TIMES_F \n" +
                       "from rp_serv_day day\n" +
                       " where day.year = to_char(sysdate, 'yyyy')\n" +
                       " and day.month = to_char(sysdate, 'mm')\n" +
                       " and day.day = to_char(sysdate, 'dd')\n" +
                       " group by day.serv_no\n" +
                       " order by TOTAL_TIMES_F desc ) where rownum  <6")
                .doQuery();
        return result;
    }

    //实时指标
    public Map<String, Object> getRealTime() {
     Map<String, Object> result = sw.buildQuery()
                .sql("select TO_CHAR(SYSDATE, 'YYYY') YEAR,TO_CHAR(SYSDATE, 'MM') MONTH,TO_CHAR(SYSDATE, 'DD') DAY,TO_CHAR(SYSDATE, 'HH24') HOUR,COUNT(INST_STATUS) TOTAL_TIMES,SUM(CASE WHEN INST_STATUS = 1 AND BIZ_STATUS = 1 THEN 1 ELSE 0 END) TOTAL_TIMES_S,SUM(CASE WHEN INST_STATUS != 1 OR BIZ_STATUS != 1 THEN 1 ELSE 0 END) TOTAL_TIMES_F ,MAX((TO_NUMBER((RES_TIME + 0) - (START_TIME + 0)) * 1440 * 60) + TO_NUMBER(SUBSTR(TO_CHAR(RES_TIME - START_TIME), 21, 3))) MAX_COST,MIN((TO_NUMBER((RES_TIME + 0) - (START_TIME + 0)) * 1440 * 60) + TO_NUMBER(SUBSTR(TO_CHAR(RES_TIME - START_TIME), 21, 3))) MIN_COST,ROUND(AVG((TO_NUMBER((RES_TIME + 0) - (START_TIME + 0)) * 1440 * 60) + TO_NUMBER(SUBSTR(TO_CHAR(RES_TIME - START_TIME), 21, 3))),2) AVG_COST,MAX(MSG_NUM) MAX_MSG_NUM,MIN(MSG_NUM) MIN_MSG_NUM,ROUND(AVG(MSG_NUM), 2) AVG_MSG_NUM,MAX(MSG_SIZE) MAX_MSG_SIZE,MIN(MSG_SIZE) MIN_MSG_SIZE,ROUND(AVG(MSG_SIZE), 2) AVG_MSG_SIZE FROM dsgc_log_instance WHERE CREATION_DATE > sysdate - 1 / 24 / 60")
                .doQueryFirst();
        return result;
    }

    //服务分布数据
    public List<Map<String, Object>> getServerDistriub() {
        List<Map<String, Object>> result = sw.buildQuery()
//                .sql("SELECT SUBSTR(SERV_NO, 0, 1) AS SERV_NO, count(1) AS SERV_SIZE FROM dsgc_SERVICES group by SUBSTR(SERV_NO, 0, 1)")
                  .sql("SELECT s.subordinate_system AS SERV_sys, count(1) AS SERV_SIZE FROM dsgc_SERVICES s group by s.subordinate_system")
                .doQuery();
        return result;
    }

    /**
     * 获取平均响应时间
     * @return
     */
    public Map<String, Object> getAvgResponseTime() {
        Map<String, Object> result = sw.buildQuery()
                .sql("select (s.sum_cost/s.sum_times) avg_cost from ( select sum(t.avg_cost*t.total_times) as sum_cost, sum(t.total_times) as sum_times  from rp_serv_day t " +
                        "where t.year = TO_CHAR(SYSDATE, 'YYYY') and t.month = TO_CHAR(SYSDATE, 'MM') and t.day = TO_CHAR(SYSDATE, 'DD') and total_times <> 0) s")
                .doQueryFirst();
        return result;
    }

    /**
     * 获取平均响应时间
     * @return
     */
    public Map<String, Object> getInvokeTimes() {
        Map<String, Object> result = sw.buildQuery()
                .sql("select sum(t.total_times) total_times, sum(t.total_times_f) total_times_f from rp_serv_day t " +
                        "where t.year = TO_CHAR(SYSDATE, 'YYYY') and t.month = TO_CHAR(SYSDATE, 'MM') and t.day = TO_CHAR(SYSDATE, 'DD')")
                .doQueryFirst();
        return result;
    }

    /**
     * 获取表空间使用情况
     * @return
     */
    public List<Map<String, Object>> getTableSpaceInfo() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("SELECT a.tablespace_name, a.bytes total, b.bytes used, c.bytes free, (b.bytes * 100) / a.bytes USED_percent, (c.bytes * 100) / a.bytes FREE_percent FROM sys.sm$ts_avail a, sys.sm$ts_used b, sys.sm$ts_free c WHERE a.tablespace_name = b.tablespace_name and a.tablespace_name in ('OFMP_IAS_UMS','DSGC','SYSAUX','OFMP_STB','LOG_OSB','OFMP_IAU','USERS','SYSTEM','OFMP_MDS','ESBTBS','OFMP_SOAINFRA','OFMP_WLS','OFMP_IAS_OPSS') AND a.tablespace_name = c.tablespace_name ")
                .doQuery();
        return result;
    }

    /**
     * 获取cpu信息
     * @return
     */
    public List<Map<String, Object>> getCpuInfo() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("SELECT t.SERVER,avg(t.usage) usage, to_char(t.creation_date, 'HH24:mi') time \n" +
                        "FROM DSGC_PC_CPU_INFO t \n" +
                        "WHERE t.creation_date > sysdate - 1 / 24 / 6 \n" +
                        "group by t.Server,to_char(t.creation_date, 'HH24:mi') \n" +
                        "ORDER BY to_char(t.creation_date, 'HH24:mi')  DESC")
                .doQuery();
        return result;
    }

    /**
     * 获取内存信息
     * @return
     */
    public List<Map<String, Object>> getMemInfo() {
        List<Map<String, Object>> result = sw.buildQuery()
//                .sql("SELECT t.SERVER,t.usage,t.mem_use,t.mem_total, to_char(t.creation_date, 'HH24:mi') time FROM DSGC_PC_MEM_INFO t WHERE t.creation_date > sysdate - 1 / 24 / 6 ORDER BY t.creation_date DESC")
                .sql("SELECT t.SERVER,avg(t.usage) usage, avg(t.mem_use) mem_use, max(t.mem_total) mem_total, to_char(t.creation_date, 'HH24:mi') time \n" +
                        "FROM DSGC_PC_MEM_INFO t \n" +
                        "WHERE t.creation_date > sysdate - 1 / 24 / 6 \n" +
                        "group by t.Server,to_char(t.creation_date, 'HH24:mi') \n" +
                        "ORDER BY to_char(t.creation_date, 'HH24:mi')  DESC")
                .doQuery();
        return result;
    }

    /**
     * 获取io信息
     * @return
     */
    public List<Map<String, Object>> getIoInfo() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("SELECT t.SERVER,avg(t.usage) usage, to_char(t.creation_date, 'HH24:mi') time \n" +
                        "FROM DSGC_PC_IO_INFO t \n" +
                        "WHERE t.creation_date > sysdate - 1 / 24 / 6 \n" +
                        "group by t.Server,to_char(t.creation_date, 'HH24:mi') \n" +
                        "ORDER BY to_char(t.creation_date, 'HH24:mi')  DESC")
                .doQuery();
        return result;
    }

    /**
     * 获取网络信息
     * @return
     */
    public List<Map<String, Object>> getNetInfo() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("SELECT t.SERVER,avg(t.usage) usage, to_char(t.creation_date, 'HH24:mi') time \n" +
                        "FROM DSGC_PC_NET_INFO t \n" +
                        "WHERE t.creation_date > sysdate - 1 / 24 / 6 \n" +
                        "group by t.Server,to_char(t.creation_date, 'HH24:mi') \n" +
                        "ORDER BY to_char(t.creation_date, 'HH24:mi')  DESC")
//                .sql("SELECT t.SERVER,t.usage, to_char(t.creation_date, 'HH24:mi') time FROM DSGC_PC_NET_INFO t WHERE t.creation_date > sysdate - 1 / 24 / 6 ORDER BY t.creation_date DESC")
                .doQuery();
        return result;
    }

    /**
     * 获取磁盘信息
     * @return
     */
    public List<Map<String, Object>> getDiskInfo() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("SELECT t.SERVER,avg(rtrim(t.usage,'%')) usage, t.disk_name, avg(rtrim(total_space,'G')) total_space, \n" +
                        "avg(rtrim(free_space,'G')) free_space, to_char(t.creation_date, 'HH24:mi') time \n" +
                        "FROM DSGC_PC_DISK_INFO t \n" +
                        "WHERE t.creation_date > sysdate - 1 / 24 / 6 \n" +
                        "group by t.Server,t.Disk_name,to_char(t.creation_date, 'HH24:mi') \n" +
                        "ORDER BY to_char(t.creation_date, 'HH24:mi')  DESC")
//                .sql("select t.SERVER,t.USAGE,t.DISK_NAME,t.TOTAL_SPACE,t.FREE_SPACE, to_char(t.creation_date, 'HH24:mi')  time from DSGC_PC_DISK_INFO t where t.creation_date > sysdate -1/24/6 order by t.creation_date desc ")
                .doQuery();
        return result;
    }

    /**
     * 获取ohs信息
     * @return
     */
    public List<Map<String, Object>> getOhsInfo() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("select id,virtual_ip,server from DSGC_OHS_IP_INFO t where t.creation_date > sysdate -1/24/60")
                .doQuery();
        return result;
    }

    /**
     * 获取服务器名字
     * @return
     */
    public List<Map<String, Object>> getServerName() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("select distinct t.server from dsgc_server_info t where   t.creation_date > sysdate -1/24/6 ")
                .doQuery();
        return result;
    }

    /**
     * 获取服务器信息
     * @return
     */
    public List<Map<String, Object>> getServerInfo() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("SELECT t.SERVER, avg(t.mem_usage) mem_usage,avg(t.mem_use) mem_use , avg(t.mem_total) mem_total,\n" +
                        "max(t.thread_usage) thread_usage,max(t.ACTIVE_EXE_THREAD_COUNT) ACTIVE_EXE_THREAD_COUNT , to_char(t.creation_date, 'HH24:mi') time \n" +
                        "FROM dsgc_server_info t \n" +
                        "WHERE t.creation_date > sysdate - 1 / 24 / 6 \n" +
                        "group by t.Server,to_char(t.creation_date, 'HH24:mi') \n" +
                        "ORDER BY to_char(t.creation_date, 'HH24:mi')  DESC")
//                .sql("select t.SERVER,t.cpu_usage,t.mem_usage,t.mem_use,t.mem_total,t.thread_usage ,t.ACTIVE_EXE_THREAD_COUNT,to_char(t.creation_date, 'HH24:mi')  time from dsgc_server_info t where t.creation_date > sysdate -1/24/6 order by t.creation_date desc ")
                .doQuery();
        return result;
    }

    /**
     * 获取总执行次数，按月
     * @return
     */
    public List<Map<String, Object>> getInvokeTimesInfo() {
//        String year = "";
        Calendar date = Calendar.getInstance();
        Integer year = date.get(Calendar.YEAR);
        Integer month = date.get(Calendar.MONTH) + 1;
        Integer day = date.get(Calendar.DAY_OF_MONTH);
//        System.out.println();
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
//        year = sdf.format(new Date());
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("select t.hour,sum(t.total_times) total from\n" +
                        " rp_serv_hour t where  t.year = #year and  t.month = #month and t.day = #day \n" +
                        " group by t.hour order by t.hour asc ")
                .setVar("year",year)
                .setVar("month",month)
                .setVar("day",day)
                .doQuery();
        return result;
    }
}
