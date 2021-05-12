package com.definesys.dsgc.service.ystar.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * SQL 常量，默认为MySQL
 */
public class SqlConstant {

    public static final String dbType = "mysql";
    public static final String DB_TYPE_MYSQL = "mysql";
    public static final String DB_TYPE_ORACLE = "oracle";

    public static final String MS_SQL_QUERY_APP_EXECUTE = "select '调用api个数' as name, count(1) as value1 from (select t.serv_no  from RP_API_DAY t   where str_to_date ( CONCAT_WS('-',t.YEAR ,t.MONTH , t.DAY), 'yyyy-mm-dd hh24:mi:ss' ) between  CURRENT_DATE -2 and CURRENT_DATE  group by t.serv_no) s ";
    public static final String ORC_SQL_QUERY_APP_EXECUTE = "select '调用api个数' as name, count(1) as value1 from (select t.serv_no  from RP_API_DAY t   where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd' ) between #startDate  and #endDate group by t.serv_no) s";
    public static String SQL_QUERY_APP_EXECUTE = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_APP_EXECUTE : ORC_SQL_QUERY_APP_EXECUTE;

    public static final String MS_SQL_QUERY_TOTAL_RUN_TIMES = "select '总次数' as name,sum(t.total_times) as value1  from RP_API_DAY t  where str_to_date(CONCAT_WS('-',t.YEAR ,t.MONTH , t.DAY), 'yyyy-mm-dd hh24:mi:ss') between #startDate  and #endDate ";
    public static final String ORC_SQL_QUERY_TOTAL_RUN_TIMES = "select '总次数' as name,sum(t.total_times) as value1  from RP_API_DAY t  where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd') between #startDate  and #endDate ";
    public static String SQL_QUERY_TOTAL_RUN_TIMES = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_TOTAL_RUN_TIMES : ORC_SQL_QUERY_TOTAL_RUN_TIMES;

    public static final String MS_SQL_QUERY_APP_DISTRIBUTE = "SELECT\t@rowno:=@rowno+1 as rowno,r.* FROM( SELECT e.sys_name AS NAME, sum( t.total_times ) AS value1, sum( t.total_200 ) AS value2  FROM RP_API_DAY t LEFT JOIN dsgc_apis a ON a.api_code = t.serv_no LEFT JOIN dsgc_system_entities e ON e.sys_code = a.app_code WHERE str_to_date ( CONCAT_WS('-',t.YEAR ,t.MONTH , t.DAY), 'yyyy-mm-dd hh24:mi:ss' ) BETWEEN #startDate  AND #endDate and a.api_code is not null GROUP BY e.sys_code,e.sys_name ORDER BY \tvalue1 DESC ) r,(select @rowno:=0) t WHERE @rowno < 8 ";
    public static final String ORC_QUERY_APP_DISTRIBUTE = "select * from (select e.sys_name as name, sum(t.total_times) as value1,sum(t.total_200) as value2 from RP_API_DAY t left join  dsgc_apis a on a.api_code=t.serv_no left join  dsgc_system_entities e on e.sys_code=a.app_code  where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd hh24:mi:ss') between #startDate and #endDate  and a.api_code is not null group by e.sys_code,e.sys_name ) where rownum<8 order by value1 desc  ";
    public static String SQL_QUERY_APP_DISTRIBUTE = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_APP_DISTRIBUTE : ORC_QUERY_APP_DISTRIBUTE;


    public static final String MS_SQL_QUERY_MONTH_CONSUMER = "select count(1) as value1,date_format(a.timecol,'%m') as name from (\n" +
            "with recursive tab1(timecol, num) as (select str_to_date(CONCAT(date_format(NOW(),'%Y'),'/12/31'),'%Y/%m/%d'), @num := 1 union all select ADDDATE(str_to_date(CONCAT(date_format(NOW(),'%Y'),'/12/31'),'%Y/%m/%d'),INTERVAL num-12 MONTH), @num := @num + 1 from tab1 where num < 12)select timecol from tab1\n" +
            ")a left join ( select e.sys_code, e.sys_name,ce.csm_code ,ce.csm_name,ce.creation_date from dsgc_system_entities e left join dsgc_apis a  \n" +
            "on e.sys_code=a.app_code left join dsgc_apis_access aa on aa.api_code=a.api_code \n" +
            "left join dsgc_consumer_entities ce on aa.csm_code=ce.csm_code where e.sys_code='' and ce.csm_code is not null \n" +
            "group by  e.sys_code, e.sys_name,ce.csm_code ,ce.csm_name,ce.creation_date) t on t.creation_date<a.timecol \n" +
            "where t.csm_code is not null group by a.timecol order by a.timecol ";
    public static final String ORC_SQL_QUERY_MONTH_CONSUMER = "select count(1) as value1,to_char(a.timecol,'mm') as name from ( select  add_months(to_date(to_char(sysdate,'yyyy')||'/12/31','yyyy/mm/dd'),-12+rownum) as timecol from dual connect by rownum <=12 )a left join ( select e.sys_code, e.sys_name,ce.csm_code ,ce.csm_name,ce.creation_date from dsgc_system_entities e left join dsgc_apis a  on e.sys_code=a.app_code left join dsgc_apis_access aa on aa.api_code=a.api_code left join dsgc_consumer_entities ce on aa.csm_code=ce.csm_code where e.sys_code=#appId and ce.csm_code is not null group by  e.sys_code, e.sys_name,ce.csm_code ,ce.csm_name,ce.creation_date) t on t.creation_date<a.timecol where t.csm_code is not null group by a.timecol order by a.timecol     ";
    public static String SQL_QUERY_MONTH_CONSUMER = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_MONTH_CONSUMER : ORC_SQL_QUERY_MONTH_CONSUMER;


    public static final String MS_SQL_QUERY_API_SEVEN = "select (case a.time when 2 then 'monday' when 3 then 'tuesday' when 4 then 'wednesday' when 5 then 'thursday' when 6 then 'friday' when 7 then 'saturday' else 'sunday' end) as name,IFNULL(b.num,0) as value1 from (\n" +
            "with recursive tab1(currentDate, time,num) as (\n" +
            "select NOW() as currentDate,DAYOFWEEK(NOW()) as time, @num := 1 \n" +
            "union all \n" +
            "select ADDDATE(NOW(),INTERVAL num-7 DAY) as currentDate ,DAYOFWEEK(ADDDATE(NOW(),INTERVAL num-7 DAY))as time, @num := @num + 1 from tab1 where num < 7\n" +
            ") select currentDate,time from tab1 )a left join (select time,count(time) as num from (SELECT DAYOFWEEK(CURRENT_DATE) as time FROM dsgc_apis  \n" +
            "   where creation_date > CURRENT_DATE-6) ct group by time order by time) b on a.time = b.time order by a.currentDate ";
    public static final String ORC_SQL_QUERY_API_SEVEN = "select a.time as name,nvl(b.num,0) as value1 from (select  sysdate-7+rownum as currentDate,to_char(sysdate-7 + rownum ,  'day') as time from dual connect by rownum <=7 )a left join (select time,count(time) as num from (SELECT to_char(creation_date,'day') as time FROM dsgc_apis where creation_date >sysdate-6) group by time order by time)b on a.time = b.time order by a.currentDate";
    public static String SQL_QUERY_API_SEVEN = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_API_SEVEN : ORC_SQL_QUERY_API_SEVEN;


    public static final String MS_SQL_QUERY_API_MONTH = "select CONCAT(a.time,'日') as name,IFNULL(b.num,0) as value1 from ( with recursive tab1(currentDate, time,num) as (select NOW() as currentDate,DATE_FORMAT(NOW(),'%d') as time, @num := 1 \n" +
            "union all select ADDDATE(NOW(),INTERVAL num-30 DAY) as currentDate ,DATE_FORMAT(ADDDATE(NOW(),INTERVAL num-30 DAY),'%d')as time, @num := @num + 1 from tab1 where num < 30 ) select currentDate,time from tab1 ) a\n" +
            "left join (select time,count(time) as num from (SELECT DATE_FORMAT(creation_date,'%d') as time FROM dsgc_apis   \n" +
            "  where creation_date > CURRENT_DATE-29 ) c group by time order by time)b on a.time = b.time order by a.currentDate";
    public static final String ORC_SQL_QUERY_API_MONTH = "select a.time||'日' as name,nvl(b.num,0) as value1 from (select  sysdate-30+rownum as currentDate,to_char( sysdate-30+rownum ,  'dd') as time from dual connect by rownum <=30 )a left join (select time,count(time) as num from (SELECT to_char(creation_date,'dd') as time FROM dsgc_apis where creation_date >sysdate-29) group by time order by time)b on a.time = b.time order by a.currentDate";
    public static String SQL_QUERY_API_MONTH = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_API_MONTH : ORC_SQL_QUERY_API_MONTH;

    public static final String MS_SQL_QUERY_API_YEAR = "select CONCAT(a.time,'月') as name,IFNULL(b.num,0) as value1 from ( \n" +
            " with recursive tab1(currentDate, time,num) as (select NOW() as currentDate,DATE_FORMAT(NOW(),'%m') as time, @num := 1 \n" +
            "union all select ADDDATE(NOW(),INTERVAL num-12 MONTH) as currentDate ,DATE_FORMAT(ADDDATE(NOW(),INTERVAL num-12 MONTH),'%m')as time, @num := @num + 1 from tab1 where num < 12 ) select currentDate,time from tab1\n" +
            "   )a left join (select time,count(time) as num from (SELECT DATE_FORMAT(creation_date,'%m') as time FROM dsgc_apis \n" +
            "   where creation_date >ADDDATE(NOW(),INTERVAL -12 MONTH)) d group by time order by time)b on a.time = b.time order by a.currentDate";
    public static final String ORC_SQL_QUERY_API_YEAR = "select a.time||'月' as name,nvl(b.num,0) as value1 from (\n" +
            " select add_months(sysdate,-12+rownum) as currentDate,to_char( add_months(sysdate,-12+rownum) ,  'mm') as time\n" +
            "  from dual connect by rownum <=12 )a left join (select time,count(time) as num from (SELECT to_char(creation_date,'mm') as time FROM dsgc_apis " +
            "  where creation_date >add_months(sysdate,-12)) group by time order by time)b on a.time = b.time order by a.currentDate ";
    public static final String SQL_QUERY_API_YEAR = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_API_YEAR : ORC_SQL_QUERY_API_YEAR;

    public static final String MS_SQL_QUERY_MONTH_SUCCESS = "select e.sys_code,e.sys_name,t.month as name,sum(t.total_200) as value1 from rp_api_month t left join  dsgc_apis a on a.api_code=t.serv_no left join  dsgc_system_entities e on e.sys_code=a.app_code where t.year=DATE_FORMAT(CURRENT_DATE,'%Y') and e.sys_code=#appId group by e.sys_code,e.sys_name,t.month order by t.month";
    public static final String ORC_SQL_QUERY_MONTH_SUCCESS = "select e.sys_code,e.sys_name,t.month as name,sum(t.total_200) as value1 from rp_api_month t left join  dsgc_apis a on a.api_code=t.serv_no left join  dsgc_system_entities e on e.sys_code=a.app_code where t.year=to_char(sysdate,'yyyy') and e.sys_code=#appId group by e.sys_code,e.sys_name,t.month order by t.month";
    public static final String SQL_QUERY_MONTH_SUCCESS = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_MONTH_SUCCESS : ORC_SQL_QUERY_MONTH_SUCCESS;

    public static final String MS_SQL_QUERY_MONTH_RUN_TIMES = "select e.sys_code,e.sys_name,t.month as name,sum(t.total_times) as value1 from rp_api_month t left join  dsgc_apis a on a.api_code=t.serv_no left join  dsgc_system_entities e on e.sys_code=a.app_code where t.year=DATE_FORMAT(CURRENT_DATE,'%Y') and e.sys_code=#appId group by e.sys_code,e.sys_name,t.month order by t.month";
    public static final String ORC_SQL_QUERY_MONTH_RUN_TIMES = "select e.sys_code,e.sys_name,t.month as name,sum(t.total_times) as value1 from rp_api_month t left join  dsgc_apis a on a.api_code=t.serv_no left join  dsgc_system_entities e on e.sys_code=a.app_code where t.year=to_char(sysdate,'yyyy')  and e.sys_code=#appId group by e.sys_code,e.sys_name,t.month order by t.month";
    public static final String SQL_QUERY_MONTH_RUN_TIMES = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_MONTH_RUN_TIMES : ORC_SQL_QUERY_MONTH_RUN_TIMES;

    public static final String SQL_QUERY_CONSUMER_TOTAL = "  select count(1) as value1 from (                      \n" +
            "     select e.csm_code   as value1 from dsgc_consumer_entities e\n" +
            "                 left join dsgc_apis_access aa on aa.csm_code=e.csm_code\n" +
            "                 left join dsgc_apis a on a.api_code=aa.api_code\n" +
            "                 where a.api_code is not null\n" +
            "                     and e.creation_date between #startDate  and #endDate\n" +
            "                   group by e.csm_code\n" +
            "                   ) a ";

    public static final String MS_SQL_QUERY_CONSUMER_SEVEN = "select (case a.time when 2 then 'monday' when 3 then 'tuesday' when 4 then 'wednesday' when 5 then 'thursday' when 6 then 'friday' when 7 then 'saturday' else 'sunday' end) as name,IFNULL(b.num,0) as value1 from ( with recursive tab1(currentDate, time,num) as (select NOW() as currentDate,DAYOFWEEK(NOW()) as time, @num := 1 union all select ADDDATE(NOW(),INTERVAL num-7 DAY) as currentDate ,DAYOFWEEK(ADDDATE(NOW(),INTERVAL num-7 DAY))as time, @num := @num + 1 from tab1 where num < 7) select currentDate,time from tab1 \n" +
            ")a left join   (select time,count(time) as num from (SELECT DAYOFWEEK(creation_date) as time FROM  \n" +
            "(select distinct e.csm_code,e.creation_date,e.csm_name   as value1 from dsgc_consumer_entities e \n" +
            "left join dsgc_apis_access aa on aa.csm_code=e.csm_code  \n" +
            "left join dsgc_apis a on a.api_code=aa.api_code \n" +
            "where a.api_code is not null) c  where creation_date >CURRENT_DATE-6) d\n" +
            "group by time order by time)b on a.time = b.time order by a.currentDate ";
    public static final String ORC_SQL_QUERY_CONSUMER_SEVEN = "select a.time as name,nvl(b.num,0) as value1 from (\n" +
            " select  sysdate-7+rownum as currentDate,to_char(sysdate-7 + rownum ,  'day') as time from dual connect by rownum <=7 )a left join (select time,count(time) as num from (SELECT to_char(creation_date,'day') as time FROM \n" +
            "   (select distinct e.csm_code,e.creation_date,e.csm_name   as value1 from dsgc_consumer_entities e\n" +
            "   left join dsgc_apis_access aa on aa.csm_code=e.csm_code left join dsgc_apis a on a.api_code=aa.api_code\n" +
            "    where a.api_code is not null) where creation_date >sysdate-6) group by time order by time)b on a.time = b.time order by a.currentDate";
    public static final String SQL_QUERY_CONSUMER_SEVEN = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_CONSUMER_SEVEN : ORC_SQL_QUERY_CONSUMER_SEVEN;

    public static final String MS_SQL_QUERY_CONSUMER_MONTH = "select CONCAT(a.time,'日') as name,IFNULL(b.num,0) as value1 from (\n" +
            "with recursive tab1(currentDate, time,num) as (select NOW() as currentDate,DATE_FORMAT(NOW(),'%d') as time, @num := 1 \n" +
            "union all select ADDDATE(NOW(),INTERVAL num-30 DAY) as currentDate ,DATE_FORMAT(ADDDATE(NOW(),INTERVAL num-30 DAY),'%d')as time, @num := @num + 1 from tab1 where num < 30 ) select currentDate,time from tab1\n" +
            ")a left join (select d.time,count(d.time) as num from (SELECT DATE_FORMAT(creation_date,'%d') as time FROM \n" +
            "(select distinct e.csm_code,e.creation_date,e.csm_name as value1 from dsgc_consumer_entities e \n" +
            "left join dsgc_apis_access aa on aa.csm_code=e.csm_code left join dsgc_apis a on a.api_code=aa.api_code\n" +
            " where a.api_code is not null) c where c.creation_date > CURRENT_DATE - 29) d group by d.time order by d.time) b on a.time = b.time \n" +
            " order by a.currentDate ";
    public static final String ORC_SQL_QUERY_CONSUMER_MONTH = "select a.time||'日' as name,nvl(b.num,0) as value1 from (\n" +
            "   select  sysdate-30+rownum as currentDate,to_char(sysdate-30 + rownum ,  'dd') as time\n" +
            "   from dual  connect by rownum <=30 )a left join\n" +
            "  (select time,count(time) as num from (SELECT to_char(creation_date,'dd') as time FROM (select distinct e.csm_code,e.creation_date,e.csm_name   as value1 from dsgc_consumer_entities e\n" +
            "    left join dsgc_apis_access aa on aa.csm_code=e.csm_code left join dsgc_apis a on a.api_code=aa.api_code\n" +
            "    where a.api_code is not null) where creation_date >sysdate-29) group by time order by time)b on a.time = b.time order by a.currentDate";
    public static final String SQL_QUERY_CONSUMER_MONTH = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_CONSUMER_MONTH : ORC_SQL_QUERY_CONSUMER_MONTH;


    public static final String MS_SQL_QUERY_CONSUMER_YEAR = "select CONCAT(a.time,'月') as name,IFNULL(b.num,0) as value1 from ( \n" +
            "   with recursive tab1(currentDate, time,num) as (select NOW() as currentDate,DATE_FORMAT(NOW(),'%m') as time, @num := 1 \n" +
            "union all select ADDDATE(NOW(),INTERVAL num-12 MONTH) as currentDate ,DATE_FORMAT(ADDDATE(NOW(),INTERVAL num-12 MONTH),'%m')as time, @num := @num + 1 from tab1 where num < 12 ) select currentDate,time from tab1\n" +
            "  )a left join (select time,count(time) as num from (SELECT DATE_FORMAT(creation_date,'%m') as time FROM dsgc_system_entities\n" +
            "   where creation_date > ADDDATE(NOW(),INTERVAL -12 MONTH)) d group by time order by time)b on a.time = b.time order by a.currentDate ";
    public static final String ORC_SQL_QUERY_CONSUMER_YEAR = "select a.time||'月' as name,nvl(b.num,0) as value1 from (\n" +
            "   select add_months(sysdate,-12+rownum) as currentDate,to_char(add_months(sysdate,-12+rownum) ,  'mm') as time\n" +
            "   from dual connect by rownum <=12 )a left join (select time,count(time) as num from (SELECT to_char(creation_date,'mm') as time FROM dsgc_system_entities   \n" +
            "   where creation_date > add_months(sysdate,-12)) group by time order by time)b on a.time = b.time order by a.currentDate";

    public static final String SQL_QUERY_CONSUMER_YEAR = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_CONSUMER_YEAR : ORC_SQL_QUERY_CONSUMER_YEAR;

    public static final String MS_SQL_QUERY_APP_SEVEN = "select (case a.time when 2 then 'monday' when 3 then 'tuesday' when 4 then 'wednesday' when 5 then 'thursday' when 6 then 'friday' when 7 then 'saturday' else 'sunday' end) as name,IFNULL(b.num,0) as value1 from ( \n" +
            "  with recursive tab1(currentDate, time,num) as (select NOW() as currentDate,DAYOFWEEK(NOW()) as time, @num := 1 union all select ADDDATE(NOW(),INTERVAL num-7 DAY) as currentDate ,DAYOFWEEK(ADDDATE(NOW(),INTERVAL num-7 DAY))as time, @num := @num + 1 from tab1 where num < 7) select currentDate,time from tab1 \n" +
            ")a left join (select time,count(time) as num from (SELECT DAYOFWEEK(NOW()) as time FROM dsgc_system_entities\n" +
            "  where creation_date >CURRENT_DATE-6) d group by time order by time)b on a.time = b.time order by a.currentDate";
    public static final String ORC_SQL_QUERY_APP_SEVEN = "select a.time as name,nvl(b.num,0) as value1 from (\n" +
            "  select  sysdate-7+rownum as currentDate,to_char(sysdate-7 + rownum ,  'day') as time\n" +
            "  from dual connect by rownum <=7 )a left join (select time,count(time) as num from (SELECT to_char(creation_date,'day') as time FROM dsgc_system_entities   \n" +
            "   where creation_date >sysdate-6) group by time order by time)b on a.time = b.time order by a.currentDate";
    public static final String SQL_QUERY_APP_SEVEN = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_APP_SEVEN : ORC_SQL_QUERY_APP_SEVEN;

    public static final String MS_QUERY_APP_MONTH = "select CONCAT(a.time,'日') as name,IFNULL(b.num,0) as value1 from (\n" +
            "  with recursive tab1(currentDate, time,num) as (select NOW() as currentDate,DATE_FORMAT(NOW(),'%d') as time, @num := 1 \n" +
            "union all select ADDDATE(NOW(),INTERVAL num-30 DAY) as currentDate ,DATE_FORMAT(ADDDATE(NOW(),INTERVAL num-30 DAY),'%d')as time, @num := @num + 1 from tab1 where num < 30 ) select currentDate,time from tab1\n" +
            ")a left join (select time,count(time) as num from (SELECT DATE_FORMAT(creation_date,'%d') as time FROM dsgc_system_entities \n" +
            "   where creation_date > CURRENT_DATE-30) d group by time order by time)b on a.time = b.time order by a.currentDate ";
    public static final String ORC_QUERY_APP_MONTH = "select a.time||'日' as name,nvl(b.num,0) as value1 from (\n" +
            " select  sysdate-30+rownum as currentDate,to_char(sysdate-30 + rownum ,  'dd') as time from dual \n" +
            " connect by rownum <=30 )a left join (select time,count(time) as num from (SELECT to_char(creation_date,'dd') as time FROM dsgc_system_entities   \n" +
            " where creation_date > sysdate-30) group by time order by time)b on a.time = b.time order by a.currentDate";
    public static final String SQL_QUERY_APP_MONTH = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_QUERY_APP_MONTH : ORC_QUERY_APP_MONTH;

    public static final String MS_SQL_QUERY_APP_YEAR = "\tselect CONCAT(a.time,'月') as name,IFNULL(b.num,0) as value1 from ( \n" +
            "   with recursive tab1(currentDate, time,num) as (select NOW() as currentDate,DATE_FORMAT(NOW(),'%m') as time, @num := 1 \n" +
            "union all select ADDDATE(NOW(),INTERVAL num-12 MONTH) as currentDate ,DATE_FORMAT(ADDDATE(NOW(),INTERVAL num-12 MONTH),'%m')as time, @num := @num + 1 from tab1 where num < 12 ) select currentDate,time from tab1\n" +
            "  )a left join (select time,count(time) as num from (SELECT DATE_FORMAT(creation_date,'%m') as time FROM dsgc_system_entities  \n" +
            " where creation_date > ADDDATE(NOW(),INTERVAL -12 MONTH)) d group by time order by time)b on a.time = b.time order by a.currentDate ";
    public static final String ORC_SQL_QUERY_APP_YEAR = "select a.time||'月' as name,nvl(b.num,0) as value1 from (\n" +
            "  select add_months(sysdate,-12+rownum) as currentDate,to_char(add_months(sysdate,-12+rownum) ,  'mm') as time\n" +
            "  from dual connect by rownum <=12 )a left join (select time,count(time) as num from (SELECT to_char(creation_date,'mm') as time FROM dsgc_system_entities   \n" +
            "  where creation_date > add_months(sysdate,-12)) group by time order by time)b on a.time = b.time order by a.currentDate";

    public static final String SQL_QUERY_APP_YEAR = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? MS_SQL_QUERY_APP_YEAR : ORC_SQL_QUERY_APP_YEAR;


    /*** report **/
    public static final String CMN_QUERY_REPORT_SQL_ORACLE = "select r.CLIENT as SYS_CODE,r.RECEIVE as TRG_SYS_CODE,r.SERV_NO as SVC_CODE,(select s.serv_name from DSGC_SERVICES s where s.SERV_NO = r.serv_no) as SVC_NAME,nvl(r.TOTAL_TIMES,0) as TOTAL_TIMES,nvl(p.AVG_COST,0) as AVG_COST,nvl(r.AVG_COST,0) as TRG_AVG_COST,nvl(p.AVG_COST-r.AVG_COST,0) as ESB_AVG_COST ";
    public static final String CMN_QUERY_REPORT_SQL_MYSQL = " select r.CLIENT as SYS_CODE,r.RECEIVE as TRG_SYS_CODE,r.SERV_NO as SVC_CODE,(select s.serv_name from DSGC_SERVICES s where s.SERV_NO = r.serv_no) as SVC_NAME,IFNULL(r.TOTAL_TIMES,0)  as TOTAL_TIMES,IFNULL(p.AVG_COST,0) as AVG_COST,IFNULL(r.AVG_COST,0) as TRG_AVG_COST,IFNULL(p.AVG_COST-r.AVG_COST,0) as ESB_AVG_COST ";
    public static final String SQL_VIEW_YEAR_END = ",r.YEAR,r.MONTH from RP_SYS_MONTH r LEFT JOIN RP_SERV_MONTH p on r.SERV_NO = p.SERV_NO and p.CLIENT = r.CLIENT and r.YEAR = p.YEAR and r.MONTH = p.MONTH  ";
    public static final String SQL_VIEW_MONTH_END = ",r.YEAR,r.MONTH from RP_SYS_MONTH r LEFT JOIN RP_SERV_MONTH p on r.SERV_NO = p.SERV_NO and p.CLIENT = r.CLIENT and r.YEAR = p.YEAR and r.MONTH = p.MONTH  ";
    public static final String SQL_VIEW_DAY_END = ",r.YEAR,r.MONTH,r.DAY,r.HOUR from RP_SYS_HOUR r LEFT JOIN RP_SERV_HOUR p on r.SERV_NO = p.SERV_NO and p.CLIENT = r.CLIENT and r.YEAR = p.YEAR and r.MONTH = p.MONTH and r.DAY = p.DAY and r.HOUR = p.HOUR ";

    public static final String SQL_VIEW_YEAR_RPT = DB_TYPE_MYSQL.equalsIgnoreCase(dbType) ? CMN_QUERY_REPORT_SQL_MYSQL + SQL_VIEW_YEAR_END : CMN_QUERY_REPORT_SQL_ORACLE + SQL_VIEW_YEAR_END;
    ;
    public static final String SQL_VIEW_MONTH_RPT = "";
    public static final String SQL_VIEW_DAY_RPT = "";
    public static final String SQL_VIEW = "";

}
