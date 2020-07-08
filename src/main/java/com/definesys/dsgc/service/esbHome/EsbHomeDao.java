package com.definesys.dsgc.service.esbHome;

import com.definesys.dsgc.service.esbHome.bean.EsbHomeHisto;
import com.definesys.dsgc.service.esbHome.bean.EsbHomeHisto;
import com.definesys.dsgc.service.market.bean.MarketEntiy;
import com.definesys.dsgc.service.utils.DayComparePrecise;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

/**
 * @ClassName ESBHomeDao
 * @Description TODO
 * @Author XIEZHONGYUAN
 * @Date 2020-3-2
 * @Version 1.0
 **/
@Repository
public class EsbHomeDao {
    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;

    //获取本月新增ESB详细数据
    public List<MarketEntiy> queryEsbIcrease(){
        String sql = null;
        if("oracle".equals(dbType)){
            sql = "SELECT t.serv_no servNo,t.serv_name servName,t.creation_date creationDate FROM dsgc_services t WHERE TO_CHAR(t.creation_date,'YYYY-MM')=TO_CHAR(SYSDATE,'YYYY-MM') order by t.creation_date desc";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT t.serv_no servNo,t.serv_name servName,t.creation_date creationDate FROM dsgc_services t WHERE date_format(t.creation_date,'%Y-%m')=date_format(CURRENT_TIMESTAMP,'%Y-%m') order by t.creation_date desc";
        }
       return  sw.buildQuery().sql(sql)
                .doQuery(MarketEntiy.class);
    }
    ///查找dsgc_apis表统计
        public EsbHomeHisto getTotalA(){
            return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_services t ").doQueryFirst(EsbHomeHisto.class);
        }
        public EsbHomeHisto getTodyTotalA(){
            String sql = null;
            if("oracle".equals(dbType)){
                sql = "SELECT COUNT(1) as value FROM dsgc_services t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE,'YYYY-MM-DD')";
            }
            if ("mysql".equals(dbType)){
                sql = "SELECT COUNT(1) as value FROM dsgc_services t WHERE date_format(t.creation_date,'%Y-%m-%d')=date_format(CURRENT_TIMESTAMP,'%Y-%m-%d')";
            }
        return sw.buildQuery().sql(sql).doQueryFirst(EsbHomeHisto.class);
        }
        public EsbHomeHisto getYestodayTotalA(){
            String sql = null;
            if("oracle".equals(dbType)){
               sql = "SELECT COUNT(1) as value FROM dsgc_services t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE-1,'YYYY-MM-DD')";
            }
            if ("mysql".equals(dbType)){
                sql = "SELECT COUNT(1) as value FROM dsgc_services t WHERE date_format(t.creation_date,'%Y-%m-%d')=date_format(CURRENT_TIMESTAMP-1,'%Y-%m-%d')";
            }
        return sw.buildQuery().sql(sql).doQueryFirst(EsbHomeHisto.class);
        }
        public EsbHomeHisto getLastWeekTotalA(){
            String sql = null;
            if("oracle".equals(dbType)){
                sql = "SELECT COUNT(1) as value FROM dsgc_services  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE-7,1))";
            }
            if ("mysql".equals(dbType)){
                sql = "SELECT COUNT(1) as value FROM dsgc_services  t WHERE t.creation_date>= DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  t.creation_date<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day),'%Y-%m-%d %H:%i:%s')";
            }
        return sw.buildQuery().sql(sql).doQueryFirst(EsbHomeHisto.class);
        }
        public EsbHomeHisto getNowWeekTotalA(){
            String sql = null;
            if("oracle".equals(dbType)){
                sql = " SELECT COUNT(1) as value FROM dsgc_services t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE,1))";
            }
            if ("mysql".equals(dbType)){
                sql = " SELECT COUNT(1) as value FROM dsgc_services t WHERE t.creation_date >= DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  t.creation_date<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day),'%Y-%m-%d %H:%i:%s')";
            }
        return sw.buildQuery().sql(sql).doQueryFirst(EsbHomeHisto.class);
        }


    //查找dsgc_system_entities表统计
    public EsbHomeHisto getTotalE(){
        return sw.buildQuery().sql("SELECT COUNT(1) as value FROM dsgc_system_entities t ").doQueryFirst(EsbHomeHisto.class);
    }
    public EsbHomeHisto getTodyTotalE(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE,'YYYY-MM-DD')";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities t WHERE DATE_FORMAT(t.creation_date,'%Y-%m-%d')=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(EsbHomeHisto.class);
    }
    public EsbHomeHisto getYestodayTotalE(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities t WHERE TO_CHAR(t.creation_date,'YYYY-MM-DD')=TO_CHAR(SYSDATE-1,'YYYY-MM-DD')";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities t WHERE DATE_FORMAT(t.creation_date,'%Y-%m-%d')=DATE_FORMAT(CURRENT_TIMESTAMP-1,'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(EsbHomeHisto.class);
    }
    public EsbHomeHisto getLastWeekTotalE(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE-7,1))";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities  t WHERE t.creation_date>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  t.creation_date<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(EsbHomeHisto.class);

    }
    public EsbHomeHisto getNowWeekTotalE(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities  t WHERE t.creation_date>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  t.creation_date<=TRUNC(NEXT_DAY(SYSDATE,1))";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT COUNT(1) as value FROM dsgc_system_entities  t WHERE t.creation_date>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  t.creation_date<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst(EsbHomeHisto.class);

    }

    //查找访问失败次数
    public Map<String, Object> getTotalF(){
        return sw.buildQuery().sql("SELECT SUM(T.TOTAL_TIMES_F) as value FROM rp_serv_year t ").doQueryFirst();
    }
    public Map<String,Object> getTodyTotalF(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT SUM(T.TOTAL_TIMES_F) as value FROM rp_serv_day t where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')";
        }
        if ("mysql".equals(dbType)){
            sql ="SELECT SUM(T.TOTAL_TIMES_F) as value FROM rp_serv_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();
    }
    public Map<String, Object> getYestodayTotalF(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT SUM(T.TOTAL_TIMES_F) as value FROM rp_serv_day t WHERE  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate-1,'yyyy-mm-dd'),'yyyy-mm-dd')";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT SUM(T.TOTAL_TIMES_F) as value FROM rp_serv_day t WHERE  str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP-1,'%Y-%m-%d'),'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();
    }
    public Map<String, Object> getLastWeekTotalF(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT SUM(T.TOTAL_TIMES_F) as value FROM rp_serv_day  t WHERE to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=TRUNC(NEXT_DAY(SYSDATE-7,1))";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT SUM(T.TOTAL_TIMES_F) as value FROM rp_serv_day  t WHERE str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
       // EsbHomeHisto d =  sw.buildQuery().sql(sql).doQueryFirst(EsbHomeHisto.class);
        Map<String, Object> map = sw.buildQuery().sql(sql).doQueryFirst();
        return map;

    }
    public Map<String,Object> getNowWeekTotalF(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = " SELECT SUM(T.TOTAL_TIMES_F) as value FROM rp_serv_day  t WHERE to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=TRUNC(NEXT_DAY(SYSDATE,1))";
        }
        if ("mysql".equals(dbType)){
            sql = " SELECT SUM(T.TOTAL_TIMES_F) as value FROM rp_serv_day  t WHERE str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();

    }

    //查找访问量
    public Map<String, Object> getTotalV(){
        return sw.buildQuery().sql("SELECT SUM(T.TOTAL_TIMES) as value FROM rp_serv_year t ").doQueryFirst();
    }
    public Map<String, Object> getTodyTotalV(){
        String sql = null;
        if("oracle".equals(dbType)) {
           sql = "SELECT SUM(T.TOTAL_TIMES) as value FROM rp_serv_day t where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')";
        }
        if ("mysql".equals(dbType)){
            sql ="SELECT SUM(T.TOTAL_TIMES) as value FROM rp_serv_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();
    }
    public Map<String, Object> getYestodayTotalV(){
        String sql = null;
        if("oracle".equals(dbType)) {
          sql = "SELECT SUM(T.TOTAL_TIMES) as value FROM rp_serv_day t WHERE  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate-1,'yyyy-mm-dd'),'yyyy-mm-dd')";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT SUM(T.TOTAL_TIMES) as value FROM rp_serv_day t WHERE  str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP-1,'%Y-%m-%d'),'%Y-%m-%d')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();
    }
    public Map<String, Object> getLastWeekTotalV(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "SELECT SUM(T.TOTAL_TIMES) as value FROM rp_serv_day  t WHERE to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>=TRUNC(NEXT_DAY(SYSDATE-7,1)-6) and  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=TRUNC(NEXT_DAY(SYSDATE-7,1))";
        }
        if ("mysql".equals(dbType)){
            sql = "SELECT SUM(T.TOTAL_TIMES) as value FROM rp_serv_day  t WHERE str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP-7,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();

    }
    public Map<String, Object> getNowWeekTotalV(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = " SELECT SUM(T.TOTAL_TIMES) as value FROM rp_serv_day  t WHERE to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>=TRUNC(NEXT_DAY(SYSDATE,1)-6) and  to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=TRUNC(NEXT_DAY(SYSDATE,1))";
        }
        if ("mysql".equals(dbType)){
            sql = " SELECT SUM(T.TOTAL_TIMES) as value FROM rp_serv_day  t WHERE str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')>=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day)-6,'%Y-%m-%d %H:%i:%s') and  str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=DATE_FORMAT(date_sub(CURRENT_TIMESTAMP,interval+1 day),'%Y-%m-%d %H:%i:%s')";
        }
        return sw.buildQuery().sql(sql).doQueryFirst();

    }





    //查询任务总数
    public List<Map<String,Object>> queryTaskTotal(String userId){
        return  sw.buildQuery().sql("select COUNT(1) from\n"+
                "    dsgc_bpm_instance dbi,\n"+
                "    dsgc_bpm_task dbt,\n"+
                " dsgc_client_auth_apply dca "+
                "    where dbt.approver = #userId\n"+
                "    and dbt.node_id = dbi.cur_node and dbt.inst_id = dbi.inst_id "+
                    " and dbi.inst_id = dca.inst_id and dca.apply_ser_type = 'servSource' "
                    )
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
    public List<EsbHomeHisto> queryEsbInfoByAppALL(){
        return  sw.buildQuery().sql("select sys_name as name,count(sys_name) as value,sys_name as legend_name  from\n" +
                "  (select a.serv_no,a.serv_name,e.sys_code,e.sys_name\n" +
                "  from dsgc_services a \n" +
                "  left join dsgc_system_entities e on a.subordinate_system=e.sys_code) t \n" +
                "  group by sys_code,sys_name").doQuery(EsbHomeHisto.class);
    }

    //按类型分类获取个人api信息
    public List<EsbHomeHisto> queryEsbInfoByType(String userRole,List<String> sysCodeList){
        List<EsbHomeHisto> result=new ArrayList<EsbHomeHisto>();
        StringBuffer str = new StringBuffer("select (case when t.sys_name is not null then t.sys_name else '其他' end)  as name," +
                " (count(case when t.sys_name is not null then t.sys_name else '其他' end)) as value ," +
                " (case when t.sys_name is not null then t.sys_name else '其他' end) as legend_name  from" +
                " (select a.serv_no,a.serv_name,c.sys_name ,c.sys_code " +
                " from dsgc_services a " +
                " left join dsgc_system_entities c on a.subordinate_system=c.sys_code) t where 1=1 ");
        MpaasQuery mq =  sw.buildQuery();

        mq.sql(str.toString());
        if("SystemLeader".equals(userRole) || "Tourist".equals(userRole)){
            str.append(" and t.sys_code in ( ");
            for (int i = 0; i < sysCodeList.size(); i++) {
                if (i < sysCodeList.size() - 1) {
                    str.append("'" + sysCodeList.get(i) + "',");
                } else {
                    str.append("'" + sysCodeList.get(i) + "') ");
                }
            }
        }
        mq.sql(str.toString());
        mq.groupBy("t.sys_name,t.sys_code");
        //  mq.groupBy("t.sys_code");
        return mq.doQuery(EsbHomeHisto.class);
//        return sw.buildQuery().sql("select \n" +
//                "(case when t.cate_name is not null then t.cate_name else '其他' end)  as name,\n" +
//                "(count(case when t.cate_name is not null then t.cate_name else '其他' end)) as value , \n" +
//                "(case when t.cate_name is not null then t.cate_name else '其他' end) as legend_name  from\n" +
//                "\n" +
//                "  (select a.api_code,a.api_name,c.cate_name ,c.cate_code \n" +
//                "  from (select a.* from dsgc_apis a,dsgc_apis_access aa,dsgc_consumer_users cu where a.api_code=aa.api_code and cu.csm_code=aa.csm_code and cu.user_id=#userId) a \n" +
//                "  left join dsgc_market_category c on a.market_category=c.cate_code) t \n" +
//                "  group by t.cate_name,t.cate_code").setVar("userId",userId).doQuery(EsbHomeHisto.class);
    }

    //按类型分类获取全部api信息
    public List<EsbHomeHisto> queryEsbInfoByTypeALL(){
        return  sw.buildQuery().sql("select (case when t.sys_name is not null then t.sys_name else '其他' end)  as name,\n" +
                "(count(case when t.sys_name is not null then t.sys_name else '其他' end)) as value ,\n" +
                "(case when t.sys_name is not null then t.sys_name else '其他' end) as legend_name  from\n" +
                "(select a.serv_no,a.serv_name,c.sys_name ,c.sys_code \n" +
                "from dsgc_services a \n" +
                "left join dsgc_system_entities c on a.subordinate_system=c.sys_code) t \n" +
                "group by t.sys_name,t.sys_code ")
                .doQuery(EsbHomeHisto.class);
    }

    public List<Map<String, Object>> queryEsbServerNodeStatus() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("select t.server,\n" +
                        "(case when t.service_status = 'WARNING' then 'WARNING' else 'RUNNING' end)\n" +
                        "as  status\n" +
                        "from \n" +
                        "(select service_status,server,creation_date from dsgc_server_info) t\n" +
                        "where t.creation_date > sysdate -1/24/6 \n" +
                        "group by t.server,t.service_status")
                .doQuery();
        return result;
    }


    //获取ESB访问排序柱状图数据
    //TODO
    public List<Map<String,Object>> querySortVist( String limitTime) {
        List<EsbHomeHisto> result = new ArrayList<EsbHomeHisto>();
        MpaasQuery resulSql = sw.buildQuery();
         if(limitTime.equals("year")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select sum(t.total_times) as total,t.month month from rp_serv_month t " +
                        " where to_date(t.year,'yyyy')=to_date(to_char(sysdate,'yyyy'),'yyyy') group  by t.month order by t.month desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select sum(t.total_times) as total,t.month month from rp_serv_year t where str_to_date(t.year,'%Y')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y'),'%Y') group  by t.month order by t.month desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("month")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select sum(t.total_times) as total,t.day day from rp_serv_day t " +
                        " where to_date(t.year||'-'||t.month,'yyyy-mm')=to_date(to_char(sysdate,'yyyy-mm'),'yyyy-mm') group  by t.day order by t.day desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select tsum(t.total_times) as total,t.day day from rp_serv_day t where str_to_date(t.year||'-'||t.month,'%Y-%m')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m'),'%Y-%m') group  by t.day order by t.day desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("week")){
            String sql = null;
            if("oracle".equals(dbType)) {
                        sql = "select sum(t.total_times) as total,t.day day from rp_serv_day t" +
                       " where to_char(to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd'),'iw')=to_char(sysdate,'iw') and t.year=to_char(sysdate,'yyyy') group  by t.day order by t.day desc";
           }
            if ("mysql".equals(dbType)){
                sql = "select tsum(t.total_times) as total,t.day day from rp_serv_day t where DATE_FORMAT(str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d'),'iw')=DATE_FORMAT(CURRENT_TIMESTAMP,'iw') and t.year=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y')group  by t.day order by t.day desc ";
            }
            resulSql.sql(sql);
        }else if(limitTime.equals("day")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select sum(t.total_times) as total,t.hour hour from rp_serv_hour t" +
                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') group  by t.hour order by t.hour desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select sum(t.total_times) as total,t.hour hour from rp_serv_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d')group  by t.hour order by t.hour desc";
            }
            resulSql .sql(sql);
        }
        return resulSql.doQuery();
    }

    //获取esb并发排序柱状图数据
    //TODO
    public List<Map<String,Object>> queryFailTotal(String startTime, String endTime, String limitTime){
        List<EsbHomeHisto> result = new ArrayList<EsbHomeHisto>();
        MpaasQuery resulSql = sw.buildQuery();
        if(limitTime.equals("year")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select sum(t.total_times_f) as total,t.month month from rp_serv_month t " +
                        " where to_date(t.year,'yyyy')=to_date(to_char(sysdate,'yyyy'),'yyyy') group  by t.month order by t.month desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select sum(t.total_times_f) as total,t.month month from rp_serv_year t where str_to_date(t.year,'%Y')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y'),'%Y') group  by t.month order by t.month desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("month")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select sum(t.total_times_f) as total,t.day day from rp_serv_day t " +
                        " where to_date(t.year||'-'||t.month,'yyyy-mm')=to_date(to_char(sysdate,'yyyy-mm'),'yyyy-mm') group  by t.day order by t.day desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select tsum(t.total_times_f) as total,t.day day from rp_serv_day t where str_to_date(t.year||'-'||t.month,'%Y-%m')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m'),'%Y-%m') group  by t.day order by t.day desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("week")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select sum(t.total_times_f) as total,t.day day from rp_serv_day t" +
                        " where to_char(to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd'),'iw')=to_char(sysdate,'iw') and t.year=to_char(sysdate,'yyyy') group  by t.day order by t.day desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select tsum(t.total_times_f) as total,t.day day from rp_serv_day t where DATE_FORMAT(str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d'),'iw')=DATE_FORMAT(CURRENT_TIMESTAMP,'iw') and t.year=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y')group  by t.day order by t.day desc ";
            }
            resulSql.sql(sql);
        }else if(limitTime.equals("day")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select sum(t.total_times_f) as total,t.hour hour from rp_serv_hour t" +
                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') group  by t.hour order by t.hour desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select sum(t.total_times_f) as total,t.hour hour from rp_serv_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d')group  by t.hour order by t.hour desc";
            }
            resulSql .sql(sql);
        }
        return resulSql.doQuery();

//        List<EsbHomeHisto> result=new ArrayList<EsbHomeHisto>();
//        MpaasQuery resulSql =sw.buildQuery();
//        if(startTime!=null&&endTime!=null&&limitTime==null) {
//            String sql = null;
//            if("oracle".equals(dbType)) {
//                sql = "select t.serv_no as name ,sum(t.total_times)  over(partition by t.serv_no) as value from rp_serv_day t " +
//                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=to_date(#endTime,'yyyy-mm-dd') and to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>to_date(#startTime,'yyyy-mm-dd') order by value desc";
//            }
//            if ("mysql".equals(dbType)){
//                sql = "select t.serv_no as name ,sum(t.total_times)  as value from rp_serv_day t  where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=str_to_date(#endTime,'%Y-%m-%d') and str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')>str_to_date(#startTime,'%Y-%m-%d') order by t.serv_no,value desc";
//            }
//            resulSql.sql(sql)
//                    .setVar("endTime",endTime).setVar("startTime",startTime);
//        }
//        else if(limitTime.equals("year")){
//            String sql = null;
//            if("oracle".equals(dbType)) {
//                sql = "select t.serv_no as name,max(t.total_times) over(partition by t.serv_no )  as value from rp_serv_day t " +
//                        " where to_date(t.year,'yyyy')=to_date(to_char(sysdate,'yyyy'),'yyyy') order by value desc";
//            }
//            if ("mysql".equals(dbType)){
//                sql = "select t.serv_no as name,max(t.total_times)  as value from rp_serv_day t where str_to_date(t.year,'%Y')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y'),'%Y') order by t.serv_no,value desc";
//            }
//            resulSql .sql(sql);
//        }else if(limitTime.equals("month")){
//            String sql = null;
//            if("oracle".equals(dbType)) {
//                sql = "select t.serv_no as name,max(t.total_times) over(partition by t.serv_no )  as value from rp_serv_day t " +
//                        " where to_date(t.year||'-'||t.month,'yyyy-mm')=to_date(to_char(sysdate,'yyyy-mm'),'yyyy-mm') order by value desc";
//            }
//            if ("mysql".equals(dbType)){
//                sql = "select t.serv_no as name,max(t.total_times) as value from rp_serv_day t where str_to_date(t.year||'-'||t.month,'%Y-%m')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m'),'%Y-%m') order by t.serv_no,value desc";
//            }
//            resulSql .sql(sql);
//        }else if(limitTime.equals("week")){
//            String sql = null;
//            if("oracle".equals(dbType)) {
//               sql = "select t.serv_no as name,max(t.total_times) over(partition by t.serv_no )  as value from rp_serv_day t " +
//                       "  where to_char(to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd'),'iw')=to_char(sysdate,'iw') and t.year=to_char(sysdate,'yyyy') order by value desc";
//            }
//            if ("mysql".equals(dbType)){
//                sql = "select t.serv_no as name,max(t.total_times)  as value from rp_serv_day t where DATE_FORMAT(str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d'),'iw')=DATE_FORMAT(CURRENT_TIMESTAMP,'iw') and t.year=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y') order by t.serv_no,value desc";
//            }
//            resulSql.sql(sql);
//        }else if(limitTime.equals("day")){
//            String sql = null;
//            if("oracle".equals(dbType)) {
//                sql = "select t.serv_no as name,max(t.total_times) over(partition by t.serv_no )  as value from rp_serv_day t " +
//                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by value desc";
//            }
//            if ("mysql".equals(dbType)){
//                sql = "select t.serv_no as name,max(t.total_times) as value from rp_serv_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') order by t.serv_no,value desc";
//            }
//            resulSql .sql(sql);
//        }
//        return resulSql.doPageQuery(1,10,EsbHomeHisto.class);


    }

    //获取ESB等待排序柱状图数据
    public PageQueryResult<EsbHomeHisto> querySortWait(String startTime, String endTime, String limitTime){
        List<EsbHomeHisto> result=new ArrayList<EsbHomeHisto>();
        MpaasQuery resulSql =sw.buildQuery();
        if(startTime!=null&&endTime!=null&&limitTime==null) {
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,sum(t.total_times)  over(partition by t.serv_no) as value from rp_serv_day t " +
                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')<=to_date(#endTime,'yyyy-mm-dd') and to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')>to_date(#startTime,'yyyy-mm-dd') order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,sum(t.total_times) as value from rp_serv_day t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')<=str_to_date(#endTime,'%Y-%m-%d') and str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')>str_to_date(#startTime,'%Y-%m-%d') order by t.serv_no,value desc";
            }
            resulSql.sql(sql)
                    .setVar("endTime",endTime).setVar("startTime",startTime);

        }
        else if(limitTime.equals("year")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,avg(t.avg_cost)  over(partition by t.serv_no) as value from rp_serv_year t" +
                        " where to_date(t.year,'yyyy')=to_date(to_char(sysdate,'yyyy'),'yyyy') order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,avg(t.avg_cost)  as value from rp_serv_year t where str_to_date(t.year,'%Y')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y'),'%Y') order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("month")){
            String sql = null;
            if("oracle".equals(dbType)) {
             sql = "select t.serv_no as name ,avg(t.avg_cost)  over(partition by t.serv_no) as value from rp_serv_month t" +
                     " where to_date(t.year||'-'||t.month,'yyyy-mm')=to_date(to_char(sysdate,'yyyy-mm'),'yyyy-mm')  order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,avg(t.avg_cost)  as value from rp_serv_month t where str_to_date(t.year||'-'||t.month,'%Y-%m')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m'),'%Y-%m')  order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }else if(limitTime.equals("week")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,avg(t.avg_cost)  over(partition by t.serv_no) as value from rp_serv_day t " +
                        "  where to_char(to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd'),'iw')=to_char(sysdate,'iw') and t.year=to_char(sysdate,'yyyy') order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,avg(t.avg_cost) as value from rp_serv_day t  where DATE_FORMAT(str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d'),'iw')=DATE_FORMAT(CURRENT_TIMESTAMP,'iw') and t.year=DATE_FORMAT(CURRENT_TIMESTAMP,'%Y') order by t.serv_no,value desc";
            }
            resulSql.sql(sql);
        }else if(limitTime.equals("day")){
            String sql = null;
            if("oracle".equals(dbType)) {
                sql = "select t.serv_no as name ,avg(t.avg_cost)  over(partition by t.serv_no) as value from rp_serv_day t " +
                        " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') order by value desc";
            }
            if ("mysql".equals(dbType)){
                sql = "select t.serv_no as name ,avg(t.avg_cost) as value from rp_serv_day t  where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') order by t.serv_no,value desc";
            }
            resulSql .sql(sql);
        }

        return resulSql.doPageQuery(1,10,EsbHomeHisto.class);
    }


    //查询当天运行次数流量
    public List<EsbHomeHisto> queryTrafficRuntimes(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "select to_char(t.creation_date,'hh24:mi') as name ,sum(t.total_times)  as value from rp_serv_hour t " +
                    " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') group by t.creation_date order by t.creation_date ";
        }
        if ("mysql".equals(dbType)){
            sql = "select DATE_FORMAT(t.creation_date,'%Y-%m') as name ,sum(t.total_times)  as value from rp_serv_hour t  where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') group by t.creation_date order by t.creation_date ";
        }
       return sw.buildQuery().sql(sql)
                .doQuery(EsbHomeHisto.class);
    }

    //查询当天平均响应时间
    public List<EsbHomeHisto> queryTrafficCost(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "select to_char(t.creation_date,'hh24:mi') as name ,avg(t.avg_cost)  as value from rp_serv_hour t " +
                    " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') group by t.creation_date order by t.creation_date";
        }
        if ("mysql".equals(dbType)){
            sql = "select DATE_FORMAT(t.creation_date,'%Y-%m') as name ,avg(t.avg_cost)  as value from rp_serv_hour t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') group by t.creation_date order by t.creation_date";
        }
        return  sw.buildQuery().sql(sql)

                .doQuery(EsbHomeHisto.class);
    }

    //查询当天错误次数流量
     public List<EsbHomeHisto> queryTrafficError(){
         String sql = null;
         if("oracle".equals(dbType)) {
             sql="select to_char(t.creation_date,'hh24:mi') as name,sum(t.total_times_f)  as value from rp_serv_hour t " +
                     " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd')=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') group by t.creation_date order by t.creation_date";
         }
         if ("mysql".equals(dbType)){
             sql ="select DATE_FORMAT(t.creation_date,'%Y-%m') as name,sum(t.total_times_f)  as value from rp_serv_hour t where str_to_date(t.year||'-'||t.month||'-'||t.day,'%Y-%m-%d')=str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d'),'%Y-%m-%d') group by t.creation_date order by t.creation_date";
         }
         return   sw.buildQuery().sql(sql)
                 .doQuery(EsbHomeHisto.class);
     }


    //查询近一小时运行次数流量
    public EsbHomeHisto queryTrafficRuntimesNow(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "select count(1) from dsgc_log_instance t \n" +
                    "                     where t.creation_date>to_date(to_char(sysdate,'yyyy-mm-dd hh24'),'yyyy-mm-dd hh24')  ";
        }
        if ("mysql".equals(dbType)){
            sql = "select count(1)   as value from dsgc_log_instance t where t.creation_date>str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d %H'),'%Y-%m-%d %H') order by t.creation_date ";
        }
        return sw.buildQuery().sql(sql)
                .doQueryFirst(EsbHomeHisto.class);
    }

    //查询近一小时平均响应时间
    public EsbHomeHisto queryTrafficCostNow(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "select avg(t.req_msg_size)   as value from dsgc_log_instance t \n" +
                    "               where t.creation_date>to_date(to_char(sysdate,'yyyy-mm-dd hh24'),'yyyy-mm-dd hh24') \n" +
                    "                order by t.creation_date";
        }
        if ("mysql".equals(dbType)){
            sql = "select avg(t.req_msg_size)  as value from dsgc_log_instance t where t.creation_date>str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d %H'),'%Y-%m-%d %H') order by t.creation_date";
        }
        return   sw.buildQuery().sql(sql)
                .doQueryFirst(EsbHomeHisto.class);
    }

    //查询近一小时错误次数流量
    public EsbHomeHisto queryTrafficErrorNow(){
        String sql = null;
        if("oracle".equals(dbType)) {
            sql = "select count(1)   as value from dsgc_log_instance t " +
                    "  where t.creation_date>to_date(to_char(sysdate,'yyyy-mm-dd hh24'),'yyyy-mm-dd hh24') and t.inst_status='0' order by t.creation_date  ";
        }
        if ("mysql".equals(dbType)){
            sql = "select count(1)   as value from dsgc_log_instance t where t.creation_date>str_to_date(DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d %H'),'%Y-%m-%d %H') and t.inst_status='0' order by t.creation_date";
        }
        return   sw.buildQuery().sql(sql)
                .doQueryFirst(EsbHomeHisto.class);
    }

    public EsbHomeHisto queryAdminLocaltion(String code){
        return sw.buildQuery().sql("select t.admin_location as name from  dsgc_env_info_cfg t where t.env_code=#code").
                setVar("code",code).doQueryFirst(EsbHomeHisto.class);
    }

}
