package com.definesys.dsgc.service.esbcockpit;

import com.definesys.dsgc.service.esbcockpit.bean.eChartsBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @ClassName esbCockpitDao
 * @Description TODO
 * @Author Xueyunlong
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
        public eChartsBean queryTotalRunTimes(Date startDate, Date endDate){
                if(endDate==null){
                        endDate=new Date();
                }
                if(startDate==null){
                        startDate=new Date();
                }
                return sw.buildQuery()
                        .sql("select '总次数' as name,sum(t.total_times) as value1  from rp_serv_day t  where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd') between #startDate  and #endDate ")
                        .setVar("startDate",startDate)
                        .setVar("endDate",endDate)
                        .doQueryFirst(eChartsBean.class);
        }

        //平台接入应用数量
        /**
         * @return result
         */
        public eChartsBean queryTotalapp(){
                return sw.buildQuery()
                        .sql("select '应用数量' as name, count(1) as value1 from dsgc_system_entities ")
                        .doQueryFirst(eChartsBean.class);
        }

        //一段时间内esb运行在各系统分布数量（成功的和失败的）
        public List<eChartsBean> queryAppDistri(Date startDate, Date endDate){
                if(endDate==null){
                        endDate=new Date();
                }
                if(startDate==null){
                        startDate=new Date();
                }
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select * from (select e.sys_name as name, sum(t.total_times) as value1,sum(t.total_times_s) as value2\n" +
                                "         from rp_serv_day t \n" +
                                "         left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                                "         left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                                "           where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd') between #startDate  and #endDate  and s.serv_no is not null\n" +
                                "          group by e.sys_code,e.sys_name  \n" +
                                "         ) where rownum<8 order by value1 desc  ";
                }
                if ("mysql".equals(dbType)){
                        sql ="select * from (select e.sys_name as name, sum(t.total_times) as value1,sum(t.total_times_s) as value2\n" +
                                "         from rp_serv_day t \n" +
                                "         left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                                "         left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                                "           where str_to_date(concat(t.year,'-',t.month,'-',t.day),'%Y-%m-%d') between #startDate  and #endDate  and s.serv_no is not null\n" +
                                "          group by e.sys_code,e.sys_name  \n" +
                                "         ) s order by value1 desc limit 8 ";
                }
                return sw.buildQuery()
                        .sql(sql)
                        .setVar("startDate",startDate)
                        .setVar("endDate",endDate)
                        .doQuery(eChartsBean.class);
        }

        //一段时间内调用esb（注意不是次数）占总数的比例
        /**
         * @param startDate
         * @param endDate
         * @return result
         */
        public eChartsBean queryAppExecute(Date startDate, Date endDate){
                if(endDate==null){
                        endDate=new Date();
                }
                if(startDate==null){
                        startDate=new Date();
                }
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select '调用esb个数' as name, count(1) as value1 from (\n" +
                                "          select t.serv_no  from rp_serv_day t   where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd' ) \n" +
                                "          between #startDate  and #endDate group by t.serv_no) s";
                }
                if ("mysql".equals(dbType)){
                        sql ="select '调用esb个数' as name, count(1) as value1 from (\n" +
                                "          select t.serv_no  from rp_serv_day t   where str_to_date(concat(t.year,'-',t.month,'-',t.day),'%Y-%m-%d' ) \n" +
                                "          between #startDate  and #endDate group by t.serv_no) s";
                }
                return sw.buildQuery()
                        .sql(sql)
                        .setVar("startDate",startDate)
                        .setVar("endDate",endDate)
                        .doQueryFirst(eChartsBean.class);
        }
        //查询所有应用
        public List<eChartsBean> queryApp(){
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
                                "                group by  (id,name)")
                        .setVar("esbId",esbId)
                        .doQuery(eChartsBean.class);
        }

        //查询年分月段执行次数
        public List<eChartsBean> queryMonthRuntimes(String esbId){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select e.sys_code,e.sys_name,t.month as name,sum(t.total_times) as value1 from rp_serv_month t\n" +
                                "                              left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                                "                              left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                                "                              where t.year=to_char(sysdate,'yyyy') \n" +
                                "                              and e.sys_code=#esbId\n" +
                                "                              group by e.sys_code,e.sys_name,t.month\n" +
                                "                               order by t.month";
                }
                if ("mysql".equals(dbType)){
                        sql ="select e.sys_code,e.sys_name,t.month as name,sum(t.total_times) as value1 from rp_serv_month t\n" +
                                "                              left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                                "                              left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                                "                              where t.year=date_format(CURRENT_TIMESTAMP,'%Y') \n" +
                                "                              and e.sys_code=#esbId\n" +
                                "                              group by e.sys_code,e.sys_name,t.month\n" +
                                "                               order by t.month";
                }
                return sw.buildQuery().sql(sql)
                        .setVar("esbId",esbId)
                        .doQuery(eChartsBean.class);
        }

        //查询年分月段成功次数
        public List<eChartsBean> queryMonthSucess(String esbId){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "      select e.sys_code,e.sys_name,t.month as name,sum(t.total_times_s) as value1 from rp_serv_month t\n" +
                                "                              left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                                "                              left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                                "                              where t.year=to_char(sysdate,'yyyy') \n" +
                                "                              and e.sys_code=#esbId\n" +
                                "                              group by e.sys_code,e.sys_name,t.month\n" +
                                "                               order by t.month";
                }
                if ("mysql".equals(dbType)){
                        sql ="      select e.sys_code,e.sys_name,t.month as name,sum(t.total_times_s) as value1 from rp_serv_month t\n" +
                                "                              left join  dsgc_services s on s.serv_no=t.serv_no\n" +
                                "                              left join  dsgc_system_entities e on e.sys_code=s.subordinate_system\n" +
                                "                              where t.year=date_format(CURRENT_TIMESTAMP,'%Y') \n" +
                                "                              and e.sys_code=#esbId\n" +
                                "                              group by e.sys_code,e.sys_name,t.month\n" +
                                "                               order by t.month";
                }
                return sw.buildQuery().sql(sql)
                        .setVar("esbId",esbId)
                        .doQuery(eChartsBean.class);
        }

        //查询年分月段消费者
        public List<eChartsBean> queryMonthConsumer(String esbId){
                //一定要根据月份从小到大排序
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select count(1) as value1,to_char(a.timecol,'mm') as name from (\n" +
                                "           select  add_months(to_date(to_char(sysdate,'yyyy')||'/12/31','yyyy/mm/dd'),-12+rownum) as timecol\n" +
                                "            from dual \n" +
                                "            connect by rownum <=12 )a\n" +
                                "          left join\n" +
                                "            (\n" +
                                "                  select e.sys_code, e.sys_name,ce.csm_code ,ce.csm_name,ce.creation_date from dsgc_system_entities e\n" +
                                "                  left join dsgc_services s  on e.sys_code=s.subordinate_system\n" +
                                "                  left join dsgc_system_access sa on sa.serv_no=s.serv_no\n" +
                                "                  left join dsgc_consumer_entities ce on sa.sys_code=ce.csm_code \n" +
                                "                  where e.sys_code=#esbId and ce.csm_code is not null\n" +
                                "                  group by  e.sys_code, e.sys_name,ce.csm_code ,ce.csm_name,ce.creation_date\n" +
                                "            ) t on t.creation_date<a.timecol\n" +
                                "            where t.csm_code is not null\n" +
                                "            group by a.timecol\n" +
                                "            order by a.timecol";
                }
                if ("mysql".equals(dbType)){
                        sql ="select count(1) as value1,date_format(a.timecol,'%m') as name from (\n" +
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
                                "                                    order by a.timecol";
                }
                return sw.buildQuery().sql(sql)
                        .setVar("esbId",esbId)
                        .doQuery(eChartsBean.class);
        }

        //累计一段时间内esb数量
        public eChartsBean queryEsbTotalDate(Date startDate, Date endDate){
                return sw.buildQuery().sql("select count(1) as value1 from dsgc_services t\n" +
                        "                        where creation_date between #startDate  and #endDate")
                        .setVar("startDate",startDate)
                        .setVar("endDate",endDate)
                        .doQueryFirst(eChartsBean.class);
        }


        //累计一段时间内esb消费者数量
        public eChartsBean queryConsumerTotalDate(Date startDate, Date endDate){
                return sw.buildQuery().sql("select count(1) as value1 from (\n" +
                        "  select e.csm_code from dsgc_consumer_entities e\n" +
                        "                   left join dsgc_system_access sa on sa.sys_code=e.csm_code\n" +
                        "                   left join dsgc_services s on sa.serv_no=s.serv_no \n" +
                        "                    where s.serv_no  is not null\n" +
                        "                    and e.creation_date between #startDate  and #endDate\n" +
                        "                   group by e.csm_code) ")
                        .setVar("startDate",startDate)
                        .setVar("endDate",endDate)
                        .doQueryFirst(eChartsBean.class);
        }

        //累计一段时间内应用数量
        public eChartsBean queryAppTotalDate(Date startDate, Date endDate){
                return sw.buildQuery().sql("select count(1)  as value1 from dsgc_system_entities \n" +
                        " where creation_date between #startDate  and #endDate")
                        .setVar("startDate",startDate)
                        .setVar("endDate",endDate)
                        .doQueryFirst(eChartsBean.class);
        }

        //过去七天esb状况
        public List<eChartsBean> queryEsbSeven(){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select a.time as name,nvl(b.num,0) as value1 from (\n" +
                                "                                                select  sysdate-7+rownum as currentDate,to_char(sysdate-7 + rownum ,  'day') as time\n" +
                                "                                                from dual \n" +
                                "                                                connect by rownum <=7 )a\n" +
                                "                                                left join\n" +
                                "                                                  (select time,count(time) as num from (SELECT to_char(creation_date,'day') as time FROM DSGC_SERVICES   \n" +
                                "                                                                where creation_date >sysdate-6) group by time order by time)b on a.time = b.time order by a.currentDate";
                }
                if ("mysql".equals(dbType)){
                        sql ="select a.time as name,IFNULL(b.num,0) as value1 from\n" +
                                " (select   DATE_FORMAT(t.datelist,'%D') as time from calendar t where t.datelist>=DATE_SUB(CURDATE(), INTERVAL 6 DAY) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY) )a\n" +
                                "\tleft join\n" +
                                " (select time,count(time) as num from \n" +
                                "\t\t(SELECT DATE_FORMAT(creation_date,'%D') as time FROM dsgc_services where creation_date >=DATE_SUB(CURDATE(), INTERVAL 6 DAY)) as son1\n" +
                                "\tgroup by time order by time) as b \n" +
                                " on a.time = b.time order by a.time;";
                }
                return sw.buildQuery().sql(sql)
                        .doQuery(eChartsBean.class);
        }
        //过去30天esb状况
        public List<eChartsBean> queryEsbMonth(){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select a.time as name,nvl(b.num,0) as value1 from (\n" +
                                "                              select  sysdate-30+rownum as currentDate,to_char( sysdate-30+rownum ,  'dd') as time\n" +
                                "                              from dual \n" +
                                "                              connect by rownum <=30 )a\n" +
                                "                              left join\n" +
                                "                                (select time,count(time) as num from (SELECT to_char(creation_date,'dd') as time FROM dsgc_services   \n" +
                                "                                              where creation_date >sysdate-29) group by time order by time)b on a.time = b.time order by a.currentDate";
                }
                if ("mysql".equals(dbType)){
                        sql ="select a.time as name,IFNULL(b.num,0) as value1 from\n" +
                                " (select   DATE_FORMAT(t.datelist,'%D') as time from calendar t where t.datelist>=DATE_SUB(CURDATE(), INTERVAL 30 DAY) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY) )a\n" +
                                "\tleft join\n" +
                                " (select time,count(time) as num from \n" +
                                "\t\t(SELECT DATE_FORMAT(creation_date,'%D') as time FROM dsgc_services where creation_date >=DATE_SUB(CURDATE(), INTERVAL 30 DAY)) as son1\n" +
                                "\tgroup by time order by time) as b \n" +
                                " on a.time = b.time;";
                }
                return sw.buildQuery().sql(sql)
                        .doQuery(eChartsBean.class);
        }
        //过去一年ESB状况
        public List<eChartsBean> queryEsbyear(){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select a.time as name,nvl(b.num,0) as value1 from (\n" +
                                "    select add_months(sysdate,-12+rownum) as currentDate,to_char( add_months(sysdate,-12+rownum) ,  'mm') as time\n" +
                                "    from dual \n" +
                                "    connect by rownum <=12 )a\n" +
                                "    left join\n" +
                                "      (select time,count(time) as num from (SELECT to_char(creation_date,'mm') as time FROM dsgc_services   \n" +
                                "                    where creation_date >add_months(sysdate,-12)) group by time order by time)b on a.time = b.time order by a.currentDate";
                }
                if ("mysql".equals(dbType)){
                        sql ="select  right(a.time,2) as name,IFNULL(b.num,0) as value1 from\n" +
                                " (select   DATE_FORMAT(t.datelist,'%y-%m') as time from calendar t where t.datelist>=DATE_SUB(CURDATE(), INTERVAL 11 MONTH) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY) \n" +
                                " group by time )a\n" +
                                "\tleft join\n" +
                                " (select time,count(time) as num from \n" +
                                "\t\t(SELECT DATE_FORMAT(creation_date,'%y-%m') as time FROM dsgc_services where creation_date >=DATE_SUB(CURDATE(), INTERVAL 11 MONTH)) as son1\n" +
                                "\tgroup by time order by time) as b \n" +
                                " on a.time = b.time order by a.time;";
                }
                return sw.buildQuery().sql(sql)
                        .doQuery(eChartsBean.class);
        }


        //过去七天消费者状况，dsgc_consumer_entities
        public List<eChartsBean> queryConsumerSeven(){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select a.time as name,nvl(b.num,0) as value1 from (\n" +
                                "                                  select  sysdate-7+rownum as currentDate,to_char(sysdate-7 + rownum ,  'day') as time\n" +
                                "                                  from dual \n" +
                                "                                  connect by rownum <=7 )a\n" +
                                "                                  left join\n" +
                                "                                    (select time,count(time) as num from (SELECT to_char(creation_date,'day') as time FROM \n" +
                                "                                         (   select distinct e.csm_code,e.creation_date,e.csm_name   as value1 from dsgc_consumer_entities e\n" +
                                "                                                                 left join dsgc_apis_access aa on aa.csm_code=e.csm_code\n" +
                                "                                                                 left join dsgc_apis a on a.api_code=aa.api_code\n" +
                                "                                                                 where a.api_code is not null)   \n" +
                                "                                                   where creation_date >sysdate-6) group by time order by time)b on a.time = b.time order by a.currentDate";
                }
                if ("mysql".equals(dbType)){
                        sql ="   select a.time as name,IFNULL(b.num,0) as value1 from (\n" +
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
                                "        on a.time = b.time order by a.currentDate";
                }
                return sw.buildQuery().sql(sql)
                        .doQuery(eChartsBean.class);
        }
        //过去30天消费者状况，dsgc_consumer_entities
        public List<eChartsBean> queryConsumerMonth(){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select a.time as name,nvl(b.num,0) as value1 from (\n" +
                                "                                  select  sysdate-30+rownum as currentDate,to_char(sysdate-30 + rownum ,  'dd') as time\n" +
                                "                                  from dual \n" +
                                "                                  connect by rownum <=30 )a\n" +
                                "                                  left join\n" +
                                "                                    (select time,count(time) as num from (SELECT to_char(creation_date,'dd') as time FROM \n" +
                                "                                         (   select distinct e.csm_code,e.creation_date,e.csm_name   as value1 from dsgc_consumer_entities e\n" +
                                "                                                                 left join dsgc_apis_access aa on aa.csm_code=e.csm_code\n" +
                                "                                                                 left join dsgc_apis a on a.api_code=aa.api_code\n" +
                                "                                                                 where a.api_code is not null)   \n" +
                                "                                                   where creation_date >sysdate-29) group by time order by time)b on a.time = b.time order by a.currentDate";
                }
                if ("mysql".equals(dbType)){
                        sql ="select a.time as name,IFNULL(b.num,0) as value1 from (\n" +
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
                                "\t\t\t\ton a.time = b.time order by a.currentDate";
                }
                return sw.buildQuery().sql(sql)
                        .doQuery(eChartsBean.class);
        }
        //过去一年消费者状况，dsgc_consumer_entities
        public List<eChartsBean> queryConsumerYear(){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select a.time as name,nvl(b.num,0) as value1 from (\n" +
                                "                        select add_months(sysdate,-12+rownum) as currentDate,to_char(add_months(sysdate,-12+rownum) ,  'mm') as time\n" +
                                "                        from dual \n" +
                                "                        connect by rownum <=12 )a\n" +
                                "                        left join\n" +
                                "                          (select time,count(time) as num from (SELECT to_char(creation_date,'mm') as time FROM dsgc_system_entities   \n" +
                                "                                        where creation_date > add_months(sysdate,-12)) group by time order by time)b on a.time = b.time order by a.currentDate";
                }
                if ("mysql".equals(dbType)){
                        sql =" select right(a.time,2) as name,IFNULL(b.num,0) as value1 from (\n" +
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
                                "\t\t\t\ton a.time = b.time  order by a.time\n";
                }
                return sw.buildQuery().sql(sql)
                        .doQuery(eChartsBean.class);
        }


        //过去七天应用状况，dsgc_system_entities
        public List<eChartsBean> queryAppSeven(){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select a.time as name,nvl(b.num,0) as value1 from (\n" +
                                "                        select  sysdate-7+rownum as currentDate,to_char(sysdate-7 + rownum ,  'day') as time\n" +
                                "                        from dual \n" +
                                "                        connect by rownum <=7 )a\n" +
                                "                        left join\n" +
                                "                          (select time,count(time) as num from (SELECT to_char(creation_date,'day') as time FROM dsgc_system_entities   \n" +
                                "                                        where creation_date >sysdate-6) group by time order by time)b on a.time = b.time order by a.currentDate";
                }
                if ("mysql".equals(dbType)){
                        sql ="select a.time as name,IFNULL(b.num,0) as value1 from (\n" +
                                " select   DATE_FORMAT(t.datelist,'%D') as time,t.datelist as currentDate from calendar t where \n" +
                                " t.datelist>=DATE_SUB(CURDATE(), INTERVAL 6 DAY) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY)) as a\n" +
                                "  left join\n" +
                                "\t\t  (select time,count(time) as num from (SELECT DATE_FORMAT(creation_date,'%D') as time FROM dsgc_system_entities   \n" +
                                "\t\t\t\t\t\twhere creation_date >DATE_SUB(CURDATE(), INTERVAL 6 DAY))  as son1 group by time order by time) b on a.time = b.time order by a.currentDate";
                }
                return sw.buildQuery().sql(sql)
                        .doQuery(eChartsBean.class);
        }

        //过去30天应用状况，dsgc_system_entities
        public List<eChartsBean> queryAppMonth(){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql ="select a.time as name,nvl(b.num,0) as value1 from (\n" +
                                "                        select  sysdate-30+rownum as currentDate,to_char(sysdate-30 + rownum ,  'dd') as time\n" +
                                "                        from dual \n" +
                                "                        connect by rownum <=30 )a\n" +
                                "                        left join\n" +
                                "                          (select time,count(time) as num from (SELECT to_char(creation_date,'dd') as time FROM dsgc_system_entities   \n" +
                                "                                        where creation_date > sysdate-30) group by time order by time)b on a.time = b.time order by a.currentDate";
                }
                if ("mysql".equals(dbType)){
                        sql ="select a.time as name,IFNULL(b.num,0) as value1 from (\n" +
                                " select   DATE_FORMAT(t.datelist,'%D') as time,t.datelist as currentDate from calendar t where \n" +
                                " t.datelist>=DATE_SUB(CURDATE(), INTERVAL 30 DAY) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY)) as a\n" +
                                "  left join\n" +
                                "\t\t  (select time,count(time) as num from (SELECT DATE_FORMAT(creation_date,'%D') as time FROM dsgc_system_entities   \n" +
                                "\t\t\t\t\t\twhere creation_date >DATE_SUB(CURDATE(), INTERVAL 30 DAY))  as son1 group by time order by time) b on a.time = b.time order by a.currentDate\n" +
                                "                        \n" +
                                "                        \n";
                }
                return sw.buildQuery().sql(sql)
                        .doQuery(eChartsBean.class);
        }

        //过去一年应用状况，dsgc_system_entities
        public List<eChartsBean> queryAppYear(){
                String sql ="";
                if("oracle".equals(dbType)) {
                        sql = "select a.time as name,nvl(b.num,0) as value1 from (\n" +
                                "                        select add_months(sysdate,-12+rownum) as currentDate,to_char(add_months(sysdate,-12+rownum) ,  'mm') as time\n" +
                                "                        from dual \n" +
                                "                        connect by rownum <=12 )a\n" +
                                "                        left join\n" +
                                "                          (select time,count(time) as num from (SELECT to_char(creation_date,'mm') as time FROM dsgc_system_entities   \n" +
                                "                                        where creation_date > add_months(sysdate,-12)) group by time order by time)b on a.time = b.time order by a.currentDate";
                }
                if ("mysql".equals(dbType)){
                        sql ="select right(a.time,2) as name,IFNULL(b.num,0) as value1 from (\n" +
                                "\tselect   DATE_FORMAT(t.datelist,'%y-%m') as time from calendar t where \n" +
                                "\t\tt.datelist>=DATE_SUB(CURDATE(), INTERVAL 11 MONTH) and t.datelist<DATE_SUB(CURDATE(), INTERVAL 0 DAY) group by time) as a\n" +
                                "\tleft join\n" +
                                "\t  (select time,count(time) as num from (SELECT  DATE_FORMAT(creation_date,'%y-%m') as time FROM dsgc_system_entities   \n" +
                                "\t\t\t\t\twhere creation_date > DATE_SUB(CURDATE(), INTERVAL 11 MONTH)) as son2 group by time order by time) b on a.time = b.time order by a.time;";
                }
                return sw.buildQuery().sql(sql)
                        .doQuery(eChartsBean.class);
        }

        //按应用分类获取esb信息,获取全部
        public List<eChartsBean> queryEsbInfoByAppALL(){
                return  sw.buildQuery().sql("select sys_name as name,count(sys_name) as value1 from\n" +
                        "                          (select s.serv_no,s.serv_name,e.sys_code,e.sys_name\n" +
                        "                          from dsgc_services s \n" +
                        "                          left join dsgc_system_entities e on s.subordinate_system=e.sys_code\n" +
                        "                          where e.sys_code is not null)\n" +
                        "                          group by sys_code,sys_name order by value1 desc").doQuery(eChartsBean.class);
        }

        //按类型分类获取全部esb信息
        public List<eChartsBean> queryEsbInfoByTypeALL(){
                return  sw.buildQuery().sql("select \n" +
                        "        (case when t.cate_name is not null then t.cate_name else '其他' end)  as name,\n" +
                        "        (count(case when t.cate_name is not null then t.cate_name else '其他' end)) as value1  \n" +
                        "          from\n" +
                        "                        \n" +
                        "          (select s.serv_no,s.serv_name,c.cate_name ,c.cate_code \n" +
                        "          from dsgc_services s \n" +
                        "          left join dsgc_market_category c on c.cate_code=s.market_category) t \n" +
                        "          group by t.cate_name,t.cate_code order by value1 desc").doQuery(eChartsBean.class);
        }

}
