package com.definesys.dsgc.service.flow.road;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.flow.bean.*;
import com.definesys.dsgc.service.flow.dto.*;
import com.definesys.dsgc.service.flow.meta.FlowMetaDao;
import com.definesys.dsgc.service.flow.mng.FlowSvcDao;
import com.definesys.dsgc.service.flow.node.FlowNodeDao;
import com.definesys.dsgc.service.flow.utils.FlowUtils;
import com.definesys.dsgc.service.svcgen.bean.TmplConfigBean;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.session.MpaasSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import java.util.*;

@Service
public class FlowRoadService {
    @Autowired
    private FlowRoadDao flowRoadDao;

    @Autowired
    private FlowSvcDao flowSvcDao;

    @Autowired
    private FlowNodeDao flowNodeDao;

    @Autowired
    private FlowMetaDao flowMetaDao;

    @Autowired
    private UserHelper userHelper;


    /**
     * 获取集成流路线图
     * @param param
     * @return
     */
    public FlowRoadDTO getFlowRoad(FlowRoadQueryDTO param) {

        FlowServices fs = this.flowSvcDao.getFlowServcieByFlowId(param.getFlowId());

        if (fs == null) {
            return null;
        }

        FlowRoadDTO res = new FlowRoadDTO();
        res.setFlowId(fs.getFlowId());
        res.setTitle(fs.getFlowName());

        FlowRoads road = this.flowRoadDao.getSavedFlowRoad(param.getFlowId(),param.getFlowVersion());

        if (road == null) {
            res.setFlowVersion(FlowConstants.FLOW_DFT_VERSION);
        } else {
            res.setFlowVersion(road.getFlowVersion());
            res.setStartNodeId(road.getStartNodeId());

//            List<FlowNodeDTO> nodelist = this.getDemoRoadGraph();
//            res.setStartNodeId(nodelist.get(0).getNodeId());
//            res.setNodeList(nodelist);

            if (road.getRoadGraph() != null) {
                List<FlowNodeDTO> nodelist = JSONObject.parseArray(road.getRoadGraph(),FlowNodeDTO.class);
                res.setNodeList(nodelist);
            }

        }
        return res;
    }

    private List<FlowNodeDTO>  getDemoRoadGraph(){
        FlowNodeDTO n = new FlowNodeDTO();
        n.setNodeId(UUID.randomUUID().toString());
        n.setCnptCode("REST");
        n.setDesc("REST监听器");
        n.setTitle("REST");
        n.setType("INPUT");


        FlowNodeDTO n1 = new FlowNodeDTO();
        n1.setNodeId(UUID.randomUUID().toString());
        n1.setCnptCode("DB");
        n1.setDesc("DB查询");
        n1.setTitle("DB查询");
        n1.setType("OUTPUT");

        n.setAfterNodeId(n1.getNodeId());

        List<FlowNodeDTO> roadGraph = new ArrayList<FlowNodeDTO>();
        roadGraph.add(n);
        roadGraph.add(n1);

        return roadGraph;

    }


    /**
     * 打开编辑状态
     * @param param
     */
    public String startFlowRoadEdit(FlowRoadEditReqDTO param){

        if(this.flowRoadDao.checkEditingIsExist(param.getFlowId(),param.getFlowVersion())) {
            return "编辑冲突，已存在编辑状态！";
        }

        this.flowRoadDao.startEditingFlowRoad(param.getFlowId(),param.getFlowVersion());

        return "Y";
    }

    /**
     * 保存并退出编辑状态
     */
    public String saveFlowRoadWithCloseEditing(FlowRoadDTO param) {

        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(param.getFlowId(),param.getFlowVersion());
        String checRes = FlowUtils.checkFlowEditingUserAuth(road);
        if (!"Y".equals(checRes)) {
            return checRes;
        }

        //将之前的配置更新为旧的状态
        this.flowRoadDao.changeFlowRoadToOldStat(param.getFlowId(),param.getFlowVersion());

        //将当前的编辑状态更新为新的状态
        road.setFlowStat(FlowConstants.FLOW_STAT_SAVED);
        road.setStartNodeId(param.getStartNodeId());
        if (param.getNodeList() != null && !param.getNodeList().isEmpty()) {
            road.setRoadGraph(JSONObject.toJSONString(param.getNodeList()));
        } else {
            road.setRoadGraph(null);
        }

        this.flowRoadDao.saveFlowRoad(road);


        return "Y";
    }


    /**
     * 放弃编辑的内容
     */
    public String backoffFlowRoadEdited(FlowRoadEditReqDTO param) {
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(param.getFlowId(),param.getFlowVersion());

        String checRes = FlowUtils.checkFlowEditingUserAuth(road);
        if (!"Y".equals(checRes)) {
            return checRes;
        }

        this.flowRoadDao.deleteFlowRoadAndNodeInfoByRoadId(road.getRoadId());

        return "Y";

    }


    public FlowRoads getEditingFlowRoad(String flowId,String flowVersion){
        return this.flowRoadDao.getEditingFlowRoad(flowId,flowVersion);
    }

    public FlowRoads getSavedFlowRoad(String flowId,String flowVersion){
        return this.flowRoadDao.getSavedFlowRoad(flowId,flowVersion);
    }


    public TmplConfigBean getSvcGenConfig(String flowId,String flowVersion){
        TmplConfigBean tc = new TmplConfigBean();
        FlowServices fs = this.flowSvcDao.getFlowServcieByFlowId(flowId);
        FlowRoads road = this.flowRoadDao.getSavedFlowRoad(fs.getFlowId(),flowVersion);


        if(road == null  || StringUtils.isBlank(road.getRoadGraph())){
            tc.setErroMsg("不完整的集成流，无法发布！");
            return tc;
        }

        //目前写死只支持db查询服务
        if(StringUtils.isNotBlank(road.getRoadGraph())){
            List<FlowNodeDTO> nodelist = JSONObject.parseArray(road.getRoadGraph(),FlowNodeDTO.class);

            if(nodelist == null || nodelist.isEmpty() || nodelist.size() == 1) {
                tc.setErroMsg("不完整的集成流，无法发布！");
                return tc;
            }

            //写死的逻辑，由于前端有bug无法正常设置node的 input还是Output
            for(FlowNodeDTO n : nodelist){
                if(road.getStartNodeId() != null && road.getStartNodeId().equals(n.getNodeId())){
                    n.setType("INPUT");
                } else {
                    n.setType("OUTPUT");
                }
            }

            tc.setServNo(flowId+"-"+flowVersion);
            tc.setServNameCode(fs.getFlowName());
            tc.setProjDir(fs.getProjectCode());
            tc.setServDir("flow");
            tc.setAppCode(fs.getAppCode());
            tc.setToSystem(fs.getAppCode());

            Set<String> scanedSet = new HashSet<String>();


            this.processFlowRoadNode(road,road.getStartNodeId(),nodelist,tc,scanedSet);
        }

        return tc;
    }


    private void processFlowRoadNode(FlowRoads road,String nodeId,List<FlowNodeDTO> nodeList,TmplConfigBean tc,Set<String> scanedSet){
        if(StringUtils.isBlank(nodeId) || scanedSet.contains(nodeId)){
            return;
        }
        //标记当前node为scaned
        scanedSet.add(nodeId);

        String nextNodeId = FlowUtils.getAfterNodeId(nodeList,nodeId);
        //获取监听的类型
        FlowNodes node = this.flowNodeDao.getFlowNode(road.getRoadId(),nodeId);

        //写死的逻辑，前端目前还有bug
        if(road.getStartNodeId().equals(nodeId)){
            node.setNodeType("INPUT");
        } else {
            node.setNodeType("OUTPUT");
        }

        if("REST".equals(node.getCnptCode()) && "INPUT".equals(node.getNodeType())){
            JSONObject paramsJO =  JSONObject.parseObject(node.getParams());
            String path = paramsJO.getString("path");
            if(path == null || StringUtils.isBlank(path)){
                tc.setErroMsg("REST发布路径不能为空！");
                return;
            }
            tc.setRestPSUri(path);
        } else if("SOAP".equals(node.getCnptCode()) && "INPUT".equals(node.getNodeType())){
            JSONObject paramsJO =  JSONObject.parseObject(node.getParams());
            String path = paramsJO.getString("path");
            if(path == null || StringUtils.isBlank(path)){
                tc.setErroMsg("SOAP发布路径不能为空！");
                return;
            }
            tc.setSoapPSUri(path);
        } else if("DATA_CONVERT".equals(node.getCnptCode())){
            if(StringUtils.isNotBlank(node.getParams())) {
                DataCovertParamDTO coverParam = JSONObject.parseObject(node.getParams(),DataCovertParamDTO.class);

                if (coverParam != null) {
                    //判断转换的类型
                    if (StringUtils.isBlank(nextNodeId)) {
                        //最后一个节点，作为Rest的Input转换
                        if (StringUtils.isNotBlank(node.getOutputMeta())) {
                            FlowMetadatas restInputMeta = this.flowMetaDao.getFlowMetaByMetaId(road.getRoadId(),node.getOutputMeta());

                            if (restInputMeta != null && StringUtils.isNotBlank(restInputMeta.getMetaTxt()) && coverParam.getConvertList() != null
                                    && !coverParam.getConvertList().isEmpty()) {
                                tc.setInputMeta(restInputMeta.getMetaTxt());
                                tc.setOutputTransMapping(JSONObject.toJSONString(coverParam.getConvertList()));
                            }
                        }


                    } else {
                        String inputPayloadMetaId = null;
                        //第一个转换，作为Rest的output转换
                        if (coverParam.getInput() != null && !coverParam.getInput().isEmpty()) {
                            for (VarDTO var : coverParam.getInput()) {
                                if ("Payload".equals(var.getVarName())) {
                                    inputPayloadMetaId = var.getVarMeta();
                                }
                            }
                        }

                        if (StringUtils.isNotBlank(inputPayloadMetaId)) {
                            FlowMetadatas restOutputMeta = this.flowMetaDao.getFlowMetaByMetaId(road.getRoadId(),inputPayloadMetaId);
                            if (restOutputMeta != null && StringUtils.isNotBlank(restOutputMeta.getMetaTxt())
                                    && coverParam.getConvertList() != null && !coverParam.getConvertList().isEmpty()) {
                                tc.setOutputMeta(restOutputMeta.getMetaTxt());
                                tc.setInputTransMapping(JSONObject.toJSONString(coverParam.getConvertList()));
                            }
                        }


                    }
                }
            }

        } else if("DB".equals(node.getCnptCode()) && "OUTPUT".equals(node.getNodeType())){
            if(StringUtils.isNotBlank(node.getParams())) {
                TmplConfigBean dbParam = JSONObject.parseObject(node.getParams(),TmplConfigBean.class);
                if(dbParam != null){
                    tc.setDbConn(dbParam.getDbConn());
                    tc.setTmplFlag(dbParam.getTmplFlag());
                    tc.setDbOper(dbParam.getDbOper());
                    tc.setRsps(dbParam.getRsps());
                    tc.setTbls(dbParam.getTbls());
                    tc.setSqlCode(dbParam.getSqlCode());
                }
            }
        }

        //继续处理下一个节点
        this.processFlowRoadNode(road,nextNodeId,nodeList,tc,scanedSet);
    }





}
