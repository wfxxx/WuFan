package com.definesys.dsgc.service.flow;

import com.definesys.dsgc.service.flow.bean.DsgcSvcgenProjInfo;
import com.definesys.dsgc.service.flow.bean.FlowServices;
import com.definesys.dsgc.service.flow.dto.FlowReqCommonDTO;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FlowSvcDao {
    @Autowired
    private MpaasQueryFactory sw;


    /**
     * 分页查询flowServices
     * @param param
     * @param pageIndex
     * @param pageSize
     * @param sysCodeList
     * @return
     */
    public PageQueryResult<FlowServices> pageQueryFlowServices(FlowReqCommonDTO param,int pageIndex,int pageSize,List<String> sysCodeList) {
        MpaasQuery query = sw.buildViewQuery("V_FLOW_SERVICE_INFO").eq("projectCode",param.getProjectName());
        if(sysCodeList != null){
            if(sysCodeList.isEmpty()){
                //没有任何归属应用，则不允许查询任何数据
                PageQueryResult noData =  new PageQueryResult<FlowServices>();
                noData.setCount(0L);
                noData.setResult(new ArrayList());

                return noData;
            } else {
                query = query.in("appCode",sysCodeList);
            }
        }
        if (param.getQueryCon0() != null && param.getQueryCon0().trim().length() > 0) {
            String[] parmArray = param.getQueryCon0().split(" ");
            for (String p : parmArray) {
                if (p != null && p.length() > 0) {
                    query = query.conjuctionAnd()
                            .or()
                            .like("flowName",p)
                            .like("flowDesc",p)
                            .like("projectCode",p)
                            .like("appCode",p)
                            .like("appName",p)
                            .like("createdByName",p);
                }
            }
        }

        return query.doPageQuery(pageIndex,pageSize,FlowServices.class);
    }


    /**
     * 根据flowId查询flowServices
     * @param flowId
     * @return
     */
    public FlowServices getFlowServcieByFlowId(String flowId){
        if(flowId == null || flowId.trim().length() == 0){
            return null;
        } else {
            return sw.buildViewQuery("V_FLOW_SERVICE_INFO").eq("flowId",flowId).doQueryFirst(FlowServices.class);
        }
    }

    /**
     * 添加flowService
     * @param flow
     * @return
     */
    public int addFlowServcie(FlowServices flow){
        if(flow.getFlowName() == null || flow.getFlowName().trim().length() ==0){
            //flowname不能为空
            return 0;
        }

        FlowServices exist =  sw.buildQuery().eq("flowName",flow.getFlowName()).doQueryFirst(FlowServices.class);
        if(exist != null){
            return -1;
        }
        sw.buildQuery().doInsert(flow);
        return 1;
    }


    /**
     * 更新flowService
     * @param flow
     * @return
     */
    public int updateFlowService(FlowServices flow){

        if(flow.getFlowName() == null || flow.getFlowName().trim().length() ==0){
            //flowname不能为空
            return 0;
        }

        FlowServices nameExist = sw.buildQuery().eq("flowName",flow.getFlowName()).ne("flowId",flow.getFlowId()).doQueryFirst(FlowServices.class);
        if(nameExist != null){
            return -1;
        }

        FlowServices old = sw.buildQuery().eq("flowId",flow.getFlowId()).doQueryFirst(FlowServices.class);
        if(old != null && !old.getFlowName().equals(flow.getFlowName())){
            //flowName不允许更改
            return -2;
        }

        sw.buildQuery().doUpdate(flow);

        return 1;

    }

    /**
     * 删除flowService
     * @param flowId
     */
    public void deleteFlowServcie(String flowId){
        if(flowId != null || flowId.trim().length() > 0) {
            sw.buildQuery().table("flow_services").update("delete_flag","Y").eq("flow_id",flowId).doUpdate();
        }
    }


    /**
     * 根据项目名称查找项目信息
     * @param projName
     * @return
     */
    public DsgcSvcgenProjInfo queryProjectDirInfo(String projName){
       return sw.buildQuery().eq("projName",projName).doQueryFirst(DsgcSvcgenProjInfo.class);
    }

}
