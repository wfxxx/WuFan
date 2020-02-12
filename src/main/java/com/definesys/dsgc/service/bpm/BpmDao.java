package com.definesys.dsgc.service.bpm;

import com.definesys.dsgc.service.bpm.bean.*;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("bpm_service")
public class BpmDao {
    @Autowired
    private MpaasQueryFactory sw;

    public void generateBpmInst(BpmInstanceBean bpmInstanceBean){
        sw.buildQuery().doInsert(bpmInstanceBean);
    }

    public PageQueryResult<BpmInstanceBean> getTaskList(BpmCommonReqBean param, String userId, int pageSize, int pageIndex){
        StringBuffer sqlStr = new StringBuffer("select dbi.* from dsgc_bpm_instance dbi,dsgc_bpm_task dbt " +
                " where dbt.approver = #userId and dbt.node_id = dbi.cur_node ");
        MpaasQuery mq = sw.buildQuery();
        mq.setVar("userId",userId);
        if(!"ALL".equals(param.getQueryType())){
            sqlStr.append(" and dbi.process_id = #queryType ");
            mq.setVar("queryType",param.getQueryType());
        }
        if (StringUtil.isNotBlank(param.getCon0())) {
            sqlStr.append(" and( upper(dbi.inst_title) like #con0 or upper(dbi.inst_stat) like #con0)");
            mq.setVar("con0", "%" + param.getCon0().toUpperCase() + "%");
        }
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex,pageSize,BpmInstanceBean.class);
//          return sw.buildQuery().sql("select dbi.* from dsgc_bpm_instance dbi,dsgc_bpm_task dbt" +
//                  " where dbt.approver = #userId and dbt.node_id = dbi.cur_node ")
//                  .or()
//                  .like("instTitle",param.getCon0())
//                  .like("instStat",param.getCon0())
//                  .setVar("userId",userId)
//                  .doPageQuery(pageIndex,pageSize,BpmInstanceBean.class);
    }
    public PageQueryResult<BpmInstanceBean> myApply(BpmCommonReqBean param, String userId, int pageSize, int pageIndex){
        StringBuffer sqlStr = new StringBuffer("select * from dsgc_bpm_instance dbi" +
                " where dbi.created_by = #userId");
        MpaasQuery mq = sw.buildQuery();
        mq.setVar("userId",userId);
        if (StringUtil.isNotBlank(param.getCon0())) {
            sqlStr.append(" and( upper(dbi.inst_title) like #con0 or upper(dbi.inst_stat) like #con0)");
            mq.setVar("con0", "%" + param.getCon0().toUpperCase() + "%");
        }
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex,pageSize,BpmInstanceBean.class);
//        return sw.buildQuery().sql("select * from dsgc_bpm_instance dbi" +
//                " where dbi.created_by = #userId")
//                .setVar("userId",userId)
//                .like("instTitle",param.getCon0())
//                .like("instStat",param.getCon0())
//                .doPageQuery(pageIndex,pageSize,BpmInstanceBean.class);
    }

    public List<BpmHistoryBean> getInstHistory(BpmCommonReqBean param){
        return sw.buildQuery()
                .eq("instId",param.getCon0())
                .doQuery(BpmHistoryBean.class);
    }

    public List<Map<String, Object>> getTaskCount(String userId){
       return sw.buildQuery().sql("select count(*) count from dsgc_bpm_instance dbi,dsgc_bpm_task dbt" +
                                        " where dbt.approver = #userId and dbt.node_id = dbi.cur_node")
                .setVar("userId",userId)
                .doQuery();
    }

    public void approveTask(BpmApproveBean param){

    }

    public void updateBpmInstance(BpmInstanceBean param){
        sw.buildQuery()
                .update("cur_node",param.getCurNode())
                .update("inst_stat",param.getInstStat())
                .update("last_updated_by",param.getLastUpdatedBy())
                .eq("instId",param.getInstId())
                .doUpdate(param);
    }

    public List<BpmInstanceBean> findBpmInstanceById(String instId){
       return sw.buildQuery().eq("instId",instId).doQuery(BpmInstanceBean.class);
    }

    public List<BpmNodeBean> findBpmNodeById(String nodeId){
    return  sw.buildQuery().eq("nodeId",nodeId).doQuery(BpmNodeBean.class);
    }

    public void addHistory(BpmHistoryBean bpmHistoryBean){
        sw.buildQuery().doInsert(bpmHistoryBean);
    }

    public void addTask(BpmTaskBean bpmTaskBean){
        sw.buildQuery().doInsert(bpmTaskBean);
    }

    public void delTask(BpmTaskBean bpmTaskBean){
        sw.buildQuery().eq("instId",bpmTaskBean.getInstId())
                .eq("nodeId",bpmTaskBean.getNodeId())
                .doDelete(BpmTaskBean.class);
    }

    public List<BpmNodeBean> getBpmNodes(BpmNodeBean bpmNodeBean){
        MpaasQuery mpaasQuery = sw.buildQuery();
        mpaasQuery.eq("processId",bpmNodeBean.getProcessId());
                if (bpmNodeBean.getNodePos() != null){
                    mpaasQuery.eq("nodePos",bpmNodeBean.getNodePos());
                }
        List<BpmNodeBean> list = mpaasQuery.doQuery(BpmNodeBean.class);
              return list;
    }

    public List<BpmNodeBean> getBpmNodeById(String nodeId){
       return sw.buildQuery().eq("nodeId",nodeId).doQuery(BpmNodeBean.class);
    }

    public void createBpmInstance(BpmInstanceBean bpmInstanceBean){
        sw.buildQuery().doInsert(bpmInstanceBean);
    }

    public List<BpmProcessBean> findBpmProcessById(String processId){
       return sw.buildQuery().eq("processId",processId).doQuery(BpmProcessBean.class);
    }

    public BpmInstanceBean queryBpmInstanceBaseInfo(BpmCommonReqBean param){
        return sw.buildQuery().eq("instId",param.getCon0()).doQueryFirst(BpmInstanceBean.class);
    }

    public List<BpmProcessBean> queryProcessTypeList(){
        return sw.buildQuery().eq("isEnable","Y").doQuery(BpmProcessBean.class);
    }
}
