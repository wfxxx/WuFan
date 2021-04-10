package com.definesys.dsgc.service.flow;

import com.definesys.dsgc.service.flow.bean.FlowMetadatas;
import com.definesys.dsgc.service.flow.bean.FlowNodes;
import com.definesys.dsgc.service.flow.bean.FlowRoads;
import com.definesys.dsgc.service.flow.dto.FlowConstants;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlowRoadDao {
    @Autowired
    private MpaasQueryFactory sw;

    /**
     * 判断flow当前是否有处于编辑状态
     * @param flowId
     * @return
     */
    public boolean checkEditingIsExist(String flowId,String flowVersion) {
        FlowRoads fr = sw.buildQuery()
                .select("roadId")
                .eq("flowId",flowId)
                .eq("flowVersion",flowVersion)
                .eq("flowStat",FlowConstants.FLOW_STAT_EDITING)
                .doQueryFirst(FlowRoads.class);
        if (fr != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取正在修改的flowroad记录
     * @param flowId
     * @param flowVersion
     * @return
     */
    public FlowRoads getEditingFlowRoad(String flowId,String flowVersion){
        return sw.buildQuery().eq("flowId",flowId)
                .eq("flowVersion",flowVersion)
                .eq("flowStat",FlowConstants.FLOW_STAT_EDITING)
                .doQueryFirst(FlowRoads.class);
    }

    /**
     * 获取当前已经保存的flowroad记录
     * @param flowId
     * @param flowVersion
     * @return
     */
    public FlowRoads getSavedFlowRoad(String flowId,String flowVersion){
        return sw.buildQuery().eq("flowId",flowId)
                .eq("flowVersion",flowVersion)
                .eq("flowStat",FlowConstants.FLOW_STAT_SAVED)
                .doQueryFirst(FlowRoads.class);
    }


    /**
     * 进入编辑状态，拷贝一份作为临时编辑区域
     * @param flowId
     */
    public void startEditingFlowRoad(String flowId,String flowVersion) {

        FlowRoads fr = sw.buildQuery()
                .eq("flowId",flowId)
                .eq("flowVersion",flowVersion)
                .eq("flowStat",FlowConstants.FLOW_STAT_SAVED)
                .doQueryFirst(FlowRoads.class);

        List<FlowNodes> nodeList = null;
        List<FlowMetadatas> metaList = null;

        if (fr == null) {
            //数据库中没有记录，则创建一条
            fr = new FlowRoads();
            fr.setFlowId(flowId);
            fr.setFlowVersion(flowVersion);
        } else {
            nodeList = sw.buildQuery().eq("roadId",fr.getRoadId())
                    .doQuery(FlowNodes.class);
            metaList = sw.buildQuery().eq("roadId",fr.getRoadId())
                    .doQuery(FlowMetadatas.class);


            fr.setRoadId(null);
        }
        //设置编辑状态
        fr.setFlowStat(FlowConstants.FLOW_STAT_EDITING);

        sw.buildQuery().doInsert(fr);

        if (nodeList != null && !nodeList.isEmpty()) {
            for (FlowNodes n : nodeList) {
                n.setFnId(null);
                n.setRoadId(fr.getRoadId());
            }
            sw.buildQuery().doBatchInsert(nodeList);
        }

        if (metaList != null && !metaList.isEmpty()) {
            for (FlowMetadatas m : metaList) {
                m.setMetaId(null);
                m.setRoadId(fr.getRoadId());
            }
            sw.buildQuery().doBatchInsert(nodeList);
        }

    }


    /**
     * 保存flowroad
     * @param fr
     */
    public void saveFlowRoad(FlowRoads fr){
        if(fr.getRoadId() != null && fr.getRoadId().trim().length()>0) {
            sw.buildQuery().eq("roadId",fr.getRoadId()).doUpdate(fr);
        }
    }


    /**
     * 将saved状态改为old状态
     * @param flowId
     * @param flowVersion
     */
    public void changeFlowRoadToOldStat(String flowId,String flowVersion) {
        if (flowId != null && flowId.trim().length() > 0
                && flowVersion != null && flowVersion.trim().length() > 0) {
//            sw.buildQuery().eq("flowId",flowId)
//                    .eq("flowVersion",flowVersion)
//                    .eq("flowStat",FlowConstants.FLOW_STAT_SAVED)
//                    .update("flow_stat",FlowConstants.FLOW_STAT_OLD)
//                    .doUpdate(FlowRoads.class);
            //上述写法有框架bug，无法更新数据库的字段
            List<FlowRoads> oldList = sw.buildQuery().eq("flowId",flowId)
                    .eq("flowVersion",flowVersion)
                    .eq("flowStat",FlowConstants.FLOW_STAT_SAVED)
                    .doQuery(FlowRoads.class);

            if(oldList != null && !oldList.isEmpty()){
                for(FlowRoads fr : oldList){
                    fr.setFlowStat(FlowConstants.FLOW_STAT_OLD);
                    sw.buildQuery().doUpdate(fr);
                }
            }
        }
    }

    /**
     * 根据roadId删除flowroad
     * @param roadId
     */
    public void deleteFlowRoadAndNodeInfoByRoadId(String roadId) {
        if (roadId != null && roadId.trim().length() > 0) {
            sw.buildQuery().eq("roadId",roadId).doDelete(FlowRoads.class);
            sw.buildQuery().eq("roadId",roadId).doDelete(FlowNodes.class);
            sw.buildQuery().eq("roadId",roadId).doDelete(FlowMetadatas.class);
        }
    }



}
