package com.definesys.dsgc.service.flow.node;


import com.definesys.dsgc.service.flow.bean.FlowNodes;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlowNodeDao {
    @Autowired
    private MpaasQueryFactory sw;


    /**
     * 删除节点
     * @param roadId
     * @param nodeId
     */
    public void deleteFlowNode(String roadId,String nodeId){
        this.sw.buildQuery().eq("roadId",roadId)
                .eq("nodeId",nodeId)
                .doDelete(FlowNodes.class);
    }


    /**
     * 查询flowNodes
     * @param roadId
     * @param nodeId
     * @return
     */
    public FlowNodes getFlowNode(String roadId,String nodeId){
        if(StringUtils.isBlank(roadId) || StringUtils.isBlank(nodeId)) {
            return null;
        }

        return this.sw.buildQuery().eq("roadId",roadId).eq("nodeId",nodeId).doQueryFirst(FlowNodes.class);
    }

    /**
     * merge flownode信息
     * @param flowNode
     */
    public void mergeFlowNode(FlowNodes flowNode) {
        if (flowNode != null) {
            if (StringUtils.isNotBlank(flowNode.getFnId())) {
                this.sw.buildQuery().eq("fnId",flowNode.getFnId()).doUpdate(flowNode);
            } else {
                this.sw.buildQuery().doInsert(flowNode);
            }
        }
    }


    /**
     * 根据roadid获取数据库中的所有节点
     * @param roadId
     * @return
     */
    public List<FlowNodes> getFlowNodeList(String roadId){
        if(StringUtils.isBlank(roadId)){
            return null;
        }
        return this.sw.buildQuery().eq("roadId",roadId).doQuery(FlowNodes.class);
    }




}
