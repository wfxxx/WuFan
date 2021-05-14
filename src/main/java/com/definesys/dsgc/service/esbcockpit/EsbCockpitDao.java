package com.definesys.dsgc.service.esbcockpit;

import com.definesys.dsgc.service.esbcockpit.bean.RadarBean;
import com.definesys.dsgc.service.esbcockpit.bean.eCharts3DBean;
import com.definesys.dsgc.service.esbcockpit.bean.eChartsBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName esbCockpitDao
 * @Description TODO
 * @Author ystar
 * @Date 2020-4-07 10:41
 * @Version 1.0
 **/
@Repository
public class EsbCockpitDao {

    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;


    //esb一段时间内总运行次数

    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public eChartsBean queryTotalRunTimes(Date startDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        if (startDate == null) {
            startDate = new Date();
        }
        return sw.buildQuery()
                .sql("select '总次数' as name,sum(t.total_times) as value1  from rp_serv_day t  where str_to_date(concat(t.year,'-',t.month,'-',t.day),'%Y-%m-%d') between #startDate  and #endDate ")
                .setVar("startDate", startDate)
                .setVar("endDate", endDate)
                .doQueryFirst(eChartsBean.class);
    }

    //查询今日esb运行次数
    public Map<String, Object> queryTotalDayRunTimes() {
        return sw.buildQuery().sql("select count(1) as all_count from dsgc_log_instance t \n" +
                "where date(t.CREATION_DATE) = curdate() ")
                .doQueryFirst();
    }

    //查询今日交易量
    public Map<String, Object> queryDayBusTimes() {
        return sw.buildQuery().sql("select sum(t.BUS_COUNT) as all_count from dsgc_log_instance t " +
                "where date(t.CREATION_DATE) = curdate()").doQueryFirst();
    }

    //查询总交易量
    public Map<String, Object> queryTotalBusTimes() {
        return sw.buildQuery().sql("select sum(t.bus_total_times) as all_count from rp_serv_total t ")
                .doQueryFirst();
    }


    //平台接入应用数量

    /**
     * @return result
     */
    public eChartsBean queryTotalapp() {
        return sw.buildQuery()
                .sql("select '应用数量' as name, count(1) as value1 from dsgc_system_entities ")
                .doQueryFirst(eChartsBean.class);
    }

    //一段时间内esb运行在各系统分布数量（成功的和失败的）
    public List<eChartsBean> queryAppDistri(Date startDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        if (startDate == null) {
            startDate = new Date();
        }
        return sw.buildQuery()
                .sql("select * from (select e.sys_name as name, sum(t.total_times) as value1,sum(t.total_times_s) as value2\n" +
                        "         from rp_serv_day t \n" +
                        "         left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                        "         left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                        "           where str_to_date(concat(t.year,'-',t.month,'-',t.day),'%Y-%m-%d') between #startDate  and #endDate  and s.serv_no is not null\n" +
                        "          group by e.sys_code,e.sys_name  \n" +
                        "         ) s order by value1 desc limit 8 ")
                .setVar("startDate", startDate)
                .setVar("endDate", endDate)
                .doQuery(eChartsBean.class);
    }

    //一段时间内调用esb（注意不是次数）占总数的比例

    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public eChartsBean queryAppExecute(Date startDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        if (startDate == null) {
            startDate = new Date();
        }
        return sw.buildQuery()
                .sql("select '调用esb个数' as name, count(1) as value1 from (\n" +
                        "          select t.serv_no  from rp_serv_day t   where str_to_date(concat(t.year,'-',t.month,'-',t.day),'%Y-%m-%d' ) \n" +
                        "          between #startDate  and #endDate group by t.serv_no) s")
                .setVar("startDate", startDate)
                .setVar("endDate", endDate)
                .doQueryFirst(eChartsBean.class);
    }

    //查询所有应用
    public List<eChartsBean> queryApp() {
        return sw.buildQuery()
                .sql("select t.sys_code as id,t.sys_name as name from DSGC_SYSTEM_ENTITIES t order by t.sys_code")
                .doQuery(eChartsBean.class);
    }

    //查询应用拥有的消费者
    public List<eChartsBean> queryConsumer(String esbId) {
        return sw.buildQuery()
                .sql("select id,name from (\n" +
                        "                select e.sys_code, e.sys_name,ce.csm_code as id,ce.csm_name as name from dsgc_system_entities e\n" +
                        "                left join dsgc_services s  on e.sys_code=s.subordinate_system\n" +
                        "                left join dsgc_system_access sa on sa.serv_no=s.serv_no\n" +
                        "                left join dsgc_consumer_entities ce on sa.sys_code=ce.csm_code  where e.sys_code=#esbId\n" +
                        "                ) s\n" +
                        "                where id is not null\n" +
                        "                group by  id,name ")
                .setVar("esbId", esbId)
                .doQuery(eChartsBean.class);
    }


    //查询年分月段执行次数
    public List<eChartsBean> queryMonthRuntimes(String esbId) {
        return sw.buildQuery().sql("select e.sys_code,e.sys_name,t.month as name,sum(t.total_times) as value1 from rp_serv_month t\n" +
                "                              left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                "                              left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                "                              where t.year=date_format(CURRENT_TIMESTAMP,'%Y') \n" +
                "                              and e.sys_code=#esbId\n" +
                "                              group by e.sys_code,e.sys_name,t.month\n" +
                "                               order by t.month")
                .setVar("esbId", esbId)
                .doQuery(eChartsBean.class);
    }

    //查询今年月份中日的执行次数
    public List<eChartsBean> queryDayRuntimes(String esbId) {
        return sw.buildQuery().sql("select e.sys_code,e.sys_name,t.day as name,sum(t.total_times) as value1 from rp_serv_day t\n" +
                "left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                "left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                "where t.year=date_format(CURRENT_TIMESTAMP,'%Y') and t.`MONTH`=date_format(CURRENT_TIMESTAMP,'%m') \n" +
                "and e.sys_code=#esbId\n" +
                "group by e.sys_code,e.sys_name,t.day\n" +
                "order by t.day")
                .setVar("esbId", esbId)
                .doQuery(eChartsBean.class);
    }

    //查询今年月份今日小时的执行次数
    public List<eChartsBean> queryHourRuntimes(String esbId) {
        return sw.buildQuery().sql("select e.sys_code,e.sys_name,t.`HOUR` as name,sum(t.total_times) as value1 from rp_serv_hour t\n" +
                "left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                "left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                "where t.year=date_format(CURRENT_TIMESTAMP,'%Y') and t.`MONTH`=date_format(CURRENT_TIMESTAMP,'%m') and t.`DAY`=date_format(CURRENT_TIMESTAMP,'%d')\n" +
                "and e.sys_code=#esbId\n" +
                "group by e.sys_code,e.sys_name,t.`HOUR`\n" +
                "order by t.`HOUR`")
                .setVar("esbId", esbId)
                .doQuery(eChartsBean.class);
    }


    //查询年分月段成功次数
    public List<eChartsBean> queryMonthSucess(String esbId) {
        return sw.buildQuery().sql("      select e.sys_code,e.sys_name,t.month as name,sum(t.total_times_s) as value1 from rp_serv_month t\n" +
                "                              left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                "                              left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                "                              where t.year=date_format(CURRENT_TIMESTAMP,'%Y') \n" +
                "                              and e.sys_code=#esbId\n" +
                "                              group by e.sys_code,e.sys_name,t.month\n" +
                "                               order by t.month")
                .setVar("esbId", esbId)
                .doQuery(eChartsBean.class);
    }

    //查询今年月份日的成功次数
    public List<eChartsBean> queryDaySucess(String esbId) {
        return sw.buildQuery().sql("select e.sys_code,e.sys_name,t.day as name,sum(t.total_times_s) as value1 from rp_serv_day t\n" +
                "left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                "left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                "where t.year=date_format(CURRENT_TIMESTAMP,'%Y') and t.month=DATE_FORMAT(CURRENT_TIMESTAMP,'%m')\n" +
                "and e.sys_code=#esbId\n" +
                "group by e.sys_code,e.sys_name,t.day\n" +
                "order by t.day")
                .setVar("esbId", esbId)
                .doQuery(eChartsBean.class);
    }

    //查询今年月日的小时成功次数
    public List<eChartsBean> queryHourSucess(String esbId) {
        return sw.buildQuery().sql("select e.sys_code,e.sys_name,t.hour as name,sum(t.total_times_s) as value1 from rp_serv_hour t\n" +
                "left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                "left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                "where t.year=date_format(CURRENT_TIMESTAMP,'%Y') and t.month=DATE_FORMAT(CURRENT_TIMESTAMP,'%m') and t.day=DATE_FORMAT(CURRENT_TIMESTAMP,'%d')\n" +
                "and e.sys_code=#esbId\n" +
                "group by e.sys_code,e.sys_name,t.hour\n" +
                "order by t.hour")
                .setVar("esbId", esbId)
                .doQuery(eChartsBean.class);
    }

    //查询年分月段消费者
    public List<eChartsBean> queryMonthConsumer(String esbId) {
        //一定要根据月份从小到大排序
        return sw.buildQuery()
                .sql("select count(1) as value1,date_format(a.timecol,'%m') as name from (\n" +
                        "                                  select date_add(s.timecol,interval @rowNum MONTH) as timecol,(@rowNum:=@rowNum+1) as rowNo from  (select date_add(str_to_date(concat(date_format(CURRENT_TIMESTAMP,'%Y'),'/12/31'),'%Y-%m-%d'),interval -12 MONTH) as timecol\n" +
                        "                                   from dual LIMIT 12) s,(Select (@rowNum :=0) ) b\n" +
                        "                                      )a\n" +
                        "                                  left join\n" +
                        "                                    (\n" +
                        "                                          select e.sys_code, e.sys_name,ce.csm_code ,ce.csm_name,ce.creation_date from dsgc_system_entities e\n" +
                        "                                          left join dsgc_services s  on e.sys_code=s.subordinate_system\n" +
                        "                                          left join dsgc_system_access sa on sa.serv_no=s.serv_no\n" +
                        "                                         left join dsgc_consumer_entities ce on sa.sys_code=ce.csm_code \n" +
                        "                                          where e.sys_code=#esbId and ce.csm_code is not null\n" +
                        "                                          group by  e.sys_code, e.sys_name,ce.csm_code ,ce.csm_name,ce.creation_date\n" +
                        "                                    ) t on t.creation_date<a.timecol\n" +
                        "                                    where t.csm_code is not null\n" +
                        "                                    group by a.timecol\n" +
                        "                                    order by a.timecol")
                .setVar("esbId", esbId)
                .doQuery(eChartsBean.class);
    }

    //累计一段时间内esb数量
    public eChartsBean queryEsbTotalDate(Date startDate, Date endDate) {
        return sw.buildQuery().sql("select count(1) as value1 from dsgc_services t\n" +
                "                        where creation_date between #startDate  and #endDate")
                .setVar("startDate", startDate)
                .setVar("endDate", endDate)
                .doQueryFirst(eChartsBean.class);
    }


    //累计一段时间内esb消费者数量
    public eChartsBean queryConsumerTotalDate(Date startDate, Date endDate) {
        return sw.buildQuery().sql("select count(1) as value1 from (\n" +
                "  select e.csm_code from dsgc_consumer_entities e\n" +
                "                   left join dsgc_system_access sa on sa.sys_code=e.csm_code\n" +
                "                   left join dsgc_services s on sa.serv_no=s.serv_no \n" +
                "                    where s.serv_no  is not null\n" +
                "                    and e.creation_date between #startDate  and #endDate\n" +
                "                   group by e.csm_code) s ")
                .setVar("startDate", startDate)
                .setVar("endDate", endDate)
                .doQueryFirst(eChartsBean.class);
    }

    //累计一段时间内应用数量
    public eChartsBean queryAppTotalDate(Date startDate, Date endDate) {
        return sw.buildQuery().sql("select count(1)  as value1 from dsgc_system_entities \n" +
                " where creation_date between #startDate  and #endDate")
                .setVar("startDate", startDate)
                .setVar("endDate", endDate)
                .doQueryFirst(eChartsBean.class);
    }

    //过去七天esb状况
    public List<eChartsBean> queryEsbSeven() {
        return sw.buildQuery().sql("select a.time as name,IFNULL(b.num,0) as value1 from\n" +
                " (select   DATE_FORMAT(t.datelist,'%D') as time from calendar t where t.datelist>=DATE_SUB(CURDATE(), INTERVAL 6 DAY) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY) )a\n" +
                "\tleft join\n" +
                " (select time,count(time) as num from \n" +
                "\t\t(SELECT DATE_FORMAT(creation_date,'%D') as time FROM dsgc_services where creation_date >=DATE_SUB(CURDATE(), INTERVAL 6 DAY)) as son1\n" +
                "\tgroup by time order by time) as b \n" +
                " on a.time = b.time order by a.time;")
                .doQuery(eChartsBean.class);
    }

    //过去30天esb状况sysdate
    public List<eChartsBean> queryEsbMonth() {
        return sw.buildQuery().sql("select a.time as name,IFNULL(b.num,0) as value1 from\n" +
                " (select   DATE_FORMAT(t.datelist,'%D') as time from calendar t where t.datelist>=DATE_SUB(CURDATE(), INTERVAL 30 DAY) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY) )a\n" +
                "\tleft join\n" +
                " (select time,count(time) as num from \n" +
                "\t\t(SELECT DATE_FORMAT(creation_date,'%D') as time FROM dsgc_services where creation_date >=DATE_SUB(CURDATE(), INTERVAL 30 DAY)) as son1\n" +
                "\tgroup by time order by time) as b \n" +
                " on a.time = b.time;")
                .doQuery(eChartsBean.class);
    }

    //过去一年ESB状况
    public List<eChartsBean> queryEsbyear() {
        return sw.buildQuery().sql("select  right(a.time,2) as name,IFNULL(b.num,0) as value1 from\n" +
                " (select   DATE_FORMAT(t.datelist,'%y-%m') as time from calendar t where t.datelist>=DATE_SUB(CURDATE(), INTERVAL 11 MONTH) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY) \n" +
                " group by time )a\n" +
                "\tleft join\n" +
                " (select time,count(time) as num from \n" +
                "\t\t(SELECT DATE_FORMAT(creation_date,'%y-%m') as time FROM dsgc_services where creation_date >=DATE_SUB(CURDATE(), INTERVAL 11 MONTH)) as son1\n" +
                "\tgroup by time order by time) as b \n" +
                " on a.time = b.time order by a.time;")
                .doQuery(eChartsBean.class);
    }


    //过去七天消费者状况，dsgc_consumer_entities
    public List<eChartsBean> queryConsumerSeven() {
        return sw.buildQuery().sql("   select a.time as name,IFNULL(b.num,0) as value1 from (\n" +
                "  select   DATE_FORMAT(t.datelist,'%D') as time,t.datelist as currentDate from calendar t where t.datelist>=DATE_SUB(CURDATE(), INTERVAL 6 DAY) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY)) as a\n" +
                "  left join\n" +
                "  (select time,count(time) as num from \n" +
                "  (select DATE_FORMAT(creation_date,'%D') as time FROM \n" +
                "        (select distinct a.SERV_NO,e.csm_code,e.creation_date,e.csm_name   as value1 from dsgc_consumer_entities e\n" +
                "             left join dsgc_system_access sa on sa.sys_code=e.csm_code\n" +
                "             left join dsgc_services a on a.serv_no=sa.serv_no\n" +
                "              where a.serv_no is not null)   as son1\n" +
                "     where creation_date>DATE_SUB(CURDATE(), INTERVAL 6 DAY)) as son2\n" +
                "        group by time order by time) as b \n" +
                "        on a.time = b.time order by a.currentDate")
                .doQuery(eChartsBean.class);
    }

    //过去30天消费者状况，dsgc_consumer_entities
    public List<eChartsBean> queryConsumerMonth() {
        return sw.buildQuery().sql("select a.time as name,IFNULL(b.num,0) as value1 from (\n" +
                "\t select   DATE_FORMAT(t.datelist,'%D') as time,t.datelist as currentDate from calendar t where t.datelist>=DATE_SUB(CURDATE(), INTERVAL 30 DAY) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY)) as a\n" +
                "\t left join\n" +
                "\t(select time,count(time) as num from \n" +
                "\t\t(select DATE_FORMAT(creation_date,'%D') as time FROM \n" +
                "\t\t\t\t(select distinct a.SERV_NO,e.csm_code,e.creation_date,e.csm_name   as value1 from dsgc_consumer_entities e\n" +
                "\t\t\t\t\t\tleft join dsgc_system_access sa on sa.sys_code=e.csm_code\n" +
                "\t\t\t\t\t\tleft join dsgc_services a on a.serv_no=sa.serv_no\n" +
                "\t\t\t\t\t\t\twhere a.serv_no is not null)   as son1\n" +
                "\t\t\t\twhere creation_date>DATE_SUB(CURDATE(), INTERVAL 30 DAY)) as son2\n" +
                "\t\t\t\tgroup by time order by time) as b \n" +
                "\t\t\t\ton a.time = b.time order by a.currentDate")
                .doQuery(eChartsBean.class);
    }

    //过去一年消费者状况，dsgc_consumer_entities
    public List<eChartsBean> queryConsumerYear() {
        return sw.buildQuery().sql(" select right(a.time,2) as name,IFNULL(b.num,0) as value1 from (\n" +
                "\t select   DATE_FORMAT(t.datelist,'%y-%m') as time from calendar t where \n" +
                "     t.datelist>=DATE_SUB(CURDATE(), INTERVAL 11 MONTH) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY) group by time) as a\n" +
                "\t left join\n" +
                "\t(select time,count(time) as num from \n" +
                "\t\t(select DATE_FORMAT(creation_date,'%y-%m') as time FROM \n" +
                "\t\t\t\t(select distinct a.SERV_NO,e.csm_code,e.creation_date,e.csm_name   as value1 from dsgc_consumer_entities e\n" +
                "\t\t\t\t\t\tleft join dsgc_system_access sa on sa.sys_code=e.csm_code\n" +
                "\t\t\t\t\t\tleft join dsgc_services a on a.serv_no=sa.serv_no\n" +
                "\t\t\t\t\t\t\twhere a.serv_no is not null)   as son1\n" +
                "\t\t\t\twhere creation_date>DATE_SUB(CURDATE(), INTERVAL 11 MONTH) ) as son2\n" +
                "\t\t\t\tgroup by time order by time) as b \n" +
                "\t\t\t\ton a.time = b.time  order by a.time\n")
                .doQuery(eChartsBean.class);
    }


    //过去七天应用状况，dsgc_system_entities
    public List<eChartsBean> queryAppSeven() {
        return sw.buildQuery().sql("select a.time as name,IFNULL(b.num,0) as value1 from (\n" +
                " select   DATE_FORMAT(t.datelist,'%D') as time,t.datelist as currentDate from calendar t where \n" +
                " t.datelist>=DATE_SUB(CURDATE(), INTERVAL 6 DAY) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY)) as a\n" +
                "  left join\n" +
                "\t\t  (select time,count(time) as num from (SELECT DATE_FORMAT(creation_date,'%D') as time FROM dsgc_system_entities   \n" +
                "\t\t\t\t\t\twhere creation_date >DATE_SUB(CURDATE(), INTERVAL 6 DAY))  as son1 group by time order by time) b on a.time = b.time order by a.currentDate")
                .doQuery(eChartsBean.class);
    }

    //过去30天应用状况，dsgc_system_entities
    public List<eChartsBean> queryAppMonth() {
        return sw.buildQuery().sql("select a.time as name,IFNULL(b.num,0) as value1 from (\n" +
                " select   DATE_FORMAT(t.datelist,'%D') as time,t.datelist as currentDate from calendar t where \n" +
                " t.datelist>=DATE_SUB(CURDATE(), INTERVAL 30 DAY) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY)) as a\n" +
                "  left join\n" +
                "\t\t  (select time,count(time) as num from (SELECT DATE_FORMAT(creation_date,'%D') as time FROM dsgc_system_entities   \n" +
                "\t\t\t\t\t\twhere creation_date >DATE_SUB(CURDATE(), INTERVAL 30 DAY))  as son1 group by time order by time) b on a.time = b.time order by a.currentDate\n" +
                "                        \n" +
                "                        \n")
                .doQuery(eChartsBean.class);
    }

    //过去一年应用状况，dsgc_system_entities
    public List<eChartsBean> queryAppYear() {
        return sw.buildQuery().sql("select right(a.time,2) as name,IFNULL(b.num,0) as value1 from (\n" +
                "\tselect   DATE_FORMAT(t.datelist,'%y-%m') as time from calendar t where \n" +
                "\t\tt.datelist>=DATE_SUB(CURDATE(), INTERVAL 11 MONTH) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY) group by time) as a\n" +
                "\tleft join\n" +
                "\t  (select time,count(time) as num from (SELECT  DATE_FORMAT(creation_date,'%y-%m') as time FROM dsgc_system_entities   \n" +
                "\t\t\t\t\twhere creation_date > DATE_SUB(CURDATE(), INTERVAL 11 MONTH)) as son2 group by time order by time) b on a.time = b.time order by a.time;")
                .doQuery(eChartsBean.class);
    }

    //按应用分类获取esb信息,获取全部
    public List<eChartsBean> queryEsbInfoByAppALL() {
        return sw.buildQuery().sql("select sys_name as name,count(sys_name) as value1 from\n" +
                "                          (select s.serv_no,s.serv_name,e.sys_code,e.sys_name\n" +
                "                          from dsgc_services s \n" +
                "                          left join dsgc_system_entities e on s.subordinate_system=e.sys_code\n" +
                "                          where e.sys_code is not null) as son1\n" +
                "                          group by sys_code,sys_name order by value1 desc").doQuery(eChartsBean.class);
    }

    //按类型分类获取全部esb信息
    public List<eChartsBean> queryEsbInfoByTypeALL() {
        return sw.buildQuery().sql("select \n" +
                "        (case when t.cate_name is not null then t.cate_name else '其他' end)  as name,\n" +
                "        (count(case when t.cate_name is not null then t.cate_name else '其他' end)) as value1  \n" +
                "          from\n" +
                "                        \n" +
                "          (select s.serv_no,s.serv_name,c.cate_name ,c.cate_code \n" +
                "          from dsgc_services s \n" +
                "          left join dsgc_market_category c on c.cate_code=s.market_category) t \n" +
                "          group by t.cate_name,t.cate_code order by value1 desc").doQuery(eChartsBean.class);
    }

    //查询一日的日志分组并标记状态
    public List<Map<String, Object>> queryFailApiServNo() {
        return sw.buildQuery().sql("SELECT  DISTINCT (select enti.SYS_NAME sys_name from dsgc_services s , dsgc_system_entities enti where s.serv_no = l.SERV_NO AND enti.SYS_CODE = s.SUBORDINATE_SYSTEM) sys_code " +
                "from dsgc_log_instance l where  INST_STATUS = '0' and TO_DAYS(l.START_TIME) = TO_DAYS(now())")
                .doQuery();
    }

    public List<Map<String, Object>> queryAllCountAndFailCount() {
        return sw.buildQuery().sql("SELECT tab.sys_name sys_name,count(sys_name)-count(tab.`status` = 0 OR NULL)  all_count,count(tab.`status` = 0 OR NULL) fail_count  \n" +
                "FROM \n" +
                "(SELECT enti.SYS_NAME sys_name, l.INST_STATUS status\n" +
                "from dsgc_log_instance l,  dsgc_services s ,  dsgc_system_entities enti\n" +
                "WHERE enti.SYS_CODE = s.SUBORDINATE_SYSTEM AND s.serv_no = l.SERV_NO AND TO_DAYS(START_TIME) = TO_DAYS(NOW()))  tab \n" +
                "GROUP BY sys_name")
                .doQuery();
    }

    //查询一周系统的调用量与
    public List<eCharts3DBean> queryWeekCountAndFailCount(int year, int month, int startDay, int endDay) {
        return sw.buildQuery().sql("SELECT tb2.sys_code code,tb2.name name,tb1.all_count  allCount,tb1.sucess_count sucessCount FROM\n" +
                "(select e.sys_code,e.sys_name ,sum(t.total_times) as all_count, sum(t.total_times_s) as sucess_count from rp_serv_day t\n" +
                "left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                "left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                "where t.year=#year and t.month=#month and t.`DAY` BETWEEN #startDay and #endDay\n" +
                "group by e.sys_code,e.sys_name) tb1 RIGHT JOIN\n" +
                "(select sys_code ,sys_name as name from(select s.serv_no,s.serv_name,e.sys_code,e.sys_name\n" +
                "from dsgc_services s \n" +
                "left join dsgc_system_entities e on s.subordinate_system=e.sys_code\n" +
                "where e.sys_code is not null) as son1\n" +
                "group by sys_code,sys_name) tb2 on tb2.sys_code = tb1.sys_code")
                .setVar("year", year)
                .setVar("month", month)
                .setVar("startDay", startDay)
                .setVar("endDay", endDay)
                .doQuery(eCharts3DBean.class);
    }

    //查询某一年月区间的每月调用总数
    public List<RadarBean> queryMonthCountByYearAndMonth(int year, int startMonth, int endMonth) {
        return sw.buildQuery().sql("select t.`YEAR` as year,t.`MONTH` as month,sum(t.total_times_s) as count from rp_serv_month t\n" +
                "where t.year=#year and t.month BETWEEN #startMonth AND #endMonth\n" +
                "group by t.`YEAR`,t.`MONTH`\n" +
                "order by t.`MONTH`")
                .setVar("year", year)
                .setVar("startMonth", startMonth)
                .setVar("endMonth", endMonth)
                .doQuery(RadarBean.class);
    }

    //查询今日3个最大的失败次数
    public List<Map<String, Object>> queryMaxFailByDay() {
        return sw.buildQuery().sql("SELECT r.SERV_NO serv_no, d.SERV_NAME serv_name, r.TOTAL_TIMES_F fail_count from rp_serv_day r, dsgc_services d \n" +
                "where r.year=date_format(CURRENT_TIMESTAMP,'%Y') and r.month=DATE_FORMAT(CURRENT_TIMESTAMP,'%m') and r.`DAY`=DATE_FORMAT(CURRENT_TIMESTAMP,'%d') and d.SERV_NO = r.SERV_NO\n" +
                "and r.TOTAL_TIMES_F <> 0\n" +
                "ORDER BY r.TOTAL_TIMES_F desc\n" +
                "LIMIT 0,3")
                .doQuery();
    }

    //查询今日3个最大的调用次数
    public List<Map<String, Object>> queryMaxByDay() {
        return sw.buildQuery().sql(
//                "SELECT r.SERV_NO serv_no, d.SERV_NAME serv_name, r.BUS_TOTAL_TIMES all_count from rp_serv_day r, dsgc_services d\n" +
//                "where r.year=date_format(CURRENT_TIMESTAMP,'%Y') and r.month=DATE_FORMAT(CURRENT_TIMESTAMP,'%m') and r.`DAY`=DATE_FORMAT(CURRENT_TIMESTAMP,'%d') and d.SERV_NO = r.SERV_NO\n" +
//                "ORDER BY r.BUS_TOTAL_TIMES desc LIMIT 0,3"
                "SELECT r.SERV_NO serv_no, d.SERV_NAME serv_name, r.BUS_COUNT all_count from dsgc_log_instance r, dsgc_services d\n" +
                        "where date(r.CREATION_DATE) = curdate() and d.SERV_NO = r.SERV_NO and r.BUS_COUNT is not null and r.BUS_COUNT <> 0\n" +
                        "GROUP BY r.SERV_NO,d.SERV_NAME ORDER BY r.BUS_COUNT desc LIMIT 0,3"
        )
                .doQuery();
    }

    //查询今日3个最大的接口调用次数
    public List<Map<String, Object>> queryMaxDayRunTimes() {
        return sw.buildQuery().sql(
                "SELECT r.SERV_NO serv_no, d.SERV_NAME serv_name, count(*) all_count from dsgc_log_instance r, dsgc_services d\n" +
                        "where date(r.CREATION_DATE) = curdate() and d.SERV_NO = r.SERV_NO \n" +
                        "GROUP BY r.SERV_NO, d.SERV_NAME\n" +
                        "ORDER BY all_count desc LIMIT 0,3"
        )
                .doQuery();
    }

    //查询今日失败的总数
    public Map<String, Object> queryDayOfFailCount() {
        return sw.buildQuery().sql("SELECT sum(r.TOTAL_TIMES_F) fail_count from rp_serv_day r\n" +
                "where r.year=date_format(CURRENT_TIMESTAMP,'%Y') " +
                "and r.month=DATE_FORMAT(CURRENT_TIMESTAMP,'%m') " +
                "and r.`DAY`=DATE_FORMAT(CURRENT_TIMESTAMP,'%d')")
                .doQueryFirst();
    }

    //查询前日最大异常3个接口
    public List<Map<String, Object>> queryBeforeYesterday(int startDay) {
        return sw.buildQuery().sql("SELECT s.SERV_NAME serv_name, s.SERV_NO serv_no, sum(t.TOTAL_TIMES_F) failCount \n" +
                "FROM rp_serv_day t , dsgc_services s\n" +
                "WHERE t.`YEAR` = date_format(CURRENT_TIMESTAMP,'%Y') AND t.`MONTH` = DATE_FORMAT(CURRENT_TIMESTAMP,\"%m\")\n" +
                "and s.SERV_NO = t.SERV_NO\n" +
                "and t.`DAY` = #startDay\n" +
                "GROUP BY serv_no\n" +
                "HAVING sum(t.TOTAL_TIMES_F) <> 0\n" +
                "ORDER BY sum(t.TOTAL_TIMES_F) desc \n" +
                "limit 0,3")
                .setVar("startDay", startDay)
                .doQuery();
    }

    //根据前日最大异常的接口No 查询今日的异常数量
    public Map<String, Object> queryToDayExcepionByServNo(int today, String servNo) {
        return sw.buildQuery().sql("SELECT s.SERV_NAME serv_name, s.SERV_NO serv_no, sum(t.TOTAL_TIMES_F) failCount FROM rp_serv_day t , dsgc_services s\n" +
                "WHERE t.`YEAR` = date_format(CURRENT_TIMESTAMP,'%Y') AND t.`MONTH` = DATE_FORMAT(CURRENT_TIMESTAMP,\"%m\")\n" +
                "and s.SERV_NO = t.SERV_NO\n" +
                "and t.`DAY` = #today\n" +
                "and s.SERV_NO = #servNo\n" +
                "GROUP BY s.SERV_NO")
                .setVar("today", today)
                .setVar("servNo", servNo)
                .doQueryFirst();
    }

    //查询今日接口连接异常与业务异常
    public Map<String, Object> queryConnectAndBusinessException() {
        return sw.buildQuery().sql("SELECT * from\n" +
                "(SELECT count(BIZ_STATUS) biz_count from dsgc_log_instance where date(START_TIME) = curdate() and BIZ_STATUS <> 1 and SERV_NO <> 'N/A') tb1,\n" +
                "(SELECT count(INST_STATUS) inst_count from dsgc_log_instance where date(START_TIME) = curdate() and INST_STATUS = 0 and SERV_NO <> 'N/A') tb2,\n" +
                "(SELECT count(BIZ_STATUS) scuess_count from dsgc_log_instance where date(START_TIME) = curdate() and INST_STATUS = 1  and SERV_NO <> 'N/A') tb3,\n" +
                "(SELECT count(BIZ_STATUS) all_count from dsgc_log_instance where date(START_TIME) = curdate() and SERV_NO <> 'N/A') tb4 ")
                .doQueryFirst();
    }


    //查询今日报文最大的三个接口
    public List<Map<String, Object>> queryMaxMsgSize() {
        return sw.buildQuery().sql("select serv_no, serv_name, MAX(msg_size) msg_size from dsgc_log_instance \n" +
                "where date(START_TIME) = curdate() and SERV_NO <> 'N/A' GROUP BY SERV_NO,serv_name ORDER BY MAX(msg_size) desc LIMIT 0,3")
                .doQuery();
    }


}
