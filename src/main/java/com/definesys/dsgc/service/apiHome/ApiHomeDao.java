package com.definesys.dsgc.service.apiHome;

import com.definesys.dsgc.service.apiHome.bean.ApiHomeHisto;
import com.definesys.dsgc.service.market.bean.MarketEntiy;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${database.type}")
    private String dbType;

    public List<MarketEntiy> queryApiIcrease(){
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT t.api_code servNo,t.api_name servName,t.creation_date creationDate FROM dsgc_apis t WHERE TO_CHAR(t.creation_date,'YYYY-MM')=TO_CHAR(SYSDATE,'YYYY-MM') order by t.creation_date desc";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT t.api_code servNo,t.api_name servName,t.creation_date creationDate FROM dsgc_apis t WHERE date_format(t.creation_date,'%Y-%m')=date_format(CURRENT_TIMESTAMP,'%Y-%m') order by t.creation_date desc";
        }
       return  sw.buildQuery().sql(sql)
                .doQuery(MarketEntiy.class);
    }
    ///查找dsgc_apis表统计
        public ApiHomeHisto getTotalA(){
            return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_apis t ").doQueryFirst(ApiHomeHisto.class);
        }
        public ApiHomeHisto getTodyTotalA(){
            String sql = null;
            if("oracle".equals(dbType)){
                sql = "SELECT COUNT(1) as value FROM dsgc_apis t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE,'YYYY-MM-DD')";
            }
            if ("mysql".equals(dbType)){
                sql = "SELECT COUNT(1) as value FROM dsgc_apis t WHERE date_format(t.creation_date,'%Y-%m-%d')=date_format(CURRENT_TIMESTAMP,'%Y-%m-%d')";
            }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);
        }
        public ApiHomeHisto getYestodayTotalA(){
            String sql = null;
            if("oracle".equals(dbType)){
               sql = "SELECT COUNT(1) as value FROM dsgc_apis t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE-1,'YYYY-MM-DD')";
            }
            if ("mysql".equals(dbType)){
                sql = "SELECT COUNT(1) as value FROM dsgc_apis t WHERE date_format(t.creation_date,'%Y-%m-%d')=date_format(CURRENT_TIMESTAMP-1,'%Y-%m-%d')";
            }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);
        }
        public ApiHomeHisto getLastWeekTotalA(){
            String sql = null;
            if("oracle".equals(dbType)){
                sql = "SELECT COUNT(1) as value FROM dsgc_apis  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE-7,1))";
            }
            if ("mysql".equals(dbType)){
                sql = "SELECT COUNT(1) as value FROM dsgc_apis  t WHERE t.creation_date>= DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  t.creation_date<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day),'%Y-%m-%d %H:%i:%s')";
            }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);
        }
        public ApiHomeHisto getNowWeekTotalA(){
            String sql = null;
            if("oracle".equals(dbType)){
                sql = " SELECT COUNT(1) as value FROM dsgc_apis t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE,1))";
            }
            if ("mysql".equals(dbType)){
                sql = " SELECT COUNT(1) as value FROM dsgc_apis t WHERE t.creation_date >= DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  t.creation_date<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day),'%Y-%m-%d %H:%i:%s')";
            }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);
        }


    //查找dsgc_system_entities表统计
    public ApiHomeHisto getTotalE(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_system_entities t ").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getTodyTotalE(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE,'YYYY-MM-DD')";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities t WHERE DATE_FORMAT(t.creation_date,'%Y-%m-%d')=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getYestodayTotalE(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE-1,'YYYY-MM-DD')";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities t WHERE DATE_FORMAT(t.creation_date,'%Y-%m-%d')=DATE_FORMAT(CURRENT_TIMESTAMP-1,'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getLastWeekTotalE(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE-7,1))";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities  t WHERE t.creation_date>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  t.creation_date<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);

    }
    public ApiHomeHisto getNowWeekTotalE(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE,1))";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities  t WHERE t.creation_date>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  t.creation_date<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);

    }

    //查找dsgc_user表统计
    public ApiHomeHisto getTotalU(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_consumer_entities t ").doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getTodyTotalU(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_consumer_entities t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE,'YYYY-MM-DD')";
        }
        if ("mysql".equals(dbType)){
           sql = "SELECT COUNT(1) as value FROM dsgc_consumer_entities t WHERE DATE_FORMAT(t.creation_date,'%Y-%m-%d')=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getYestodayTotalU(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_consumer_entities t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE-1,'YYYY-MM-DD')";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT COUNT(1) as value FROM dsgc_consumer_entities t WHERE DATE_FORMAT(t.creation_date,'%Y-%m-%d')=DATE_FORMAT(CURRENT_TIMESTAMP-1,'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);
    }
    public ApiHomeHisto getLastWeekTotalU(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_consumer_entities  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE-7,1))";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT COUNT(1) as value FROM dsgc_consumer_entities  t WHERE t.creation_date>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  t.creation_date<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);

    }
    public ApiHomeHisto getNowWeekTotalU(){
        String sql = null;
        if("oracle".equals(dbType)) {
         sql = "SELECT COUNT(1) as value FROM dsgc_consumer_entities  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE,1))";
        }
        if ("mysql".equals(dbType)){
           sql = "SELECT COUNT(1) as value FROM dsgc_consumer_entities  t WHERE t.creation_date>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  t.creation_date<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(ApiHomeHisto.class);

    }

    //查找访问量
    public Map<String, Object> getTotalV(){
        return sw.buildQuery().sql("SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_year t ").doQueryFirst();
    }
    public Map<String, Object> getTodyTotalV(){
        String sql = null;
        if("oracle".equals(dbType)) {
           sql = "SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day t where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')";
        }
        if ("mysql".equals(dbType)){
            sql ="SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();
    }
    public Map<String, Object> getYestodayTotalV(){
        String sql = null;
        if("oracle".equals(dbType)) {
          sql = "SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day t WHERE  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate-1,'yyyy-mm-dd'),'yyyy-mm-dd')";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day t WHERE  str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP-1,'%Y-%m-%d'),'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();
    }
    //ApiHomeHisto
    public Map<String, Object> getLastWeekTotalV(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day  t WHERE to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=TRUNC(NEXT_DAY(SYSDATE-7,1))";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day  t WHERE str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();

    }
    public Map<String, Object> getNowWeekTotalV(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = " SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day  t WHERE to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=TRUNC(NEXT_DAY(SYSDATE,1))";
        }
        if ("mysql".equals(dbType)){
            sql = " SELECT SUM(T.TOTAL_TIMES) as value FROM rp_api_day  t WHERE str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();

    }





    //查询任务总数
    public List<Map<String,Object>> queryTaskTotal(String userId){
        return  sw.buildQuery().sql("select COUNT(1) from\n"+
                "    dsgc_bpm_instance dbi,\n"+
                "    dsgc_bpm_task dbt\n"+
                "    where dbt.approver = #userId\n"+
                "    and dbt.node_id = dbi.cur_node and dbt.inst_id = dbi.inst_id")
                .setVar("userId",userId)
                .doQuery();

    }


    //查询新增任务
    public List<Map<String,Object>> queryTaskdayIncrease(String userId){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "select count(1) from dsgc_bpm_instance dbi,dsgc_bpm_task dbt where dbt.approver = #userId and dbt.node_id = dbi.cur_node and dbt.inst_id = dbi.inst_id  and to_char(dbt.creation_date,'YYYY-MM-DD')=to_char(sysdate,'YYYY-MM-DD')";
        }
        if ("mysql".equals(dbType)){
            sql = "select COUNT(1) from dsgc_bpm_instance dbi,dsgc_bpm_task dbt where dbt.approver = #userId and dbt.node_id = dbi.cur_node and dbt.inst_id = dbi.inst_id  and DATE_FORMAT(dbt.creation_date,'%Y-%m-%d')=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')";
        }
        return  sw.buildQuery().sql(sql)
                .setVar("userId",userId)
                .doQuery();

    }

    //按应用分类获取api信息,获取全部
    public List<ApiHomeHisto> queryApiInfoByAppALL(){
    List<ApiHomeHisto> result=new ArrayList<ApiHomeHisto>();

        return  sw.buildQuery().sql("select sys_name as name,count(sys_name) as value,sys_name as legend_name  from\n" +
                "  (select a.api_code,a.api_name,e.sys_code,e.sys_name\n" +
                "  from dsgc_apis a \n" +
                "  left join dsgc_system_entities e on a.app_code=e.sys_code) t \n" +
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
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,sum(t.total_times)   as value from rp_api_day t \n" +
                        "                         where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=to_date(#endTime,'yyyy-mm-dd') \n" +
                        "                        and to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>to_date(#startTime,'yyyy-mm-dd') \n" +
                        "                        group by t.serv_no\n" +
                        "                        order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,sum(t.total_times) as value from rp_api_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=str_to_date('','%Y-%m-%d') and str_to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>str_to_date('','%Y-%m-%d') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql.sql(sql)
                    .setVar("endTime",endTime).setVar("startTime",startTime);
        }
        else if(limitTime.equals("year")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,sum(t.total_times)  as value from rp_api_year t " +
                        " where to_date(t.year,'yyyy')=to_date(to_char(sysdate,'yyyy'),'yyyy') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,sum(t.total_times) as value   from rp_api_year t where str_to_date(t.year,'%Y')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y'),'%Y') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("month")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,sum(t.total_times)  as value from rp_api_month t " +
                        " where to_date(t.year||'-'||t.month,'yyyy-mm')=to_date(to_char(sysdate,'yyyy-mm'),'yyyy-mm') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,sum(t.total_times)  as value from rp_api_month t where str_to_date(t.year||'-'||t.month,'%Y-%m')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m'),'%Y-%m') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("week")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,sum(t.total_times)  as value from rp_api_day t" +
                        " where to_char(to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd'),'iw')=to_char(sysdate,'iw') and t.year=to_char(sysdate,'yyyy') group by t.serv_no order by value desc ";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,sum(t.total_times) as value from rp_api_day t where DATE_FORMAT(str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d'),'iw')=DATE_FORMAT(CURRENT_TIMESTAMP,'iw') and t.year=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y') group by t.serv_no order by t.serv_no,value desc ";
            }
            resulSql.sql(sql);
        }else if(limitTime.equals("day")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,sum(t.total_times)   as value from rp_api_day t" +
                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,sum(t.total_times) as value from rp_api_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }
        return resulSql.doPageQuery(1,10,ApiHomeHisto.class);
    }

    //获取Api并发排序柱状图数据
    //TODO
    public PageQueryResult<ApiHomeHisto> querySortConcurrent(String startTime,String endTime,String limitTime){

        List<ApiHomeHisto> result=new ArrayList<ApiHomeHisto>();
        MpaasQuery resulSql =sw.buildQuery();
        if(startTime!=null&&endTime!=null&&limitTime==null) {
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,sum(t.total_times)  as value from rp_api_day t " +
                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=to_date(#endTime,'yyyy-mm-dd') and to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>to_date(#startTime,'yyyy-mm-dd') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,sum(t.total_times)  as value from rp_api_day t  where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=str_to_date(#endTime,'%Y-%m-%d') and str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')>str_to_date(#startTime,'%Y-%m-%d') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql.sql(sql)
                    .setVar("endTime",endTime).setVar("startTime",startTime);
        }
        else if(limitTime.equals("year")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name,max(t.total_times)  as value from rp_api_day t " +
                        " where to_date(t.year,'yyyy')=to_date(to_char(sysdate,'yyyy'),'yyyy') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name,max(t.total_times)  as value from rp_api_day t where str_to_date(t.year,'%Y')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y'),'%Y') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("month")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name,max(t.total_times)  as value from rp_api_day t " +
                        " where to_date(t.year||'-'||t.month,'yyyy-mm')=to_date(to_char(sysdate,'yyyy-mm'),'yyyy-mm') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name,max(t.total_times) as value from rp_api_day t where str_to_date(t.year||'-'||t.month,'%Y-%m')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m'),'%Y-%m') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("week")){
            String sql = null;
            if("oracle".equals(dbType)) {
               sql = "select t.serv_no as name,max(t.total_times)  as value from rp_api_day t " +
                       "  where to_char(to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd'),'iw')=to_char(sysdate,'iw') and t.year=to_char(sysdate,'yyyy') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name,max(t.total_times)  as value from rp_api_day t where DATE_FORMAT(str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d'),'iw')=DATE_FORMAT(CURRENT_TIMESTAMP,'iw') and t.year=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql.sql(sql);
        }else if(limitTime.equals("day")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name,max(t.total_times)   as value from rp_api_day t " +
                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name,max(t.total_times) as value from rp_api_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }
        return resulSql.doPageQuery(1,10,ApiHomeHisto.class);


    }

    //获取Api等待排序柱状图数据
    public PageQueryResult<ApiHomeHisto> querySortWait(String startTime, String endTime, String limitTime){
        List<ApiHomeHisto> result=new ArrayList<ApiHomeHisto>();
        MpaasQuery resulSql =sw.buildQuery();
        if(startTime!=null&&endTime!=null&&limitTime==null) {
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,sum(t.total_times)   as value from rp_api_day t " +
                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=to_date(#endTime,'yyyy-mm-dd') and to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>to_date(#startTime,'yyyy-mm-dd') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,sum(t.total_times) as value from rp_api_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=str_to_date(#endTime,'%Y-%m-%d') and str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')>str_to_date(#startTime,'%Y-%m-%d') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql.sql(sql)
                    .setVar("endTime",endTime).setVar("startTime",startTime);

        }
        else if(limitTime.equals("year")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,avg(t.avg_cost)   as value from rp_api_year t" +
                        " where to_date(t.year,'yyyy')=to_date(to_char(sysdate,'yyyy'),'yyyy') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,avg(t.avg_cost)  as value from rp_api_year t where str_to_date(t.year,'%Y')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y'),'%Y') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("month")){
            String sql = null;
            if("oracle".equals(dbType)) {
             sql = "select t.serv_no as name ,avg(t.avg_cost)   as value from rp_api_month t" +
                     " where to_date(t.year||'-'||t.month,'yyyy-mm')=to_date(to_char(sysdate,'yyyy-mm'),'yyyy-mm') group by t.serv_no  order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,avg(t.avg_cost)  as value from rp_api_month t where str_to_date(t.year||'-'||t.month,'%Y-%m')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m'),'%Y-%m') group by t.serv_no  order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("week")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,avg(t.avg_cost)   as value from rp_api_day t " +
                        "  where to_char(to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd'),'iw')=to_char(sysdate,'iw') and t.year=to_char(sysdate,'yyyy') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,avg(t.avg_cost) as value from rp_api_day t  where DATE_FORMAT(str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d'),'iw')=DATE_FORMAT(CURRENT_TIMESTAMP,'iw') and t.year=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql.sql(sql);
        }else if(limitTime.equals("day")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,avg(t.avg_cost)   as value from rp_api_day t " +
                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') group by t.serv_no order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,avg(t.avg_cost) as value from rp_api_day t  where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') group by t.serv_no order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }

        return resulSql.doPageQuery(1,10,ApiHomeHisto.class);
    }


    //查询当天运行次数流量
    public List<ApiHomeHisto> queryTrafficRuntimes(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "select to_char(t.creation_date,'hh24:mi') as name ,sum(t.total_times)  over(partition by t.hour) as value from rp_api_hour t " +
                    " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by t.creation_date ";
        }
        if ("mysql".equals(dbType)){
            sql = "select DATE_FORMAT(t.creation_date,'%Y-%m') as name ,sum(t.total_times)  as value from rp_api_hour t  where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') order by t.hour,t.creation_date ";
        }
       return sw.buildQuery().sql(sql)
                .doQuery(ApiHomeHisto.class);
    }

    //查询当天平均响应时间
    public List<ApiHomeHisto> queryTrafficCost(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "select to_char(t.creation_date,'hh24:mi') as name ,avg(t.avg_cost)   over(partition by t.hour) as value from rp_api_hour t " +
                    " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by t.creation_date";
        }
        if ("mysql".equals(dbType)){
            sql = "select DATE_FORMAT(t.creation_date,'%Y-%m') as name ,avg(t.avg_cost)  as value from rp_api_hour t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') order by t.hour,t.creation_date";
        }
        return  sw.buildQuery().sql(sql)

                .doQuery(ApiHomeHisto.class);
    }

    //查询当天错误次数流量
     public List<ApiHomeHisto> queryTrafficError(){
         String sql = null;
         if("oracle".equals(dbType)) {
             sql="select to_char(t.creation_date,'hh24:mi') as name,sum(t.total_1xx+t.total_2xx+t.total_3xx+t.total_4xx+t.total_5xx-t.total_200)  over(partition by t.hour) as value from rp_api_hour t " +
                     " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by t.creation_date";
         }
         if ("mysql".equals(dbType)){
             sql ="select DATE_FORMAT(t.creation_date,'%Y-%m') as name,sum(t.total_1xx+t.total_2xx+t.total_3xx+t.total_4xx+t.total_5xx)  as value from rp_api_hour t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') order by t.hour,t.creation_date";
         }
         return   sw.buildQuery().sql(sql)
                 .doQuery(ApiHomeHisto.class);
     }


    //查询近一小时运行次数流量
    public ApiHomeHisto queryTrafficRuntimesNow(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "select count(1)   as value from dsgc_log_instance t " +
                    " where sysdate>to_date(to_char(sysdate,'yyyy-mm-dd hh24'),'yyyy-mm-dd hh24') order by t.creation_date ";
        }
        if ("mysql".equals(dbType)){
            sql = "select count(1)   as value from dsgc_log_instance t where CURRENT_TIMESTAMP>str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d %H'),'%Y-%m-%d %H') order by t.creation_date ";
        }
        return sw.buildQuery().sql(sql)
                .doQueryFirst(ApiHomeHisto.class);
    }

    //查询近一小时平均响应时间
//    public ApiHomeHisto queryTrafficCostNow(){
//
//    }

    //查询近一小时错误次数流量
    public ApiHomeHisto queryTrafficErrorNow(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "select count(1)   as value from dsgc_log_instance t " +
                    "  where sysdate>to_date(to_char(sysdate,'yyyy-mm-dd hh24'),'yyyy-mm-dd hh24') and t.inst_status='0' order by t.creation_date  ";
        }
        if ("mysql".equals(dbType)){
            sql = "select count(1)   as value from dsgc_log_instance t where CURRENT_TIMESTAMP>str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d %H'),'%Y-%m-%d %H') and t.inst_status='0' order by t.creation_date";
        }
        return   sw.buildQuery().sql(sql)
                .doQueryFirst(ApiHomeHisto.class);
    }

    public  ApiHomeHisto queryAdminLocaltion(String code){
        return sw.buildQuery().sql("select t.admin_location as name from  dsgc_env_info_cfg t where t.env_code=#code").
                setVar("code",code).doQueryFirst(ApiHomeHisto.class);
    }

}
