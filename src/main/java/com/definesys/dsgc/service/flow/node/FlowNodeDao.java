package com.definesys.dsgc.service.flow.node;


import com.definesys.dsgc.service.flow.bean.FlowMetadatas;
import com.definesys.dsgc.service.flow.bean.FlowNodes;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
     * 删除节点自动创建的meta
     * @param roadId
     * @param nodeId
     */
    public void deleteFlowNodeMeta(String roadId,String nodeId){
        this.sw.buildQuery().eq("roadId",roadId)
                .eq("sour",nodeId)
                .doDelete(FlowMetadatas.class);
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
                this.sw.buildQuery().doUpdate(flowNode);
            } else {
                this.sw.buildQuery().doInsert(flowNode);
            }
        }
    }

}
