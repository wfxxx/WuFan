package com.definesys.dsgc.service.apicockpit;

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
                        .sql("select t.serv_no as name, sum(t.total_times) as value1,sum(t.total_200) as value2\n" +
                                " from RP_API_DAY t \n" +
                                " where to_date(t.year||'-'||t.month||'-'||t.day,'yyyy-mm-dd') between #startDate  and #endDate group by t.serv_no ")
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
}
