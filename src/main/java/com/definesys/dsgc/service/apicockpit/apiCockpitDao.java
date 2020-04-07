package com.definesys.dsgc.service.apicockpit;

import com.definesys.dsgc.service.apiHome.bean.ApiHomeHisto;
import com.definesys.dsgc.service.apicockpit.bean.eChartsBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @ClassName apiCockpitDao
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-30 10:41
 * @Version 1.0
 **/
@Repository
public class apiCockpitDao {

        @Autowired
        private MpaasQueryFactory sw;


        //api一段时间内总调用次数
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
                        .sql("select '总次数' as name,sum(t.total_times) as value1  from RP_API_DAY t  where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd') between #startDate  and #endDate ")
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

        //一段时间内API调用在各系统分布数量（成功的和失败的）
        public List<eChartsBean> queryAppDistri(Date startDate, Date endDate){
                if(endDate==null){
                        endDate=new Date();
                }
                if(startDate==null){
                        startDate=new Date();
                }
                return sw.buildQuery()
                        .sql("select * from (select t.serv_no as name, sum(t.total_times) as value1,sum(t.total_200) as value2\n" +
                                " from RP_API_DAY t \n" +
                                " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd') between #startDate  and #endDate group by t.serv_no " +
                                " ) where rownum<8 order by value1 desc ")
                        .setVar("startDate",startDate)
                        .setVar("endDate",endDate)
                        .doQuery(eChartsBean.class);
        }

        //一段时间内调用到的API个数（注意不是次数）占总数的比例
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
                return sw.buildQuery()
                        .sql("select '调用api个数' as name, count(1) as value1 from (\n" +
                                "select t.serv_no  from RP_API_DAY t   where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd' ) between #startDate  and #endDate group by t.serv_no) s")
                        .setVar("startDate",startDate)
                        .setVar("endDate",endDate)
                        .doQueryFirst(eChartsBean.class);
        }


        public List<eChartsBean> queryApp(){
                return sw.buildQuery()
                        .sql("select t.sys_code as id,t.sys_name as name from DSGC_SYSTEM_ENTITIES t order by t.sys_code")
                        .doQuery(eChartsBean.class);
        }

        public List<eChartsBean> queryConsumer(String appId) {
                return sw.buildQuery()
                        .sql("select id,name from (\n" +
                                "select e.sys_code, e.sys_name,ce.csm_code as id,ce.csm_name as name from dsgc_system_entities e\n" +
                                "left join dsgc_apis a  on e.sys_code=a.app_code\n" +
                                "left join dsgc_apis_access aa on aa.api_code=a.api_code\n" +
                                "left join dsgc_consumer_entities ce on aa.csm_code=ce.csm_code where e.sys_code=#appId\n" +
                                ") s\n" +
                                "where id is not null\n" +
                                "group by  (id,name) ")
                        .setVar("appId",appId)
                        .doQuery(eChartsBean.class);
        }




        //查询年分月段执行次数
        public List<eChartsBean> queryMonthRuntimes(){
                return sw.buildQuery().sql("select * from (\n" +
                        "                        select  t.month as name ,sum(t.total_times) over(partition by t.month) as value1    from rp_api_month t\n" +
                        "                        where t.year=to_char(sysdate,'yyyy') order by t.month\n" +
                        "                        ) group by (name,value1) order by  name")
                        .doQuery(eChartsBean.class);
        }

        //查询年分月段成功次数
        public List<eChartsBean> queryMonthSucess(){
                return sw.buildQuery().sql("select * from (\n" +
                        "                        select  t.month as name ,sum(t.total_200) over(partition by t.month) as value1    from rp_api_month t\n" +
                        "                        where t.year=to_char(sysdate,'yyyy') order by t.month\n" +
                        "                        ) group by (name,value1) order by name")
                        .doQuery(eChartsBean.class);
        }

        //查询年分月段消费者
        public List<eChartsBean> queryMonthConsumer(){
                return sw.buildQuery().sql("select mon as name ,count(1) as value1 from (\n" +
                        "                        select to_char(t.creation_date,'mm') as mon,t.csm_code as id,t.csm_name as name\n" +
                        "                         from dsgc_consumer_entities t \n" +
                        "                         where to_char(t.creation_date,'yyyy')=to_char(sysdate,'yyyy')\n" +
                        "                        order by mon)\n" +
                        "                         group by mon order by mon")
                        .doQuery(eChartsBean.class);
        }

        //累计一段时间内API数量
        public eChartsBean queryApiTotalDate(Date startDate, Date endDate){
                return sw.buildQuery().sql("select count(1) as value1 from dsgc_apis t\n" +
                        "where creation_date between #startDate  and #endDate")
                        .setVar("startDate",startDate)
                        .setVar("endDate",endDate)
                        .doQueryFirst(eChartsBean.class);
        }


        //累计一段时间内消费者数量
        public eChartsBean queryConsumerTotalDate(Date startDate, Date endDate){
                return sw.buildQuery().sql("select count(1)  as value1 from dsgc_consumer_entities \n" +
                        " where creation_date between #startDate  and #endDate")
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

        //过去七天API状况
        public List<eChartsBean> queryApiSeven(){
                return sw.buildQuery().sql("select a.time as name,nvl(b.num,0) as value1 from (\n" +
                        "select  sysdate-7+rownum as currentDate,to_char(sysdate-7 + rownum ,  'day') as time\n" +
                        "from dual \n" +
                        "connect by rownum <=7 )a\n" +
                        "left join\n" +
                        "  (select time,count(time) as num from (SELECT to_char(creation_date,'yyyy-mm-dd') as time FROM dsgc_apis   \n" +
                        "                where creation_date >sysdate-6) group by time order by time)b on a.time = b.time order by a.currentDate")
                        .doQuery(eChartsBean.class);
        }


        //过去七天消费者状况，dsgc_consumer_entities
        public List<eChartsBean> queryConsumerSeven(){
                return sw.buildQuery().sql("select a.time as name,nvl(b.num,0) as value1 from (\n" +
                        "select  sysdate-7+rownum as currentDate,to_char(sysdate-7 + rownum ,  'day') as time\n" +
                        "from dual \n" +
                        "connect by rownum <=7 )a\n" +
                        "left join\n" +
                        "  (select time,count(time) as num from (SELECT to_char(creation_date,'yyyy-mm-dd') as time FROM dsgc_consumer_entities   \n" +
                        "                where creation_date >sysdate-6) group by time order by time)b on a.time = b.time order by a.currentDate")
                        .doQuery(eChartsBean.class);
        }


        //过去七天应用状况，dsgc_system_entities
        public List<eChartsBean> queryAppSeven(){
                return sw.buildQuery().sql("select a.time as name,nvl(b.num,0) as value1 from (\n" +
                        "select  sysdate-7+rownum as currentDate,to_char(sysdate-7 + rownum ,  'day') as time\n" +
                        "from dual \n" +
                        "connect by rownum <=7 )a\n" +
                        "left join\n" +
                        "  (select time,count(time) as num from (SELECT to_char(creation_date,'yyyy-mm-dd') as time FROM dsgc_system_entities   \n" +
                        "                where creation_date >sysdate-6) group by time order by time)b on a.time = b.time order by a.currentDate")
                        .doQuery(eChartsBean.class);
        }

        //按应用分类获取api信息,获取全部
        public List<eChartsBean> queryApiInfoByAppALL(){
                return  sw.buildQuery().sql("select sys_name as name,count(sys_name) as value1 from\n" +
                        "  (select a.api_code,a.api_name,e.sys_code,e.sys_name\n" +
                        "  from dsgc_apis a \n" +
                        "  left join dsgc_system_entities e on a.app_code=e.sys_code)\n" +
                        "  group by sys_code,sys_name order by value1 desc").doQuery(eChartsBean.class);
        }

        //按类型分类获取全部api信息
        public List<eChartsBean> queryApiInfoByTypeALL(){
                return  sw.buildQuery().sql("select \n" +
                        "(case when t.cate_name is not null then t.cate_name else '未知' end)  as name,\n" +
                        "(count(case when t.cate_name is not null then t.cate_name else '未知' end)) as value1  \n" +
                        "  from\n" +
                        "\n" +
                        "  (select a.api_code,a.api_name,c.cate_name ,c.cate_code \n" +
                        "  from dsgc_apis a \n" +
                        "  left join dsgc_market_category c on a.market_category=c.cate_code) t \n" +
                        "  group by t.cate_name,t.cate_code order by value1 desc").doQuery(eChartsBean.class);
        }





















}
