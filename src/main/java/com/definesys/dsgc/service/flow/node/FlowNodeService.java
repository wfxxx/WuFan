package com.definesys.dsgc.service.flow.node;

import com.definesys.dsgc.service.flow.bean.FlowNodes;
import com.definesys.dsgc.service.flow.bean.FlowRoads;
import com.definesys.dsgc.service.flow.dto.FlowNodeCommonDTO;
import com.definesys.dsgc.service.flow.dto.FlowNodeDTO;
import com.definesys.dsgc.service.flow.dto.FlowRoadDTO;
import com.definesys.dsgc.service.flow.node.dto.ParamPanelDTO;
import com.definesys.dsgc.service.flow.road.FlowRoadDao;
import com.definesys.mpaas.query.session.MpaasSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowNodeService {

    @Autowired
    private FlowNodeDao flowNodeDao;

    @Autowired
    private FlowRoadDao flowRoadDao;


    /**
     * 放弃编辑的内容
     */
    public String removeFlowNode(FlowNodeCommonDTO param) {
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(param.getFlowId(),param.getFlowVersion());

        if (road != null) {
            String uid = MpaasSession.getCurrentUser();
            if (uid == null || uid != null && !uid.equals(road.getCreatedBy())) {
                return "非法的操作权限！";
            }

            this.flowNodeDao.deleteFlowNodeMeta(road.getRoadId(),param.getNodeId());
            this.flowNodeDao.deleteFlowNodeMeta(road.getRoadId(),param.getNodeId());

        } else {
            return "无效的编辑状态！";
        }

        return "Y";

    }


    /**
     * 保存节点参数配置
     *
     * @param panel
     * @param paramsJsonTxt
     * @return
     */
    public String mergeFlowNodeParam(ParamPanelDTO panel,String paramsJsonTxt) {
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(panel.getFlowId(),panel.getFlowVersion());

        if (road != null) {
            String uid = MpaasSession.getCurrentUser();
            if (uid == null || uid != null && !uid.equals(road.getCreatedBy())) {
                return "非法的操作权限！";
            }


            //获取节点的信息
            FlowNodeDTO nodeInfo = null;
            FlowRoadDTO newRoad = panel.getFlow();
            if (newRoad != null && newRoad.getNodeList() != null && !newRoad.getNodeList().isEmpty()) {
                for (FlowNodeDTO n : newRoad.getNodeList()) {
                    if (panel.getNodeId().equals(n.getNodeId())) {
                        nodeInfo = n;
                        break;
                    }
                }
            }


            //查询数据库中是否已经存在
            FlowNodes flowNode = this.flowNodeDao.getFlowNode(road.getRoadId(),panel.getNodeId());
            if (flowNode == null) {
                //存在则更新
                flowNode = new FlowNodes();
            }

            flowNode.setRoadId(road.getRoadId());
            flowNode.setNodeId(panel.getNodeId());

            flowNode.setCnptCode(nodeInfo != null ? nodeInfo.getCnptCode() : null);
            flowNode.setNodeName(nodeInfo != null ? nodeInfo.getTitle() : null);
            flowNode.setNodeType(nodeInfo != null ? nodeInfo.getType() : null);
            flowNode.setNodeDesc(nodeInfo != null ? nodeInfo.getDesc() : null);

            flowNode.setInputType(panel.getInputType());
            flowNode.setInputMeta(panel.getInputMeta());
            flowNode.setInputValue(panel.getInputValue());

            flowNode.setOutputType(panel.getOutputType());
            flowNode.setOutputMeta(panel.getOutputMeta());
            flowNode.setOutputValue(panel.getOutputValue());

            flowNode.setParams(paramsJsonTxt);

            this.flowNodeDao.mergeFlowNode(flowNode);

        } else {
            return "无效的编辑状态！";
        }

        return "Y";

    }


    /**
     * 获取flowNode 状态自动识别
     * @param flowId
     * @param flowVersion
     * @param nodeId
     * @return
     */
    public FlowNodes getFlowNode(String flowId,String flowVersion,String nodeId){
        //有限获取正在编辑的
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(flowId,flowVersion);
        if(road == null){
            //没有开启编辑状态，则获取保存状态的
            road = this.flowRoadDao.getSavedFlowRoad(flowId,flowVersion);
        }

        if(road == null){
            return null;
        } else {
          return this.flowNodeDao.getFlowNode(road.getRoadId(),nodeId);
        }
    }

}
