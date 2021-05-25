package com.definesys.dsgc.service.ystar.common;

import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.dsgc.service.svclog.bean.DSGCLogInstance;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.ystar.common.bean.LssuesListBean;
import com.definesys.dsgc.service.ystar.constant.sql.SQLLoader;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("CommonDao")
public class CommonDao {
    @Autowired
    private MpaasQueryFactory sw;

    private SWordLogger logger;

    //查询上游系统的名称
    public List<DSGCSystemEntities> getOnSystem() {
        return sw.buildQuery().sql("select sys_code,sys_name from dsgc_system_entities").doQuery(DSGCSystemEntities.class);
    }

    //查询下游系统的名称
    public List<DSGCConsumerEntities> getDownSystm() {
        return sw.buildQuery().sql("select csm_code,csm_name from Dsgc_Consumer_Entities").doQuery(DSGCConsumerEntities.class);
    }

    //查询Lssues_List
    public PageQueryResult QueryList(int page, int pageSize, String sysCode, String csmCode, String servNo, String levelJ, String toSolve) {
        String sql = "select *\n" +
                "  from (select t.*, s.sys_name as sysName, c.csm_name as csmName, v.serv_name servName\n" +
                "          from LSSUES_LIST            t,\n" +
                "               dsgc_system_entities   s,\n" +
                "               dsgc_services          v,\n" +
                "               dsgc_consumer_entities c\n" +
                "         where t.sys_code = s.sys_code\n" +
                "           and t.csm_code = c.csm_code\n" +
                "           and t.serv_no = v.serv_no) a";
        MpaasQuery mq = sw.buildQuery().sql(sql);
        if (sysCode != null && !sysCode.equals("null")) {
            mq = mq.eq("sysCode", sysCode);
        }
        if (csmCode != null && !csmCode.equals("null")) {
            mq = mq.eq("csmCode", csmCode);
        }
        if (servNo != null && !servNo.equals("null")) {
            mq = mq.eq("servNo", servNo);
        }
        if (levelJ != null && !levelJ.equals("null")) {
            mq = mq.eq("levelJ", levelJ);
        }
        if (toSolve != null && !toSolve.equals("null")) {
            mq = mq.eq("toSolve", toSolve);
        }
        return mq.doPageQuery(page, pageSize, LssuesListBean.class);
    }

    //查询系统负责人的接口问题清单
    public PageQueryResult Querylists(int page, int pageSize, String sysCode, String csmCode, String servNo, String levelJ, String toSolve, String uid) {
        String sql = "select *\n" +
                "  from (select t.*,\n" +
                "               s.sys_name  as sysName,\n" +
                "               c.csm_name  as csmName,\n" +
                "               v.serv_name servName\n" +
                "          from (select *\n" +
                "                  from LSSUES_LIST\n" +
                "                 where sys_code in (select sys_code\n" +
                "                                      from dsgc_system_user\n" +
                "                                     where user_Id = #uid)\n" +
                "                    or csm_code in (select csm_code\n" +
                "                                      from dsgc_consumer_users\n" +
                "                                     where user_Id = #uid)) t,\n" +
                "               dsgc_system_entities s,\n" +
                "               dsgc_services v,\n" +
                "               dsgc_consumer_entities c\n" +
                "         where t.sys_code = s.sys_code\n" +
                "           and t.csm_code = c.csm_code\n" +
                "           and t.serv_no = v.serv_no)";
        MpaasQuery mq = sw.buildQuery().sql(sql).setVar("uid", uid);
        if (sysCode != null && !sysCode.equals("null")) {
            mq = mq.eq("sysCode", sysCode);
        }
        if (csmCode != null && !csmCode.equals("null")) {
            mq = mq.eq("csmCode", csmCode);
        }
        if (servNo != null && !servNo.equals("null")) {
            mq = mq.eq("servNo", servNo);
        }
        if (levelJ != null && !levelJ.equals("null")) {
            mq = mq.eq("levelJ", levelJ);
        }
        if (toSolve != null && !toSolve.equals("null")) {
            mq = mq.eq("toSolve", toSolve);
        }

        return mq.doPageQuery(page, pageSize, LssuesListBean.class);
    }

    //查询每个接口对应的上游系统和类别
    public List<Map<String, Object>> QueryMap() {
        String sql = "select d.serv_no,d.serv_name,d.tech_type,s.sys_code,s.sys_name from dsgc_services d left join dsgc_system_entities s on d.subordinate_system=s.sys_code";
        return sw.buildQuery().sql(sql).doQuery();
    }


    public String Test() {
        return SQLLoader.sql("SQL_GET_DB_LOCK");
    }

    public List<DSGCLogInstance> queryLogInstances(String svcCode,String startTime, String endTime) {
        return this.sw.buildQuery().like("servNo",svcCode).lt("creationDate", endTime).gteq("creationDate", startTime).doQuery(DSGCLogInstance.class);
    }

    public Map<String, Object> queryBodyPayloadById(String trackId) {
            return this.sw.buildQuery().sql("select p.TRACK_ID,p.PAYLOAD_DATA from dsgc_log_body_payload p where p.PL_ID like '%OB' and p.TRACK_ID = #trackId").setVar("trackId", trackId).doQueryFirst();
    }

    public void updLogInstanceBusCount(String id, String count) {
        this.sw.buildQuery().eq("track_Id", id).update("bus_Count", count).doUpdate(DSGCLogInstance.class);
    }
}
