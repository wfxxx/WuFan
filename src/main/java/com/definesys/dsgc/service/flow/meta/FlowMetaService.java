package com.definesys.dsgc.service.flow.meta;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.flow.bean.FlowMetadatas;
import com.definesys.dsgc.service.flow.bean.FlowNodes;
import com.definesys.dsgc.service.flow.bean.FlowRoads;
import com.definesys.dsgc.service.flow.meta.dto.DataNodeDTO;
import com.definesys.dsgc.service.flow.meta.dto.FlowMetaDTO;
import com.definesys.dsgc.service.flow.meta.dto.FlowMetaGenerateReqDTO;
import com.definesys.dsgc.service.flow.node.FlowNodeDao;
import com.definesys.dsgc.service.flow.road.FlowRoadDao;
import com.definesys.dsgc.service.flow.utils.FlowUtils;
import com.definesys.dsgc.service.svcgen.bean.TmplConfigBean;
import com.definesys.dsgc.service.svcgen.utils.ServiceGenerateProxy;
import com.definesys.dsgc.service.svcgen.utils.TreeNode;
import com.definesys.mpaas.common.util.MpaasUtil;
import com.definesys.mpaas.query.session.MpaasSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlowMetaService {

    @Autowired
    private FlowMetaDao flowMetaDao;

    @Autowired
    private FlowRoadDao flowRoadDao;

    @Autowired
    private FlowNodeDao flowNodeDao;

    private ServiceGenerateProxy sgProxy = ServiceGenerateProxy.newInstance();


    /**
     * 根据metaid删除metadata
     * @param flowId
     * @param flowVersion
     * @param metaId
     * @return
     */
    public String removeFlowMetaByMetaId(String flowId,String flowVersion,String metaId) {
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(flowId,flowVersion);

        String checRes = FlowUtils.checkFlowEditingUserAuth(road);
        if (!"Y".equals(checRes)) {
            return checRes;
        }

        this.flowMetaDao.deleteFlowMetaByMetaId(road.getRoadId(),metaId);

        return "Y";
    }


    /**
     * 更新或者新增meta
     * @param metaDto
     * @return
     */
    public String mergeFlowMeta(FlowMetaDTO metaDto) {
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(metaDto.getFlowId(),metaDto.getFlowVersion());

        String checRes = FlowUtils.checkFlowEditingUserAuth(road);
        if (!"Y".equals(checRes)) {
            return "error:" + checRes;
        }

        FlowMetadatas metaBean = null;

        if (StringUtils.isBlank(metaDto.getMetaId())) {
            metaBean = new FlowMetadatas();
            metaBean.setMetaId(MpaasUtil.guuid());
        } else {
            metaBean = this.flowMetaDao.getFlowMetaByMetaId(road.getRoadId(),metaDto.getMetaId());
            //查询数据库中是否已经存在
            if (metaBean == null) {
                return "error:未找到记录，无法更新！";
            }
        }

        metaBean.setRoadId(road.getRoadId());
        metaBean.setSour(metaDto.getSour());
        metaBean.setMetaType(metaDto.getMetaType());
        metaBean.setMetaName(metaDto.getMetaName());
        metaBean.setMetaTxt(metaDto.getMetaTree() != null ? JSONObject.toJSONString(metaDto.getMetaTree()) : null);

        this.flowMetaDao.mergeFlowMeta(metaBean);

        return metaBean.getMetaId();

    }


    /**
     * 获取FlowMeata 状态自动识别
     * @param flowId
     * @param flowVersion
     * @param metaId
     * @return
     */
    public FlowMetaDTO getFlowMeta(String flowId,String flowVersion,String metaId){
        //优先获取正在编辑的
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(flowId,flowVersion);
        if(road == null){
            //没有开启编辑状态，则获取保存状态的
            road = this.flowRoadDao.getSavedFlowRoad(flowId,flowVersion);
        }

        if(road == null){
            return null;
        } else {
            FlowMetadatas metaBean = this.flowMetaDao.getFlowMetaByMetaId(road.getRoadId(),metaId);
            return this.covertMetaToDTO(metaBean,flowId,flowVersion);
        }
    }

    public List<FlowMetaDTO> getAllFlowMeta(String flowId,String flowVersion){
        //优先获取正在编辑的
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(flowId,flowVersion);
        if(road == null){
            //没有开启编辑状态，则获取保存状态的
            road = this.flowRoadDao.getSavedFlowRoad(flowId,flowVersion);
        }

        if(road == null){
            return null;
        } else {
            List<FlowMetadatas> metaList = this.flowMetaDao.getAllFlowMeta(road.getRoadId());
            List<FlowMetaDTO> dtoList = new ArrayList<FlowMetaDTO>();
            if(metaList !=null && !metaList.isEmpty()) {
                for(FlowMetadatas metaBean : metaList){
                    dtoList.add(this.covertMetaToDTO(metaBean,flowId,flowVersion));
                }
            }
            return dtoList;
        }
    }

    /**
     * 转换为metadto
     * @param metaBean
     * @param flowId
     * @param flowVersion
     * @return
     */
    private FlowMetaDTO covertMetaToDTO(FlowMetadatas metaBean,String flowId,String flowVersion){
        if(metaBean != null){
            FlowMetaDTO metaDto = new FlowMetaDTO();
            metaDto.setFlowId(flowId);
            metaDto.setFlowVersion(flowVersion);
            metaDto.setMetaId(metaBean.getMetaId());
            metaDto.setMetaName(metaBean.getMetaName());
            metaDto.setMetaType(metaBean.getMetaType());
            metaDto.setSour(metaBean.getSour());
            if(StringUtils.isNotBlank(metaBean.getMetaTxt())){
                metaDto.setMetaTree(JSONObject.parseObject(metaBean.getMetaTxt(),DataNodeDTO.class));
            }
            return metaDto;
        } else {
            return null;
        }
    }


    /**
     * 根据节点的配置生成参数结构
     * @param param
     * @return
     */
    public FlowMetaDTO generateNodeMeta(FlowMetaGenerateReqDTO param,FlowRoads road){

       FlowNodes node = this.flowNodeDao.getFlowNode(road.getRoadId(),param.getNodeId());

       FlowMetaDTO meta = new FlowMetaDTO();
       meta.setFlowId(param.getFlowId());
       meta.setFlowVersion(param.getFlowVersion());
       meta.setSour(param.getNodeId());


       meta.setMetaType("XML");
       String jsonInput ="{\n" +
               "            \"title\": \"QueryParams\",\n" +
               "            \"key\": \"QueryParams\",\n" +
               "            \"expanded\": true,\n" +
               "            \"children\": [\n" +
               "                {\n" +
               "                    \"title\": \"bsId\",\n" +
               "                    \"key\": \"bsId\",\n" +
               "                    \"expanded\": false,\n" +
               "                    \"children\": null,\n" +
               "                    \"valueType\": \"String\",\n" +
               "                    \"minOccur\": \"1\",\n" +
               "                    \"maxOccur\": \"1\",\n" +
               "                    \"leaf\": true\n" +
               "                },\n" +
               "                {\n" +
               "                    \"title\": \"tstId\",\n" +
               "                    \"key\": \"tstId\",\n" +
               "                    \"expanded\": false,\n" +
               "                    \"children\": null,\n" +
               "                    \"valueType\": \"String\",\n" +
               "                    \"minOccur\": \"1\",\n" +
               "                    \"maxOccur\": \"1\",\n" +
               "                    \"leaf\": true\n" +
               "                }\n" +
               "            ],\n" +
               "            \"valueType\": \"Object\",\n" +
               "            \"minOccur\": \"1\",\n" +
               "            \"maxOccur\": \"1\",\n" +
               "            \"leaf\": false\n" +
               "        }";

       String jsonOutput = "{\n" +
               "            \"title\": \"DagBsCollection\",\n" +
               "            \"key\": \"DagBsCollection\",\n" +
               "            \"expanded\": true,\n" +
               "            \"children\": [\n" +
               "                {\n" +
               "                    \"title\": \"DagBs\",\n" +
               "                    \"key\": \"DagBs\",\n" +
               "                    \"expanded\": true,\n" +
               "                    \"children\": [\n" +
               "                        {\n" +
               "                            \"title\": \"bsId\",\n" +
               "                            \"key\": \"bsId\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"1\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"title\": \"bsCode\",\n" +
               "                            \"key\": \"bsCode\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"title\": \"bsDesc\",\n" +
               "                            \"key\": \"bsDesc\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"title\": \"appCode\",\n" +
               "                            \"key\": \"appCode\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"title\": \"createdBy\",\n" +
               "                            \"key\": \"createdBy\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"title\": \"creationDate\",\n" +
               "                            \"key\": \"creationDate\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"Date\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"title\": \"lastUpdatedBy\",\n" +
               "                            \"key\": \"lastUpdatedBy\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"title\": \"lastUpdateDate\",\n" +
               "                            \"key\": \"lastUpdateDate\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"Date\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"title\": \"objectVersionNumber\",\n" +
               "                            \"key\": \"objectVersionNumber\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"Number\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"title\": \"sfsfs\",\n" +
               "                            \"key\": \"sfsfs\",\n" +
               "                            \"expanded\": true,\n" +
               "                            \"children\": [\n" +
               "                                {\n" +
               "                                    \"title\": \"DagBsDtl\",\n" +
               "                                    \"key\": \"DagBsDtl\",\n" +
               "                                    \"expanded\": true,\n" +
               "                                    \"children\": [\n" +
               "                                        {\n" +
               "                                            \"title\": \"vid\",\n" +
               "                                            \"key\": \"vid\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"protocal\",\n" +
               "                                            \"key\": \"protocal\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"hostName\",\n" +
               "                                            \"key\": \"hostName\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"port\",\n" +
               "                                            \"key\": \"port\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"paths\",\n" +
               "                                            \"key\": \"paths\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"rtyCount\",\n" +
               "                                            \"key\": \"rtyCount\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Number\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"connectTimeout\",\n" +
               "                                            \"key\": \"connectTimeout\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Number\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"sendTimeout\",\n" +
               "                                            \"key\": \"sendTimeout\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Number\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"readTimeout\",\n" +
               "                                            \"key\": \"readTimeout\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Number\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"createdBy\",\n" +
               "                                            \"key\": \"createdBy\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"creationDate\",\n" +
               "                                            \"key\": \"creationDate\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Date\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"lastUpdatedBy\",\n" +
               "                                            \"key\": \"lastUpdatedBy\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"lastUpdateDate\",\n" +
               "                                            \"key\": \"lastUpdateDate\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Date\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"title\": \"objectVersionNumber\",\n" +
               "                                            \"key\": \"objectVersionNumber\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Number\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        }\n" +
               "                                    ],\n" +
               "                                    \"valueType\": \"Object\",\n" +
               "                                    \"minOccur\": \"0\",\n" +
               "                                    \"maxOccur\": \"2147483647\",\n" +
               "                                    \"leaf\": false\n" +
               "                                }\n" +
               "                            ],\n" +
               "                            \"valueType\": \"Object\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": false\n" +
               "                        }\n" +
               "                    ],\n" +
               "                    \"valueType\": \"Object\",\n" +
               "                    \"minOccur\": \"0\",\n" +
               "                    \"maxOccur\": \"2147483647\",\n" +
               "                    \"leaf\": false\n" +
               "                }\n" +
               "            ],\n" +
               "            \"valueType\": \"Object\",\n" +
               "            \"minOccur\": \"1\",\n" +
               "            \"maxOccur\": \"1\",\n" +
               "            \"leaf\": false\n" +
               "        }";

       if("INPUT".equals(param.getInOrOut())) {
           meta.setMetaTree(JSONObject.parseObject(jsonInput,DataNodeDTO.class));
       } else {
           meta.setMetaTree(JSONObject.parseObject(jsonOutput,DataNodeDTO.class));
       }


//       if(node != null && "DB".equals(node.getCnptCode()) && "OUTPUT".equals(node.getNodeType())){
//           //解析db请求的参数
//           meta.setMetaType("XML");
//           this.createSampleMetaForDBConnector(param,road,node,meta);
//
//       }

       return meta;
    }


    private void createSampleMetaForDBConnector(FlowMetaGenerateReqDTO param,FlowRoads road,FlowNodes node,FlowMetaDTO meta) {
        if (node.getParams() == null) {
            return;
        }
        try {

            TmplConfigBean tcb = JSONObject.parseObject(node.getParams(),TmplConfigBean.class);
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("inOrOut",param.getInOrOut());
            String treeJsonTxt = this.sgProxy.createXsdTree(MpaasSession.getCurrentUser(),tcb,paramMap);
            if (treeJsonTxt != null) {
                meta.setMetaTree(JSONObject.parseObject(treeJsonTxt,DataNodeDTO.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
