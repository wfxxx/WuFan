package com.definesys.dsgc.service.apiHome;

import com.definesys.dsgc.service.apiHome.bean.ApiHomeHisto;
import com.definesys.dsgc.service.market.bean.MarketEntiy;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ApiHomeDao
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-28 10:26
 * @Version 1.0
 **/
@Repository
public class ApiHomeDao {
    @Autowired
    private MpaasQueryFactory sw;


    public List<MarketEntiy> queryApiIcrease(){
       return  sw.buildQuery().sql("SELECT t.api_code servNo,t.api_name servName,t.creation_date creationDate FROM dsgc_apis t " +
               "WHERE TO_CHAR(t.creation_date,'YYYY-MM')=TO_CHAR(SYSDATE,'YYYY-MM') order by t.creation_date desc")
                .doQuery(MarketEntiy.class);
    }
    ///查找dsgc_apis表统计
        public ApiHomeHisto getTotalA(){
            return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_apis t ").doQueryFirst(ApiHomeHisto.class);
        }
        public ApiHomeHisto getTodyTotalA(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_apis t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE,'YYYY-MM-DD')").doQueryFirst(ApiHomeHisto.class);
        }
        public ApiHomeHisto getYestodayTotalA(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_apis t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE-1,'YYYY-MM-DD')").doQueryFirst(ApiHomeHisto.class);
        }
        public ApiHomeHisto getLastWeekTotalA(){
        return sw.buildQuery().sql("  SELECT COUNT(1) as value FROM dsgc_apis  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE-7,1))").doQueryFirst(ApiHomeHisto.class);
        }
        public ApiHomeHisto getNowWeekTotalA(){
        return sw.buildQuery().sql("  SELECT COUNT(1) as value FROM dsgc_apis t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE,1))").doQueryFirst(ApiHomeHisto.class);
        }


    //查找dsgc_system_entities表统计
    public ApiHomeHisto getTotalE(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_system_entities t ").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getTodyTotalE(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_system_entities t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE,'YYYY-MM-DD')").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getYestodayTotalE(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_system_entities t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE-1,'YYYY-MM-DD')").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getLastWeekTotalE(){
        return sw.buildQuery().sql("  SELECT COUNT(1) as value FROM dsgc_system_entities  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE-7,1))").doQueryFirst(ApiHomeHisto.class);

    }
    public ApiHomeHisto getNowWeekTotalE(){
        return sw.buildQuery().sql("  SELECT COUNT(1) as value FROM dsgc_system_entities  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE,1))").doQueryFirst(ApiHomeHisto.class);

    }

    //查找dsgc_user表统计
    public ApiHomeHisto getTotalU(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_consumer_entities t ").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getTodyTotalU(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_consumer_entities t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE,'YYYY-MM-DD')").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getYestodayTotalU(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_consumer_entities t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE-1,'YYYY-MM-DD')").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getLastWeekTotalU(){
        return sw.buildQuery().sql("  SELECT COUNT(1) as value FROM dsgc_consumer_entities  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE-7,1))").doQueryFirst(ApiHomeHisto.class);

    }
    public ApiHomeHisto getNowWeekTotalU(){
        return sw.buildQuery().sql("  SELECT COUNT(1) as value FROM dsgc_consumer_entities  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE,1))").doQueryFirst(ApiHomeHisto.class);

    }

    //查找访问量
    public ApiHomeHisto getTotalV(){
        return sw.buildQuery().sql("SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_year t ").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getTodyTotalV(){
        return sw.buildQuery().sql("SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day t where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getYestodayTotalV(){
        return sw.buildQuery().sql("SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day t WHERE  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate-1,'yyyy-mm-dd'),'yyyy-mm-dd')").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getLastWeekTotalV(){
        return sw.buildQuery().sql("  SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day  t WHERE to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=TRUNC(NEXT_DAY(SYSDATE-7,1))").doQueryFirst(ApiHomeHisto.class);

    }
    public ApiHomeHisto getNowWeekTotalV(){
        return sw.buildQuery().sql("  SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day  t WHERE to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=TRUNC(NEXT_DAY(SYSDATE,1))").doQueryFirst(ApiHomeHisto.class);

    }





    //查询任务总数
    public List<Map<String,Object>> queryTaskTotal(String userId){
        return  sw.buildQuery().sql("select count(1) from\n"+
                "    dsgc_bpm_instance dbi,\n"+
                "    dsgc_bpm_task dbt\n"+
                "    where dbt.approver = #userId\n"+
                "    and dbt.node_id = dbi.cur_node and dbt.inst_id = dbi.inst_id")
                .setVar("userId",userId)
                .doQuery();

    }


    //查询新增任务
    public List<Map<String,Object>> queryTaskdayIncrease(String userId){
        return  sw.buildQuery().sql("select count(1) from\n"+
                "    dsgc_bpm_instance dbi,\n"+
                "    dsgc_bpm_task dbt\n"+
                "    where dbt.approver = #userId\n"+
                "    and dbt.node_id = dbi.cur_node and dbt.inst_id = dbi.inst_id  and to_char(dbt.creation_date,'YYYY-MM-DD')=to_char(sysdate,'YYYY-MM-DD')")
                .setVar("userId",userId)
                .doQuery();

    }

    //按应用分类获取api信息,获取全部
    public List<ApiHomeHisto> queryApiInfoByAppALL(){
    List<ApiHomeHisto> result=new ArrayList<ApiHomeHisto>();

        return  sw.buildQuery().sql("select sys_name as name,count(sys_name) as value,sys_name as legend_name  from\n" +
                "  (select a.api_code,a.api_name,e.sys_code,e.sys_name\n" +
                "  from dsgc_apis a \n" +
                "  left join dsgc_system_entities e on a.app_code=e.sys_code)\n" +
                "  group by sys_code,sys_name").doQuery(ApiHomeHisto.class);
    }

    //按类型分类获取个人api信息
    public List<ApiHomeHisto> queryApiInfoByType(String userId){
        return sw.buildQuery().sql("select \n" +
                "(case when t.cate_name is not null then t.cate_name else '未知' end)  as name,\n" +
                "(count(case when t.cate_name is not null then t.cate_name else '未知' end)) as value , \n" +
                "(case when t.cate_name is not null then t.cate_name else '未知' end) as legend_name  from\n" +
                "\n" +
                "  (select a.api_code,a.api_name,c.cate_name ,c.cate_code \n" +
                "  from (select a.* from dsgc_apis a,dsgc_apis_access aa,dsgc_consumer_users cu where a.api_code=aa.api_code and cu.csm_code=aa.csm_code and cu.user_id=#userId) a \n" +
                "  left join dsgc_market_category c on a.market_category=c.cate_code) t \n" +
                "  group by t.cate_name,t.cate_code").setVar("userId",userId).doQuery(ApiHomeHisto.class);
    }

    //按类型分类获取全部api信息
    public List<ApiHomeHisto> queryApiInfoByTypeALL(){
        return  sw.buildQuery().sql("select \n" +
                "(case when t.cate_name is not null then t.cate_name else '未知' end)  as name,\n" +
                "(count(case when t.cate_name is not null then t.cate_name else '未知' end)) as value , \n" +
                "(case when t.cate_name is not null then t.cate_name else '未知' end) as legend_name  from\n" +
                "\n" +
                "  (select a.api_code,a.api_name,c.cate_name ,c.cate_code \n" +
                "  from dsgc_apis a \n" +
                "  left join dsgc_market_category c on a.market_category=c.cate_code) t \n" +
                "  group by t.cate_name,t.cate_code").doQuery(ApiHomeHisto.class);
    }



    //获取Api访问排序柱状图数据
    //TODO
    public PageQueryResult<ApiHomeHisto> querySortVist(String startTime,String endTime,String limitTime){
        List<ApiHomeHisto> result=new ArrayList<ApiHomeHisto>();
        MpaasQuery resulSql =sw.buildQuery();
        if(startTime!=null&&endTime!=null&&limitTime==null) {
            resulSql.sql("select t.serv_no as name ,sum(t.total_times)  over(partition by t.serv_no) as value from rp_api_day t \n" +
                    "                    where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=to_date(#endTime,'yyyy-mm-dd') and to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>to_date(#startTime,'yyyy-mm-dd') order by value desc")
                    .setVar("endTime",endTime).setVar("startTime",startTime);
        }
        else if(limitTime.equals("year")){
            resulSql .sql("select t.serv_no as name ,sum(t.total_times)  over(partition by t.serv_no) as value from rp_api_year t\n" +
                    " where to_date(t.year,'yyyy')=to_date(to_char(sysdate,'yyyy'),'yyyy') order by value desc");
        }else if(limitTime.equals("month")){
            resulSql .sql("select t.serv_no as name ,sum(t.total_times)  over(partition by t.serv_no) as value from rp_api_month t\n" +
                    " where to_date(t.year||'-'||t.month,'yyyy-mm')=to_date(to_char(sysdate,'yyyy-mm'),'yyyy-mm') order by value desc");
        }else if(limitTime.equals("week")){
            resulSql.sql("select t.serv_no as name ,sum(t.total_times)  over(partition by t.serv_no) as value from rp_api_day t\n" +
                    "  where to_char(to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd'),'iw')=to_char(sysdate,'iw') and t.year=to_char(sysdate,'yyyy') order by value desc ");
        }else if(limitTime.equals("day")){
            resulSql .sql("select t.serv_no as name ,sum(t.total_times)  over(partition by t.serv_no) as value from rp_api_day t\n" +
                    " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by value desc");
        }
        return resulSql.doPageQuery(1,10,ApiHomeHisto.class);
    }

    //获取Api并发排序柱状图数据
    //TODO
    public PageQueryResult<ApiHomeHisto> querySortConcurrent(String startTime,String endTime,String limitTime){

        List<ApiHomeHisto> result=new ArrayList<ApiHomeHisto>();
        MpaasQuery resulSql =sw.buildQuery();
        if(startTime!=null&&endTime!=null&&limitTime==null) {
            resulSql.sql("select t.serv_no as name ,sum(t.total_times)  over(partition by t.serv_no) as value from rp_api_day t \n" +
                    "                    where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=to_date(#endTime,'yyyy-mm-dd') and to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>to_date(#startTime,'yyyy-mm-dd') order by value desc")
                    .setVar("endTime",endTime).setVar("startTime",startTime);
        }
        else if(limitTime.equals("year")){
            resulSql .sql("select t.serv_no as name,max(t.total_times) over(partition by t.serv_no )  as value from rp_api_day t\n" +
                    " where to_date(t.year,'yyyy')=to_date(to_char(sysdate,'yyyy'),'yyyy') order by value desc");
        }else if(limitTime.equals("month")){
            resulSql .sql("select t.serv_no as name,max(t.total_times) over(partition by t.serv_no )  as value from rp_api_day t \n" +
                    " where to_date(t.year||'-'||t.month,'yyyy-mm')=to_date(to_char(sysdate,'yyyy-mm'),'yyyy-mm') order by value desc");
        }else if(limitTime.equals("week")){
            resulSql.sql("select t.serv_no as name,max(t.total_times) over(partition by t.serv_no )  as value from rp_api_day t \n" +
                    "  where to_char(to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd'),'iw')=to_char(sysdate,'iw') and t.year=to_char(sysdate,'yyyy') order by value desc");
        }else if(limitTime.equals("day")){
            resulSql .sql("select t.serv_no as name,max(t.total_times) over(partition by t.serv_no )  as value from rp_api_day t \n" +
                    " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by value desc");
        }
        return resulSql.doPageQuery(1,10,ApiHomeHisto.class);


    }

    //获取Api等待排序柱状图数据
    public PageQueryResult<ApiHomeHisto> querySortWait(String startTime, String endTime, String limitTime){
        List<ApiHomeHisto> result=new ArrayList<ApiHomeHisto>();
        MpaasQuery resulSql =sw.buildQuery();
        if(startTime!=null&&endTime!=null&&limitTime==null) {
            resulSql.sql("select t.serv_no as name ,sum(t.total_times)  over(partition by t.serv_no) as value from rp_api_day t \n" +
                    "                    where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=to_date(#endTime,'yyyy-mm-dd') and to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>to_date(#startTime,'yyyy-mm-dd') order by value desc")
                    .setVar("endTime",endTime).setVar("startTime",startTime);

        }
        else if(limitTime.equals("year")){
            resulSql .sql("select t.serv_no as name ,avg(t.avg_cost)  over(partition by t.serv_no) as value from rp_api_year t\n" +
                    "                     where to_date(t.year,'yyyy')=to_date(to_char(sysdate,'yyyy'),'yyyy') order by value desc");
        }else if(limitTime.equals("month")){
            resulSql .sql("select t.serv_no as name ,avg(t.avg_cost)  over(partition by t.serv_no) as value from rp_api_month t\n" +
                    " where to_date(t.year||'-'||t.month,'yyyy-mm')=to_date(to_char(sysdate,'yyyy-mm'),'yyyy-mm')  order by value desc");
        }else if(limitTime.equals("week")){
            resulSql.sql("select t.serv_no as name ,avg(t.avg_cost)  over(partition by t.serv_no) as value from rp_api_day t\n" +
                    "  where to_char(to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd'),'iw')=to_char(sysdate,'iw') and t.year=to_char(sysdate,'yyyy') order by value desc");
        }else if(limitTime.equals("day")){
            resulSql .sql("select t.serv_no as name ,avg(t.avg_cost)  over(partition by t.serv_no) as value from rp_api_day t\n" +
                    " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by value desc");
        }

        return resulSql.doPageQuery(1,10,ApiHomeHisto.class);
    }


    //查询当天运行次数流量
    public List<ApiHomeHisto> queryTrafficRuntimes(){
       return sw.buildQuery().sql("select to_char(t.creation_date,'hh24:mi') as name ,sum(t.total_times)  over(partition by t.hour) as value from rp_api_hour t \n" +
               "                where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by t.creation_date   ")
                .doQuery(ApiHomeHisto.class);
    }

    //查询当天平均响应时间
    public List<ApiHomeHisto> queryTrafficCost(){
        return  sw.buildQuery().sql("        select to_char(t.creation_date,'hh24:mi') as name ,avg(t.avg_cost)   over(partition by t.hour) as value from rp_api_hour t\n" +
                "        where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by t.creation_date")

                .doQuery(ApiHomeHisto.class);
    }

    //查询当天错误次数流量
     public List<ApiHomeHisto> queryTrafficError(){
         return   sw.buildQuery().sql("select to_char(t.creation_date,'hh24:mi') as name,sum(t.total_1xx+t.total_2xx+t.total_3xx+t.total_4xx+t.total_5xx)  over(partition by t.hour) as value from rp_api_hour t \n" +
                 "                where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by t.creation_date")
                 .doQuery(ApiHomeHisto.class);
     }


    //查询近一小时运行次数流量
    public ApiHomeHisto queryTrafficRuntimesNow(){
        return sw.buildQuery().sql("select count(1)   as value from dsgc_log_instance t \n" +
                "where sysdate>to_date(to_char(sysdate,'yyyy-mm-dd hh24'),'yyyy-mm-dd hh24') order by t.creation_date   ")
                .doQueryFirst(ApiHomeHisto.class);
    }

    //查询近一小时平均响应时间
//    public ApiHomeHisto queryTrafficCostNow(){
//
//    }

    //查询近一小时错误次数流量
    public ApiHomeHisto queryTrafficErrorNow(){
        return   sw.buildQuery().sql("select count(1)   as value from dsgc_log_instance t \n" +
                "                where sysdate>to_date(to_char(sysdate,'yyyy-mm-dd hh24'),'yyyy-mm-dd hh24') and t.inst_status='0' order by t.creation_date  ")
                .doQueryFirst(ApiHomeHisto.class);
    }

    public  ApiHomeHisto queryAdminLocaltion(String code){
        return sw.buildQuery().sql("select t.admin_location as name from  dsgc_env_info_cfg t where t.env_code=#code").
                setVar("code",code).doQueryFirst(ApiHomeHisto.class);
    }

}
