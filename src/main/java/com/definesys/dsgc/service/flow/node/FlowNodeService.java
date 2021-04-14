package com.definesys.dsgc.service.flow.node;

import com.definesys.dsgc.service.flow.bean.FlowNodes;
import com.definesys.dsgc.service.flow.bean.FlowRoads;
import com.definesys.dsgc.service.flow.dto.FlowConstants;
import com.definesys.dsgc.service.flow.dto.FlowNodeCommonDTO;
import com.definesys.dsgc.service.flow.dto.FlowNodeDTO;
import com.definesys.dsgc.service.flow.dto.FlowRoadDTO;
import com.definesys.dsgc.service.flow.meta.FlowMetaDao;
import com.definesys.dsgc.service.flow.node.dto.ParamPanelDTO;
import com.definesys.dsgc.service.flow.node.dto.VariableDTO;
import com.definesys.dsgc.service.flow.node.dto.VariablePreviewDTO;
import com.definesys.dsgc.service.flow.node.dto.VariablePreviewReqDTO;
import com.definesys.dsgc.service.flow.road.FlowRoadDao;
import com.definesys.dsgc.service.flow.utils.FlowUtils;
import com.definesys.mpaas.common.util.MpaasUtil;
import com.definesys.mpaas.query.session.MpaasSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FlowNodeService {

    @Autowired
    private FlowNodeDao flowNodeDao;

    @Autowired
    private FlowRoadDao flowRoadDao;

    @Autowired
    private FlowMetaDao flowMetaDao;


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

            this.flowNodeDao.deleteFlowNode(road.getRoadId(),param.getNodeId());
            this.flowMetaDao.deleteFlowNodeMetaByNodeId(road.getRoadId(),param.getNodeId());

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
                return "error:非法的操作权限！";
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

            FlowNodes flowNode = null;

            if(StringUtils.isBlank(panel.getNodeId())) {
                flowNode = new FlowNodes();
                //生成nodeid，后续返回给前端
                flowNode.setNodeId(MpaasUtil.guuid());
            } else {
                flowNode = this.flowNodeDao.getFlowNode(road.getRoadId(),panel.getNodeId());
                //查询数据库中是否已经存在
                if(flowNode == null){
                    flowNode = new FlowNodes();
                    //生成nodeid，后续返回给前端
                    flowNode.setNodeId(panel.getNodeId());
                }
            }

            flowNode.setRoadId(road.getRoadId());

            flowNode.setCnptCode(nodeInfo != null ? nodeInfo.getCnptCode() : null);
            flowNode.setNodeName(nodeInfo != null ? nodeInfo.getTitle() : null);
            flowNode.setNodeType(nodeInfo != null ? nodeInfo.getType() : null);
            flowNode.setNodeDesc(nodeInfo != null ? nodeInfo.getDesc() : null);

            flowNode.setInputMeta(panel.getInputMeta());
            flowNode.setInputValue(panel.getInputValue());

            flowNode.setOutputMeta(panel.getOutputMeta());
            flowNode.setOutputValue(panel.getOutputValue());

            flowNode.setParams(paramsJsonTxt);

            this.flowNodeDao.mergeFlowNode(flowNode);
            return flowNode.getNodeId();

        } else {
            return "error:无效的编辑状态！";
        }
    }


    /**
     * 获取flowNode 状态自动识别
     * @param flowId
     * @param flowVersion
     * @param nodeId
     * @return
     */
    public FlowNodes getFlowNode(String flowId,String flowVersion,String nodeId){
        //优先获取正在编辑的
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


    /**
     * 列出当前节点上下文中的变量信息
     * @param param
     * @return
     */
    public VariablePreviewDTO listUpstreamVariables(VariablePreviewReqDTO param) {

        VariablePreviewDTO var = new VariablePreviewDTO();
        var.setPayloadType(FlowConstants.FLOW_VAR_TYPE_ANY);

        //优先获取正在编辑的
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(param.getFlow().getFlowId(),param.getFlow().getFlowVersion());
        if (road == null) {
            //没有开启编辑状态，则获取保存状态的
            road = this.flowRoadDao.getSavedFlowRoad(param.getFlow().getFlowId(),param.getFlow().getFlowVersion());
        }

        if (road == null) {
            return var;
        }

        //查询出所有的节点的参数面板
        List<FlowNodes> nodes = this.flowNodeDao.getFlowNodeList(road.getRoadId());


        if (nodes == null || nodes.isEmpty()) {
            return var;
        }
        Map<String,FlowNodes> nodeCfgMap = new HashMap<String,FlowNodes>();
        //转换为map用于快速查找
        for (FlowNodes n : nodes) {
            nodeCfgMap.put(n.getNodeId(),n);
        }

        //递归遍历整个road,基于road图查找所有的变量以及变量最新的数据结构

        Map<String,String> varMap = new HashMap<String,String>();
        varMap.put(FlowConstants.FLOW_VAR_PAYLOAD,FlowConstants.FLOW_VAR_TYPE_ANY);

        //防止遍历死循环
        Set<String> checkedMap = new HashSet<String>();
        checkedMap.add(param.getNodeId());//当前节点为终止节点

        //递归查找路线图中的变量
        this.findVars(nodeCfgMap,param.getFlow(),checkedMap,param.getFlow().getStartNodeId(),varMap);

        var.setPayloadType(varMap.get(FlowConstants.FLOW_VAR_PAYLOAD));

        Set<Map.Entry<String,String>> enset = varMap.entrySet();
        Iterator<Map.Entry<String,String>> iter = enset.iterator();

        List<VariableDTO> varList = new ArrayList<VariableDTO>();
        while (iter.hasNext()) {
            Map.Entry<String,String> e = iter.next();
            if (!FlowConstants.FLOW_VAR_PAYLOAD.equals(e.getKey())) {
                VariableDTO v = new VariableDTO();
                v.setVarName(e.getKey());
                v.setVarType(e.getValue());
                varList.add(v);
            }
        }

        var.setVarList(varList);

        return var;
    }


    private void findVars(Map<String,FlowNodes> nodeCfgMap,FlowRoadDTO road,Set<String> checkedMap,String nodeId,Map<String,String> varMap) {
        //递归终止条件，nodeid为空，且之前没有遍历过
        if (StringUtils.isNotBlank(nodeId) && !checkedMap.contains(nodeId)) {
            FlowNodes nodeCfg = nodeCfgMap.get(nodeId);
            if (nodeCfg != null) {
                String outputVar = nodeCfg.getOutputValue();
                String outputType = StringUtils.isNotBlank(nodeCfg.getOutputMeta()) ? nodeCfg.getOutputMeta() : FlowConstants.FLOW_VAR_TYPE_ANY;
                if (StringUtils.isNotBlank(outputVar)) {
                    String varExistType = varMap.get(outputVar);
                    if (varExistType != null) {
                        if (!(!FlowConstants.FLOW_VAR_TYPE_ANY.equals(varExistType) && FlowConstants.FLOW_VAR_TYPE_ANY.equals(outputType))) {
                            varMap.put(outputVar,outputType);
                        }

                    } else {
                        varMap.put(outputVar,outputType);
                    }
                }
            }
            checkedMap.add(nodeId);

            //递归处理后续的节点
            FlowNodeDTO nodeLink = FlowUtils.getNodeFromRoad(road,nodeId);

            if (nodeLink != null) {
                //遍历子节点
                if (nodeLink.getChildren() != null && !nodeLink.getChildren().isEmpty()) {
                    for (FlowNodeDTO childrenNode : nodeLink.getChildren()) {
                        this.findVars(nodeCfgMap,road,checkedMap,childrenNode.getNodeId(),varMap);
                    }
                }

                //遍历后续节点
                this.findVars(nodeCfgMap,road,checkedMap,nodeLink.getAfterNodeId(),varMap);
            } else {
                this.findVars(nodeCfgMap,road,checkedMap,null,varMap);
            }
        }
    }



}
