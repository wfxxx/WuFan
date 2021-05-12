package com.definesys.dsgc.service.fullScreen.dao;

import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
/**
 * @author qirenchao E-mail:3063973019@qq.com
 * @version 创建时间：2020/1/9 15:04
 */
@Repository
public class BigScreenDao {

    @Autowired
    private MpaasQueryFactory sw;


    /**
     * 获取系统数量
     *
     * @return
     */
    public Object getSystems() {
        Map<String, Object> result = sw.buildQuery()
                .sql("select count(*) sys_count from dsgc_sysTEM_ENTITIES")
                .doQueryFirst();
        return result.get("SYS_COUNT");
    }


    /**
     * 获取接口数量
     *
     * @return
     */
    public Object getServices() {
        Map<String, Object> result = sw.buildQuery()
                .sql("select count(*) service_count from dsgc_SERVICES")
                .doQueryFirst();
        return result.get("SERVICE_COUNT");
    }


    /**
     * 实时指标（最近10分钟）
     * 并发量与平均响应时间
     *
     * @return
     */
    public Map<String, Object> getRealTime() {
        return sw.buildQuery()
//                .sql("select TO_CHAR(SYSDATE, 'YYYY') YEAR,TO_CHAR(SYSDATE, 'MM') MONTH,TO_CHAR(SYSDATE, 'DD') DAY,TO_CHAR(SYSDATE, 'HH24') HOUR,COUNT(INST_STATUS) TOTAL_TIMES,SUM(CASE WHEN INST_STATUS = 1 AND BIZ_STATUS = 1 THEN 1 ELSE 0 END) TOTAL_TIMES_S," +
//                        "SUM(CASE WHEN INST_STATUS != 1 OR BIZ_STATUS != 1 THEN 1 ELSE 0 END) TOTAL_TIMES_F ,MAX((TO_NUMBER((RES_TIME + 0) - (START_TIME + 0)) * 1440 * 60) + TO_NUMBER(SUBSTR(TO_CHAR(RES_TIME - START_TIME), 21, 3))) MAX_COST," +
//                        "MIN((TO_NUMBER((RES_TIME + 0) - (START_TIME + 0)) * 1440 * 60) + TO_NUMBER(SUBSTR(TO_CHAR(RES_TIME - START_TIME), 21, 3))) MIN_COST,ROUND(AVG((TO_NUMBER((RES_TIME + 0) - (START_TIME + 0)) * 1440 * 60) + TO_NUMBER(SUBSTR(TO_CHAR(RES_TIME - START_TIME), 21, 3))),2) AVG_COST," +
//                        "MAX(MSG_NUM) MAX_MSG_NUM,MIN(MSG_NUM) MIN_MSG_NUM,ROUND(AVG(MSG_NUM), 2) AVG_MSG_NUM,MAX(MSG_SIZE) MAX_MSG_SIZE,MIN(MSG_SIZE) MIN_MSG_SIZE,ROUND(AVG(MSG_SIZE), 2) AVG_MSG_SIZE FROM dsgc_log_instance WHERE CREATION_DATE > sysdate - 1    and INST_STATUS in ('0','1') ")
               .sql("select TO_CHAR(SYSDATE, 'YYYY') YEAR,\n" +
                       "       TO_CHAR(SYSDATE, 'MM') MONTH,\n" +
                       "       TO_CHAR(SYSDATE, 'DD') DAY,\n" +
                       "       TO_CHAR(SYSDATE, 'HH24') HOUR,\n" +
                       "       COUNT(INST_STATUS) TOTAL_TIMES,\n" +
                       "       SUM(CASE\n" +
                       "               WHEN INST_STATUS = 1 AND BIZ_STATUS = 1 THEN\n" +
                       "                   1\n" +
                       "               ELSE\n" +
                       "                   0\n" +
                       "           END) TOTAL_TIMES_S,\n" +
                       "       SUM(CASE\n" +
                       "               WHEN INST_STATUS != 1 OR BIZ_STATUS != 1 THEN\n" +
                       "                   1\n" +
                       "               ELSE\n" +
                       "                   0\n" +
                       "           END) TOTAL_TIMES_F,\n" +
                       "       MAX((TO_NUMBER((RES_TIME + 0) - (START_TIME + 0)) * 1440 * 60) +\n" +
                       "           TO_NUMBER(SUBSTR(TO_CHAR(RES_TIME - START_TIME), 21, 3))) MAX_COST,\n" +
                       "       MIN((TO_NUMBER((RES_TIME + 0) - (START_TIME + 0)) * 1440 * 60) +\n" +
                       "           TO_NUMBER(SUBSTR(TO_CHAR(RES_TIME - START_TIME), 21, 3))) MIN_COST,\n" +
                       "       ROUND(AVG((TO_NUMBER((RES_TIME + 0) - (START_TIME + 0)) * 1440 * 60) +\n" +
                       "                 TO_NUMBER(SUBSTR(TO_CHAR(RES_TIME - START_TIME), 21, 3))),\n" +
                       "             2) AVG_COST,\n" +
                       "       MAX(MSG_NUM) MAX_MSG_NUM,\n" +
                       "       MIN(MSG_NUM) MIN_MSG_NUM,\n" +
                       "       ROUND(AVG(MSG_NUM), 2) AVG_MSG_NUM,\n" +
                       "       MAX(MSG_SIZE) MAX_MSG_SIZE,\n" +
                       "       MIN(MSG_SIZE) MIN_MSG_SIZE,\n" +
                       "       ROUND(AVG(MSG_SIZE), 2) AVG_MSG_SIZE\n" +
                       "FROM dsgc_log_instance\n" +
                       "WHERE CREATION_DATE > sysdate - 10/ 24 / 60")
                .doQueryFirst();
    }


    /**
     * 获取运行次数
     * 总次数、成功次数、失败次数、业务未知次数、失败率
     *
     * @return
     */
    public Map<String, Object> getInvokeTimes() {
        return sw.buildQuery()
                .sql("select sum(t.total_times) total_times,sum(t.total_times_s) total_times_s, sum(t.total_times_f) total_times_f \n" +
                        "from rp_serv_day t\n" +
                        "where t.year = TO_CHAR(SYSDATE, 'YYYY') and t.month = TO_CHAR(SYSDATE, 'MM') and t.day = TO_CHAR(SYSDATE, 'DD')")
                .doQueryFirst();
    }

    /**
     * 获取服务资产信息
     *
     * @return
     */
    public List<Map<String, Object>>  getServiceAssetsCount() {
        return sw.buildQuery()
                .sql("select count(e.sys_name) as systemCount, e.sys_name as \n" +
                        "systemNmae from dsgc_services s，DSGC_SYSTEM_ENTITIES e where s.subordinate_system=e.sys_code group by e.sys_name")
                .doQuery();
    }

    /**
     * 获取服务通知信息
     *
     * @return
     */
    public List<Map<String, Object>> getWorkNotice() {
        return sw.buildQuery()
                .sql("select s.mn_type,count(s.mn_type) mn_count from dsgc_mn_notices s group by s.mn_type ")
                .doQuery();
    }

    /**
     * 获取服务分布信息
     *
     * @return
     */
    public List<Map<String, Object>> getServiceDistribution() {
        return sw.buildQuery()
                .sql("select * from ( SELECT s.subordinate_system sys_code, count(1) service_count FROM dsgc_services s group by s.subordinate_system) order by service_count desc ")
                .doQuery();
    }

    /**
     * 服务执行次数小时统计表（当天）
     *
     * @return
     */
    public List<Map<String, Object>> getCurDayExecuteTimes() {
        return sw.buildQuery()
                .sql("select to_char(time,'yyyy-MM-dd Hh24:mi:ss') time, count from\n" +
                        "(select time, sum(count) count from\n" +
                        "    (select to_date(sh.year||'-'||sh.month||'-'||sh.day||' '||sh.hour||':00:00','yyyy-MM-dd Hh24:mi:ss') time , sh.total_times count from rp_serv_hour sh)\n" +
                        "    sh\n" +
                        "group by time\n" +
                        "order by time desc) hu where rownum <25")
                .doQuery();
    }

    /**
     * 服务执行次数日统计表（1周）
     *
     * @return
     */
    public List<Map<String, Object>> getCurWeekExecuteTimes() {
        return sw.buildQuery()
                .sql(" select rs.year,rs.month,rs.day,sum(rs.total_times) count from (select s.year,s.month,s.day, " +
                        " case when to_number(s.month)< 10 then '0'|| s.month else ''|| s.month end month_str, " +
                        " case when to_number(s.day)<10 then '0'||s.day else ''||s.day end day_str,s.total_times  " +
                        " from rp_serv_day s) rs where to_date(rs.year||rs.month_str||rs.day_str,'yyyyMMdd') > trunc(sysdate -7,'DD') " +
                        " group by rs.year,rs.month,rs.day order by rs.year,rs.month,rs.day  ")
                .doQuery();
    }

    /**
     * 获取当天总执行次数Top5
     *
     * @return
     */
    public List<Map<String, Object>> getCurDayInstanceTop() {
        return sw.buildQuery()
                .sql("select * from (select rs.serv_no,rs.total_times from " +
                        "(select s.serv_no,sum(s.total_times) total_times  from rp_serv_day s where  s.year = to_char(sysdate,'yyyy') and s.month = to_char(sysdate,'MM') and s.day = to_char(sysdate,'dd')" +
                        " group by  s.serv_no) rs order by rs.total_times desc )rp where rownum <= 5  ")
                .doQuery();
    }

    /**
     * 获取当天总执行次数Top5
     *
     * @return
     */
    public List<Map<String, Object>> getCurDayInstanceFailTop() {
        return sw.buildQuery()
                .sql("select * from (select rs.serv_no,rs.total_times_f from  " +
                        "(select s.serv_no,sum(s.total_times_f) total_times_f from rp_serv_day s where  s.year = to_char(sysdate,'yyyy') and s.month = to_char(sysdate,'MM') and s.day = to_char(sysdate,'dd')\n" +
                        "group by  s.serv_no) rs order by rs.total_times_f desc )rp where rownum <=5  ")
                .doQuery();
    }

    /**
     * 获取服务器名字
     *
     * @return
     */
    public List<Map<String, Object>> getServerStatusInfo() {
        return sw.buildQuery()
                .sql("select distinct t.server server_name,t.service_status server_status from dsgc_server_info t where   t.creation_date > sysdate -1/24/6 ")
                .doQuery();
    }

    public List<Map<String,Object>> getcurDayInstanceSeTopData() {
        return sw.buildQuery()
                .sql("select * from (SELECT * FROM RP_SERV_TOTAL WHERE  creation_date\n" +
                        "         between\n" +
                        "         to_date(to_char(sysdate-1, 'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss')\n" +
                        "         and\n" +
                        "         to_date(to_char(sysdate, 'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss')\n" +
                        "         \n" +
                        "          and SERV_NO != 'N/A'   ORDER BY TOTAL_TIMES DESC) where rownum <=5")
                .doQuery();

    }


    //调用统计top5数据
    public List<Map<String, Object>> getTotalTop5() {
        List<Map<String, Object>> result = sw.buildQuery()
                // .sql("select SERV_NO, sum(TOTAL_TIMES) TOTAL_TIMES, sum(TOTAL_TIMES_S) TOTAL_TIMES_S, SUM(TOTAL_TIMES_F) TOTAL_TIMES_F from RP_SERV_TOTAL t where rownum < 6 and t.total_times_f != 0 and t.serv_No not in('N/A') GROUP BY SERV_NO order by TOTAL_TIMES_F desc ")
                .sql("select ser.SERV_NAME name,ser.SERV_NO,rp_serv_day.total_times,rp_serv_day.total_times_s value from ( select day.serv_no,sum(day.total_times) TOTAL_TIMES,sum(day.total_times_s) TOTAL_TIMES_S,sum(day.total_times_f) TOTAL_TIMES_F\n" +
                        " from rp_serv_day day\n" +
                        " where day.year = to_char(sysdate, 'yyyy')\n" +
                        " and day.month = to_char(sysdate, 'mm')\n" +
                        " and day.day = to_char(sysdate, 'dd')\n" +
                        " group by day.serv_no\n" +
                        " order by TOTAL_TIMES desc )rp_serv_day , DSGC_SERVICES ser\n" +
                        " where rp_serv_day.serv_no=ser.serv_no and rownum  <6 and rp_serv_day.total_times_s >0")
                .doQuery();
        return result;
    }


    //调用失败top5数据
    public List<Map<String, Object>> getFailureTop5() {
        List<Map<String, Object>> result = sw.buildQuery()
                // .sql("select SERV_NO, sum(TOTAL_TIMES) TOTAL_TIMES, sum(TOTAL_TIMES_S) TOTAL_TIMES_S, SUM(TOTAL_TIMES_F) TOTAL_TIMES_F from RP_SERV_TOTAL t where rownum < 6 and t.total_times_f != 0 and t.serv_No not in('N/A') GROUP BY SERV_NO order by TOTAL_TIMES_F desc ")
                .sql("select ser.SERV_NAME name,ser.SERV_NO,rp_serv_day.total_times,rp_serv_day.total_times_f value from ( select day.serv_no,sum(day.total_times) TOTAL_TIMES,sum(day.total_times_s) TOTAL_TIMES_S,sum(day.total_times_f) TOTAL_TIMES_F\n" +
                        "from rp_serv_day day\n" +
                        "where day.year = to_char(sysdate, 'yyyy')\n" +
                        "and day.month = to_char(sysdate, 'mm')\n" +
                        "and day.day = to_char(sysdate, 'dd')\n" +
                        "group by day.serv_no\n" +
                        "order by TOTAL_TIMES_F desc ) rp_serv_day , DSGC_SERVICES ser\n" +
                        "where rp_serv_day.serv_no=ser.serv_no and rownum  <6 and rp_serv_day.total_times_f >0")
                .doQuery();
        return result;
    }

}
